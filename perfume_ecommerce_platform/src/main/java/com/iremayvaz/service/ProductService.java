package com.iremayvaz.service;

import com.iremayvaz.exceptionHandler.InsufficientStockException;
import com.iremayvaz.model.dto.admin.request.DtoAdminProductRequest;
import com.iremayvaz.model.dto.admin.response.DtoAdminProductResponse;
import com.iremayvaz.model.dto.response.CategoryDetailResponse;
import com.iremayvaz.model.dto.response.ProductDetailResponse;
import com.iremayvaz.model.dto.response.ProductResponse;
import com.iremayvaz.model.entity.Category;
import com.iremayvaz.model.entity.Note;
import com.iremayvaz.model.entity.Product;
import com.iremayvaz.repository.CategoryRepository;
import com.iremayvaz.repository.NoteRepository;
import com.iremayvaz.repository.ProductRepository;
import com.iremayvaz.repository.specs.ProductSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final NoteRepository noteRepository;

    public List<ProductResponse> getProductList(){
        List<ProductResponse> productsResponse = new ArrayList<>();

        List<Product> productList = productRepository.findAll();

        for(Product p: productList){
            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(p,response);
            productsResponse.add(response);
        }

        return productsResponse;
    }

    public List<ProductResponse> filterProduct(String column, String content) {
        List<ProductResponse> productsResponse = new ArrayList<>();

        var spec  = ProductSpecifications.filterByColumn(column, content);
        List<Product> filteredProducts = productRepository.findAll(spec);

        for(Product p : filteredProducts){
            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(p, response);
            productsResponse.add(response);
        }

        return productsResponse;
    }

    public ProductDetailResponse getProductInfo(Long id) {
        Optional<Product> optional = productRepository.findById(id);

        if (optional.isPresent()){
            CategoryDetailResponse categoryDetailResponse
                    = new CategoryDetailResponse(optional.get().getCategory().getId(),
                                                optional.get().getCategory().getGender(),
                                                optional.get().getCategory().getConcentration(),
                                                optional.get().getCategory().getSeason(),
                                                optional.get().getCategory().getAccord());
            ProductDetailResponse productDetailResponse
                    = new ProductDetailResponse(optional.get().getId(),
                    optional.get().getName(),
                    optional.get().getBrand(),
                    categoryDetailResponse,
                    optional.get().getPrice(),
                    optional.get().getRating(),
                    mapNoteNames(optional.get().getTopNotes()),
                    mapNoteNames(optional.get().getHeartNotes()),
                    mapNoteNames(optional.get().getBaseNotes()));

            return productDetailResponse;
        } else {
            throw new IllegalArgumentException("Id ile kayıtlı ürün yok!");
        }
    }

    private static Set<String> mapNoteNames(Set<Note> notes) {
        if (notes == null) return Collections.emptySet();
        return notes.stream()
                .map(Note::getName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı: " + productId));

        // İş kuralı: negatif stok yok
        if (product.getStockQuantity() == null) {
            throw new IllegalStateException("Ürünün stok bilgisi tanımlı değil: " + productId);
        }

        if (product.getStockQuantity() < quantity) { // Yeterli stok yok
            throw new InsufficientStockException(
                    "Yeterli stok yok. İstenen: " + quantity +
                            ", mevcut: " + product.getStockQuantity()
            );
        }
            // Entity managed olduğu için transaction commit edilirken Hibernate UPDATE+version artırma yapacak.
            product.setStockQuantity(product.getStockQuantity() - quantity);
        }

    // İade / Sipariş iptal için
    @Transactional
    public void increaseStock(Long productId, int quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı: " + productId));

        int newQuantity = product.getStockQuantity() + quantity;
        product.setStockQuantity(newQuantity);
        productRepository.save(product);
    }

    @Transactional
    public void addProduct(DtoAdminProductRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setDescription(request.getDescription());
        product.setImageUrl("https://" + request.getImageUrl());
        product.setRating(0.0);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori Yok!"));
        product.setCategory(category);

        if (request.getTopNoteIds() != null)
            product.setTopNotes(new HashSet<>(noteRepository.findAllById(request.getTopNoteIds())));

        if (request.getHeartNoteIds() != null)
            product.setHeartNotes(new HashSet<>(noteRepository.findAllById(request.getHeartNoteIds())));

        if (request.getBaseNoteIds() != null)
            product.setBaseNotes(new HashSet<>(noteRepository.findAllById(request.getBaseNoteIds())));

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getStockQuantity() != null ? product.getStockQuantity() : 0,
                product.getRating(),
                product.getImageUrl()
        );
    }

    public void updateProduct(DtoAdminProductRequest dto) {
        if (dto.getId() == null) throw new IllegalArgumentException("Product id is required for update");

        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found. Id: " + dto.getId()));

        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setDescription(dto.getDescription());
        product.setStockQuantity(dto.getStockQuantity());

        var category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found. Id: " + dto.getCategoryId()));
        product.setCategory(category);

        // 5) Notlar (top/heart/base) alanlarını da DTO'ya eklediysen burada handle edebilirsin
        // Örneğin:
        // Set<Note> selectedNotes = noteRepository.findAllById(dto.getNoteIds());
        // product.setNotes(selectedNotes);

        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found. Id: " + id));
    }
}

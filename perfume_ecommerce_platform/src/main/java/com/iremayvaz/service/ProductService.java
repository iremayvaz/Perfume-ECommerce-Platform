package com.iremayvaz.service;

import com.iremayvaz.model.dto.CategoryDetailResponse;
import com.iremayvaz.model.dto.ProductDetailResponse;
import com.iremayvaz.model.dto.ProductResponse;
import com.iremayvaz.model.entity.Note;
import com.iremayvaz.model.entity.Product;
import com.iremayvaz.repository.ProductRepository;
import com.iremayvaz.repository.specs.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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
                                                optional.get().getCategory().getConcentrationName(),
                                                optional.get().getCategory().getSeason(),
                                                optional.get().getCategory().getAccord());
            ProductDetailResponse productDetailResponse
                    = new ProductDetailResponse(optional.get().getId(),
                    optional.get().getProductName(),
                    optional.get().getBrandName(),
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
                .map(Note::getNoteName)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}

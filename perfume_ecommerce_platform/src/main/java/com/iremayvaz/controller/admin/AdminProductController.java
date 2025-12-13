package com.iremayvaz.controller.admin;

import com.iremayvaz.model.dto.admin.request.DtoAdminProductRequest;
import com.iremayvaz.model.dto.admin.request.StockForm;
import com.iremayvaz.model.entity.Note;
import com.iremayvaz.model.entity.Product; // Entity sınıfın
import com.iremayvaz.model.enums.NoteType;
import com.iremayvaz.repository.CategoryRepository;
import com.iremayvaz.repository.NoteRepository;
import com.iremayvaz.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller // DİKKAT: RestController değil!
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final NoteRepository noteRepository;

    // 1. Ürünleri Listeleme Sayfası
    @GetMapping
    public String listProducts(Model model) {
        // "products" anahtarı ile listeyi HTML'e gönderiyoruz
        model.addAttribute("products", productService.getProductList());
        return "admin/product-list"; // templates/admin/product-list.html dosyasını açar
    }

    // 2. Yeni Ürün Ekleme Formu
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new DtoAdminProductRequest());

        // 1. Tüm Kategorileri Gönder (Erkek-Kış-EDP vb. hazır olsun)
        model.addAttribute("categories", categoryRepository.findAll());

        // 2. Tüm Notaları Gönder
        List<Note> allNotes = noteRepository.findAll();

        // Notaları tiplerine göre ayırıp sayfaya atıyoruz ki ayrı kutularda çıksın
        model.addAttribute("topNotes", allNotes.stream().filter(n -> n.getType() == NoteType.TOP).toList());
        model.addAttribute("heartNotes", allNotes.stream().filter(n -> n.getType() == NoteType.HEART).toList());
        model.addAttribute("baseNotes", allNotes.stream().filter(n -> n.getType() == NoteType.BASE).toList());

        return "admin/product-form";
    }

    // 3. Ürünü Kaydetme İşlemi (Form Submit edildiğinde buraya düşer)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") DtoAdminProductRequest dto) {
        if (dto.getId() == null) {
            productService.addProduct(dto);     // CREATE
        } else {
            productService.updateProduct(dto);  // UPDATE
        }
        return "redirect:/admin/products";
    }

    // 4. Silme İşlemi
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); // Service'ine delete metodu eklemen gerekebilir
        return "redirect:/admin/products";
    }

    // 5. Ürün Düzenleme Formu
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);

        DtoAdminProductRequest dto = new DtoAdminProductRequest();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategoryId(product.getCategory().getId());

        dto.setTopNoteIds(
                product.getTopNotes().stream()
                        .map(Note::getId)
                        .collect(Collectors.toSet())
        );
        dto.setHeartNoteIds(
                product.getHeartNotes().stream()
                        .map(Note::getId)
                        .collect(Collectors.toSet())
        );
        dto.setBaseNoteIds(
                product.getBaseNotes().stream()
                        .map(Note::getId)
                        .collect(Collectors.toSet())
        );

        model.addAttribute("product", dto);

        model.addAttribute("categories", categoryRepository.findAll());

        List<Note> allNotes = noteRepository.findAll();
        model.addAttribute("topNotes",   allNotes.stream().filter(n -> n.getType() == NoteType.TOP).toList());
        model.addAttribute("heartNotes", allNotes.stream().filter(n -> n.getType() == NoteType.HEART).toList());
        model.addAttribute("baseNotes",  allNotes.stream().filter(n -> n.getType() == NoteType.BASE).toList());

        return "admin/product-form";
    }

    @GetMapping("/{id}/stock")
    public String showStockForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("stockForm", new StockForm()); // sadece miktar
        return "admin/product-stock"; // minik bir sayfa
    }

    @PostMapping("/{id}/stock")
    public String updateStock(
            @PathVariable Long id,
            @RequestParam("amount") int amount
    ) {
        if (amount <= 0) {
            // istersen burada hata mesajı verip sayfaya geri dönebilirsin
            // şimdilik basit olsun:
            return "redirect:/admin/products/" + id + "/stock";
        }

        productService.increaseStock(id, amount);
        return "redirect:/admin/dashboard";  // veya ürün listesi: /admin/products
    }

}
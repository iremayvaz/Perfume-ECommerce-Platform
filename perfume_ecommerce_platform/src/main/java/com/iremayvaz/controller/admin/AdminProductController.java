package com.iremayvaz.controller.admin;

import com.iremayvaz.model.dto.admin.request.DtoAdminProductRequest;
import com.iremayvaz.model.dto.admin.request.StockForm;
import com.iremayvaz.model.dto.response.ProductResponse;
import com.iremayvaz.model.entity.Note;
import com.iremayvaz.model.entity.Product; // Entity sınıfın
import com.iremayvaz.model.entity.User;
import com.iremayvaz.model.enums.NoteType;
import com.iremayvaz.service.CategoryService;
import com.iremayvaz.service.NoteService;
import com.iremayvaz.service.ProductService;
import com.iremayvaz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final NoteService noteService;
    private final UserService userService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getProductList());
        return "admin/product-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Authentication authentication, Model model) {
        model.addAttribute("product", new DtoAdminProductRequest());

        model.addAttribute("categories", categoryService.findAll());

        List<Note> allNotes = noteService.findAll();

        model.addAttribute("topNotes", allNotes.stream().filter(n -> n.getType() == NoteType.TOP).toList());
        model.addAttribute("heartNotes", allNotes.stream().filter(n -> n.getType() == NoteType.HEART).toList());
        model.addAttribute("baseNotes", allNotes.stream().filter(n -> n.getType() == NoteType.BASE).toList());

        return "admin/product-form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") DtoAdminProductRequest dto) {
        if (dto.getId() == null) {
            productService.addProduct(dto);     // CREATE
        } else {
            productService.updateProduct(dto);  // UPDATE
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
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
        dto.setImageUrl(product.getImageUrl());
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

        model.addAttribute("categories", categoryService.findAll());

        List<Note> allNotes = noteService.findAll();
        model.addAttribute("topNotes",   allNotes.stream().filter(n -> n.getType() == NoteType.TOP).toList());
        model.addAttribute("heartNotes", allNotes.stream().filter(n -> n.getType() == NoteType.HEART).toList());
        model.addAttribute("baseNotes",  allNotes.stream().filter(n -> n.getType() == NoteType.BASE).toList());

        return "admin/product-form";
    }

    @GetMapping("/{id}/stock")
    public String showStockForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("stockForm", new StockForm());
        return "admin/product-stock";
    }

    @PostMapping("/{id}/stock")
    public String updateStock(
            @PathVariable Long id,
            @RequestParam("amount") int amount) {
        if (amount <= 0) {
            return "redirect:/admin/products/" + id + "/stock";
        }

        productService.increaseStock(id, amount);
        return "redirect:/admin/dashboard";
    }

    @GetMapping(params = {"column", "content"})
    public String filterProducts(@RequestParam(required = false) String column,
                                 @RequestParam(required = false) String content,
                                 Model model) {
        if (content == null || content.isBlank() || column.equals("all")) {
            model.addAttribute("products", productService.getProductList());
        } else {
            model.addAttribute("products", productService.filterProduct(column, content));
        }
        return "admin/product-list";
    }
}
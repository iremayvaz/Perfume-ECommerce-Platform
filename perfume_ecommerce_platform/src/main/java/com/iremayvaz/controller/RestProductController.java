package com.iremayvaz.controller;

import com.iremayvaz.model.dto.response.ProductDetailResponse;
import com.iremayvaz.model.dto.response.ProductResponse;
import com.iremayvaz.repository.ProductRepository;
import com.iremayvaz.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Product API", description = "Ürün işlemleri")
@RestController
@RequestMapping("/perfume")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RestProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @Operation(description = "Tüm ürünleri listele")
    @GetMapping("/list")
    public List<ProductResponse> getProductList(){
        return productService.getProductList();
    }

    @Operation(description = "Filtrelenen ürünleri listele")
    @GetMapping("/filter")
    public List<ProductResponse> filterProduct(@RequestParam(required = false) String column,
                                               @RequestParam(required = false) String content) {
        return productService.filterProduct(column, content);
    }

    @Operation(description = "Ürün bilgisi al")
    @GetMapping("/{id}")
    public ProductDetailResponse getProductInfo(@PathVariable(value = "id") @NotNull Long id) {
        return productService.getProductInfo(id);
    }

    @GetMapping("/filter-options")
    public ResponseEntity<Map<String, Object>> getFilterOptions() {
        Map<String, Object> options = productService.getFilterOptions();
        return ResponseEntity.ok(options);
    }
}

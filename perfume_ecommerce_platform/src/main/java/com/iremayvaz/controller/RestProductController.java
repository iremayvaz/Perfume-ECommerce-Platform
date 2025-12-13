package com.iremayvaz.controller;

import com.iremayvaz.model.dto.response.ProductDetailResponse;
import com.iremayvaz.model.dto.response.ProductResponse;
import com.iremayvaz.service.ProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfume")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RestProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public List<ProductResponse> getProductList(){
        return productService.getProductList();
    }

    @GetMapping("/filter")
    public List<ProductResponse> filterProduct(@RequestParam(required = false) String column,
                                               @RequestParam(required = false) String content) {
        return productService.filterProduct(column, content);
    }

    @GetMapping("/{id}")
    public ProductDetailResponse getProductInfo(@PathVariable(value = "id") @NotNull Long id) {
        return productService.getProductInfo(id);
    }

}

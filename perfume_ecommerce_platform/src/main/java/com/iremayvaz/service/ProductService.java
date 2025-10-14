package com.iremayvaz.service;

import com.iremayvaz.model.dto.ProductDetailResponse;
import com.iremayvaz.model.dto.ProductResponse;
import com.iremayvaz.model.entity.Product;
import com.iremayvaz.repository.ProductRepository;
import com.iremayvaz.repository.specs.ProductSpecifications;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    ProductRepository productRepository;

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
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        Optional<Product> optional = productRepository.findById(id);

        if (optional.isPresent()){
            BeanUtils.copyProperties(optional.get(), productDetailResponse);
            return productDetailResponse;
        } else {
            throw new IllegalArgumentException("Id ile kayıtlı ürün yok!");
        }
    }
}

package com.ilham.products_app.product;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.ilham.products_app.dto.BaseResponse;
import com.ilham.products_app.dto.PageableRequest;
import com.ilham.products_app.dto.PageableResponse;
import com.ilham.products_app.dto.ProductRequest;
import com.ilham.products_app.dto.ProductResponse;
import com.ilham.products_app.exception.NotFoundException;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public BaseResponse findAll(PageableRequest request) {
        PageRequest pagination = PageRequest.of(request.getPage(), request.getSize(), request.getSort(), "code");
        Page<Product> products = productRepository.findAll(pagination);

        List<ProductResponse> productsList = Optional.ofNullable(products)
            .map(Slice::getContent)
            .orElse(Collections.emptyList())
            .stream()
            .map(product -> mapToResponse(product, new ProductResponse()))
            .collect(Collectors.toList());

        return BaseResponse.builder()
            .data(productsList)
            .page(getData(products))
            .build();
    }

    public BaseResponse get(String code) {
        return BaseResponse.builder()
            .data(productRepository.findById(code)
                .map(product -> mapToResponse(product, new ProductResponse()))
                .orElseThrow(NotFoundException::new))
            .build();
    }

    public BaseResponse create(ProductRequest productDTO) {
        Product product = new Product();
        mapToEntity(productDTO, product);
        product.setCode(productDTO.getCode());
        return BaseResponse.builder()
            .data(productRepository.save(product).getCode())
            .build();
    }

    public BaseResponse update(String code, ProductRequest productDTO) {
        Product product = productRepository.findById(code)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        return BaseResponse.builder()
            .data(mapToResponse(productRepository.save(product), new ProductResponse()))
            .build();
    }

    public void delete(String code) {
        productRepository.deleteById(code);
    }

    private ProductResponse mapToResponse(Product product, ProductResponse productDTO) {
        productDTO.setCode(product.getCode());
        productDTO.setName(product.getName());
        productDTO.setCategory(product.getCategory());
        productDTO.setBrand(product.getBrand());
        productDTO.setType(product.getType());
        productDTO.setDescription(product.getDescription());
        return productDTO;
    }

    private Product mapToEntity(ProductRequest productDTO, Product product) {
        product.setName(productDTO.getName());
        product.setCategory(productDTO.getCategory());
        product.setBrand(productDTO.getBrand());
        product.setType(productDTO.getType());
        product.setDescription(productDTO.getDescription());
        return product;
    }

    public boolean codeExists(String code) {
        return productRepository.existsByCodeIgnoreCase(code);
    }

    private PageableResponse getData(Page<?> page) {
        int start = page.getSize() * page.getNumber();
        int end = start + page.getNumberOfElements();
        return PageableResponse.builder()
            .page(page.getNumber())
            .start(end == 0 ? 0 : start + 1)
            .end(end)
            .total(page.getTotalElements())
            .build();
    }

}

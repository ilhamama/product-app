package com.ilham.products_app.product;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilham.products_app.dto.BaseResponse;
import com.ilham.products_app.dto.PageableRequest;
import com.ilham.products_app.dto.ProductRequest;


@RestController
@RequestMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<BaseResponse> getAllProducts(@ModelAttribute @Valid PageableRequest request) {
        BaseResponse response = productService.findAll(request);
        response.setStatus(HttpStatus.OK.name());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{code}")
    public ResponseEntity<BaseResponse> getProduct(@PathVariable final String code) {
        BaseResponse response = productService.get(code);
        response.setStatus(HttpStatus.OK.name());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createProduct(@RequestBody @Valid final ProductRequest request,
            final BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (!bindingResult.hasFieldErrors("code") && request.getCode() == null) {
            bindingResult.rejectValue("code", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("code") && productService.codeExists(request.getCode())) {
            bindingResult.rejectValue("code", "Exists.product.code");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethods()[0], -1), bindingResult);
        }
        BaseResponse response = productService.create(request);
        response.setStatus(HttpStatus.CREATED.name());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<BaseResponse> updateProduct(@PathVariable final String code,
            @RequestBody @Valid final ProductRequest request) {
        BaseResponse response = productService.update(code, request);
        response.setStatus(HttpStatus.OK.name());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final String code) {
        productService.delete(code);
        return ResponseEntity.noContent().build();
    }

}

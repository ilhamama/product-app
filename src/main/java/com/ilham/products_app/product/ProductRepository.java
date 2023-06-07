package com.ilham.products_app.product;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, String> {

    boolean existsByCodeIgnoreCase(String code);

}

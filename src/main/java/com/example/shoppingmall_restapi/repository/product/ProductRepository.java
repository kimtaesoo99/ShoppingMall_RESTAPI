package com.example.shoppingmall_restapi.repository.product;

import com.example.shoppingmall_restapi.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}

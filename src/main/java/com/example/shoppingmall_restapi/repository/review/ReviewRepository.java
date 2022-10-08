package com.example.shoppingmall_restapi.repository.review;

import com.example.shoppingmall_restapi.entity.product.Product;
import com.example.shoppingmall_restapi.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findAllByProduct(Product product, Pageable pageable);
}

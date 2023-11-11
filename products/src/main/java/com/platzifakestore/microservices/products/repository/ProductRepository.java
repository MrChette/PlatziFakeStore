package com.platzifakestore.microservices.products.repository;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.platzifakestore.microservices.products.entity.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Serializable>{

}

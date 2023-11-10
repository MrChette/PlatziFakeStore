package com.platzifakestore.microservices.products.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	
    private long id;
    private String title;
    private double price;
    private String description;
    private List<String> images;
    private String createdAt;
    private String updatedAt;
    private Category category;

   
}


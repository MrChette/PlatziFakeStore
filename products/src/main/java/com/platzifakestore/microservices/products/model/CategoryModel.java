package com.platzifakestore.microservices.products.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
	
	private long id;
    private String name;
    private String image;
    private String createdAt;
    private String updatedAt;

}

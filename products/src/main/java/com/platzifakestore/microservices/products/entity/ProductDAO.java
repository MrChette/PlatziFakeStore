package com.platzifakestore.microservices.products.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDAO {
	
	@Id
	@Column(name="id")
	private long id;
	
	@Column(name="title", unique = false, nullable = false)
	private String title;
	
	@Column(name="price", unique = false, nullable = false)
	private double price;
	
	@Column(name="description", unique = false, nullable = false)
	private String description;
	
	@Column(name="images", unique = false, nullable = false)
	private List<String> images;
	
	@Column(name="createdAt", unique = false, nullable = false)
	private String createdAt;
	
	@Column(name="updatedAt", unique = false, nullable = false)
	private String updatedAt;
	
	@ManyToOne 
	@JoinColumn(name="category_id", nullable=false) 
	private CategoryDAO category;
	

}

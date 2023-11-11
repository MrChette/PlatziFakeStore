package com.platzifakestore.microservices.products.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.platzifakestore.microservices.products.entity.Product;
import com.platzifakestore.microservices.products.serviceImpl.ProductServiceImpl;


@RestController
@RequestMapping("/api")
public class ProductController {
	
	private final RestTemplate restTemplate = new RestTemplate();

	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductServiceImpl productService;
	
	
	/**
    * Endpoint para obtener productos remotos desde una API externa (PlatziFakeStore).
    *
    * @return Lista de productos obtenidos desde la API remota.
    */
	@GetMapping("/products")
	@ResponseBody
	public List<Product> obtenerProductosRemotos() {
		String apiUrl = "https://api.escuelajs.co/api/v1/products";
	    ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(apiUrl, Product[].class);
	    Product[] products = responseEntity.getBody();
	    return Arrays.asList(products);
	}

}

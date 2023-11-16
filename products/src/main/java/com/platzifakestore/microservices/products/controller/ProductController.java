package com.platzifakestore.microservices.products.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.platzifakestore.microservices.products.entity.Category;
import com.platzifakestore.microservices.products.entity.Product;
import com.platzifakestore.microservices.products.serviceImpl.CategoryServiceImpl;
import com.platzifakestore.microservices.products.serviceImpl.ProductServiceImpl;


@RestController
@RequestMapping("/api")
public class ProductController {
	
	private static final String API_BASE_URL = "https://api.escuelajs.co/api/v1/products";
	
	private final RestTemplate restTemplate = new RestTemplate();

	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductServiceImpl productService;
	
	
	@Autowired
	@Qualifier("categoryServiceImpl")
	private CategoryServiceImpl categoryService;
	
	
	/**
	 * Endpoint para obtener todos los productos desde (PlatziFakeStore).
	 *
	 * @return ResponseEntity con la lista de productos si la respuesta es OK,
	 *         o un ResponseEntity con un cuerpo vacío y el código de estado correspondiente en caso de error.
	 */
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
	    String apiUrl = API_BASE_URL;
	    try {
	        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(apiUrl, Product[].class);

	        // Verificar si el estado de la respuesta es OK (200)
	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        	Product[] productos = responseEntity.getBody();
	            return ResponseEntity.ok(Arrays.asList(productos));
	        } else {
	            // Manejar códigos de estado no OK
	            System.err.println("Error: " + responseEntity.getStatusCode());
	            return ResponseEntity.status(responseEntity.getStatusCode()).body(Collections.emptyList());
	        }
	    } catch (RestClientException e) {
	        // Manejar otras RestClientExceptions
	        System.err.println("Excepción del cliente REST: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
	}
	
	
	/**
	 * Endpoint para obtener un producto por ID de la API (PlatziFakeStore).
	 *
	 * @param id El identificador del producto.
	 * @return ResponseEntity con el producto si la respuesta es OK,
	 *         o un ResponseEntity con un cuerpo vacío y el código de estado correspondiente en caso de error.
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		String apiUrl = API_BASE_URL+"/"+id;
	    try {
	        ResponseEntity<Product> responseEntity = restTemplate.getForEntity(apiUrl, Product.class, id);
	        
	        // Verificar si el estado de la respuesta es OK (200)
	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        	Product productos = responseEntity.getBody();
	            return ResponseEntity.ok(productos);
	        } else {
	            // Manejar códigos de estado no OK
	            System.err.println("Error: " + responseEntity.getStatusCode());
	            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
	        }
	    } catch (RestClientException e) {
	        // Manejar otras RestClientExceptions
	        System.err.println("Excepción del cliente REST: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@GetMapping("/productstest/{id}")
	public ResponseEntity<Product> getProductByIdtest(@PathVariable Long id) {
		String apiUrl = API_BASE_URL+"/"+id;
	    try {
	        ResponseEntity<Product> responseEntity = restTemplate.getForEntity(apiUrl, Product.class, id);
	        
	        // Verificar si el estado de la respuesta es OK (200)
	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        	Product productos = responseEntity.getBody();
	        	categoryService.addEntity(categoryService.transformToModel(productos.getCategory()));
	        	productService.addEntity(productService.transformToModel(productos));
	            return ResponseEntity.ok(productos);
	        } else {
	            // Manejar códigos de estado no OK
	            System.err.println("Error: " + responseEntity.getStatusCode());
	            return ResponseEntity.status(responseEntity.getStatusCode()).body(null);
	        }
	    } catch (RestClientException e) {
	        // Manejar otras RestClientExceptions
	        System.err.println("Excepción del cliente REST: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@GetMapping("/products/update")
	public ResponseEntity<?> updateProductList(){
		String apiUrl= API_BASE_URL;
		
		try {
	        ResponseEntity<Product[]> responseEntity = restTemplate.getForEntity(apiUrl, Product[].class);

	        // Verificar si el estado de la respuesta es OK (200)
	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        	ArrayList<Product> products = new ArrayList<Product>();
	        	Product[] productos = responseEntity.getBody();
	        	products.addAll(Arrays.asList(productos));

	        	/* Await sobre updateCategories 
	        	 *(para evitar ejecutarse de forma asincrona al ser operaciones de red que pueden tartar)
	        	 */ 
	            CompletableFuture<Void> updateCategoryFuture = CompletableFuture.runAsync(() -> {
	            	updateCategories(productos);
	            });

	            try {
	            	// Esperar a que se complete la actualización de la categoría
	            	updateCategoryFuture.get();
	                // Guardar los productos después de que la actualización de la categoría se haya completado
	            	productService.saveAllEntities(products);
	             } catch (InterruptedException | ExecutionException e) {
	                 e.printStackTrace();
	             }
	            return ResponseEntity.ok().body(products);
	        } else {
	            // Manejar códigos de estado no OK
	            System.err.println("Error: " + responseEntity.getStatusCode());
	            return ResponseEntity.status(responseEntity.getStatusCode()).body(Collections.emptyList());
	        }
	    } catch (RestClientException e) {
	        // Manejar otras RestClientExceptions
	        System.err.println("Excepción del cliente REST: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
		
	}
	
	
	//Update all categories 
	private void updateCategories(Product[] productos) {
		ArrayList<Category> categoryList = new ArrayList<Category>();
		for(Product p: productos) {
			if(categoryService.findEntityById(p.getCategory().getId()) == null) {
				categoryList.add(p.getCategory());		
			}
		}
		categoryService.saveAllEntities(categoryList);
	}

}

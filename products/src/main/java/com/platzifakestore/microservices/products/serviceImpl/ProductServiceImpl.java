package com.platzifakestore.microservices.products.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.platzifakestore.microservices.products.entity.Product;
import com.platzifakestore.microservices.products.model.ProductModel;
import com.platzifakestore.microservices.products.repository.ProductRepository;
import com.platzifakestore.microservices.products.service.GenericService;

import jakarta.persistence.EntityNotFoundException;



@Service("productServiceImpl")
public class ProductServiceImpl implements GenericService<Product, ProductModel, Long>{
	
	@Autowired
	@Qualifier("productRepository")
	private ProductRepository productRepository;

	
	/**
	* Agrega un nuevo producto a la base de datos utilizando la información proporcionada en el modelo.
	*
	* @param model El modelo que contiene la información para agregar el nuevo producto.
	* @return El producto recién creado después de la operación de agregado.
	*/
	@Override
	public Product addEntity(ProductModel model) {
		return productRepository.save(transform(model));
	}

	
	/**
	* Elimina un producto de la base de datos según su ID (en caso de que exista).
	*
	* @param id El ID del producto que se va a eliminar.
	* @return 'true' si se eliminó el producto correctamente, 'false' si el producto no existía en la base de datos.
	*/
	@Override
	public boolean removeEntity(Long id) {
		if(!productRepository.existsById(id)) {
			productRepository.deleteById(id);
			return true;
		}
		return false;
	}
	

	/**
	* Actualiza un producto (en caso de que exista) en la base de datos con la información proporcionada en el modelo.
	*
	* @param model El modelo que contiene la información para la actualización.
	* @return El producto actualizado después de la operación de actualización.
	* @throws EntityNotFoundException Si no se encuentra un producto con el ID proporcionado en la base de datos.
	*/
	@Override
	public Product updateEntity(ProductModel model) {
		if (!productRepository.existsById(model.getId())) {
	        throw new EntityNotFoundException("Product not found");
	    }
		Product updatedProduct = transform(model);
		return productRepository.save(updatedProduct);
	}

	
	/**
	* Devuelve el producto (en caso de que exista)
	*
	* @return El producto actualizado después de la operación de actualización.
	* @throws EntityNotFoundException Si no se encuentra un producto con el ID proporcionado en la base de datos.
	*/
	@Override
	public Product findEntityById(Long id) {
		return productRepository.findById(id)
                .orElse(null);
	}

	
	/**
	 * Anulado 
	 */
	@Override
	public ProductModel findModelById(Long id) {
		return null;
	}

	
	/**
	* Convierte un modelo de producto (ProductModel) en una entidad de producto (Product).
	*
	* @param model El modelo de producto que se va a convertir.
	* @return La entidad de producto resultante después de la conversión.
	*/
	@Override
	public Product transform(ProductModel model) {
		ModelMapper mp = new ModelMapper();
		return mp.map(model, Product.class);
		
	}

	
	/**
	* Convierte un entity de producto (Product) en un model de producto (ProductModel).
	*
	* @param entity El entity de producto que se va a convertir.
	* @return El model de producto resultante después de la conversión.
	*/
	@Override
	public ProductModel transformToModel(Product entity) {
		ModelMapper mp = new ModelMapper();
		return mp.map(entity, ProductModel.class);
	}

	
	/**
	* Obtiene una lista de todos los modelos de productos disponibles en la base de datos.
	*
	* @return Lista de modelos de productos.
	*/
	@Override
	public List<ProductModel> listAll() {
		return productRepository.findAll().stream()
				.map(p-> transformToModel(p)).collect(Collectors.toList());
	}


	@Override
	public List<Product> saveAllEntities(List<Product> models) {
		List<Product> savedProducts = productRepository.saveAll(models);
		return savedProducts;
	}




}

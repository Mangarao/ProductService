package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceController {
	private static Map<String, Product> productRepo = new HashMap<>();
	static {
		Product cookie = new Product();
		cookie.setId("1"); 
		
		cookie.setName("Honey");
		cookie.setPrice(20.00);
		productRepo.put(cookie.getId(), cookie);

		Product almond = new Product();
		almond.setId("2");
		almond.setName("Almond");
		almond.setPrice(40);
		productRepo.put(almond.getId(), almond);
	}

	/**
	 * Method to get all products
	 * 
	 * @return
	 */
	@RequestMapping(value = "/products")
	public ResponseEntity<Object> getAllProducts() {
		return new ResponseEntity<>(productRepo.values(), HttpStatus.OK);
	}

	/**
	 * Method to get product details of given Id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getProducts(@PathVariable("id") String id) {
		
		return new ResponseEntity<>(productRepo.get(id), HttpStatus.OK);
	}

	/**
	 * Method to add new product
	 * 
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	public ResponseEntity<Object> createProduct(@RequestBody Product product) {
		if (product.getId() != null) {
			productRepo.put(product.getId(), product);
			return new ResponseEntity<>("Product is created successfully", HttpStatus.CREATED);
		}
		return new ResponseEntity<>("Product is not created. Please enter product details correctly",
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Method to update product details for the given id
	 * 
	 * @param id
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
		try {
			productRepo.remove(id);
			product.setId(id);
			productRepo.put(id, product);
			return new ResponseEntity<>("Product is updated successsfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Product is not updated due to technical error",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Method to delete product for given id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete(@PathVariable("id") String id) {
		try {
			if (productRepo.containsKey(id)) {
				productRepo.remove(id);
				return new ResponseEntity<>("Product is deleted successsfully", HttpStatus.OK);
			}
		} catch (Exception e) {
			// Exception is not handled
		}
		return new ResponseEntity<>("Product doesn't exit to delete", HttpStatus.OK);

	}

	/**
	 * Method to get product details of given Id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/productsWithRate/{price}", method = RequestMethod.GET)
	public ResponseEntity<Object> getProductsCount(@PathVariable("price") double price) {
		Map<String, Product> productsWithSameRate = new HashMap<>();
		for (Product product : productRepo.values()) {
			if (product.getPrice() == price) {
				productsWithSameRate.put(product.getId(), product);
			}
		}
		if (productsWithSameRate.size() > 0) {
			return new ResponseEntity<>(productsWithSameRate, HttpStatus.OK);
		}

		return new ResponseEntity<>("No products available with this rate", HttpStatus.OK);
	}
}

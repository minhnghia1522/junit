package com.sysexevn.sunshinecity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysexevn.sunshinecity.dto.ProductDto;
import com.sysexevn.sunshinecity.exception.NotFoundException;
import com.sysexevn.sunshinecity.service.IProductService;

@RestController
@RequestMapping("product")
public class ProductController {
	@Autowired
	public IProductService service;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody ProductDto p) {
		ProductDto result = service.save(p);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		ProductDto result = service.getById(id);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/get-list")
	public ResponseEntity<?> getList() {
		List<ProductDto> results = service.getList();
		return ResponseEntity.ok(results);
	}

	@PatchMapping
	public ResponseEntity<?> update(@RequestBody ProductDto p) {
		ProductDto result = service.update(p);
		if (ObjectUtils.isEmpty(result)) {
			throw new NotFoundException();
		}
		return ResponseEntity.ok(result);
	}

	@DeleteMapping
	public ResponseEntity<?> delete(@RequestParam int id) {
		service.delete(id);
		return ResponseEntity.ok("Deleted");
		
	}

}

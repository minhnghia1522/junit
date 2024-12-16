package com.sysexevn.sunshinecity.service;

import java.util.List;

import com.sysexevn.sunshinecity.dto.ProductDto;

public interface IProductService {

	ProductDto save(ProductDto pDto);

	ProductDto getById(int id);

	List<ProductDto> getList();
	
	ProductDto update(ProductDto pDto);
	
	ProductDto updateNewPrice(ProductDto newProductDto);
	
	void delete(ProductDto pDto);
}

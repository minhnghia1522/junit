package com.sysexevn.sunshinecity.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.seasar.doma.jdbc.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sysexevn.sunshinecity.converter.ProductConverter;
import com.sysexevn.sunshinecity.dao.IProductDao;
import com.sysexevn.sunshinecity.dto.ProductDto;
import com.sysexevn.sunshinecity.entity.Product;
import com.sysexevn.sunshinecity.exception.BadRequestException;
import com.sysexevn.sunshinecity.exception.NotFoundException;
import com.sysexevn.sunshinecity.service.IProductService;

@Service
public class ProductServiceImp implements IProductService {

	@Autowired
	private IProductDao repository;

	@Autowired
	private ProductConverter mapper;

	@Override
	public ProductDto save(ProductDto pDto) {
		Product product = mapper.convert(pDto);
		Product result = repository.insert(product).getEntity();
		return mapper.convert(result);
	}

	@Override
	public ProductDto getById(int id) {
		Product result = repository.selectById(id).orElseThrow(NotFoundException::new);
		return mapper.convert(result);

	}

	@Override
	public List<ProductDto> getList() {
		List<Product> results = repository.selectAll();
		List<ProductDto> productDtoes = results.stream().map(p -> mapper.convert(p)).collect(Collectors.toList());
		return productDtoes;
	}

	@Override
	public ProductDto update(ProductDto pDto) {
		Result<Product> result = repository.update(mapper.convert(pDto));
		if (result.getCount() > 0) {
			return mapper.convert(result.getEntity());
		}
		throw new NotFoundException();
	}

	@Override
	public void delete(ProductDto pDto) {
		Result<Product> result = repository.delete(mapper.convert(pDto));
		if (result.getCount() < 1) {
			throw new NotFoundException();
		}
	}

	@Override
	public ProductDto updateNewPrice(ProductDto newProductDto) {
		// Check price must greater > 0
		if (newProductDto.getNewPrice().compareTo(BigDecimal.ZERO) < 0) {
			throw new BadRequestException();
		}
		Optional<Product> oldProductOptional = repository.selectById(newProductDto.getId());
		if (oldProductOptional.isEmpty()) {
			throw new NotFoundException();
		}
		Product product = oldProductOptional.get();
		newProductDto.setOldPrice(product.getNewPrice());
		// update
		Product productEntity = mapper.convert(newProductDto);
		Product ressult = repository.update(productEntity).getEntity();
		return mapper.convert(ressult);
	}
}

package com.sysexevn.sunshinecity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seasar.doma.jdbc.Result;

import com.sysexevn.sunshinecity.converter.ProductConverter;
import com.sysexevn.sunshinecity.dao.IProductDao;
import com.sysexevn.sunshinecity.dto.ProductDto;
import com.sysexevn.sunshinecity.entity.Product;
import com.sysexevn.sunshinecity.exception.BadRequestException;
import com.sysexevn.sunshinecity.exception.NotFoundException;
import com.sysexevn.sunshinecity.service.impl.ProductServiceImp;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest2 {

	@InjectMocks
	public ProductServiceImp service;
	@Mock
	private IProductDao productDao;
	@Spy
	private ProductConverter productConverter = Mappers.getMapper(ProductConverter.class);

	ProductDto productDto;
	Product productEnity;

	@Test
	public void callUpdatePriceDefault() {
		// Create mock data
		productDto = ProductDto.builder().id(1).newPrice(BigDecimal.valueOf(100000)).build();
		productEnity = new Product(1, null, null, null, BigDecimal.valueOf(50000), BigDecimal.valueOf(100000), null);
		// Behavior definition
		lenient().when(productDao.selectById(anyInt())).thenReturn(Optional.of(productEnity));
		lenient().when(productDao.update(any(Product.class))).thenReturn(new Result<Product>(1, productEnity));
		// Call method
		ProductDto result = service.updateNewPrice(productDto);
		// Check the result
		assertEquals(result.getNewPrice(), productDto.getNewPrice());
	}

	@Test
	public void callUpdateProductNotFound() {
		// Create mock data
		productDto = ProductDto.builder().id(1).newPrice(BigDecimal.valueOf(100000)).build();
		productEnity = new Product(1, null, null, null, BigDecimal.valueOf(50000), BigDecimal.valueOf(100000), null);
		// Create mock data
		lenient().when(productDao.selectById(anyInt())).thenReturn(Optional.ofNullable(null));
		// Call method & Check the result
		assertThrows(NotFoundException.class, () -> service.updateNewPrice(productDto));
	}

	@Test
	public void callUpdatePriceLessthanzero() {
		productDto = ProductDto.builder().id(1).newPrice(BigDecimal.valueOf(-10000)).build();
		// Call method & Check the result
		assertThrows(BadRequestException.class, () -> service.updateNewPrice(productDto));
	}

}

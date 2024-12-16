package com.sysexevn.sunshinecity.dao;

import java.util.List;
import java.util.Optional;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.Result;
import org.springframework.transaction.annotation.Transactional;

import com.sysexevn.sunshinecity.entity.Product;

@Dao
@ConfigAutowireable
@Transactional
public interface IProductDao {

	@Insert
	public Result<Product> insert(Product p);

	@Select
	public Optional<Product> selectById(Integer id);

	@Select
	public List<Product> selectAll();
	
	@Update
	public Result<Product> update(Product p);

	@Update
	public Result<Product> updateNewPrice(Product p);

	@Delete(sqlFile = true)
	public Result<Product> delete(Product p);
	
	

}

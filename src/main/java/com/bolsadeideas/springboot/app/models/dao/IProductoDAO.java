package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Repository
public interface IProductoDAO extends CrudRepository<Producto, Long> {

	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> buscarPorNombre(String term);

}

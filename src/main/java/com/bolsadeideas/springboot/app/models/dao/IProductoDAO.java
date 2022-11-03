package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Repository
public interface IProductoDAO extends CrudRepository<Producto, Long> {

	// Primera Opcion con query codificada manualmente
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> buscarPorNombre(String term);

	// Segunda Opcion con query prefedinidas
	public List<Producto> findByNombreLikeIgnoreCase(String term);
}

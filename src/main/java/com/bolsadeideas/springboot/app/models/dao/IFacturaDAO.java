package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.bolsadeideas.springboot.app.models.entity.Factura;

@Repository
public interface IFacturaDAO extends CrudRepository<Factura, Long>{
	
	@Query("SELECT f FROM Factura f JOIN FETCH f.cliente c JOIN FETCH f.items l JOIN FETCH l.producto WHERE f.id = ?1")
	public Factura  fetchByIdWithClienteWhitItemFacturaWithProducto(Long id);

}

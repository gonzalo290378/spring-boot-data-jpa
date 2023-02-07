package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Repository
//Aca Implementamos PagingAndSortingRepository es una clase para implementar Paginatori, 
//sin olvidarnos de CrudRepository ya que PagingAndSortingRepository lo hereda y no tenemos problema en heredar todos los metodos automaticos de JPA
public interface IClienteDAO extends PagingAndSortingRepository<Cliente, Long> {

	@Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.facturas f WHERE c.id = ?1")
	public Cliente fetchByIdWithFacturas(Long id);
}

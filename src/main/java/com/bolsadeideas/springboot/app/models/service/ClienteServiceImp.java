package com.bolsadeideas.springboot.app.models.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bolsadeideas.springboot.app.models.dao.IClienteDAO;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDAO;
import com.bolsadeideas.springboot.app.models.dao.IProductoDAO;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImp implements IClienteService {
	
	@Autowired
	private IClienteDAO clienteDAO;
	
	@Autowired
	private IProductoDAO productoDAO;

	@Autowired
	private IFacturaDAO facturaDAO;
	
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDAO.findAll();
	}

	@Transactional
	public void save(Cliente cliente) {
		clienteDAO.save(cliente);
	}

	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		return clienteDAO.findById(id).orElse(null);
	}

	@Transactional
	public void delete(Long id) {
		clienteDAO.deleteById(id);
	}

	@Transactional(readOnly = true)
 	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDAO.findAll(pageable);
	}

	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		return productoDAO.findByNombreLikeIgnoreCase(term);
	}

	@Transactional
	public void saveFactura(Factura factura) {
		facturaDAO.save(factura);
	}

	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		return productoDAO.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return facturaDAO.findById(id).orElse(null);
	}

	@Transactional
	public void deleteFactura(Long id) {
		facturaDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchByIdWithClienteWhitItemFacturaWithProducto(Long id) {
		return facturaDAO.fetchByIdWithClienteWhitItemFacturaWithProducto(id);
	}
	
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		return clienteDAO.fetchByIdWithFacturas(id);
	}
	
	

}

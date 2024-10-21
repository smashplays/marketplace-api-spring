package com.eoi.es.marketplace.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eoi.es.marketplace.dto.PedidoDto;
import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.dto.ArticuloCantidadDto;
import com.eoi.es.marketplace.dto.ArticuloDto;
import com.eoi.es.marketplace.entity.Pedido;
import com.eoi.es.marketplace.entity.PedidoArticulo;
import com.eoi.es.marketplace.entity.Usuario;
import com.eoi.es.marketplace.entity.Articulo;
import com.eoi.es.marketplace.repository.PedidoRepository;
import com.eoi.es.marketplace.repository.UsuarioRepository;
import com.eoi.es.marketplace.repository.ArticuloRepository;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ArticuloRepository articuloRepository;

	public PedidoDto findById(String id) {
	    Pedido entity = pedidoRepository.findById(Integer.valueOf(id)).orElse(null);

	    if (entity == null) {
	        return null;
	    }

	    PedidoDto pedidoDto = new PedidoDto();
	    pedidoDto.setId(String.valueOf(entity.getId()));
	    pedidoDto.setFecha(entity.getFecha());
	    pedidoDto.setNombre(entity.getNombre());
	    pedidoDto.setUsuario_id(String.valueOf(entity.getUsuario().getId()));
	    pedidoDto.setArticulos(convertToArticuloCantidadDtoList(entity.getArticulos()));

	    double total = 0.0;
	    for (PedidoArticulo pedidoArticulo : entity.getArticulos()) {
	        Articulo articulo = pedidoArticulo.getArticulo();
	        total += articulo.getPrecio() * pedidoArticulo.getCantidad();
	    }
	    pedidoDto.setTotal(total);

	    return pedidoDto;
	}

	public List<PedidoDto> findAll() {
	    List<PedidoDto> dtos = new ArrayList<PedidoDto>();
	    List<Pedido> entities = pedidoRepository.findAll();

	    for (Pedido pedido : entities) {
	        PedidoDto dto = new PedidoDto();
	        dto.setId(String.valueOf(pedido.getId()));
	        dto.setFecha(pedido.getFecha());
	        dto.setNombre(pedido.getNombre());
	        dto.setUsuario_id(String.valueOf(pedido.getUsuario().getId()));
	        dto.setArticulos(convertToArticuloCantidadDtoList(pedido.getArticulos()));

	        double total = 0.0;
	        for (PedidoArticulo pedidoArticulo : pedido.getArticulos()) {
	            Articulo articulo = pedidoArticulo.getArticulo();
	            total += articulo.getPrecio() * pedidoArticulo.getCantidad();
	        }
	        dto.setTotal(total);

	        dtos.add(dto);
	    }

	    return dtos;
	}


	private List<ArticuloCantidadDto> convertToArticuloCantidadDtoList(List<PedidoArticulo> pedidoArticulos) {
	    List<ArticuloCantidadDto> dtos = new ArrayList<>();
	    for (PedidoArticulo pedidoArticulo : pedidoArticulos) {
	        ArticuloCantidadDto dto = new ArticuloCantidadDto();
	        dto.setId(String.valueOf(pedidoArticulo.getArticulo().getId()));
	        dto.setNombre(pedidoArticulo.getArticulo().getNombre());
	        dto.setCantidad(pedidoArticulo.getCantidad() != null ? pedidoArticulo.getCantidad() : 0);
	        dtos.add(dto);
	    }
	    return dtos;
	}

	public List<PedidoDto> findByNombreParcial(String nombre) {
		List<PedidoDto> dtos = new ArrayList<>();
		List<Pedido> entities = pedidoRepository.findByNombreContainingIgnoreCase(nombre);

		for (Pedido pedido : entities) {
			PedidoDto dto = new PedidoDto();
			dto.setId(String.valueOf(pedido.getId()));
			dto.setFecha(pedido.getFecha());
			dto.setNombre(pedido.getNombre());
			dto.setArticulos(convertToArticuloCantidadDtoList(pedido.getArticulos()));

			Usuario usuario = pedido.getUsuario();
			if (usuario != null) {
				UsuarioDto usuarioDto = new UsuarioDto();
				usuarioDto.setId(String.valueOf(usuario.getId()));
				usuarioDto.setNombre(usuario.getNombre());
				usuarioDto.setPassword(usuario.getPassword());
				dto.setUsuario_id(usuarioDto.getId());
			}

			dtos.add(dto);
		}

		return dtos;
	}

	public void deleteById(String id) {
	    Pedido pedido = pedidoRepository.findById(Integer.valueOf(id))
	        .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

	    Usuario usuario = pedido.getUsuario();
	    
	    if (usuario != null && usuario.getCantidadPedidos() > 0) {
	        usuario.setCantidadPedidos(usuario.getCantidadPedidos() - 1);
	        usuarioRepository.save(usuario);
	    }

	    for (PedidoArticulo pedidoArticulo : pedido.getArticulos()) {
	        Articulo articulo = pedidoArticulo.getArticulo();
	        articulo.setStock(articulo.getStock() + pedidoArticulo.getCantidad());

	        if (articulo.getVecesPedido() > 0) {
	            articulo.setVecesPedido(articulo.getVecesPedido() - pedidoArticulo.getCantidad());
	        }

	        articuloRepository.save(articulo);
	    }

	    pedidoRepository.deleteById(Integer.valueOf(id));
	}




	public void create(PedidoDto dto) {
	    Pedido entity = new Pedido();
	    entity.setFecha(dto.getFecha());
	    entity.setNombre(dto.getNombre());

	    Usuario usuario = usuarioRepository.findById(Integer.valueOf(dto.getUsuario_id())).orElse(null);
	    if (usuario != null) {
	        entity.setUsuario(usuario);
	        
	        usuario.setCantidadPedidos(usuario.getCantidadPedidos() + 1);
	        usuarioRepository.save(usuario);
	    }

	    Map<Integer, PedidoArticulo> articuloMap = new HashMap<>();

	    for (ArticuloCantidadDto articuloCantidad : dto.getArticulos()) {
	        Articulo articulo = articuloRepository.findById(Integer.valueOf(articuloCantidad.getId())).orElse(null);
	        if (articulo != null) {
	            if (articulo.getStock() >= articuloCantidad.getCantidad()) {
	                
	                articulo.setStock(articulo.getStock() - articuloCantidad.getCantidad());
	                articulo.setVecesPedido(articulo.getVecesPedido() + articuloCantidad.getCantidad());

	                articuloRepository.save(articulo);

	                PedidoArticulo pedidoArticulo = articuloMap.get(articulo.getId());
	                if (pedidoArticulo != null) {
	                    pedidoArticulo.setCantidad(pedidoArticulo.getCantidad() + articuloCantidad.getCantidad());
	                } else {
	                    pedidoArticulo = new PedidoArticulo();
	                    pedidoArticulo.setPedido(entity);
	                    pedidoArticulo.setArticulo(articulo);
	                    pedidoArticulo.setCantidad(articuloCantidad.getCantidad());

	                    articuloMap.put(articulo.getId(), pedidoArticulo);
	                }
	            }
	        }
	    }

	    entity.setArticulos(new ArrayList<>(articuloMap.values()));
	    pedidoRepository.save(entity);
	}



	public void update(PedidoDto dto) {
	    Pedido entity = pedidoRepository.findById(Integer.valueOf(dto.getId())).orElse(null);
	    if (entity == null) {
	        throw new EntityNotFoundException("Pedido no encontrado");
	    }

	    entity.setFecha(dto.getFecha());
	    entity.setNombre(dto.getNombre());

	    Usuario usuario = usuarioRepository.findById(Integer.valueOf(dto.getUsuario_id())).orElse(null);
	    if (usuario != null) {
	        entity.setUsuario(usuario);
	    }

	    entity.getArticulos().clear();

	    for (ArticuloCantidadDto articuloCantidad : dto.getArticulos()) {
	        Articulo articulo = articuloRepository.findById(Integer.valueOf(articuloCantidad.getId())).orElse(null);
	        if (articulo != null) {
	            if (articulo.getStock() >= articuloCantidad.getCantidad()) {
	                articulo.setStock(articulo.getStock() - articuloCantidad.getCantidad());
	                articulo.setVecesPedido(articulo.getVecesPedido() + articuloCantidad.getCantidad());
	                articuloRepository.save(articulo);

	                PedidoArticulo pedidoArticulo = new PedidoArticulo();
	                pedidoArticulo.setPedido(entity);
	                pedidoArticulo.setArticulo(articulo);
	                pedidoArticulo.setCantidad(articuloCantidad.getCantidad());

	                entity.getArticulos().add(pedidoArticulo);
	            }
	        }
	    }

	    pedidoRepository.save(entity);
	}
	
	public List<ArticuloDto> top3ArticulosByUsuario(String usuarioId) {
	    List<ArticuloDto> topArticulos = new ArrayList<>();
	    
	    Pageable top3 = PageRequest.of(0, 3);

	    List<Object[]> results = pedidoRepository.findTop3ArticulosByUsuario(Integer.valueOf(usuarioId), top3);
	    
	    for (Object[] result : results) {
	        Articulo articulo = (Articulo) result[0];
	        long cantidad = (long) result[1];
	        
	        ArticuloDto articuloDto = new ArticuloDto();
	        articuloDto.setId(String.valueOf(articulo.getId()));
	        articuloDto.setNombre(articulo.getNombre());
	        articuloDto.setPrecio(articulo.getPrecio());
	        articuloDto.setStock(articulo.getStock());
	        
	        articuloDto.setVecesPedido((int) cantidad);
	        
	        topArticulos.add(articuloDto);
	    }
	    
	    return topArticulos;
	}

}

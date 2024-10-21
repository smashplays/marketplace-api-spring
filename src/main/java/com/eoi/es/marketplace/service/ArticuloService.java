package com.eoi.es.marketplace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eoi.es.marketplace.dto.ArticuloDto;
import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.entity.Articulo;
import com.eoi.es.marketplace.entity.Usuario;
import com.eoi.es.marketplace.repository.ArticuloRepository;

@Service
public class ArticuloService {
	@Autowired
	ArticuloRepository articuloRepository;

	public ArticuloDto findById(String id) {
		Articulo entity = articuloRepository.findById(Integer.valueOf(id)).get();
		
		if (entity == null) {
			return null;
		}
		
		ArticuloDto articuloDto = new ArticuloDto();
		articuloDto.setId(String.valueOf(entity.getId()));
		articuloDto.setNombre(entity.getNombre());
		articuloDto.setPrecio(entity.getPrecio());
		articuloDto.setStock(entity.getStock());
		articuloDto.setVecesPedido(entity.getVecesPedido());

		return articuloDto;
	}
	
	public List<ArticuloDto> findAll() {

		List<ArticuloDto> dtos = new ArrayList<ArticuloDto>();
		List<Articulo> entities = articuloRepository.findAll();

		for (Articulo articulo : entities) {
			ArticuloDto dto = new ArticuloDto();
			dto.setId(String.valueOf(articulo.getId()));
			dto.setNombre(articulo.getNombre());
			dto.setPrecio(articulo.getPrecio());
			dto.setStock(articulo.getStock());
			dto.setVecesPedido(articulo.getVecesPedido());

			dtos.add(dto);
		}

		return dtos;

	}

	public List<ArticuloDto> findByNombreParcial(String nombre) {
		List<ArticuloDto> dtos = new ArrayList<>();
		List<Articulo> entities = articuloRepository.findByNombreContainingIgnoreCase(nombre);

		for (Articulo articulo : entities) {
			ArticuloDto dto = new ArticuloDto();
			dto.setId(String.valueOf(articulo.getId()));
			dto.setNombre(articulo.getNombre());
			dto.setPrecio(articulo.getPrecio());
			dto.setStock(articulo.getStock());
			dto.setVecesPedido(articulo.getVecesPedido());
			dtos.add(dto);
		}

		return dtos;
	}
	
	public void deleteById(String id) {
		articuloRepository.deleteById(Integer.valueOf(id));
	}

	public void create(ArticuloDto dto) {

		Articulo entity = new Articulo();
		entity.setNombre(dto.getNombre());
		entity.setPrecio(dto.getPrecio());
		entity.setStock(dto.getStock());

		articuloRepository.save(entity);

	}

	public void update(ArticuloDto dto) {

		Articulo entity = new Articulo();
		entity.setId(Integer.valueOf(dto.getId()));
		entity.setNombre(dto.getNombre());
		entity.setPrecio(dto.getPrecio());
		entity.setStock(dto.getStock());

		articuloRepository.save(entity);

	}
}

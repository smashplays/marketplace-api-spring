package com.eoi.es.marketplace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eoi.es.marketplace.dto.ArticuloDto;
import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.entity.Usuario;
import com.eoi.es.marketplace.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	PedidoService pedidoService;


	public UsuarioDto findById(String id) {

		Usuario entity = usuarioRepository.findById(Integer.valueOf(id)).get();
		if (entity == null) {
			return null;
		}
		
		UsuarioDto usuarioDto = new UsuarioDto();		
		usuarioDto.setId(String.valueOf(entity.getId()));
		usuarioDto.setNombre(entity.getNombre());
		usuarioDto.setPassword(entity.getPassword());
		usuarioDto.setCantidadPedidos(entity.getCantidadPedidos());
		
	    List<ArticuloDto> top3Articulos = pedidoService.top3ArticulosByUsuario(usuarioDto.getId());
	    usuarioDto.setTop3Articulos(top3Articulos);

		return usuarioDto;
	}

	public List<UsuarioDto> findAll() {

		List<UsuarioDto> dtos = new ArrayList<UsuarioDto>();
		List<Usuario> entities = usuarioRepository.findAll();

		for (Usuario usuario : entities) {
			UsuarioDto dto = new UsuarioDto();
			dto.setId(String.valueOf(usuario.getId()));
			dto.setNombre(usuario.getNombre());
			dto.setPassword(usuario.getPassword());
			dto.setCantidadPedidos(usuario.getCantidadPedidos());
			
	        List<ArticuloDto> top3Articulos = pedidoService.top3ArticulosByUsuario(dto.getId());
	        dto.setTop3Articulos(top3Articulos);
			
			
			dtos.add(dto);
		}

		return dtos;

	}
	
	public List<UsuarioDto> findByNombreParcial(String nombre) {
		List<UsuarioDto> dtos = new ArrayList<>();
		List<Usuario> entities = usuarioRepository.findByNombreContainingIgnoreCase(nombre);

		for (Usuario usuario : entities) {
			UsuarioDto dto = new UsuarioDto();
			dto.setId(String.valueOf(usuario.getId()));
			dto.setNombre(usuario.getNombre());
			dto.setPassword(usuario.getPassword());
			dto.setCantidadPedidos(usuario.getCantidadPedidos());
			
	        List<ArticuloDto> top3Articulos = pedidoService.top3ArticulosByUsuario(dto.getId());
	        dto.setTop3Articulos(top3Articulos);
			
			dtos.add(dto);
		}

		return dtos;
	}

	public void create(UsuarioDto dto) {

		Usuario entity = new Usuario();
		entity.setNombre(dto.getNombre());
		entity.setPassword(dto.getPassword());

		usuarioRepository.save(entity);

	}
	
	public void deleteById(String id) {
		usuarioRepository.deleteById(Integer.valueOf(id));
	}

	public void update(UsuarioDto dto) {

		Usuario entity = new Usuario();
		entity.setId(Integer.valueOf(dto.getId()));
		entity.setNombre(dto.getNombre());
		entity.setPassword(dto.getPassword());

		usuarioRepository.save(entity);

	}

	public UsuarioDto validarUsuario(UsuarioDto dto) {
		Usuario usuario = usuarioRepository.findByNombreAndPassword(dto.getNombre(), dto.getPassword());

		if (usuario != null) {
			UsuarioDto usuarioDto = new UsuarioDto();
			usuarioDto.setId(String.valueOf(usuario.getId()));
			usuarioDto.setNombre(usuario.getNombre());
			usuarioDto.setPassword(usuario.getPassword());
			return usuarioDto;
		}

		return null;
	}
}

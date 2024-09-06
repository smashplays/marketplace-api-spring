package com.eoi.es.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.service.UsuarioService;

@RestController
@RequestMapping("/marketplace/usuarios")
public class UsuariosController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
		List<UsuarioDto> usuarios = usuarioService.findAll();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> getUsuarioById(@PathVariable String id) {
		UsuarioDto usuario = usuarioService.findById(id);
		if (usuario != null) {
			return new ResponseEntity<>(usuario, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping
	public ResponseEntity<Void> createUsuario(@RequestBody UsuarioDto usuarioDto) {
		usuarioService.create(usuarioDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUsuario(@PathVariable String id, @RequestBody UsuarioDto usuarioDto) {
		usuarioDto.setId(id);
		usuarioService.update(usuarioDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<UsuarioDto> loginUsuario(@RequestBody UsuarioDto usuarioDto) {
		UsuarioDto usuarioValidado = usuarioService.validarUsuario(usuarioDto);
		if (usuarioValidado != null) {
			return new ResponseEntity<>(usuarioValidado, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
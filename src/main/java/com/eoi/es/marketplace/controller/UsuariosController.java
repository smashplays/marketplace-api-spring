package com.eoi.es.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eoi.es.marketplace.dto.ArticuloDto;
import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.service.UsuarioService;

@RestController
@RequestMapping("/marketplace/usuarios")
public class UsuariosController {

	@Autowired
	private UsuarioService usuarioService;

	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<List<UsuarioDto>> getAllUsuarios() {
		List<UsuarioDto> usuarios = usuarioService.findAll();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/{nombreparcial}/nombre")
	public ResponseEntity<List<UsuarioDto>> getArticulosByNombreParcial(@PathVariable String nombreparcial) {
		List<UsuarioDto> usuarios = usuarioService.findByNombreParcial(nombreparcial);
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> getUsuarioById(@PathVariable String id) {
		UsuarioDto usuario = usuarioService.findById(id);
		if (usuario != null) {
			return new ResponseEntity<>(usuario, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin(origins = "*")
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioById(@PathVariable String id) {
        try {
            usuarioService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

	@CrossOrigin(origins = "*")
	@PostMapping
	public ResponseEntity<Void> createUsuario(@RequestBody UsuarioDto usuarioDto) {
		usuarioService.create(usuarioDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@CrossOrigin(origins = "*")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUsuario(@PathVariable String id, @RequestBody UsuarioDto usuarioDto) {
		usuarioDto.setId(id);
		usuarioService.update(usuarioDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
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

package com.eoi.es.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eoi.es.marketplace.dto.ArticuloDto;
import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.service.ArticuloService;

@RestController
@RequestMapping("/marketplace/articulos")
public class ArticulosController {

	@Autowired
	private ArticuloService articuloService;

	@CrossOrigin(origins = "*")
	@GetMapping
	public ResponseEntity<List<ArticuloDto>> getAllArticulos() {
		List<ArticuloDto> articulos = articuloService.findAll();
		return new ResponseEntity<>(articulos, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("/{nombreparcial}/nombre")
	public ResponseEntity<List<ArticuloDto>> getArticulosByNombreParcial(@PathVariable String nombreparcial) {
		List<ArticuloDto> articulos = articuloService.findByNombreParcial(nombreparcial);
		return new ResponseEntity<>(articulos, HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/{id}")
	public ResponseEntity<ArticuloDto> getArticuloById(@PathVariable String id) {
		ArticuloDto articulo = articuloService.findById(id);
		if (articulo != null) {
			return new ResponseEntity<>(articulo, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@CrossOrigin(origins = "*")
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticuloById(@PathVariable String id) {
        try {
            articuloService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

	@CrossOrigin(origins = "*")
	@PostMapping
	public ResponseEntity<Void> createArticulo(@RequestBody ArticuloDto articuloDto) {
		articuloService.create(articuloDto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@CrossOrigin(origins = "*")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateArticulo(@PathVariable String id, @RequestBody ArticuloDto articuloDto) {
		articuloDto.setId(id);
		articuloService.update(articuloDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

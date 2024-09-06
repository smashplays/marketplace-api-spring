package com.eoi.es.marketplace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eoi.es.marketplace.dto.PedidoDto;
import com.eoi.es.marketplace.service.PedidoService;

@RestController
@RequestMapping("/marketplace/pedidos")
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> getPedidoById(@PathVariable String id) {
        PedidoDto pedido = pedidoService.findById(id);
        if (pedido != null) {
            return new ResponseEntity<>(pedido, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{nombreparcial}/nombre")
    public ResponseEntity<List<PedidoDto>> getPedidosByNombreParcial(@PathVariable String nombreparcial) {
        List<PedidoDto> pedidos = pedidoService.findByNombreParcial(nombreparcial);
        return new ResponseEntity<>(pedidos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedidoById(@PathVariable String id) {
        try {
            pedidoService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createPedido(@RequestBody PedidoDto pedidoDto) {
        pedidoService.create(pedidoDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePedido(@PathVariable String id, @RequestBody PedidoDto pedidoDto) {
        pedidoDto.setId(id);
        pedidoService.update(pedidoDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


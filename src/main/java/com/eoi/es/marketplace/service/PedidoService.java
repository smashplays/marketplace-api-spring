package com.eoi.es.marketplace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

        return pedidoDto;
    }

    private List<ArticuloCantidadDto> convertToArticuloCantidadDtoList(List<PedidoArticulo> pedidoArticulos) {
        List<ArticuloCantidadDto> dtos = new ArrayList<>();
        for (PedidoArticulo pedidoArticulo : pedidoArticulos) {
            ArticuloCantidadDto dto = new ArticuloCantidadDto();
            dto.setId(String.valueOf(pedidoArticulo.getArticulo().getId()));
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
        pedidoRepository.deleteById(Integer.valueOf(id));
    }

    public void create(PedidoDto dto) {
        Pedido entity = new Pedido();
        entity.setFecha(dto.getFecha());
        entity.setNombre(dto.getNombre());

        Usuario usuario = usuarioRepository.findById(Integer.valueOf(dto.getUsuario_id())).orElse(null);
        if (usuario != null) {
            entity.setUsuario(usuario);
        }

        List<PedidoArticulo> pedidoArticulos = new ArrayList<>();
        for (ArticuloCantidadDto articuloCantidad : dto.getArticulos()) {
            Articulo articulo = articuloRepository.findById(Integer.valueOf(articuloCantidad.getId())).orElse(null);
            if (articulo != null) {
                if (articulo.getStock() >= articuloCantidad.getCantidad()) {
                    articulo.setStock(articulo.getStock() - articuloCantidad.getCantidad());
                    articuloRepository.save(articulo);

                    PedidoArticulo pedidoArticulo = new PedidoArticulo();
                    pedidoArticulo.setPedido(entity);
                    pedidoArticulo.setArticulo(articulo);
                    pedidoArticulo.setCantidad(articuloCantidad.getCantidad());

                    pedidoArticulos.add(pedidoArticulo);
                }
            }
        }
        entity.setArticulos(pedidoArticulos);

        pedidoRepository.save(entity);
    }

    public void update(PedidoDto dto) {
        Pedido entity = pedidoRepository.findById(Integer.valueOf(dto.getId())).orElse(null);
        if (entity == null) {
            return;
        }
        entity.setFecha(dto.getFecha());
        entity.setNombre(dto.getNombre());

        Usuario usuario = usuarioRepository.findById(Integer.valueOf(dto.getUsuario_id())).orElse(null);
        if (usuario != null) {
            entity.setUsuario(usuario);
        }

        List<PedidoArticulo> pedidoArticulos = new ArrayList<>();
        for (ArticuloCantidadDto articuloCantidad : dto.getArticulos()) {
            Articulo articulo = articuloRepository.findById(Integer.valueOf(articuloCantidad.getId())).orElse(null);
            if (articulo != null) {
                if (articulo.getStock() >= articuloCantidad.getCantidad()) {
                    articulo.setStock(articulo.getStock() - articuloCantidad.getCantidad());
                    articuloRepository.save(articulo);

                    PedidoArticulo pedidoArticulo = new PedidoArticulo();
                    pedidoArticulo.setPedido(entity);
                    pedidoArticulo.setArticulo(articulo);
                    pedidoArticulo.setCantidad(articuloCantidad.getCantidad());

                    pedidoArticulos.add(pedidoArticulo);
                }
            }
        }
        entity.setArticulos(pedidoArticulos);

        pedidoRepository.save(entity);
    }
}



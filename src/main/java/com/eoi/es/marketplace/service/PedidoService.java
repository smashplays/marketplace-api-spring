package com.eoi.es.marketplace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eoi.es.marketplace.dto.PedidoDto;
import com.eoi.es.marketplace.dto.UsuarioDto;
import com.eoi.es.marketplace.dto.ArticuloDto;
import com.eoi.es.marketplace.entity.Pedido;
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
        pedidoDto.setArticulos(convertToArticuloDtoList(entity.getArticulos()));

        Usuario usuario = entity.getUsuario();
        if (usuario != null) {
            UsuarioDto usuarioDto = new UsuarioDto();
            usuarioDto.setId(String.valueOf(usuario.getId()));
            usuarioDto.setNombre(usuario.getNombre());
            usuarioDto.setPassword(usuario.getPassword());
            pedidoDto.setUsuario(usuarioDto);
        }

        return pedidoDto;
    }

    private List<ArticuloDto> convertToArticuloDtoList(List<Articulo> articulos) {
        List<ArticuloDto> dtos = new ArrayList<>();
        for (Articulo articulo : articulos) {
            ArticuloDto dto = new ArticuloDto();
            dto.setId(String.valueOf(articulo.getId()));
            dto.setNombre(articulo.getNombre());
            dto.setPrecio(articulo.getPrecio());
            dto.setStock(articulo.getStock());
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
            dto.setArticulos(convertToArticuloDtoList(pedido.getArticulos()));

            Usuario usuario = pedido.getUsuario();
            if (usuario != null) {
                UsuarioDto usuarioDto = new UsuarioDto();
                usuarioDto.setId(String.valueOf(usuario.getId()));
                usuarioDto.setNombre(usuario.getNombre());
                usuarioDto.setPassword(usuario.getPassword());
                dto.setUsuario(usuarioDto);
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
        
        Usuario usuario = usuarioRepository.findById(Integer.valueOf(dto.getUsuario().getId())).orElse(null);
        if (usuario != null) {
            entity.setUsuario(usuario);
        }
        
        entity.setArticulos(convertToArticuloList(dto.getArticulos()));

        pedidoRepository.save(entity);
    }

    private List<Articulo> convertToArticuloList(List<ArticuloDto> articuloDtos) {
        List<Articulo> articulos = new ArrayList<>();
        for (ArticuloDto dto : articuloDtos) {
            Articulo articulo = articuloRepository.findById(Integer.valueOf(dto.getId())).orElse(new Articulo());
            articulo.setNombre(dto.getNombre());
            articulo.setPrecio(dto.getPrecio());
            articulo.setStock(dto.getStock());
            articulos.add(articulo);
        }
        return articulos;
    }

    public void update(PedidoDto dto) {
        Pedido entity = new Pedido();
        entity.setId(Integer.valueOf(dto.getId()));
        entity.setFecha(dto.getFecha());
        entity.setNombre(dto.getNombre());
        
        Usuario usuario = usuarioRepository.findById(Integer.valueOf(dto.getUsuario().getId())).orElse(null);
        if (usuario != null) {
            entity.setUsuario(usuario);
        }
        
        entity.setArticulos(convertToArticuloList(dto.getArticulos()));

        pedidoRepository.save(entity);
    }
}



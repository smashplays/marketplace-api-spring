package com.eoi.es.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eoi.es.marketplace.entity.Articulo;

public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {
    List<Articulo> findByNombreContainingIgnoreCase(String nombre);
}

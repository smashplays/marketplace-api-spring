package com.eoi.es.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eoi.es.marketplace.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByNombreContainingIgnoreCase(String nombre);
}

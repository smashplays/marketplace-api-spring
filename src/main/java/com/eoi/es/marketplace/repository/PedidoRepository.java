package com.eoi.es.marketplace.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eoi.es.marketplace.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByNombreContainingIgnoreCase(String nombre);
	
	@Query("SELECT pa.articulo, SUM(pa.cantidad) as totalPedidos " +
		       "FROM PedidoArticulo pa " +
		       "JOIN pa.pedido p " +
		       "WHERE p.usuario.id = :usuarioId " +
		       "GROUP BY pa.articulo " +
		       "ORDER BY totalPedidos DESC")
	List<Object[]> findTop3ArticulosByUsuario(@Param("usuarioId") int usuarioId, Pageable pageable);

}

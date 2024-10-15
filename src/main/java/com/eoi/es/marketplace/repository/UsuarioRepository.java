package com.eoi.es.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eoi.es.marketplace.entity.Articulo;
import com.eoi.es.marketplace.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	@Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre AND u.password = :password")
    Usuario findByNombreAndPassword(@Param("nombre") String nombre, @Param("password") String password);
	
	List<Usuario> findByNombreContainingIgnoreCase(String nombre);
}

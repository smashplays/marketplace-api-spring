package com.eoi.es.marketplace.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pedido")
@Getter
@Setter
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombre;

	private Date fecha;

	@ManyToOne
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "pedido_articulo", joinColumns = @JoinColumn(name = "pedido_id"), inverseJoinColumns = @JoinColumn(name = "articulo_id"))
	private List<Articulo> articulos;
}

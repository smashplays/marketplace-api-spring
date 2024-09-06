package com.eoi.es.marketplace.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "articulo")
@Getter
@Setter
public class Articulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String nombre;
	
	@Column
	private int precio;
	
	@Column
	private int stock;
	
}

package com.eoi.es.marketplace.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto {
	
	private String id;
	
	private String nombre;
	
	private String password;
	
	private int cantidadPedidos;
	
    private List<ArticuloDto> top3Articulos;
}

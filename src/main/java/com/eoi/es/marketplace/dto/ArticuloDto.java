package com.eoi.es.marketplace.dto;

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
public class ArticuloDto {

    private String id;

    private String nombre;

    private int precio;

    private int stock;

    private int vecesPedido;
}


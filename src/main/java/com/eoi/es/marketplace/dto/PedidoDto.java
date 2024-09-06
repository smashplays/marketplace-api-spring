package com.eoi.es.marketplace.dto;

import java.util.Date;
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
public class PedidoDto {

    private String id;
    
    private String nombre;
    
    private Date fecha;
    
    private UsuarioDto usuario;
    
    private List<ArticuloDto> articulos;
}

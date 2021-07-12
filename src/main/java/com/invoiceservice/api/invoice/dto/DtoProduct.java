package com.invoiceservice.api.invoice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DtoProduct {

    private int id;
    private String codigo;
    private String producto;
    private String descripcion;
    private double precio;
    private int cantidad;
    private LocalDateTime fecha_creacion;
    private int id_categoria;
}

package com.invoiceservice.api.invoice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    @Column(name= "id_invoice")
    private Integer idInvoice;

    @JsonProperty("customer_code")
    @Column(name = "customer_code")
    @NotNull(message = "customer code required")
    @Min(value=1, message = "customer code must be greater then 0")
    private Integer customerCode;

    @JsonProperty("subtotal")
    @Transient
    private Double subtotal;

    @JsonProperty("taxes")
    @Transient
    private Double taxes;

    @JsonProperty("total")
    @Transient
    private Double total;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private LocalDate createdAt;

    @JsonIgnore
    private Integer status;

    @JsonProperty("invoice_items")
    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "id_invoice")
    private List<InvoiceItem> invoiceItems;


}

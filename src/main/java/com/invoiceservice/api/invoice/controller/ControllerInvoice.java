package com.invoiceservice.api.invoice.controller;

import com.invoiceservice.api.invoice.entity.Invoice;
import com.invoiceservice.api.invoice.service.ServiceInvoice;
import com.invoiceservice.exceptionHandling.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class ControllerInvoice {

    @Autowired
    ServiceInvoice serviceInvoice;

    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoices() {
        return new ResponseEntity<>(serviceInvoice.getInvoices(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/id_invoice")
    public ResponseEntity<Invoice> getInvoice(@PathVariable("id_invoice") Integer id_invoice) {
        return new ResponseEntity<>(serviceInvoice.getInvoice(id_invoice), HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ApiException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        serviceInvoice.createInvoice(invoice);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/id_invoice")
    public ResponseEntity<HttpStatus> deleteInvoice(@PathVariable("id_invoice") Integer id_invoice) {
        serviceInvoice.deleteInvoice(id_invoice);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

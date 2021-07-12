package com.invoiceservice.api.invoice.service;

import com.invoiceservice.api.invoice.entity.Invoice;

import java.util.List;

public interface ServiceInvoice {

    public List<Invoice> getInvoices();

    public Invoice getInvoice(Integer id);

    public void createInvoice(Invoice invoice);

    public void deleteInvoice(Integer id);
}

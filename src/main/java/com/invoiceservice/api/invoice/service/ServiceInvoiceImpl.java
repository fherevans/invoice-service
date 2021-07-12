package com.invoiceservice.api.invoice.service;

import com.invoiceservice.api.invoice.dto.DtoCustomer;
import com.invoiceservice.api.invoice.dto.DtoProduct;
import com.invoiceservice.api.invoice.entity.Invoice;
import com.invoiceservice.api.invoice.entity.InvoiceItem;
import com.invoiceservice.api.invoice.repository.RepositoryInvoice;
import com.invoiceservice.exceptionHandling.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceInvoiceImpl implements ServiceInvoice{

    @Autowired
    RepositoryInvoice repositoryInvoice;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Invoice> getInvoices() {
        try{
            List<Invoice> invoices = repositoryInvoice.getInvoices();
            for(Invoice i : invoices) {
                setCostsInvoice(i);
            }
            return invoices;
        } catch(Exception e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
    }

    @Override
    public Invoice getInvoice(Integer id) {
        try{
            Invoice invoice = ((Optional<Invoice>) repositoryInvoice.findById(id)).orElse(null);
            if(invoice == null || invoice.getStatus()==0) throw new ApiException(HttpStatus.NOT_FOUND, "Invoice doesn't exists");
            setCostsInvoice(invoice);
            return invoice;
        } catch(Exception e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
    }

    @Override
    public void createInvoice(Invoice invoice) {
        try {
            getCustomer(invoice.getCustomerCode());
            for(InvoiceItem i : invoice.getInvoiceItems()) {
                getProduct(i.getProductCode());
            }
            invoice.setStatus(1);
            invoice.setCreatedAt(LocalDate.now());
            repositoryInvoice.save(invoice);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        }
    }

    @Override
    public void deleteInvoice(Integer id) {
        try {
            getInvoice(id);
            repositoryInvoice.deleteInvoice(id);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Customer code " + id + " is not valid");
        }
    }

    private DtoCustomer getCustomer(Integer customerCode) {
        try {
            ResponseEntity<DtoCustomer> response = restTemplate.getForEntity(
                    "http://localhost:8080/customer/" + customerCode,
                    DtoCustomer.class);
            return response.getBody();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.NOT_FOUND, "customer code " + customerCode + " is invalid");
        }
    }

    private DtoProduct getProduct(String productCode) {
            try {
                ResponseEntity<DtoProduct> responde = restTemplate.getForEntity(
                        "http://localhost:8081/product/" + productCode,
                        DtoProduct.class);
                return responde.getBody();
            } catch (Exception e) {
                throw new ApiException(HttpStatus.NOT_FOUND, "product code " +productCode + " not valid");
            }
        }
    private void setCostsInvoiceItem(InvoiceItem item) {
        DtoProduct p = getProduct(item.getProductCode());
        item.setUnitPrice(p.getPrecio());
        item.setSubtotal(p.getPrecio()*item.getQuantity());
        item.setTotal(item.getSubtotal() * (1 + item.getTaxPercentage()));
    }

    private void setCostsInvoice(Invoice invoice) {
        double sub = 0.0;
        double tax = 0.0;
        for(InvoiceItem i : invoice.getInvoiceItems()) {
            setCostsInvoiceItem(i);
            sub += i.getSubtotal();
            tax += i.getSubtotal() * i.getTaxPercentage();
        }
        invoice.setSubtotal(sub);
        invoice.setTaxes(tax);
        invoice.setTotal(sub+tax);
    }
}

package com.event.booking.management.controller;

import com.event.booking.management.model.Invoice;
import com.event.booking.management.service.InvoiceService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/by-payment/{paymentId}")
    public Invoice getInvoice(@PathVariable Integer paymentId) {
        return invoiceService.getInvoiceByPaymentId(paymentId);
    }
    
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Integer id) {
        try {
            byte[] pdfData = invoiceService.generateInvoicePdf(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

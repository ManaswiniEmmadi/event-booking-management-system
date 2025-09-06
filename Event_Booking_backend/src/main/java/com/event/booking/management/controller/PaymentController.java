package com.event.booking.management.controller;

import com.event.booking.management.DTO.PaymentRequestDTO;
import com.event.booking.management.DTO.PaymentResponseDTO;
import com.event.booking.management.model.Payment;
import com.event.booking.management.service.InvoiceService;
import com.event.booking.management.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

	@Autowired
    private final PaymentService paymentService;
    
    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> makePayment(@RequestBody @Valid PaymentRequestDTO requestDTO) {
        PaymentResponseDTO response = paymentService.processPayment(requestDTO);

        Payment savedPayment = paymentService.getPaymentEntityById(response.getPaymentId());
        invoiceService.generateInvoice(savedPayment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPayment(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public List<Payment> getPaymentsByBooking(@PathVariable Integer bookingId) {
        return paymentService.getPaymentsByBookingId(bookingId);
    }
    
    @GetMapping("/ticket/{bookingId}")
    public ResponseEntity<byte[]> getTicket(
            @PathVariable Long bookingId,
            @RequestParam double amount) {

        byte[] pdfBytes = invoiceService.generateTicket(bookingId, amount);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + bookingId + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdfBytes);
    }
    
    


}

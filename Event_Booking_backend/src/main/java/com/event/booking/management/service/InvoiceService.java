package com.event.booking.management.service;

import com.event.booking.management.model.Booking;
import com.event.booking.management.model.Event;
import com.event.booking.management.model.Invoice;
import com.event.booking.management.model.Payment;
import com.event.booking.management.repository.BookingRepository;
import com.event.booking.management.repository.EventRepository;
import com.event.booking.management.repository.InvoiceRepository;
import com.event.booking.management.repository.PaymentRepository;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InvoiceService {

	@Autowired
    private InvoiceRepository invoiceRepository;
    
	@Autowired
    private BookingRepository bookingRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;

    public void generateInvoice(Payment payment) {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Invoice invoice = Invoice.builder()
                .payment(payment)
                .invoiceNumber(invoiceNumber)
                .fileUrl("/invoices/" + invoiceNumber + ".pdf") // Placeholder
                .build();

        invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceByPaymentId(Integer paymentId) {
        return invoiceRepository.findByPayment_PaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for payment ID: " + paymentId));
    }
    
    public byte[] generateTicket(Long bookingId, double amount) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        Event event = eventRepository.findById(booking.getEventId())
            .orElseThrow(() -> new RuntimeException("Event not found"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("Booking Ticket", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Details
            document.add(new Paragraph("Booking Date: " + LocalDate.now()));
            document.add(new Paragraph("Booking ID: " + booking.getBookingId()));
            document.add(new Paragraph("Customer Name: " + booking.getUserName()));
            document.add(new Paragraph("Email: " + booking.getEmail()));
            document.add(new Paragraph("Mobile: " + booking.getMobile()));
            document.add(new Paragraph("Event Name: " + event.getEventName()));
            document.add(new Paragraph("Seat Type: " + booking.getSeatType().getSeatType()));
            document.add(new Paragraph("Number of Seats: " + booking.getNumberOfSeats()));
            document.add(new Paragraph("Status: " + booking.getStatus()));
            document.add(new Paragraph("Amount Paid: ₹" + amount));

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating invoice", e);
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    public byte[] generateInvoicePdf(Integer paymentId) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Here you would normally fetch from DB:
            Payment payment = paymentRepository.findById(paymentId)
                 .orElseThrow(() -> new RuntimeException("Payment not found"));

            // For now, dummy data:
            document.add(new Paragraph("Invoice for Payment ID: " + paymentId));
            document.add(new Paragraph("Transaction ID: TXN" + payment.getTransactionId()));
            document.add(new Paragraph("Amount Paid: " + payment.getAmount())); // replace with payment.getAmount()
            document.add(new Paragraph("Payment Method: " + payment.getPaymentMethod())); // replace with payment.getMethod()
            document.add(new Paragraph("Status: " + payment.getPaymentStatus()));
            document.add(new Paragraph("\nThank you for your payment!"));

            document.close();

            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Error while generating invoice PDF", e);
        }
    }


}

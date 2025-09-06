package com.event.booking.management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @PrePersist
    private void setGeneratedAt() {
        if (this.generatedAt == null) {
            this.generatedAt = LocalDateTime.now();
        }
    }
}

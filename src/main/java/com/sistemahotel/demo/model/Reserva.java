package com.sistemahotel.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;
    
    @ManyToOne
    @JoinColumn(name = "hospede_id", nullable = false)
    private Hospede hospede;
    
    @Column(nullable = false)
    private LocalDate dataEntrada;
    
    @Column(nullable = false)
    private LocalDate dataSaida;
    
    @Enumerated(EnumType.STRING)
    private ReservaStatus status = ReservaStatus.ATIVA;
    
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private LocalDate dataCancelamento;
    
}
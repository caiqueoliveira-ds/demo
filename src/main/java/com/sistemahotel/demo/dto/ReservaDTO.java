package com.sistemahotel.demo.dto;

import jakarta.validation.constraints.Future;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservaDTO {

    private Long id;
    private Long quartoId;
    private String quartoNumero;
    private Long hospedeId;
    private String hospedeNome;

    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataEntrada;

    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataSaida;

    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataCancelamento;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataOperacao;

}
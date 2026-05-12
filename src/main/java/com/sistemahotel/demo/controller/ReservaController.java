package com.sistemahotel.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.sistemahotel.demo.dto.ReservaDTO;
import com.sistemahotel.demo.service.ReservaService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaDTO criar(@Valid @RequestBody ReservaDTO dto) {

        return reservaService.criarReserva(dto);

    }
    
    @PutMapping("/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@Valid @RequestBody ReservaDTO dto) {

        reservaService.cancelarReserva(dto);

    }
    
    @PutMapping("/checkin")
    @ResponseStatus(HttpStatus.OK)
    public void checkIn(@Valid @RequestBody ReservaDTO dto) {

        reservaService.realizarCheckIn(dto);

    }
    
    @PutMapping("/checkout")
    @ResponseStatus(HttpStatus.OK)
    public void checkOut(@Valid @RequestBody ReservaDTO dto) {

        reservaService.realizarCheckOut(dto);

    }
    
    @GetMapping("/hospede/{hospedeId}")
    public List<ReservaDTO> consultarPorHospede(@PathVariable Long hospedeId) {

        return reservaService.consultarReservasPorHospede(hospedeId);

    }
    
    /*@GetMapping("/quartos-disponiveis")
    public List<Quarto> listarQuartosDisponiveis(

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return reservaService.listarQuartosDisponiveis(dataInicio, dataFim);

    }*/
}
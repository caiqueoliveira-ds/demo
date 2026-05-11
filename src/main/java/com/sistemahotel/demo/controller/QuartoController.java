package com.sistemahotel.demo.controller;

import com.sistemahotel.demo.dto.QuartoRequestDTO;
import com.sistemahotel.demo.dto.QuartoResponseDTO;
import com.sistemahotel.demo.service.QuartoService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quartos")
public class QuartoController {

    private final QuartoService service;

    public QuartoController(QuartoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<QuartoResponseDTO> cadastrar(@RequestBody QuartoRequestDTO dto) {
        QuartoResponseDTO response = service.cadastrar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<QuartoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
}
}
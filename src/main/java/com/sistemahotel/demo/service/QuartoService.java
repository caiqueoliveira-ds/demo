package com.sistemahotel.demo.service;

import com.sistemahotel.demo.dto.QuartoRequestDTO;
import com.sistemahotel.demo.dto.QuartoResponseDTO;
import com.sistemahotel.demo.entity.Quarto;
import com.sistemahotel.demo.repository.QuartoRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class QuartoService {

    private final QuartoRepository repository;

    public QuartoService(QuartoRepository repository) {
        this.repository = repository;
    }

    public QuartoResponseDTO cadastrar(QuartoRequestDTO dto) {
        if (repository.existsByNumero(dto.numero())) {
            throw new RuntimeException("Número de quarto já existe");
        }
    
        Quarto quarto = new Quarto();
        quarto.setNumero(dto.numero());
        quarto.setTipo(dto.tipo());
        quarto.setPreco(dto.preco());
    
        Quarto salvo = repository.save(quarto);
    
        return new QuartoResponseDTO(salvo.getId(), salvo.getNumero(), salvo.getTipo(), salvo.getPreco());
    }


    public List<QuartoResponseDTO> listarTodos() {
        return repository.findAll()
            .stream()
            .map(q -> new QuartoResponseDTO(q.getId(), q.getNumero(), q.getTipo(), q.getPreco()))
            .toList();
    }
}
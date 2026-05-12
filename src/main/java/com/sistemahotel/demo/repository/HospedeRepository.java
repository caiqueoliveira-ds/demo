package com.sistemahotel.demo.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.sistemahotel.demo.model.Hospede;

public class HospedeRepository {

    private final List<Hospede> hospedes = new ArrayList<>();

    // salvar
    public void salvar(Hospede hospede) {
        boolean cpfJaCadastrado = hospedes.stream()
                .anyMatch(h -> h.getCpf().equals(hospede.getCpf()));

        if (cpfJaCadastrado) {
            throw new IllegalArgumentException("Já existe um hóspede com o CPF: " + hospede.getCpf());
        }

        hospedes.add(hospede);
    }

    // listar
    public List<Hospede> listar() {
        return Collections.unmodifiableList(hospedes);
    }

    // buscar por id
    public Optional<Hospede> buscarPorId(Long id) {
        return hospedes.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
    }

    // buscar por CPF
    public Optional<Hospede> buscarPorCpf(String cpf) {
        return hospedes.stream()
                .filter(h -> h.getCpf().equals(cpf))
                .findFirst();
    }

    // remover por id
    public boolean remover(Long id) {
        return hospedes.removeIf(h -> h.getId().equals(id));
    }
}

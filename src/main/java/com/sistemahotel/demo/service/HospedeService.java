package com.sistemahotel.demo.service;

import com.sistemahotel.demo.dto.HospedeDTO;
import com.sistemahotel.demo.model.Hospede;
import com.sistemahotel.demo.repository.HospedeRepository;

import java.util.Optional;

public class HospedeService {

    private final HospedeRepository repository = new HospedeRepository();

    private Long contadorId = 1L;

    // cadastrar
    public void cadastrarHospede(HospedeDTO dto) {

        Hospede hospede = new Hospede(
                contadorId++,
                dto.getNome(),
                dto.getCpf(),
                dto.getTelefone(),
                dto.getEmail()
        );

        repository.salvar(hospede);

        System.out.println("\nHóspede cadastrado!");
    }

    // listar
    public void listarHospedes() {

        if (repository.listar().isEmpty()) {
            System.out.println("\nNenhum hóspede cadastrado.");
            return;
        }

        for (Hospede hospede : repository.listar()) {
            System.out.println(hospede);
            System.out.println("----------------------");
        }
    }

    // atualizar
    public void atualizarHospede(Long id, HospedeDTO dto) {

        Optional<Hospede> optional = repository.buscarPorId(id);

        if (optional.isPresent()) {

            Hospede hospede = optional.get();
            hospede.setNome(dto.getNome());
            hospede.setTelefone(dto.getTelefone());
            hospede.setEmail(dto.getEmail());

            System.out.println("\nHóspede atualizado!");

        } else {

            System.out.println("\nHóspede não encontrado.");
        }
    }

    // remover
    public void removerHospede(Long id) {

        boolean removido = repository.remover(id);

        if (removido) {
            System.out.println("\nHóspede removido!");
        } else {
            System.out.println("\nHóspede não encontrado.");
        }
    }
}

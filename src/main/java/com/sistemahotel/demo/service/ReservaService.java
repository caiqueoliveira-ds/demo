package com.sistemahotel.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistemahotel.demo.dto.ReservaDTO;
import com.sistemahotel.demo.entity.Quarto;
import com.sistemahotel.demo.model.Hospede;
import com.sistemahotel.demo.model.Reserva;
import com.sistemahotel.demo.model.ReservaStatus;
import com.sistemahotel.demo.repository.HospedeRepository;
import com.sistemahotel.demo.repository.QuartoRepository;
import com.sistemahotel.demo.repository.ReservaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final QuartoRepository quartoRepository;
    private final HospedeRepository hospedeRepository;

    @Transactional
    public ReservaDTO criarReserva(ReservaDTO dto) {

        // busca quarto pelo ID
        Quarto quarto = quartoRepository.findById(dto.getQuartoId())
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        // busca hóspede pelo ID (estava faltando completamente)
        Hospede hospede = hospedeRepository.buscarPorId(dto.getHospedeId())
                .orElseThrow(() -> new RuntimeException("Hóspede não encontrado"));

        if (!dto.getDataEntrada().isBefore(dto.getDataSaida())) {
            throw new RuntimeException("Data de entrada deve ser anterior à data de saída");
        }

        List<Reserva> sobrepostas = reservaRepository.findOverlappingReservations(
                quarto.getId(), dto.getDataEntrada(), dto.getDataSaida());

        if (!sobrepostas.isEmpty()) {
            throw new RuntimeException("Quarto já possui reserva para o período solicitado");
        }

        long ativas = reservaRepository.countByHospedeIdAndStatusIn(hospede.getId(),
                List.of(ReservaStatus.ATIVA, ReservaStatus.CHECKED_IN));

        if (ativas >= 2) {
            throw new RuntimeException("Hóspede já possui 2 reservas ativas");
        }

        Reserva reserva = new Reserva();
        reserva.setQuarto(quarto);
        reserva.setHospede(hospede);
        reserva.setDataEntrada(dto.getDataEntrada());
        reserva.setDataSaida(dto.getDataSaida());
        reserva.setStatus(ReservaStatus.ATIVA);
        reserva = reservaRepository.save(reserva);

        return toDTO(reserva);
    }

    @Transactional
    public void cancelarReserva(ReservaDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (reserva.getStatus() != ReservaStatus.ATIVA) {
            throw new RuntimeException("Apenas reservas ativas podem ser canceladas");
        }

        LocalDateTime cancelamento = dto.getDataCancelamento();

        if (cancelamento == null) {
            throw new RuntimeException("Data de cancelamento é obrigatória");
        }

        LocalDateTime entradaDateTime = reserva.getDataEntrada().atStartOfDay();
        long horasAntecedencia = java.time.Duration.between(cancelamento, entradaDateTime).toHours();

        if (horasAntecedencia < 24) {
            throw new RuntimeException("Cancelamento deve ser feito com pelo menos 24h de antecedência");
        }

        reserva.setStatus(ReservaStatus.CANCELADA);
        reserva.setDataCancelamento(cancelamento.toLocalDate());
        reservaRepository.save(reserva);
    }

    @Transactional
    public void realizarCheckIn(ReservaDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (reserva.getStatus() != ReservaStatus.ATIVA) {
            throw new RuntimeException("Apenas reservas ativas podem fazer check-in");
        }

        LocalDate operacao = dto.getDataOperacao();

        if (operacao == null || !operacao.equals(reserva.getDataEntrada())) {
            throw new RuntimeException("Check-in permitido apenas na data de entrada da reserva");
        }

        reserva.setStatus(ReservaStatus.CHECKED_IN);
        reserva.setDataCheckIn(operacao);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void realizarCheckOut(ReservaDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (reserva.getStatus() != ReservaStatus.CHECKED_IN) {
            throw new RuntimeException("Apenas reservas com check-in realizado podem fazer check-out");
        }

        reserva.setStatus(ReservaStatus.CHECKED_OUT);
        reserva.setDataCheckOut(dto.getDataOperacao());
        reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> consultarReservasPorHospede(Long hospedeId) {
        return reservaRepository.findByHospedeId(hospedeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Quarto> listarQuartosDisponiveis(LocalDate dataInicio, LocalDate dataFim) {
        return quartoRepository.findAll().stream()
                .filter(quarto -> reservaRepository
                        .findOverlappingReservations(quarto.getId(), dataInicio, dataFim).isEmpty())
                .collect(Collectors.toList());
    }

    private ReservaDTO toDTO(Reserva reserva) {

        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setQuartoId(reserva.getQuarto().getId());
        // getNumero() retorna Integer — convertido para String
        dto.setQuartoNumero(String.valueOf(reserva.getQuarto().getNumero()));
        dto.setHospedeId(reserva.getHospede().getId());
        dto.setHospedeNome(reserva.getHospede().getNome());
        dto.setDataEntrada(reserva.getDataEntrada());
        dto.setDataSaida(reserva.getDataSaida());
        dto.setStatus(reserva.getStatus().toString());
        return dto;
    }
}

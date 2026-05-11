package com.sistemahotel.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistemahotel.demo.dto.ReservaDTO;
import com.sistemahotel.demo.model.Reserva;
import com.sistemahotel.demo.model.ReservaStatus;
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

        Quarto quarto = quartoRepository.findById(dto.getQuartoId())
                .orElseThrow(() -> new BusinessException("Quarto não encontrado"));

        Hospede hospede = hospedeRepository.findById(dto.getHospedeId())
                .orElseThrow(() -> new BusinessException("Hóspede não encontrado"));

        if (!dto.getDataEntrada().isBefore(dto.getDataSaida())) {

            throw new BusinessException("Data de entrada deve ser anterior à data de saída");

        }

        List<Reserva> sobrepostas = reservaRepository.findOverlappingReservations(quarto.getId(), dto.getDataEntrada(), dto.getDataSaida());

        if (!sobrepostas.isEmpty()) {

            throw new BusinessException("Quarto já possui reserva para o período solicitado");

        }

        long ativas = reservaRepository.countByHospedeIdAndStatusIn(hospede.getId(),
                List.of(ReservaStatus.ATIVA, ReservaStatus.CHECKED_IN));

        if (ativas >= 2) {

            throw new BusinessException("Hóspede já possui 2 reservas ativas");

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
                .orElseThrow(() -> new BusinessException("Reserva não encontrada"));

        if (reserva.getStatus() != ReservaStatus.ATIVA) {

            throw new BusinessException("Apenas reservas ativas podem ser canceladas");

        }

        LocalDateTime cancelamento = dto.getDataCancelamento();

        if (cancelamento == null) {

            throw new BusinessException("Data de cancelamento é obrigatória");

        }

        LocalDateTime entradaDateTime = reserva.getDataEntrada().atStartOfDay();
        long horasAntecedencia = java.time.Duration.between(cancelamento, entradaDateTime).toHours();

        if (horasAntecedencia < 24) {

            throw new BusinessException("Cancelamento deve ser feito com pelo menos 24h de antecedência");

        }

        reserva.setStatus(ReservaStatus.CANCELADA);
        reserva.setDataCancelamento(cancelamento.toLocalDate());
        reservaRepository.save(reserva);

    }

    @Transactional
    public void realizarCheckIn(ReservaDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException("Reserva não encontrada"));

        if (reserva.getStatus() != ReservaStatus.ATIVA) {

            throw new BusinessException("Apenas reservas ativas podem fazer check-in");

        }

        LocalDate operacao = dto.getDataOperacao();

        if (operacao == null || !operacao.equals(reserva.getDataEntrada())) {

            throw new BusinessException("Check-in permitido apenas na data de entrada da reserva");

        }

        reserva.setStatus(ReservaStatus.CHECKED_IN);
        reserva.setDataCheckIn(operacao);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void realizarCheckOut(ReservaDTO dto) {

        Reserva reserva = reservaRepository.findById(dto.getId())
                .orElseThrow(() -> new BusinessException("Reserva não encontrada"));

        if (reserva.getStatus() != ReservaStatus.CHECKED_IN) {

            throw new BusinessException("Apenas reservas com check-in realizado podem fazer check-out");

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
                .filter(quarto -> reservaRepository.findOverlappingReservations(quarto.getId(), dataInicio, dataFim).isEmpty())
                .collect(Collectors.toList());

    }

    private ReservaDTO toDTO(Reserva reserva) {

        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setQuartoId(reserva.getQuarto().getId());
        dto.setQuartoNumero(reserva.getQuarto().getNumero());
        dto.setHospedeId(reserva.getHospede().getId());
        dto.setHospedeNome(reserva.getHospede().getNome());
        dto.setDataEntrada(reserva.getDataEntrada());
        dto.setDataSaida(reserva.getDataSaida());
        dto.setStatus(reserva.getStatus().toString());
        return dto;
        
    }
}
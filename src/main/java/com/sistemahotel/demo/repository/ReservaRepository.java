package com.sistemahotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.sistemahotel.demo.model.Reserva;
import com.sistemahotel.demo.model.ReservaStatus;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByHospedeId(Long hospedeId);

    @Query("SELECT r FROM Reserva r WHERE r.quarto.id = :quartoId AND r.status NOT IN ('CANCELADA', 'CHECKED_OUT') AND "+ "((r.dataEntrada <= :dataSaida AND r.dataSaida >= :dataEntrada))")

    List<Reserva> findOverlappingReservations(@Param("quartoId") Long quartoId,@Param("dataEntrada") LocalDate dataEntrada, @Param("dataSaida") LocalDate dataSaida);

    long countByHospedeIdAndStatusIn(Long hospedeId, List<ReservaStatus> statuses);
    
}
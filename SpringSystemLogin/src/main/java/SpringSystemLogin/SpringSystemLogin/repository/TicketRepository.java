package SpringSystemLogin.SpringSystemLogin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringSystemLogin.SpringSystemLogin.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

    List<Ticket> findByUsuarioId(Long usuarioId);

}
package SpringSystemLogin.SpringSystemLogin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringSystemLogin.SpringSystemLogin.model.TicketMensagem;

public interface TicketMensagemRepository extends JpaRepository<TicketMensagem, Long>{

    List<TicketMensagem> findByTicketId(Long ticketId);

}
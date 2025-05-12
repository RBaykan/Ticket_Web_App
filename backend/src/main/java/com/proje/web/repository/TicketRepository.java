package com.proje.web.repository;

import com.proje.web.model.Ticket;
import com.proje.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByUser(User user);
    Optional<List<Ticket>> findListByUser(User user);

}

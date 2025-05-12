package com.proje.web.repository;

import com.proje.web.model.Message;
import com.proje.web.model.Ticket;
import com.proje.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {



}

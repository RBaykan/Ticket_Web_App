package com.proje.web.service;


import java.util.List;
import java.util.Map;

import com.proje.web.dto.*;
import com.proje.web.model.Message;
import com.proje.web.model.Ticket;
import org.springframework.stereotype.Service;


@Service
public interface UserService {
	List<UserDTO> getAllUser();

	UserDTO registerNewUser(CreateUser user);
	String login(LoginUser user);

	UserDTO getUserWithName(String username);


	TicketDTO createTicket(CreateTicket createTicket);

	List<TicketDTO> allTickets();

	MessageDTO sendMessage(SendMessage message);
	TicketDTO closeTicket(CloseTicket closeTicket);


}

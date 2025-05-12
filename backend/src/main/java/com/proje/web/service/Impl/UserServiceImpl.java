package com.proje.web.service.Impl;
import java.util.*;
import java.util.stream.Collectors;

import com.proje.security.exceptions.TicketClosed;
import com.proje.security.exceptions.TicketNotFound;
import com.proje.web.dto.*;
import com.proje.web.mapper.MessegaMapper;
import com.proje.web.mapper.TicketMapper;
import com.proje.web.model.*;
import com.proje.web.repository.MessageRepository;
import com.proje.web.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.proje.security.exceptions.UserNotFound;
import com.proje.web.mapper.UserMapper;
import com.proje.web.repository.UserRepository;
import com.proje.web.service.UserService;




@Service
public class UserServiceImpl implements UserService {



	@Autowired

	private final UserRepository userRepository;
	private final TicketRepository ticketRepository;
	private final UserMapper  userMapper;
	private final TicketMapper ticketMapper;
	private final MessegaMapper messegaMapper;
	private final MessageRepository messageRepository;


	private final PasswordEncoder encoder;




	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, TicketMapper ticketMapper,
			MessageRepository messageRepository,
			MessegaMapper messegaMapper,
			TicketRepository ticketRepository, @Qualifier("bEncoder")  PasswordEncoder encoder) {

		this.userRepository = userRepository;
		this.encoder = encoder;
		this.ticketRepository = ticketRepository;
		this.userMapper = userMapper;
		this.ticketMapper = ticketMapper;
		this.messegaMapper = messegaMapper;
		this.messageRepository = messageRepository;




	}



	@Override
	//@Cacheable("users")
	public List<UserDTO> getAllUser() {

		List<User> users = userRepository.findAll();
		return users.stream().map(
				userMapper::modelToDTO
				).collect(Collectors.toList());
	}



	@Override
	//@CachePut(key = "#user.username", cacheNames = {"users"})
	public UserDTO registerNewUser(final CreateUser user) {


		User newUser = new User();

		newUser.setPassword(encoder.encode(user.getPassword()));
		newUser.setUsername(user.getUsername());
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());

		Set<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setRole("ROLE_USER");
		roles.add(role);
		newUser.setRoles(roles);

		userRepository.save(newUser);


		return userMapper.modelToDTO(newUser);

	}



	@Override
	public String login(LoginUser user) {

		User loginUser = userRepository.findByUsername(user.getUsername())
				.orElseThrow(UserNotFound::new);

		if(encoder.matches(user.getPassword(), loginUser.getPassword())) {
			return user.getUsername();
		}

		throw new UserNotFound();
	}


	@Override


	//@CachePut(key = "#username", cacheNames = {"users"})
	public UserDTO getUserWithName(String username) {


		User user = userRepository.findByUsername(username).orElseThrow(UserNotFound::new);


        return userMapper.modelToDTO(user);

	}


	@Override
	//@Cacheable("tickets")
	public List<TicketDTO> allTickets() {
		List<Ticket> tickets = ticketRepository.findAll();

		return tickets.stream()
				.map(ticket -> {
					TicketDTO ticketDTO = ticketMapper.modelToDTO(ticket);
					ticketDTO.setUsername(ticket.getUser().getUsername());
					return ticketDTO;
				})
				.toList();

	}


	@Override
	//@CachePut(key = "#result.id", cacheNames = {"tickets"})
	public TicketDTO createTicket(CreateTicket createTicket) {
		User user = userRepository.findById(createTicket.getUserId()).orElseThrow(UserNotFound::new);

		Ticket ticket = new Ticket();
		ticket.setTicketCategory(createTicket.getTicketCategory());
		ticket.setThread(createTicket.getThread());
		ticket.setStatus(TicketStatus.OPEN);
		ticket.setUser(user);





		List<Message> messages = new ArrayList<Message>();
		Message message = new Message();
		message.setUser(user.getUsername());
		message.setMessage(createTicket.getMessage());
		message.setTicket(ticket);

		messages.add(message);
		ticket.setMessages(messages);

		ticket = ticketRepository.save(ticket);

		TicketDTO dto = ticketMapper.modelToDTO(ticket);
		dto.setUsername(user.getUsername());
        return dto;
	}


	@Override
	//@CachePut(key = "#sendMessage.ticketId", cacheNames = {"tickets"})
	public MessageDTO sendMessage(SendMessage sendMessage) {


		Ticket ticket = ticketRepository.findById(sendMessage.getTicketId()).orElseThrow(TicketNotFound::new);



		if(ticket.getStatus() != TicketStatus.CLOSED){



		User user = userRepository.findById(sendMessage.getUserId()).orElseThrow(UserNotFound::new);

		ticket.setStatus(TicketStatus.REPLIED);


		Message m = new Message();

		if(ticket.getUser() == user) {
			String username = user.getUsername();
			String msg = sendMessage.getMes();


			m.setUser(user.getUsername());
			m.setMessage(sendMessage.getMes());
			m.setTicket(ticket);



		} else {

			boolean admin = false;
			for(var role : user.getRoles()) {
				if(role.getRole().equals("ROLE_ADMIN")) {
					admin = true;
					break;
				}
			}

			if(admin) {
				System.err.println(ticket.getId() +  ": 8");
				m = new Message();
				m.setUser(user.getUsername());
				m.setMessage(sendMessage.getMes());
				m.setTicket(ticket);



			}


		}

			ticket.getMessages().add(m);


		 Message saved = messageRepository.save(m);




			return messegaMapper.modelToDTO(saved);

		}else {
			throw new TicketClosed();
		}


	}

	@Override
	//@CachePut(key = "#closeTicket.id", cacheNames = {"tickets"})
	public TicketDTO closeTicket(CloseTicket closeTicket) {
		Ticket ticket = ticketRepository.findById(closeTicket.getId()).orElseThrow(TicketNotFound::new);

		ticket.setStatus(TicketStatus.CLOSED);

		Ticket savedTicket = ticketRepository.save(ticket);

		return ticketMapper.modelToDTO(savedTicket);
	}


}



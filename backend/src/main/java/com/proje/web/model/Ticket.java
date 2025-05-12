package com.proje.web.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {


	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long id;

	private String thread;


	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Message> messages;

	private TicketCategory ticketCategory;

	private TicketStatus status;


	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;








}

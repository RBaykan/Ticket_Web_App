package com.proje.web.dto;

import java.util.List;
import java.util.Set;

import com.proje.web.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private Long id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private Set<Role> roles;
	private List<TicketDTO> tickets;


	
}

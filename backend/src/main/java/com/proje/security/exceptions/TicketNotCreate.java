package com.proje.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Ticket Not Create" )
public class TicketNotCreate extends RuntimeException{
	
	
	

}

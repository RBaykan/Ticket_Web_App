package com.proje.web.dto;

import com.proje.security.validation.PasswordMatches;
import com.proje.web.model.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class CreateTicket {
    @NotBlank
     private Long userId;
    @NotBlank
    private String thread;
    @NotBlank
    private String message;
    @NotBlank
    private TicketCategory ticketCategory;



}

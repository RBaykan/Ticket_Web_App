package com.proje.web.dto;

import com.proje.web.model.Message;
import com.proje.web.model.TicketCategory;
import com.proje.web.model.TicketStatus;
import com.proje.web.model.User;
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
public class TicketDTO {

    private Long id;
    private String thread;
    private List<Message> messages;
    private TicketCategory ticketCategory;
    private TicketStatus status;
    private String username;

}

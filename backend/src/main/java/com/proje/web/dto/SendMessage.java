package com.proje.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessage {

    private Long userId;
    private Long ticketId;
    private String mes;


}

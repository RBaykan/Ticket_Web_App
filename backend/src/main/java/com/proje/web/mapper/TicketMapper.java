package com.proje.web.mapper;

import com.proje.web.dto.TicketDTO;
import com.proje.web.model.Ticket;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TicketMapper {



    Ticket DTOtoModel(TicketDTO ticket);

    TicketDTO modelToDTO(Ticket ticket);


}
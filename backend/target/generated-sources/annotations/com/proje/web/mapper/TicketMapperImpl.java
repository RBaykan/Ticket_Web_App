package com.proje.web.mapper;

import com.proje.web.dto.TicketDTO;
import com.proje.web.model.Message;
import com.proje.web.model.Ticket;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-12T15:36:50+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Arch Linux)"
)
@Component
public class TicketMapperImpl implements TicketMapper {

    @Override
    public Ticket DTOtoModel(TicketDTO ticket) {
        if ( ticket == null ) {
            return null;
        }

        Ticket.TicketBuilder ticket1 = Ticket.builder();

        ticket1.id( ticket.getId() );
        ticket1.thread( ticket.getThread() );
        List<Message> list = ticket.getMessages();
        if ( list != null ) {
            ticket1.messages( new ArrayList<Message>( list ) );
        }
        ticket1.ticketCategory( ticket.getTicketCategory() );
        ticket1.status( ticket.getStatus() );

        return ticket1.build();
    }

    @Override
    public TicketDTO modelToDTO(Ticket ticket) {
        if ( ticket == null ) {
            return null;
        }

        TicketDTO.TicketDTOBuilder ticketDTO = TicketDTO.builder();

        ticketDTO.id( ticket.getId() );
        ticketDTO.thread( ticket.getThread() );
        List<Message> list = ticket.getMessages();
        if ( list != null ) {
            ticketDTO.messages( new ArrayList<Message>( list ) );
        }
        ticketDTO.ticketCategory( ticket.getTicketCategory() );
        ticketDTO.status( ticket.getStatus() );

        return ticketDTO.build();
    }
}

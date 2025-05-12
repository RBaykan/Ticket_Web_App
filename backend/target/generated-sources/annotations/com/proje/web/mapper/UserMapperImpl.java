package com.proje.web.mapper;

import com.proje.web.dto.TicketDTO;
import com.proje.web.dto.UserDTO;
import com.proje.web.model.Message;
import com.proje.web.model.Role;
import com.proje.web.model.Ticket;
import com.proje.web.model.User;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-12T14:27:12+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Arch Linux)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User dtoToModel(UserDTO user) {
        if ( user == null ) {
            return null;
        }

        User.UserBuilder user1 = User.builder();

        user1.id( user.getId() );
        user1.firstname( user.getFirstname() );
        user1.lastname( user.getLastname() );
        user1.username( user.getUsername() );
        user1.password( user.getPassword() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            user1.roles( new LinkedHashSet<Role>( set ) );
        }
        user1.tickets( ticketDTOListToTicketList( user.getTickets() ) );

        return user1.build();
    }

    @Override
    public UserDTO modelToDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.firstname( user.getFirstname() );
        userDTO.lastname( user.getLastname() );
        userDTO.username( user.getUsername() );
        userDTO.password( user.getPassword() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userDTO.roles( new LinkedHashSet<Role>( set ) );
        }
        userDTO.tickets( ticketListToTicketDTOList( user.getTickets() ) );

        return userDTO.build();
    }

    protected Ticket ticketDTOToTicket(TicketDTO ticketDTO) {
        if ( ticketDTO == null ) {
            return null;
        }

        Ticket.TicketBuilder ticket = Ticket.builder();

        ticket.id( ticketDTO.getId() );
        ticket.thread( ticketDTO.getThread() );
        List<Message> list = ticketDTO.getMessages();
        if ( list != null ) {
            ticket.messages( new ArrayList<Message>( list ) );
        }
        ticket.ticketCategory( ticketDTO.getTicketCategory() );
        ticket.status( ticketDTO.getStatus() );

        return ticket.build();
    }

    protected List<Ticket> ticketDTOListToTicketList(List<TicketDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Ticket> list1 = new ArrayList<Ticket>( list.size() );
        for ( TicketDTO ticketDTO : list ) {
            list1.add( ticketDTOToTicket( ticketDTO ) );
        }

        return list1;
    }

    protected TicketDTO ticketToTicketDTO(Ticket ticket) {
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

    protected List<TicketDTO> ticketListToTicketDTOList(List<Ticket> list) {
        if ( list == null ) {
            return null;
        }

        List<TicketDTO> list1 = new ArrayList<TicketDTO>( list.size() );
        for ( Ticket ticket : list ) {
            list1.add( ticketToTicketDTO( ticket ) );
        }

        return list1;
    }
}

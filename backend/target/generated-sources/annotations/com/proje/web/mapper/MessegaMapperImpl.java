package com.proje.web.mapper;

import com.proje.web.dto.MessageDTO;
import com.proje.web.model.Message;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-12T14:27:12+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Arch Linux)"
)
@Component
public class MessegaMapperImpl implements MessegaMapper {

    @Override
    public Message DTOtoModel(MessageDTO messageDTO) {
        if ( messageDTO == null ) {
            return null;
        }

        Message message = new Message();

        message.setId( messageDTO.getId() );
        message.setUser( messageDTO.getUser() );
        message.setMessage( messageDTO.getMessage() );

        return message;
    }

    @Override
    public MessageDTO modelToDTO(Message message) {
        if ( message == null ) {
            return null;
        }

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setId( message.getId() );
        messageDTO.setUser( message.getUser() );
        messageDTO.setMessage( message.getMessage() );

        return messageDTO;
    }
}

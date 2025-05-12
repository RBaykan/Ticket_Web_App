package com.proje.web.mapper;

import com.proje.web.dto.MessageDTO;
import com.proje.web.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessegaMapper {

    Message DTOtoModel(MessageDTO messageDTO);

    MessageDTO modelToDTO(Message message);


}

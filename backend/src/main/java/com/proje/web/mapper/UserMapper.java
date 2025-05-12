package com.proje.web.mapper;

import com.proje.web.dto.UserDTO;
import com.proje.web.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToModel(UserDTO user);

    UserDTO modelToDTO(User user);


}

package com.usermanagement.mapper;

import com.usermanagement.dto.UserCreationDto;
import com.usermanagement.dto.UserResponseDto;
import com.usermanagement.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UserDtoMapper {

    @Mapping(target = "userId" , source = "user.userId")
    @Mapping(target = "email" , source = "user.email")
    @Mapping(target = "phone" , source = "user.phone")
    @Mapping(target = "rank" , source = "user.userRank.name")
    @Mapping(target = "referrer" , source = "user.referrer")
    UserResponseDto userEntityToDto(User entity);
    List<UserResponseDto> userEntityToDtoList(List<User> userEntityList);

    User userDtoToEntity(UserResponseDto entity);

    User userCreationDtoToEntity(UserCreationDto entity);


}

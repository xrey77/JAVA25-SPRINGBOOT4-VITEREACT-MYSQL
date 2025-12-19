package com.java25.java25.springboot4.mysql.repository;

import org.mapstruct.Mapper;

import com.java25.java25.springboot4.mysql.dto.UserlistDto;
import com.java25.java25.springboot4.mysql.entities.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserlistDto toDto(User user);

    List<UserlistDto> toDtoList(List<User> users);
}


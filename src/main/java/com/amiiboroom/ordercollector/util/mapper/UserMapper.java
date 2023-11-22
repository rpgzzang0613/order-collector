package com.amiiboroom.ordercollector.util.mapper;

import com.amiiboroom.ordercollector.dto.users.UserDTO;
import com.amiiboroom.ordercollector.dto.users.UserSignupDTO;
import com.amiiboroom.ordercollector.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends GenericMapper<UserDTO, Users> {

    @Override
    UserDTO toDto(Users entity);

    @Override
    Users toEntity(UserDTO dto);

    Users userSignupDtoToEntity(UserSignupDTO dto);

}

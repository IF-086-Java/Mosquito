package com.softserve.mosquito.services.api;

import com.softserve.mosquito.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto save(UserDto user);

    UserDto update(UserDto user);

    void delete(Long id);

    UserDto getById(Long id);

    List<UserDto> getAll();

    UserDto getByEmail(String email);

    List<UserDto> getBySpecializationId(Long specializationId);

    void activateUser(String key);

    boolean isConfirmed(String email);
}

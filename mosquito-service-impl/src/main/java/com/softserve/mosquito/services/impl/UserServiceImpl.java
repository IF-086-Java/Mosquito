package com.softserve.mosquito.services.impl;


import com.softserve.mosquito.dtos.UserDto;
import com.softserve.mosquito.entities.Specialization;
import com.softserve.mosquito.entities.User;
import com.softserve.mosquito.impl.UserTransformer;
import com.softserve.mosquito.repo.api.UserRepo;
import com.softserve.mosquito.repo.impl.UserRepoImpl;
import com.softserve.mosquito.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private UserTransformer transformer;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserTransformer transformer) {
        this.transformer = transformer;
        this.userRepo = userRepo;
    }

    @Override
    public UserDto createUser(UserDto user) {
        return transformer.toDTO(userRepo.create(transformer.toEntity(user)));
    }

    @Override
    public UserDto updateUser(UserDto user) {
        return transformer.toDTO(userRepo.update(transformer.toEntity(user)));
    }

    @Override
    public void removeUser(Long id) {
        userRepo.delete(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepo.readAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(transformer.toDTO(user));
        }

        return userDtos;
    }

    @Override
    public UserDto getUserById(Long id) {
        return transformer.toDTO(userRepo.read(id));
    }


    public UserDto getUserByEmail(String email) {
        return transformer.toDTO(userRepo.readUserByEmail(email));
    }

    @Override
    public List<UserDto> getUsersBySpecialization(Byte specializationId) {
        List<User> users = userRepo.readAll();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            for (Specialization specialization : user.getSpecializations()) {
                if (specialization.getId() == specializationId)
                    userDtos.add(transformer.toDTO(user));
            }
        }

        return userDtos;
    }
}

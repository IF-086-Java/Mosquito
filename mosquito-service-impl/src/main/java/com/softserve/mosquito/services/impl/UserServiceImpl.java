package com.softserve.mosquito.services.impl;


import com.softserve.mosquito.dtos.UserDto;
import com.softserve.mosquito.entities.Specialization;
import com.softserve.mosquito.entities.User;
import com.softserve.mosquito.repo.api.UserRepo;
import com.softserve.mosquito.services.api.UserService;
import com.softserve.mosquito.services.mail.MailSender;
import com.softserve.mosquito.transformer.UserTransformer;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, MailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    @Transactional
    @Override
    public UserDto save(UserDto user) {
        User activateUser = userRepo.create(UserTransformer.toEntity(user));
        Hashids hashids = new Hashids();
        String urlForActivate = "http://localhost:8080/activate/" + hashids.encode(activateUser.getId());
        sendMessageForActivation(user, urlForActivate);
        return user;
    }

    @Transactional
    @Override
    public UserDto update(UserDto user) {
        UserDto oldUser = getById(user.getId());
        if(!oldUser.getEmail().equals(user.getEmail()) && getByEmail(user.getEmail()) != null){
            return null;
        }
        return UserTransformer.toDto(userRepo.update(UserTransformer.toEntity(user)));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepo.delete(id);
    }

    @Transactional
    @Override
    public List<UserDto> getAll() {
        return UserTransformer.toDtoList(userRepo.readAll());
    }

    @Transactional
    @Override
    public UserDto getById(Long id) {
        return UserTransformer.toDto(userRepo.read(id));
    }

    @Transactional
    public UserDto getByEmail(String email) {
        return UserTransformer.toDto(userRepo.readByEmail(email));
    }

    @Transactional
    @Override
    public List<UserDto> getBySpecializationId(Long specializationId) {
        List<User> users = userRepo.readAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            for (Specialization item : user.getSpecializations()) {
                if (item.getId().equals(specializationId))
                    userDtos.add(UserTransformer.toDto(user));
            }
        }
        return userDtos;
    }

    @Override
    public boolean isConfirmed(String email) {
        return userRepo.isConfirmed(email);
    }

    @Transactional
    @Override
    public void activateUser(String key) {
        long id = new Hashids().decode(key)[0];
        User user = userRepo.read(id);
        user.setConfirmed(true);
        userRepo.update(user);
    }

    private void sendMessageForActivation(UserDto userDto, String message) {
        mailSender.sendMessage(userDto, "Activate your account " + message, "Mosquito Activate Account");
    }

}



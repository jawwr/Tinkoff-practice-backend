package com.project.tinkoff.service.implementation;

import com.project.tinkoff.mapper.UserMapper;
import com.project.tinkoff.repository.models.UserDetailImpl;
import com.project.tinkoff.repository.models.UserDto;
import com.project.tinkoff.service.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserContextServiceImpl implements UserContextService {
    private final UserMapper userMapper;

    @Override
    public UserDto getCurrentUser() {
        return userMapper.fromUserDetails((UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}

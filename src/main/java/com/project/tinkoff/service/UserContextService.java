package com.project.tinkoff.service;

import com.project.tinkoff.repository.models.UserDto;

public interface UserContextService {
    UserDto getCurrentUser();
}

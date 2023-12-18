package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.Card;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardResponse fromModel(Card card);
}

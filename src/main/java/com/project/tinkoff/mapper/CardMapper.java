package com.project.tinkoff.mapper;

import com.project.tinkoff.repository.models.Card;
import com.project.tinkoff.rest.v1.models.response.CardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "authorName", source = "authorName")
    CardResponse fromModel(Card card, String authorName);
}

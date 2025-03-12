package com.atech.pos.mappers;

import java.util.List;

public interface EntityMapper <Dto, Entity> {

    Dto mapToDto(Entity entity);

    Entity mapToEntity(Dto dto);

    List<Dto> mapToDtoList(List<Entity> entities);

    List<Entity> mapToEntityList(List<Dto> dtos);
}

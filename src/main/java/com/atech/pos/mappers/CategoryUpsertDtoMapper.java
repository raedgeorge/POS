package com.atech.pos.mappers;

import com.atech.pos.dtos.CategoryUpsertDto;
import com.atech.pos.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryUpsertDtoMapper extends EntityMapper<CategoryUpsertDto, Category>{
}

package com.atech.pos.mappers;

import com.atech.pos.dtos.CategoryDto;
import com.atech.pos.entity.Category;
import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper extends EntityMapper<CategoryDto, Category>{
}

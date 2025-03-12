package com.atech.pos.mappers;

import com.atech.pos.dtos.ProductUpsertDto;
import com.atech.pos.entity.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductUpsertDtoMapper extends EntityMapper<ProductUpsertDto, Product>{
}

package com.atech.pos.mappers;

import com.atech.pos.dtos.ProductDto;
import com.atech.pos.entity.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper extends EntityMapper<ProductDto, Product>{
}

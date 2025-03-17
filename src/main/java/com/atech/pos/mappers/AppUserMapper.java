package com.atech.pos.mappers;

import com.atech.pos.dtos.AppUserDto;
import com.atech.pos.entity.AppUser;
import org.mapstruct.Mapper;

@Mapper
public interface AppUserMapper extends EntityMapper<AppUserDto, AppUser>{
}

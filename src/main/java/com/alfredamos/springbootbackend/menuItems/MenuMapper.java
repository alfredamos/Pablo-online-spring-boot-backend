package com.alfredamos.springbootbackend.menuItems;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuMapper {
    Menu toEntity(MenuDto menuDto);

   @Mapping(source = "user.id", target = "userId")
    MenuDto toDTO(Menu menu);

    List<MenuDto> toDTOList(List<Menu> menus);
}

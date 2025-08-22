package com.alfredamos.springbootbackend.menuItems;

import com.alfredamos.springbootbackend.exceptions.NotFoundException;
import com.alfredamos.springbootbackend.users.UserRepository;
import com.alfredamos.springbootbackend.utils.ResponseMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MenuService {
   private final MenuMapper menuMapper;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    public MenuDto createMenu(MenuDto menuDto) {
        //----> get the user associated with the creation.
        var user = userRepository.findById(menuDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

        //----> Map menu-dto to menu.
        var menu = menuMapper.toEntity(menuDto);

        //----> Set the user.
        menu.setUser(user);

        //----> save the menu in the database.
        menuRepository.save(menu);

        return menuDto;
    }

    public ResponseMessage deleteMenu(UUID id) {
        //----> Check for existence of menu to be deleted.
        var menu = getOneMenu(id);

        //----> Delete the menu.
        menuRepository.delete(menu);

        return new ResponseMessage("Success", "Order has been deleted successfully!", 204);

    }

    public MenuDto editMenu(UUID id, MenuDto menuDto) {
        //----> Check for existence of menu to be edited.
        getOneMenu(id);

        //----> get the user associated with the creation.
        var user = userRepository.findById(menuDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));

        //----> Edit the menu.
        var menu = menuMapper.toEntity(menuDto);
        menu.setUser(user);
        menuRepository.save(menu);

        return menuMapper.toDTO(menu);
    }

    public MenuDto getMenuById(UUID id){
        //----> Check for existence of menu to be fetched.
        var menu = getOneMenu(id);

        return menuMapper.toDTO(menu);
    }

    public List<MenuDto> getAllMenu(){
        //----> Get all the menus from the database.
        var menus = menuRepository.findAll();

        return menuMapper.toDTOList(menus);
    }


    private Menu getOneMenu(UUID id){
        return menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Menu not found in the database!"));
    }


}

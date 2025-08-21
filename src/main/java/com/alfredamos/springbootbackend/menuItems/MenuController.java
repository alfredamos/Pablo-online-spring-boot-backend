package com.alfredamos.springbootbackend.menuItems;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/api/menu-items")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<MenuDto> createMenu(@Valid @RequestBody MenuDto menuDto) {
        System.out.println("Creating Menu Item, menuDto: " + menuDto);
        //----> Create a new menu.
        var response = menuService.createMenu(menuDto);

        //----> Send back the response.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable(name = "id") UUID id) {
        System.out.println("Deleting Menu Item, id: " + id);
        //----> Delete the menu with the given id.
        var response = menuService.deleteMenu(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editMenu(@PathVariable(name = "id") UUID id, @Valid @RequestBody MenuDto menuDto) {
        System.out.println("Updating Menu Item, id: " + id);
        //----> Update the menu with the given id.
        var response = menuService.editMenu(id, menuDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuDto>> findAllMenu() {
        //----> Retrieve all the menus.
        System.out.println("Finding All Menu Items");
        var response = menuService.getAllMenu();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> findMenuById(@PathVariable(name = "id") UUID id) {
        //----> Retrieve all the menus.
        System.out.println("Find menu by id " + id);
        var response = menuService.getMenuById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

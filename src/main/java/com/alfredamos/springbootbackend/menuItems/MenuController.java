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

package com.alfredamos.springbootbackend.menuItems;

import com.alfredamos.springbootbackend.orderDetail.OrderDetailDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuDto {
    @NotBlank(message = "Name is required.")
    private String name;

    @Positive(message = "Price must be positive.")
    private Double price;

    @Positive(message = "Quantity must be greater than zero.")
    private Integer quantity;

    @NotBlank(message = "Image is required.")
    private String image;

    @NotBlank(message = "Description is required.")
    private String description;

    private UUID userId;

}

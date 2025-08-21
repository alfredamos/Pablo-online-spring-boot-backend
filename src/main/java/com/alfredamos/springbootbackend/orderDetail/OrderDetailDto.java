package com.alfredamos.springbootbackend.orderDetail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Image is required.")
    private String image;

    @Positive(message = "Price must be positive.")
    private Double price;

    @Positive(message = "Quantity must be greater than zero.")
    @Size(min=1, max=20, message = "Quantity must be between 1 and 20.")
    private Integer quantity;

    @NotBlank(message = "MenuId is required.")
    private UUID menuId;


}

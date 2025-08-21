package com.alfredamos.springbootbackend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private String status;
    private String message;
    private int statusCodes;
}

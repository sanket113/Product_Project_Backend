package com.example.rbac.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequestDto {
    private String title;
    private String description;
    private String status;
}

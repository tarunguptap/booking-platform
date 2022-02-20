package com.organization.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class CityDto {
    private Long id;
    private Integer version;
    private Date createdDate;
    private Date updatedDate;
    @NotBlank
    @Size(min = 1, max = 256, message = "The name field length should be between 1 to 256")
    private String name;
    private String pincode;
    private String state;
    private String country;
}

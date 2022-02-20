package com.organization.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ShowDto {
    private Long id;
    private Date createdDate;
    private Date updatedDate;
    private Date startTime;
    private Date endTime;
    private Long movieId;
    private Long auditoriumId;
}

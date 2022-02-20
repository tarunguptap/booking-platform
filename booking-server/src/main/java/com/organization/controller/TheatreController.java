package com.organization.controller;

import com.organization.dto.TheatreDto;
import com.organization.service.TheatreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/{cityId}/theatres")
public class TheatreController {
    private final TheatreService theatreService;

    public TheatreController(TheatreService theatreService) {
        this.theatreService = theatreService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TheatreDto> get(@PathVariable Long cityId) {
        log.info("Calling endpoint GET v3/{}/theatres", cityId);
        return theatreService.getTheatres(cityId);
    }

    @GetMapping(value = "{movieName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TheatreDto> browse(@PathVariable Long cityId,
                                   @PathVariable String movieName,
                                   @RequestParam(value = "showDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS") Date showDate) {
        log.info("Calling endpoint GET v3/{}/theatres", cityId);
        return theatreService.searchTheater(cityId, movieName, showDate);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public TheatreDto post(@PathVariable Long cityId, @Validated @RequestBody TheatreDto dto) {
        log.info("Calling endpoint POST v3/{}/theatres", cityId);
        return theatreService.save(cityId, dto);
    }

    @PostMapping(value = "/bulk", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TheatreDto> postBulk(@PathVariable Long cityId, @Validated @RequestBody List<TheatreDto> dtos) {
        log.info("Calling endpoint POST v3/{}/theatres", cityId);
        return theatreService.saveBulk(cityId, dtos);
    }
}

package com.example.demo.controller;

import com.example.demo.model.JukeBox;
import com.example.demo.service.JbSettingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
public class JbSettingController {

    private JbSettingService jbSettingService;

    @Autowired
    public JbSettingController(JbSettingService jbSettingService) {
        this.jbSettingService = jbSettingService;
    }

    @Operation(summary = "Get list of supported jukeboxes for setting ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found supported jukeboxes",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JukeBox.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Setting ID not found.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)})
    @GetMapping(value = "/supported-jukeboxes")
    public ResponseEntity<List<JukeBox>> getSupportedJukeBoxes(@Parameter(description = "Setting ID")  @RequestParam(name = "settingId") String settingId,
                                                               @Parameter(description = "filter by model name (optional)") @RequestParam(name = "model", required = false) String model,
                                                               @Parameter(description = "specifies at what index start the page (optional)")
                                                                   @RequestParam(name = "offset", required = false, defaultValue = "0") @PositiveOrZero(message = "Offset must be greater than or equal to 0.") int offset,
                                                               @Parameter(description = "specifies the page size (optional)")
                                                                   @RequestParam(name = "limit", required = false) @Positive(message = "Limit must be greater than or equal to 1.") Integer limit) {

        return new ResponseEntity<>(jbSettingService.getSupportedJukeboxes(settingId, model, offset, limit), HttpStatus.OK);
    }

}

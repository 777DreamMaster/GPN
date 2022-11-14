package club.gpn.controller;

import club.gpn.model.ApiRequest;
import club.gpn.model.Result;
import club.gpn.service.VKService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/getUserIsMember")
@RequiredArgsConstructor
public class MemberController {
    private final VKService vkService;

    @Operation(summary = "Get the user's first name, last name and whether he is a member of the group",
            security = @SecurityRequirement(name = "auth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Response with correct data",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Result.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping
    public Result getUserIsMember(@RequestHeader("vk_service_token") String token,
                                  @RequestBody ApiRequest body)
            throws IOException, URISyntaxException, InterruptedException {

        return vkService.getResponse(body, token);
    }
}
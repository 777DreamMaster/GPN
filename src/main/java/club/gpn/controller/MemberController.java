package club.gpn.controller;

import club.gpn.model.ApiRequest;
import club.gpn.model.Result;
import club.gpn.service.VKService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/getUserIsMember")
@RequiredArgsConstructor
public class MemberController {
    private final VKService vkService;
    @PostMapping
    public Result getUserIsMember(@RequestHeader("vk_service_token") String token,
                                  @RequestBody ApiRequest body)
            throws IOException, URISyntaxException, InterruptedException {

        return vkService.getResponse(body, token);
    }
}
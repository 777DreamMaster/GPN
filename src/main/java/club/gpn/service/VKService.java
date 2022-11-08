package club.gpn.service;

import club.gpn.exception.IllegalParameterException;
import club.gpn.model.ApiRequest;
import club.gpn.model.Result;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VKService {
    private static final String baseURL = "https://api.vk.com/method/";
    private static final String VERSION = "5.131";
    private final VKRepository vkRepository;
    private final CacheManager cacheManager;

    public URI createUserURI(String method,
                             Map<String, String> params,
                             String access_token) throws URISyntaxException {
        URIBuilder uri = new URIBuilder(baseURL + method);
        for (String key : params.keySet()) {
            uri.addParameter(key, params.get(key));
        }
        uri.addParameter("access_token", access_token);
        uri.addParameter("v", VERSION);
        return uri.build();
    }

    public JsonNode getUserInfo(ApiRequest request, String access_token)
            throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", request.getUser_id());
        return vkRepository.sendRequest(createUserURI("users.get", params, access_token));
    }

    public JsonNode getUserIsMember(ApiRequest request, String access_token)
            throws URISyntaxException, IOException, InterruptedException {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", request.getUser_id());
        params.put("group_id", request.getGroup_id());
        return vkRepository.sendRequest(createUserURI("groups.isMember", params, access_token));
    }

    public Result getResponse(ApiRequest body, String token)
            throws URISyntaxException, IOException, InterruptedException {
        validateRequest(body);

        JsonNode user = getUserInfo(body, token);
        JsonNode isMember = getUserIsMember(body, token);
        validateVKResponse(user, isMember, token);

        Result response = new Result();
        response.setLast_name(user.get("response").get(0).get("last_name").asText());
        response.setFirst_name(user.get("response").get(0).get("first_name").asText());
        response.setMember(isMember.get("response").asInt() == 1);

        return response;
    }

    private static void validateRequest(ApiRequest request) {
        String invParams = "Error";
        boolean f = false;
        if (!isInt(request.getUser_id()) || Integer.parseInt(request.getUser_id()) <= 0) {
            invParams += " Invalid user_id: " + request.getUser_id() + ".";
            f = true;
        }
        if (!isInt(request.getGroup_id()) || Integer.parseInt(request.getGroup_id()) <= 0) {
            invParams += " Invalid group_id: " + request.getGroup_id() + ".";
            f = true;
        }
        if (f) throw new IllegalParameterException(invParams);
    }

    private static void validateVKResponse(JsonNode user, JsonNode isMember, String token) {
        String errorText = "Error. ";
        if (user.has("error")) {
            int error_code = user.get("error").get("error_code").asInt();
            String error_msg = "VK Api response: " + user.get("error").get("error_msg").asText();
            errorText += switch (error_code) {
                case 5 -> "Invalid access_token: " + token + error_msg;
                case 8 -> "Invalid VK Api version. Contact api dev: version " + VERSION + error_msg;
                default -> error_msg;
            };
            throw new IllegalParameterException(errorText);
        }
        if (isMember.has("error")) {
            int error_code = isMember.get("error").get("error_code").asInt();
            String error_msg = " VK Api response: " + isMember.get("error").get("error_msg").asText();
            errorText += switch (error_code) {
                case 5 -> "Invalid access_token: " + token + error_msg;
                case 8 -> "Invalid VK Api version. Contact api dev: version " + VERSION + error_msg;
                case 15 -> "Access to Group with this ID denied. " + error_msg;
                case 100 -> "Invalid user_id or group_id. " + error_msg;
                default -> error_msg;
            };
            throw new IllegalParameterException(errorText);
        }
    }

    public static boolean isInt(String string) {
        if (string == null || string.equals("")) {
            return false;
        }
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
package club.gpn;

import club.gpn.controller.MemberController;
import club.gpn.exception.IllegalParameterException;
import club.gpn.model.ApiRequest;
import club.gpn.model.Result;
import club.gpn.repository.VKRepo;
import club.gpn.service.VKService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor()
class GpnApplicationTests {
    @Autowired
    CacheManager cacheManager;
    final MemberController Controller = new MemberController(new VKService(new VKRepo(), cacheManager));
    ApiRequest correctRequest;
    String correctToken = "dedb9ecbdedb9ecbdedb9ecb09ddcaca3dddedbdedb9ecbbdbe91ed1fa67754ea3e7892";

    static ApiRequest buildRequest(String user, String group) {
        return new ApiRequest(user, group);
    }

    static Result buildMember(String first_name, String last_name, boolean member){
        return new Result(last_name, first_name, member);
    }

    @BeforeEach
    void createCorrectRequest() {
        correctRequest = new ApiRequest("78385", "93559769");
    }

    @AfterEach
    void waitVKApi() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(300);
    }

    @ParameterizedTest(name = "{index}. Correct input ({0}, {1})")
    @CsvFileSource(resources = "/correct.csv", numLinesToSkip = 1, delimiter = ';')
    void testCorrectInput(String user, String group, String last_name, String first_name, boolean member)
            throws IOException, URISyntaxException, InterruptedException {
        ApiRequest request = buildRequest(user, group);
        Result expected = buildMember(first_name, last_name, member);
        assertEquals(expected, Controller.getUserIsMember(correctToken, request));
    }

    @ParameterizedTest(name = "{index}. IncorrectToken ({0})")
    @ValueSource(strings = {"dedb9ecbdedb9ecbdedb9ecb09ddcaca", " ", "", "null", "0"})
    void testIncorrectToken(String token) {
        Throwable thrown = assertThrows(IllegalParameterException.class,
                () -> Controller.getUserIsMember(token, correctRequest));
        assertTrue(thrown.getMessage().contains("Invalid access_token:"));
    }

    @ParameterizedTest(name = "{index}. Incorrect input ({0}, {1})")
    @CsvFileSource(resources = "/incorrectData.csv", numLinesToSkip = 1, delimiter = ';')
    void testUserGroupID(String user, String group) {
        ApiRequest request = buildRequest(user, group);

        Throwable thrown = assertThrows(IllegalParameterException.class,
                () -> Controller.getUserIsMember(correctToken, request));
        assertTrue(thrown.getMessage().contains("Error"));
    }

}

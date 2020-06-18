package util;

import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import model.Environment;
import org.hamcrest.core.Is;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
//import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class TestAssertionUtils {
    public static void validatePispResponseHeaders(Environment env, Response actualResponse){
        validateContentTypeHeader(actualResponse);
        if(env.getPisp()!=null && env.getPisp().getHeaders()!=null) {
            if (env.getPisp().getHeaders().get("x-fapi-interaction-id") != null) {
                log.info("Value of x-fapi-interaction-id in Response Header : {}", actualResponse.getHeader("x-fapi-interaction-id"));
                assertThat(actualResponse.getHeader("x-fapi-interaction-id"), equalToIgnoringCase(env.getPisp().getHeaders().get("x-fapi-interaction-id")));
            } else {
                log.info("value of x-fapi-interaction-id in Resonse header : {}", actualResponse.getHeader("x-fapi-interaction-id"));
                assertThat(actualResponse.getHeader("x-fapi-interaction-id"), Is.isA(String.class));
            }
        }
        String headers = actualResponse.headers().asList().toString();
        log.debug("Headers received in response: {}",headers);

    }

    private static void validateContentTypeHeader(Response actualResonse){
        log.debug("Checking headers.");
        if(actualResonse.getStatusCode()!=204){
            assertThat(actualResonse.getHeader("Content-Type"),containsString("application/json"));
        }
    }
}

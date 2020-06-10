import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class oauth2 {
    @Test
    public static void getTest() {

        final RequestSpecification request = RestAssured.given();
        request.contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .formParam("grant_type","client_credentials")
                .formParam("client_id","3123123213")
                .formParam("client_secret","234234fewrwefw")
                .formParam("scope","payments")
                .log().all();

        final Response response;
        response = request.when()
                .post("http://localhost:8089/oAuth2");
        response.then().log().all();
        //System.out.println(response.getBody().prettyPrint());
    }
@Test
    public static void getTes2t() {

        RestAssured.given().when().log().all()
                .post("http://localhost:8089/oAuth2").then().log().all();
        //System.out.println(response.getBody().prettyPrint());
    }

    public static void main(String[] args) {
        getTest();
    }
}

package steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import model.AuthenticationResult;
import model.CustomerAuthenticationContext;
import model.Environment;
import model.TokenEndpointResponse;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import util.JWTUtils;
import util.Sha256Utils;
import util.TestUtils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;


@Slf4j
public class AuthenticationSteps {

    public String getTppAccessToken(final Environment env){
        final String token = this.getTppAccessTokenResponse(env)
                .then()
                .statusCode(200)
                .body("access_token", notNullValue())
                .body("access_token",not(isEmptyOrNullString()))
                .extract()
                .path("access_token");
        return token;
    }

    public Response getTppAccessTokenResponse(final Environment env){
        Response response = null;
        final RequestSpecification request = TestUtils.given(env);
        request.contentType("application/x-www-form-urlencoded")
                .accept("application/json")
                .formParam("grant_type","client_credentials")
                .formParam("client_id","3123123213")
                .formParam("client_secret","234234fewrwefw")
                .formParam("scope","payments")
                .log().all();

        response = request.when()
                .post(env.getTppAuthN().getTokenEndpointUrl()+"/oAuth2");
        response.then().log().all();
        //System.out.println(response.getBody().prettyPrint());
        return response;
    }

    public TokenEndpointResponse getTokensForAuthorisationCode(final Environment env, final String authCode, final String pkceChallenge){

        final RequestSpecification request = TestUtils.given(env)
                .param("code_verifier", pkceChallenge)
                .param("code_challenge_method","S256")
                .param("code",authCode)
                .param("redirect_url",env.getTppAuthN().getRedirectUrl())
                .param("client_id",env.getTppAuthN().getClientId())
                .param("client_secret",env.getTppAuthN().getClientSecret());
        request.log().all();
        Response response =  request.when()
                .post(env.getTppAuthN().getTokenEndpointUrl()+"/oAuth2AuthCode");
        TokenEndpointResponse tokenResponse = response.then()
                .statusCode(200)
                .extract().as(TokenEndpointResponse.class);
        response.then().log().all();

        try{
            Assert.assertThat(tokenResponse.getAccessToken(), Matchers.is(not(emptyOrNullString())));
        }catch(AssertionError e){
            throw e;
        }
        return tokenResponse;

    }

    public AuthenticationResult authenticateCustomer(final Environment env, final String consentId){

        if(env.getCustomer().getCustomerId()!=null && env.getCustomer().getUserId()!=null){
            return this.authenticateCustomer(env,consentId,
                    this::generateBanklineUrl,
                    this::getBanklineUpdatedContext);
        }else{
            return this.authenticateCustomer(env,consentId,
                    this::genereateDbIdUrl,
                    this::getBanklineUpdatedContext);
        }
    }

    private AuthenticationResult authenticateCustomer(final Environment env, final String consentId,
                                                      BiFunction<Environment, CustomerAuthenticationContext, String> generateUrl,
                                                      BiConsumer<Environment, CustomerAuthenticationContext> getUpdatedContext){

        final CustomerAuthenticationContext context = new CustomerAuthenticationContext().setConsentId(consentId);

        //Step 0 - get url to access

        final String dbidUrl = generateUrl.apply(env, context);
        context.setDbidURL(dbidUrl);

        getUpdatedContext.accept(env, context);

        //step 3 - submit 4P
        //final Response fourPSubmissionResponse = this.submit4PPageAndReturnRedirect(env,context);

        return new AuthenticationResult()
                .setAccessToken("access-token value")
                .addCookie("PF",context.getPfCookieValue())
                .addCookie("userState", context.getUserStateCookieValue())
                .addCookie("consentSessId", context.getConsentSessIdCookieValue())
                .setContext(context)
                .setAccessURL("url");
    }
    public String generateBanklineUrl(final Environment env, final CustomerAuthenticationContext context){

        final String pkceChallenge = JWTUtils.generatePkceChallenge();
        final String hashedPkceChallenge = Sha256Utils.getSHA256UrlEncoded(pkceChallenge);
        final String nonce = JWTUtils.generateNonce();

        context.setPkceChallenge(pkceChallenge);

        return String.format(
                "%?client_id=%s&response_type=code id_token&code_challenge_method=%s&code_challenge=%s&request=%s&request=%s&nonce=%s&authnMethod=%s",
                env.getCustomerAuthN().getBaseUrl()+"/as/authorization.oauth2",env.getTppAuthN().getClientId(),"256",hashedPkceChallenge,
                JWTUtils.getJWTRequest(env, context.getConsentId(), nonce), nonce,"C4P");

    }
    public String genereateDbIdUrl(final Environment env, final CustomerAuthenticationContext context){

        final String pkceChallenge = JWTUtils.generatePkceChallenge();
        final String hashedPkceChallenge = Sha256Utils.getSHA256UrlEncoded(pkceChallenge);
        final String nonce = JWTUtils.generateNonce();

        context.setPkceChallenge(pkceChallenge);

        return String.format(
                "%?client_id=%s&response_type=code id_token&code_challenge_method=%s&code_challenge=%s&request=%s&nonce=%s",
                env.getCustomerAuthN().getBaseUrl()+"/as/authorization.oauth2",env.getTppAuthN().getClientId(),"256",hashedPkceChallenge,
                JWTUtils.getJWTRequest(env, context.getConsentId(), nonce), nonce);

    }

    public void getBanklineUpdatedContext(Environment env, CustomerAuthenticationContext context){
        //step 1 - grab the login page and send dbid then grab the login page of bankline
        this.getBankLinePageAndUpdateContext(env, context);
        this.assertGetBanklinePage(context);

    }
    private void getBankLinePageAndUpdateContext(final Environment env, final CustomerAuthenticationContext context){
        RequestSpecification request = RestAssured.given();
        final Response bankLinePResponse =  request.accept("text/html")
                .when()
                .get(context.getDbidURL());

        final String bankLinePageHtml = bankLinePResponse.then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        final Element formElement = extractBanklineFormFromUserIdPage(bankLinePageHtml);
        context.setDbidFormAction(formElement.attr("action"));
        context.setHiddenBrandField(formElement.select("input[name=brand]").attr("value"));
        context.setHiddenSubmitUserDetailsField(formElement.select("input[name='submitUserDetails']").attr("value"));
        context.setHiddenAuthnMethodField(formElement.select("input[name='authnMethod']").attr("value"));
        context.setPfCookieValue(bankLinePResponse.getCookie("PF"));
    }

    private void assertGetBanklinePage(final CustomerAuthenticationContext context){
        assertThat(context.getDbidFormAction(),is(not(isEmptyOrNullString())));
        assertThat(context.getHiddenBrandField(),is(not(isEmptyOrNullString())));
        assertThat(context.getHiddenAuthnMethodField(),is(not(isEmptyOrNullString())));


    }

    private Element extractBanklineFormFromUserIdPage(final String bankLinePageHtml){
        log.debug("Bankline Page: \n"+ bankLinePageHtml);
        final Document banklinePage = Jsoup.parse(bankLinePageHtml);

        final String errorMessage = extractErrorMessage(banklinePage);
        if(StringUtils.isNotBlank(errorMessage)){
            throw new RuntimeException(String.format("Error shown on Bankline User Id Page - %s", errorMessage));
        }

        final Elements formElement = banklinePage.select("form#user-id-page");

        if(formElement.isEmpty()){
            throw new RuntimeException("Invalid Banline User ID Page - are the login services functioning correctly?");
        }

        return formElement.first();
    }

    private String extractErrorMessage(final Document page){
        return page.select(".ob-error-panel.zb-notification-body").text();
    }
}

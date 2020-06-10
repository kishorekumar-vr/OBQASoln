package cpb.pisp.v3.ip.fp.submit;

import integrationtest.AbstractTestEnvironmentAwareSuper;
import io.restassured.response.Response;
import model.Environment;
import model.TokenEndpointResponse;
import org.hamcrest.CoreMatchers;
import util.JWTUtils;
import util.TestAssertionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;

public class BaseSubmitTests extends AbstractTestEnvironmentAwareSuper {
    public static String CONSENT_ID = "Data.ConsentId";
    public BaseSubmitTests(String baseEnvironmentPath) {
        super("cpb/pisp/v3/ip/fp/submit"+ baseEnvironmentPath);
    }

    protected void successfulSubmit(Environment env){

        Map<String, String> map = this.successfulAuthentication(env);
        Response response = pispStepsV3.createInternationalPayment(env, map.get("TOKEN"), map.get("CONSENT_ID"));

        assertNotNull(response);
        TestAssertionUtils.validatePispResponseHeaders(env,response);
        response.then()
                .statusCode(201)
                .body("Data.InternationalPaymentId", CoreMatchers.is(CoreMatchers.notNullValue()))
                .body("Data.Status",CoreMatchers.is(CoreMatchers.notNullValue()))
                .body("Data.Status",CoreMatchers.is(not(equalTo("Rejected"))));
        verifyLinkInSubmitResponse(env,response);
    }

    protected Map<String, String> successfulAuthentication(Environment env){

        final String tppAccessToken = authenticationSteps.getTppAccessToken(env);

        final Response createConsentResponse = this.pispStepsV3.createInternationalPaymentConsent(env,tppAccessToken);
        final String paymentConsentId = createConsentResponse.then()
                .statusCode(201)
                .extract().body().path("Data.ConsentId");
         String fapi = createConsentResponse.then()
                .extract().header("x-fapi-interaction-id");
         HashMap<String, String> headersNew = new HashMap<String, String>();
         headersNew = env.getPisp().getHeaders();
         headersNew.put("x-fapi-interaction-id", fapi);
         env.getPisp().setHeaders(headersNew);
        //System.out.println(fapi);
        verifyLinkInSetupResponse(env, createConsentResponse);

        /*final AuthenticationResult authResult = this.authenticationSteps.authenticateCustomer(env, paymentConsentId);

        assertNotNull(authResult);
        assertNotNull(authResult.getAccessToken());

        final AuthorizationContext context = new AuthorizationContext()
                .setCookies(authResult.getCookies())
                .setAccessToken(authResult.getAccessToken());*/

        //final Response response = this.authorizationSteps.bootStrap(env, context);
        //assertNotNull(response);

      /*  if(env.getPisp().getDebtorDetails()!=null){
            final Response selectAccountsResponse = this.authorizationSteps.selectAccounts(env, env.getAuthz().getAccountsToUse());

            assertNotNull(selectAccountsResponse);
        }*/

        /*final Response authorizeResponse = this.authorizationSteps.authorize(env,context);
        assertNotNull(authorizeResponse);
        assertNotNull(authorizeResponse.statusCode(), CoreMatchers.is(200));
        */

        //final Response redirectUrlResponse = this.authorizationSteps.getRedirectUrl(env,context);

        //final String redirectToPingUrl = redirectUrlResponse.then().extract().path("url");

        //final String authCode = this.authenticationSteps.getAuthCodeFromPostAuthZRedirect(env, authResult.getCookies(),redirectToPingUrl);
        final String authCode = UUID.randomUUID().toString();

        final TokenEndpointResponse tokens = this.authenticationSteps.getTokensForAuthorisationCode(env,authCode, JWTUtils.generatePkceChallenge());

        Map<String, String> map = new HashMap<>();
        map.put("CONSENT_ID", paymentConsentId);
        map.put("TOKEN", tokens.getAccessToken());
        map.put("REFRESH_TOKEN",UUID.randomUUID().toString().substring(0,18));

        return map;

    }

    private void verifyLinkInSetupResponse(final Environment env, final Response setupResponse){
        final String expectedLink = env.getPisp().getBaseUrl()+"/open-banking/v3.1/pisp/international-payments-consents/"+setupResponse.then().extract().body().path("Data.ConsentId");
        setupResponse.then().body("Data.Links.Self", CoreMatchers.is(equalTo(expectedLink)));

    }
    private void verifyLinkInSubmitResponse(final Environment env, final Response submitResponse){
        final String expectedLink = env.getPisp().getBaseUrl()+"/open-banking/v3.1/pisp/international-payments/"+submitResponse.then().extract().body().path("Data.InternationalPaymentId");
        submitResponse.then().body("Data.Links.Self", CoreMatchers.is(equalTo(expectedLink)));
    }

}


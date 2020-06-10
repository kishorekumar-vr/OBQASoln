package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Environment;
import model.IdentifierScheme;
import model.PispEnvironment;
import org.apache.commons.lang3.StringUtils;
import paymentModel.OBCashAccount;
import paymentModel.OBDomestic1InstructedAmount;
import paymentModel.OBInternational1;

import java.util.Map;

public class PISPStepsV3 {

    private String apiVersion = "v3.1";
    ObjectMapper mapper = new ObjectMapper();

    public Response createInternationalPaymentConsent (final Environment env, final String accessToken){
/*
        OBInternational1 internationalConsent = this.createInternationalPaymentConsentRequest(env);
        String requestBody="";
        try{
            requestBody = mapper.writeValueAsString(internationalConsent);
        }catch(Exception e){
            e.printStackTrace();
        }*/

        final RequestSpecification request = buildPispRequest(env, accessToken, false)
                .body(createInternationalPaymentConsentRequest(env));
        //System.out.println(request);
                //request.log().all();
        final Response response = request.when()
                .post(env.getPisp().getBaseUrl()+"/open-banking/"+ apiVersion + "/pisp/international-payment-consents");
        response.then().log().all();

        return response;
    }

    public Response createInternationalPayment(final Environment env, final String accessToken, final String consentId){
        OBInternational1 internationalPayment = this.createInternationalPaymentRequest(env,consentId);
        String requestBody = "";
        try{
            requestBody = mapper.writeValueAsString(internationalPayment);
        }catch(Exception e){
            e.printStackTrace();
        }
        final RequestSpecification request = buildPispRequest(env,accessToken,false)
                .body(internationalPayment);
        //request.log().all();
        final Response response = request.when()
                .post(env.getPisp().getBaseUrl()+"/open-banking/" + apiVersion+"/pisp/international-payments");
        response.then().log().all();
        return response;
    }

    private RequestSpecification buildPispRequest(Environment env, String accessToken, boolean cbpii){
        RequestSpecification request = RestAssured.given()
                .accept("application/json")
                .contentType("application/json")
                .header("Host", env.getPisp().getHostHeader())
                .header("Authorization","Bearer "+accessToken);

        final Map<String, String> headerToAdd = env.getPisp().getHeaders();
        headerToAdd.keySet().forEach(headerName -> request.given().header(headerName, headerToAdd.get(headerName)));
        request.log().all();
        return request;
    }

    private OBInternational1 createInternationalPaymentConsentRequest(final Environment env){
        final PispEnvironment pispDetails = env.getPisp();
        final String creditorAccountIdentifier = pispDetails.getCreditorDetails().getAccountIdentifier();
        final String creditorAccountScheme = mapScheme(env, pispDetails.getCreditorDetails().getAccountScheme());

        final OBInternational1 request =new OBInternational1()
                .setInstructionIdentification(pispDetails.getInstructionId())
                .setEndToEndIdentification(pispDetails.getEndToEndId())
                .setCurrencyOfTransfer(pispDetails.getInternationalPaymentDetails().getCurrencyOfTransfer())
                .setInstructedAmount(new OBDomestic1InstructedAmount()
                                    .setCurrency(pispDetails.getCurrency()))
                .setCreditorAccount(new OBCashAccount()
                    .setIdentification(creditorAccountIdentifier)
                    .setSchemeName(creditorAccountScheme)
                    .setName(pispDetails.getCreditorDetails().getName())
                    .setSecondaryIdentification(pispDetails.getCreditorDetails().getSecondaryAccountIdentifier()));


        return request;
    }

    private OBInternational1 createInternationalPaymentRequest(final Environment env,final String consentId){
        final PispEnvironment pispDetails = env.getPisp();

        final String creditorAccountIdentifier = pispDetails.getCreditorDetails().getAccountIdentifier();
        final String creditorAccountScheme = mapScheme(env, pispDetails.getCreditorDetails().getAccountScheme());

        final OBInternational1 request =new OBInternational1()
                .setConsentId(consentId)
                .setInstructionIdentification(pispDetails.getInstructionId())
                .setEndToEndIdentification(pispDetails.getEndToEndId())
                .setCurrencyOfTransfer(pispDetails.getInternationalPaymentDetails().getCurrencyOfTransfer())
                .setInstructedAmount(new OBDomestic1InstructedAmount()
                        .setCurrency(pispDetails.getCurrency()))
                .setCreditorAccount(new OBCashAccount()
                        .setIdentification(creditorAccountIdentifier)
                        .setSchemeName(creditorAccountScheme)
                        .setName(pispDetails.getCreditorDetails().getName())
                        .setSecondaryIdentification(pispDetails.getCreditorDetails().getSecondaryAccountIdentifier()));


        return request;
    }
    private String mapScheme(final Environment env, final IdentifierScheme scheme){
        if(IdentifierScheme.SCAN.equals(scheme)){
            return "UK.OBIE.SortCodeAccountNumber";
        }else if(IdentifierScheme.IBAN.equals(scheme)){
            return "UK.OBIE.IBAN";
        }else if (IdentifierScheme.OTHER.equals(scheme)){
            String newBrand;
            if(StringUtils.equalsIgnoreCase(env.getExecutionEnvironment().getBrand(),"NWI")){
                newBrand = "NWO";
            }else newBrand = env.getExecutionEnvironment().getBrand().toUpperCase();
            return "UK."+newBrand + ".Other";
        }else if (IdentifierScheme.PAN.equals(scheme)){
            return "UK.OBIE.PAN";
        } else {
            throw new RuntimeException("Unable to map identifierScheme = "+ scheme.name());
        }

    }
}

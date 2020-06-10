package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class CustomerAuthenticationContext {
    private String dbidURL;
    private String dbidFormAction;
    private String hiddenBrandField;
    private String hiddenSubmit1Field;
    private String hiddenSubmit2Field;
    private String hiddenSubmitUserDetailsField;
    private String hiddenAuthnMethodField;
    private String pfCookieValue;
    private String pf1CookieValue;
    private String TS013bfd1d;
    private String userStateCookieValue;
    private String fourPFormAction;
    private String hiddenSubmitChallengeField;
    private Integer pin1Index;
    private Integer pin2Index;
    private Integer pin3Index;
    private Integer pwd1Index;
    private Integer pwd2Index;
    private Integer pwd3Index;
    private String consentSessIdCookieValue;
    private String consentCaptureCookie;
    private String TS01c96431;


    private String authZAccessToken;
    private String consentId;
    private String pkceChallenge;
    private String bankLineUrl;
    private String xcsrftoken;

}

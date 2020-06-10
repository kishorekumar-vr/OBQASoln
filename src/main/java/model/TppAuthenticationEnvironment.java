package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
public class TppAuthenticationEnvironment {
    private String clientId;
    private String clientSecret;
    private String tokenEndpointUrl;
    private String TokenEndpointHostHeader;
    private String redirectUrl;
    private String scope;
    private String acrValue;
    private String signingKeyStore;
    private String signingKeyStorePassword;
    private String signingId;
    private String signingKeyAlias;
    private String signingKeyId;
    private String enableMATLS;
    private String transportTrustStore;
    private String transportTrustStorePassword;
    private String transportKeyStore;
    private String transportKeyStorePassword;
    private String ignoreServerCert;
    private boolean sslEnabled = false;

    public TppAuthenticationEnvironment mergeIn(final TppAuthenticationEnvironment toMerge ){
        if(toMerge.clientId != null){
            this.clientId = toMerge.clientId;
        }
        return this;
    }
    public String getClientId(){
        //return encryptionService.decrypt(this.clientId);
        return this.clientId;
    }

}

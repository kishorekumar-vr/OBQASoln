package model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IamEnvironment {

    private String pingFederateHost;
    private String registrationHost;
    private String enableMATLS;
    private String transportTrustStore;
    private String transportTrustStorePassword;
    private String transportKeyStore;
    private String transportKeyStorePassword;
    private String signingKeyStore;
    private String signingKeyStorePassword;
    private String signingKeyAlias;
    private String signingKeyId;

    public IamEnvironment mergeIn(final IamEnvironment toMerge){
        if(toMerge.pingFederateHost!=null){
            this.pingFederateHost = toMerge.pingFederateHost;
        }
        if(toMerge.registrationHost!=null){
            this.registrationHost = toMerge.registrationHost;
        }
        if(toMerge.enableMATLS!=null){
            this.enableMATLS = toMerge.enableMATLS;
        }
        if(toMerge.transportTrustStore!=null){
            this.transportTrustStore = toMerge.transportTrustStore;
        }
        if(toMerge.transportTrustStorePassword!=null){
            this.transportTrustStorePassword = toMerge.transportTrustStorePassword;
        }
        if(toMerge.transportKeyStore!=null){
            this.transportKeyStore = toMerge.transportKeyStore;
        }
        if(toMerge.transportKeyStorePassword!=null){
            this.transportKeyStorePassword = toMerge.transportKeyStorePassword;
        }
        if(toMerge.signingKeyStore!=null){
            this.signingKeyStore = toMerge.signingKeyStore;
        }
        if(toMerge.signingKeyStorePassword!=null){
            this.signingKeyStorePassword = toMerge.signingKeyStorePassword;
        }
        return this;
    }
}

package util;


import io.restassured.RestAssured;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import model.Environment;
import model.TppAuthenticationEnvironment;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import static io.restassured.config.EncoderConfig.encoderConfig;

public class TestUtils {

    public static RequestSpecification given(final Environment env){
        //RequestSpecification request;
        if(env.getTppAuthN().isSslEnabled()){
            return given(env,sslConfig(env.getTppAuthN()));
        }else {
            return RestAssured.given();
        }
    }

    private static RequestSpecification given(final Environment env,final SSLConfig sslConfig){
        final RequestSpecification requestSpec = RestAssured.given().config(new RestAssuredConfig()
        .sslConfig(sslConfig)
        .redirect(new RedirectConfig().followRedirects(false))
        .encoderConfig(encoderConfig().encodeContentTypeAs("application/jwt", ContentType.TEXT)))
        .filter(new RequestLoggingFilter())
        .filter(new ResponseLoggingFilter())
        //.filter(new FapiInteractionIdRecorderFilter(env))
                ;
        return requestSpec;
    }
    private static SSLConfig sslConfig(TppAuthenticationEnvironment env){
        return sslConfig(env.getTransportTrustStore(),
                env.getTransportTrustStorePassword(),
                env.getEnableMATLS(),
                env.getIgnoreServerCert(),
                env.getTransportKeyStore(),
                env.getTransportKeyStorePassword()
        );
    }

    private static SSLConfig sslConfig(String transportTrustStore, String transportTrustStorePassword,
                                       String enableMATLS,String ignoreServerCert, String transportKeyStore,
                                       String transportKeyStorePassword){
        SSLSocketFactory clientAuthFactory = null;

        try {
            final KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(TestUtils.class.getResourceAsStream(transportTrustStore),
                    transportTrustStorePassword.toCharArray());

            if (!Boolean.parseBoolean(enableMATLS)) {
                return SSLConfig.sslConfig().relaxedHTTPSValidation().trustStore(trustStore);
            } else {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                keyStore.load(TestUtils.class.getResourceAsStream(transportKeyStore),
                        transportKeyStorePassword.toCharArray());

                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keyStore, transportKeyStorePassword.toCharArray());
                KeyManager[] keyManager = kmf.getKeyManagers();

                TrustManager[] trustManager;
                if (Boolean.parseBoolean(ignoreServerCert)) {
                    trustManager = new TrustManager[]{new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }};
                } else {
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(trustStore);
                    trustManager = trustManagerFactory.getTrustManagers();
                }

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManager, trustManager, null);

                clientAuthFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }
        }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        return SSLConfig.sslConfig().with().sslSocketFactory(clientAuthFactory);

    }
}

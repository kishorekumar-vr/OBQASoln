package iam;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;

public class KeytoolHelper {

    private static KeyStore getKeyStore(final String keyStorePath, final String keyStorePassword){
        try{
            String keyStoreType = keyStoreType(keyStorePath);
            final KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStorePath);
            keyStore.load(is,keyStorePassword.toCharArray());

            return keyStore;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private static String keyStoreType(String keyStorePath){
        String keyStoreType;
        if(keyStorePath.endsWith(".p12")){
            keyStoreType = "pkcs12";
        }else{
            keyStoreType="JKS";
        }
        return  keyStoreType;
    }

    public static Key key(final String keyStorePath, final String keyStorePassword, final String alias)throws Exception{
        String keystoreAlias  = determineAlias(keyStorePath,alias);
        return KeytoolHelper.getKeyStore(keyStorePath, keyStorePassword).getKey(keystoreAlias, keyStorePassword.toCharArray());
    }

    private static boolean isP12File(String keyStorePath){
        return keyStoreType(keyStorePath).equalsIgnoreCase("pkcs12");
    }

    private static String determineAlias(String keyStorePath, String alias){
        return isP12File(keyStorePath)? "1": alias;
    }
}

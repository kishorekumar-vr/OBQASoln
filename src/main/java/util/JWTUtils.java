package util;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import iam.KeytoolHelper;
import model.Environment;
import org.apache.commons.net.util.Base64;
import org.json.JSONObject;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JWTUtils {

    public static String generatePkceChallenge(){
        final byte[] pkceChallengeBytes = new byte[32];
        Sha256Utils.getRandom(pkceChallengeBytes);
        return Base64.encodeBase64URLSafeString(pkceChallengeBytes);
    }

    public static String generateNonce(){
        return UUID.randomUUID().toString();
    }

    public static String getJWTRequest(Environment env, String consentId, String nonce){
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("iss",env.getTppAuthN().getClientId());
        attributes.put("response_type","code id_token");
        attributes.put("client_id",env.getTppAuthN().getClientId());
        attributes.put("redirect_uri",env.getTppAuthN().getRedirectUrl());
        attributes.put("scope","openid"+env.getTppAuthN().getScope());
        attributes.put("consentRefId",consentId);
        attributes.put("exp",(new Date().getTime() / 1000)+300);
        attributes.put("nonce",nonce);

        JSONObject obIntentId = new JSONObject();
        obIntentId.put("value", consentId);
        obIntentId.put("essential", true);

        JSONObject acr = new JSONObject();
        acr.put("essential",true);

        JSONObject userInfo = new JSONObject();
        userInfo.put("openbanking_intent_id", obIntentId);
        userInfo.put("acr", acr);

        JSONObject idToken = new JSONObject();
        idToken.put("openbanking_intent_id", obIntentId);
        idToken.put("acr", acr);


        JSONObject claims = new JSONObject();
        claims.put("userInfo", userInfo);
        claims.put("id_token", idToken);

        attributes.put("cliams",claims);

        if(env.getTppAuthN().getAcrValue()!=null){
            attributes.put("acr_values", env.getTppAuthN().getAcrValue());
        }

        String token = "";

        try{
            token = JWTUtils.generateSignedToken(env,attributes);
        }catch(Exception e){
            throw new RuntimeException();
        }
        return token;
    }

    public static String generateSignedToken(final Environment env, final Map<String, Object> jwsValue) throws Exception{

        RSAPrivateKey key = (RSAPrivateKey) KeytoolHelper.key(env.getTppAuthN().getSigningKeyStore(),
                env.getTppAuthN().getSigningKeyStorePassword(),
                env.getTppAuthN().getSigningKeyAlias());

        JWSSigner jwsSigner = new RSASSASigner(key);

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(env.getTppAuthN().getSigningKeyId()).build(),
                new Payload(new net.minidev.json.JSONObject(jwsValue).toJSONString()));
        jwsObject.sign(jwsSigner);
        return jwsObject.serialize();
    }





}

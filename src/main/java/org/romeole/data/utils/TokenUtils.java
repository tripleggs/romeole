package org.romeole.data.utils;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import net.minidev.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongzhou
 * @title: TokenUtils
 * @projectName romeole
 * @description: TODO
 * @date 2020/8/2116:34
 */
public class TokenUtils {
    /**
     * 1.创建一个32-byte的密匙
     */

    public static final int EXPIRED = 0;

    public static final int INVALID = -1;

    public static final int SUCCESS = 1;

    private static final byte[] secret = "geiwodiangasfdjsikolkjikolkijswe".getBytes();

    //生成一个token
    public static String creatToken(Map<String, Object> payloadMap) throws JOSEException {
        /**
         * JWSHeader参数：1.加密算法法则,2.类型，3.。。。。。。。
         * 一般只需要传入加密算法法则就可以。
         * 这里则采用HS256
         * JWSAlgorithm类里面有所有的加密算法法则，直接调用。
         */
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //建立一个载荷Payload
        Payload payload = new Payload(new JSONObject(payloadMap));
        //将头部和载荷结合在一起
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        //建立一个密匙
        JWSSigner jwsSigner = new MACSigner(secret);
        //签名
        jwsObject.sign(jwsSigner);
        //生成token
        return jwsObject.serialize();
    }

    //解析一个token
    public static Map<String, Object> valid(String token) throws ParseException, JOSEException {
    //解析token
        JWSObject jwsObject = JWSObject.parse(token);
        //获取到载荷
        Payload payload = jwsObject.getPayload();
        //建立一个解锁密匙
        JWSVerifier jwsVerifier = new MACVerifier(secret);

        Map<String, Object> resultMap = new HashMap<>();
        //判断token
        if (jwsObject.verify(jwsVerifier)) {
            resultMap.put("Result", 1);
            //载荷的数据解析成json对象。
            JSONObject jsonObject = payload.toJSONObject();
            resultMap.put("data", jsonObject);
            //判断token是否过期
            if (jsonObject.containsKey("exp")) {
                Long expTime = Long.valueOf(jsonObject.get("exp").toString());
                Long nowTime = new Date().getTime();
                //判断是否过期
                if (nowTime > expTime) {
                    //已经过期
                    resultMap.clear();
                    resultMap.put("Result", 0);
                }
            }
        } else {
            resultMap.put("Result", -1);
        }
        return resultMap;

    }



    public static String createToken(String uid, Long exp) {
        //获取生成token
        Map<String, Object> map = new HashMap<>();
        //建立载荷，这些数据根据业务，自己定义。
        map.put("uid", uid);
        //生成时间
        map.put("sta", new Date().getTime());
        //过期时间
        map.put("exp", new Date().getTime() + exp);
        try {
            String token = TokenUtils.creatToken(map);
            return token;
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;

    }

    //处理解析的业务逻辑
    public static boolean validToken(String token) {
        if (token == null || token.isEmpty())
            return false;
        String[] split = token.split("\\.");
        if (split.length != 3)
            return false;
        if (split[0].length() != 20)
            return false;
        if (split[1].length() != 92)
            return false;
        if (split[2].length() != 43)
            return false;
        return true;
    }

    public static Map<String, Object> getValidTokenInfo(String token) {
        //解析token
        Map<String, Object> validMap = new HashMap<>();
        try {
            validMap = TokenUtils.valid(token);
            int res = (int) validMap.get("Result");
            JSONObject data = (JSONObject)validMap.get("data");
            validMap.clear();
            validMap.put("res",res);
            validMap.put("uid",data.getAsString("uid"));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        } finally {
            return validMap;
        }
    }

    public static RSAKey getKey() throws JOSEException {
        RSAKeyGenerator rsaKeyGenerator = new RSAKeyGenerator(2048);
        RSAKey rsaJWK = rsaKeyGenerator.generate();
        return rsaJWK;
    }

    public static String creatTokenRS256(Map<String,Object> payloadMap,RSAKey rsaJWK) throws JOSEException {

        //私密钥匙
        JWSSigner signer = new RSASSASigner(rsaJWK);

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
                new Payload(new JSONObject(payloadMap))
        );
        //进行加密
        jwsObject.sign(signer);

        String token= jwsObject.serialize();
        return token;
    }

    //验证token
    public static boolean validRS256(String token,RSAKey rsaJWK) throws ParseException, JOSEException {
        //获取到公钥
        RSAKey rsaKey = rsaJWK.toPublicJWK();
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier jwsVerifier = new RSASSAVerifier(rsaKey);
        //验证数据
        return jwsObject.verify(jwsVerifier);
    }

    public static void main(String[] args) {
        //生成周期长一些的token便于测试
        String token = createToken("2019091115310259161", 100*24*60*60*1000l);
        /*try {
            System.out.println("thread start sleep");
            Thread.sleep(5l*1000l);
            System.out.println("thread stop sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        Map<String,Object> data = getValidTokenInfo(token);
        System.out.println("valid token : " + token);
        System.out.println("valid result : " + (int)data.get("res"));
        System.out.println("valid uid : " + data.get("uid").toString());
    }

}

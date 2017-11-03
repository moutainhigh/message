package com.zhongan.icare.message.im.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


public class TlsSignatureUtil {
    private static final String ACCOUNT_TYPE = "0";
    private static final long EXPIRE_SECOND = (long) (60 * 60 * 24 * 180);
    private static final String BC = "BC";
    private static final String SHA256SHA_256_WITH_ECDSAECDSA = "SHA256withECDSA";

    private static final String UTF8 = "UTF-8";

    private TlsSignatureUtil() {
    }

    private static Signature getSignatureWithPrivateKey(PrivateKey privateKey, String content) throws Exception {
        Signature signature = Signature.getInstance(SHA256SHA_256_WITH_ECDSAECDSA, BC);
        signature.initSign(privateKey);
        signature.update(content.getBytes(Charset.forName(UTF8)));
        return signature;
    }

    private static Signature getSignatureWithPublicKey(PublicKey publicKey, String content) throws Exception {
        Signature signature = Signature.getInstance(SHA256SHA_256_WITH_ECDSAECDSA, BC);
        signature.initVerify(publicKey);
        signature.update(content.getBytes(Charset.forName(UTF8)));
        return signature;
    }

    private static PrivateKey getPrivateKey(String privateKeyStr) throws IOException {
        Security.addProvider(new BouncyCastleProvider());
        Reader reader = new CharArrayReader(privateKeyStr.toCharArray());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PEMParser parser = new PEMParser(reader);
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) parser.readObject();
        parser.close();
        return converter.getPrivateKey(privateKeyInfo);
    }

    private static PublicKey getPublicKey(String publicKeyStr) throws IOException {
        Reader reader = new CharArrayReader(publicKeyStr.toCharArray());
        PEMParser parser = new PEMParser(reader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        SubjectPublicKeyInfo subjectPublicKeyInfo = (SubjectPublicKeyInfo) parser.readObject();
        parser.close();
        return converter.getPublicKey(subjectPublicKeyInfo);
    }

    private static String getContent(String identifier, String time, long appId) {
        StringBuilder sb = new StringBuilder();
        sb.append("TLS.appid_at_3rd:").append(0).append("\n").append("TLS.account_type:").append(ACCOUNT_TYPE).append("\n").append("TLS.identifier:")
                .append(identifier).append("\n").append("TLS.sdk_appid:").append(appId).append("\n").append("TLS.time:").append(time).append("\n")
                .append("TLS.expire_after:").append(EXPIRE_SECOND).append("\n");
        return sb.toString();
    }

    /**
     * 生成UserSig
     *
     * @param identifier 用户ID
     * @return sig
     * @throws Exception
     */
    public static String getUserSig(String identifier, long appId, String privateKey) throws Exception {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        Signature signature = getSignatureWithPrivateKey(getPrivateKey(privateKey), getContent(identifier, time, appId));
        byte[] signatureBytes = signature.sign();

        String sigTLS = Base64.encodeBase64String(signatureBytes);

        Map<String, Object> map = new HashMap<>();
        map.put("TLS.account_type", 0);
        map.put("TLS.identifier", identifier);
        map.put("TLS.appid_at_3rd", 0);
        map.put("TLS.sdk_appid", appId);
        map.put("TLS.expire_after", EXPIRE_SECOND);
        map.put("TLS.version", "201512300000");
        map.put("TLS.sig", sigTLS);
        map.put("TLS.time", time);
        String jsonString = JSON.toJSONString(map);

        Deflater deflater = new Deflater();
        deflater.setInput(jsonString.getBytes(Charset.forName(UTF8)));

        deflater.finish();
        byte[] compressBytes = new byte[512];
        int compressBytesLength = deflater.deflate(compressBytes);
        deflater.end();
        return new String(Base64.encodeBase64(Arrays.copyOfRange(compressBytes, 0, compressBytesLength)));
    }

    /**
     * 验证签名
     *
     * @param userSig    签名
     * @param identifier 用户ID
     * @return
     * @throws Exception
     */
    public static boolean checkUserSig(String userSig, String identifier, String publicKey, long appId) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        Base64 decoder = new Base64();

        byte[] compressBytes = Base64.decodeBase64(userSig.getBytes(Charset.forName(UTF8)));

        Inflater inflater = new Inflater();
        inflater.setInput(compressBytes, 0, compressBytes.length);
        byte[] decompressBytes = new byte[1024];
        int decompressLength = inflater.inflate(decompressBytes);
        inflater.end();

        String jsonString = new String(Arrays.copyOfRange(decompressBytes, 0, decompressLength));

        JSONObject jsonObject = JSON.parseObject(jsonString);
        String sigTLS = jsonObject.getString("TLS.sig");

        byte[] signatureBytes = decoder.decode(sigTLS.getBytes(Charset.forName(UTF8)));

        String sigTime = jsonObject.getString("TLS.time");
        Signature signature = getSignatureWithPublicKey(TlsSignatureUtil.getPublicKey(publicKey), getContent(identifier, sigTime, appId));
        return signature.verify(signatureBytes);
    }

}

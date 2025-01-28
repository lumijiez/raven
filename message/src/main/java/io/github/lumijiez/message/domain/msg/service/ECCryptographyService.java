package io.github.lumijiez.message.domain.msg.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

@Service
public class ECCryptographyService {
    private static final String CURVE = "secp256r1";
    private final KeyPair keyPair;

    public ECCryptographyService() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE);
            keyPairGenerator.initialize(ecSpec, new SecureRandom());
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ECC service", e);
        }
    }

    public byte[] encrypt(String message) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
        keyAgreement.init(keyPair.getPrivate());

        KeyPairGenerator ephemeralKeyPairGen = KeyPairGenerator.getInstance("EC", "BC");
        ephemeralKeyPairGen.initialize(new ECGenParameterSpec(CURVE));
        KeyPair ephemeralKeyPair = ephemeralKeyPairGen.generateKeyPair();

        keyAgreement.doPhase(ephemeralKeyPair.getPublic(), true);
        byte[] sharedSecret = keyAgreement.generateSecret();

        byte[] encryptionKey = deriveKey(sharedSecret);

        SecretKey secretKey = new SecretKeySpec(encryptionKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

        byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(ephemeralKeyPair.getPublic().getEncoded());
        outputStream.write(encrypted);

        return outputStream.toByteArray();
    }

    public String decrypt(byte[] encryptedData) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptedData);
        byte[] iv = new byte[12];
        inputStream.read(iv);

        byte[] ephemeralPubKeyBytes = new byte[91];
        inputStream.read(ephemeralPubKeyBytes);

        byte[] encrypted = inputStream.readAllBytes();

        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(ephemeralPubKeyBytes);
        PublicKey ephemeralPubKey = keyFactory.generatePublic(pubKeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
        keyAgreement.init(keyPair.getPrivate());
        keyAgreement.doPhase(ephemeralPubKey, true);
        byte[] sharedSecret = keyAgreement.generateSecret();

        byte[] decryptionKey = deriveKey(sharedSecret);

        SecretKey secretKey = new SecretKeySpec(decryptionKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private byte[] deriveKey(byte[] sharedSecret) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        byte[] salt = "ECCServiceSalt".getBytes(StandardCharsets.UTF_8);
        byte[] info = "EncryptionKey".getBytes(StandardCharsets.UTF_8);

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(salt, "HmacSHA256");
        hmac.init(secretKey);

        byte[] prk = hmac.doFinal(sharedSecret);
        hmac.init(new SecretKeySpec(prk, "HmacSHA256"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] t = new byte[0];

        for (int i = 1; outputStream.size() < 32; i++) {
            hmac.update(t);
            hmac.update(info);
            hmac.update((byte) i);
            t = hmac.doFinal();
            outputStream.write(t);
        }

        return Arrays.copyOf(outputStream.toByteArray(), 32);
    }
}
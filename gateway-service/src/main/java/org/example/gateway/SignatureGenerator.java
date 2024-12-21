package org.example.gateway;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignatureGenerator {

    public static void main(String[] args) {
        String apiKey = "e179f5173ceb11e43f0c5bed4fe2aa3b";
        String secretKey = "f9ce85650e";

        long timestamp = System.currentTimeMillis() / 1000L;

        String signatureString = apiKey + secretKey + timestamp;

        String xSignature = generateSignature(signatureString);

        System.out.println("X-Signature: " + xSignature);
    }

    private static String generateSignature(String signatureString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(signatureString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating X-Signature", e);
        }
    }
}


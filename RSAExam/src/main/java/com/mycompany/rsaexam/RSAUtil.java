package com.mycompany.rsaexam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RSAUtil {
    public static String encryptLicense(File license) throws Exception {
        PrivateKey priKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(readKey("secret.key.pkcs8")));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        byte[] rawLicense = Files.readAllBytes(license.toPath());
        byte[] encryptedLicense = cipher.doFinal(rawLicense);
        byte[] base64License = Base64.getMimeEncoder().encode(encryptedLicense);
        return new String(base64License);
    }

    public static String decryptLicense(File license) throws Exception {
        PublicKey pubKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(readKey("public.key")));

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);

        byte[] base64License = Files.readAllBytes(license.toPath());
        byte[] encryptedLicense = Base64.getMimeDecoder().decode(base64License);
        byte[] rawLicense = cipher.doFinal(encryptedLicense);
        return new String(rawLicense);
    }
    
    private static byte[] readKey(final String fileName) throws Exception {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream keyStream = loader.getResourceAsStream(fileName);
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(keyStream))) {
            String line;
            StringBuilder sb = new StringBuilder();
            boolean isContents = false;

            while ((line = br.readLine()) != null) {
                if (line.matches("[-]+BEGIN[ A-Z]+[-]+")) {
                    isContents = true;
                } else if (line.matches("[-]+END[ A-Z]+[-]+")) {
                    break;
                } else if (isContents) {
                    sb.append(line);
                }
            }

            return Base64.getDecoder().decode(sb.toString());
        } catch (FileNotFoundException e) {
            throw new Exception("File not found.", e);
        } catch (IOException e) {
            throw new Exception("can't read the PEM file.", e);
        }
    }    
}

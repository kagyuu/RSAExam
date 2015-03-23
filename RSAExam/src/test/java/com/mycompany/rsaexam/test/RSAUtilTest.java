package com.mycompany.rsaexam.test;

import com.mycompany.rsaexam.RSAUtil;
import java.io.File;
import java.io.FileOutputStream;
import org.junit.Test;

public class RSAUtilTest {

    public RSAUtilTest() {
    }

    @Test
    public void hello() throws Exception {
        String encrypted = RSAUtil.encryptLicense(new File("src/test/resources/License.txt"));
        System.out.println("***** ENCREPTYED *****");
        System.out.println(encrypted);
        
        File tmpFile = File.createTempFile("tmp", ".txt");
        tmpFile.deleteOnExit();
        try (FileOutputStream fout = new FileOutputStream(tmpFile)) {
            fout.write(encrypted.getBytes());
        }
        
        String decripted = RSAUtil.decryptLicense(tmpFile);
        System.out.println("***** DECRYPTED *****");
        System.out.println(decripted);
    }
}

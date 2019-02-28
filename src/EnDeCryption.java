import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import static javax.crypto.Cipher.DECRYPT_MODE;

public class EnDeCryption {

    String password = "key-secret";

    public byte[] encryption(byte[] data){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password));

            byte[] cipher_data = cipher.update(data);
            byte[] rest = cipher.doFinal();
            byte[] result = new byte[cipher_data.length + rest.length];
            System.arraycopy(cipher_data, 0, result, 0, cipher_data.length);
            System.arraycopy(rest, 0, result, cipher_data.length, rest.length);
            System.out.println();
            System.out.println(Arrays.toString(cipher_data));
            System.out.println(Arrays.toString(rest));
            System.out.println((Arrays.toString(result)));
            System.out.println();

            return result;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public byte[] decryption(byte[] data){
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(DECRYPT_MODE, generateKey(password));
            cipher.update(data);
            return cipher.doFinal();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public SecretKeySpec generateKey(String password){
        try {
            byte[] salt = new byte[8];
            SecureRandom random = SecureRandom.getInstanceStrong();
            random.nextBytes(salt);
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
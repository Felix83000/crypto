import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import static javax.crypto.Cipher.DECRYPT_MODE;

public class EnDeCryption {

    String password = "";

    public byte[] encryption(byte[] data){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password));
            cipher.update(data);
            return cipher.doFinal();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public byte[] decryption(byte[] data){
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
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
                    SecretKeyFactory.getInstance("NoPadding");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
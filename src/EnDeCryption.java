import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class EnDeCryption {

    public byte[] encryption(byte[] data, String password){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            Key key = generateKey(password);

            System.out.println("key : "+key);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipher_data = cipher.update(data);
            byte[] rest = cipher.doFinal();
            byte[] result = new byte[cipher_data.length + rest.length];
            System.arraycopy(cipher_data, 0, result, 0, cipher_data.length);
            System.arraycopy(rest, 0, result, cipher_data.length, rest.length);
            System.out.println();
            System.out.println("cipher data : "+Arrays.toString(cipher_data));
            System.out.println("rest : "+Arrays.toString(rest));
            System.out.println("result : "+Arrays.toString(result));
            System.out.println();

            return result;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public byte[] decryption(byte[] data, String password){
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            Key key = generateKey(password);

            System.out.println("key : "+key);
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plain_data = cipher.update(data);
            byte[] rest = cipher.doFinal();
            byte[] result = new byte[plain_data.length + rest.length];
            System.arraycopy(plain_data, 0, result, 0, plain_data.length);
            System.arraycopy(rest, 0, result, plain_data.length, rest.length);

            return result;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private Key generateKey(String password){
        try {
            byte[] salt = {0,0,0,0,0,0,0,0};
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            Key k = new SecretKeySpec(tmp.getEncoded(),"AES");
            return k;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

}
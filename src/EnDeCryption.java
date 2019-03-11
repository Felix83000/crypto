import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.KeySpec;


public class EnDeCryption {
    private String password;

    public EnDeCryption(String password){
        this.password = password;
    }

    public byte[] encryption(byte[] data, String name){
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            Key key = generateKey(this.password, name);

            //System.out.println("key : "+key);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] plainBloc = new byte[16];
            byte[] cipherBloc = new byte[16];
            byte[] cipherText = new byte[data.length];

            // CBC
            // https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_(CBC)
            // Chiffre par bloc de 16 octets
            for (int i = 0; i < data.length-1; i += 16) {
                // Si ce n'est pas la première étape
                if (i != 0) {
                    // Séparation du bloc
                    System.arraycopy(data, i, plainBloc, 0, 16);
                    // XOR entre le bloc chiffré et le Bloc clair
                    plainBloc = xor(cipherBloc, plainBloc);
                } else {
                    // Séparation du bloc
                    System.arraycopy(data, i, plainBloc, 0, 16);
                }
                //Chiffrement du bloc
                cipherBloc = cipher.update(plainBloc);

                //Copie du bloc chiffré dans le bloc final
                System.arraycopy(cipherBloc, 0, cipherText, i, 16);
            }

            /*
            byte[] lastBloc = new byte[16];
            byte[] tmpBloc = new byte[16];
            byte[] finalBloc = new byte[16];
            byte[] b = new byte[16];
            // CTS
            // https://fr.wikipedia.org/wiki/Mode_d%27op%C3%A9ration_(cryptographie)#Chiffrement_avec_vol_de_texte_:_%C2%AB_CipherText_Stealing_%C2%BB_(CTS)
            // Avant dernier bloc
            System.arraycopy(data, (data.length / 16) , lastBloc, 0, 16);
            System.arraycopy(data, (data.length / 16) + 16, tmpBloc, 0, data.length % 16);
            for (int j = data.length % 16; j < tmpBloc.length; j++) {
                lastBloc[j] = 0x0;
            }
            System.arraycopy(cipher.update(xor(lastBloc, cipherBloc)),0,finalBloc,0,16);
            System.arraycopy(cipher.update(xor(tmpBloc, finalBloc)),0,b,0,16);

            System.arraycopy(b, 0, cipherText, (data.length / 16), 16);
            System.arraycopy(finalBloc, 0, cipherText, (data.length - data.length % 16), data.length % 16);
            */

            /* Façon conventionnel
            byte[] cipher_data = cipher.update(data);
            byte[] rest = cipher.doFinal();
            byte[] result = new byte[cipher_data.length + rest.length];
            System.arraycopy(cipher_data, 0, result, 0, cipher_data.length);
            System.arraycopy(rest, 0, result, cipher_data.length, rest.length);
            */

            return cipherText;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] xor(byte[] tab1, byte[] tab2) {
        byte[] result = new byte[tab1.length];

        for (int i = 0; i < tab1.length; i++) {
            result[i] = (byte) (tab1[i] ^ tab2[i]);
        }
        return result;
    }

    public byte[] decryption(byte[] data, String name) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

            Key key = generateKey(this.password, name);

            cipher.init(Cipher.DECRYPT_MODE, key);


            byte[] cipherBloc = new byte[16];
            byte[] cipherBlocxor = new byte[16];
            byte[] plainText = new byte[data.length];
            byte[] plainBloc = new byte[16];

            // CBC
            for (int i = 0; i < data.length-1; i += 16) {
                // Si ce n'est pas la première étape
                if (i == 0) {
                    // Séparation du bloc
                    System.arraycopy(data, i, cipherBloc, 0, 16);
                    // Sauvgarde du bloc chiffré pour le XOR
                    System.arraycopy(data, i, cipherBlocxor, 0, 16);
                    //Chiffrement du bloc
                    plainBloc = cipher.update(cipherBloc);
                } else {
                    // Séparation du bloc
                    System.arraycopy(data, i, cipherBloc, 0, 16);
                    //Chiffrement du bloc
                    plainBloc = cipher.update(cipherBloc);
                    // XOR entre le bloc chiffré et le Bloc clair
                    plainBloc = xor(cipherBlocxor, plainBloc);
                    // Sauvgarde du bloc chiffré pour le XOR
                    System.arraycopy(data, i, cipherBlocxor, 0, 16);
                }
                //Copie du bloc chiffré dans le bloc final
                System.arraycopy(plainBloc, 0, plainText, i, 16);
            }

           /* Façon conventionnel
            byte[] plain_data = cipher.update(data);
            byte[] rest = cipher.doFinal();
            byte[] result = new byte[plain_data.length + rest.length];
            System.arraycopy(plain_data, 0, result, 0, plain_data.length);
            System.arraycopy(rest, 0, result, plain_data.length, rest.length);
            */

            return plainText;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Key generateKey(String password, String Salt) {
        try {
            //byte[] salt = {0, 0, 0, 0, 0, 0, 0, 0};
            byte[] salt = Salt.getBytes();
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 128);
            SecretKey tmp = factory.generateSecret(spec);
            Key k = new SecretKeySpec(tmp.getEncoded(), "AES");
            return k;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
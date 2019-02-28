
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Crypto {
    public static void main(final String[] args) throws Exception {
        if (args.length > 0) {
            if (args[1].equals("in")) {
                byte[] encoded = Files.readAllBytes(Paths.get(args[2]));
                System.out.println(Arrays.toString(encoded));
                EnDeCryption cryption = new EnDeCryption();
                System.out.println(Arrays.toString(cryption.encryption(encoded)));
                //Affichage binaire
                BigInteger bi = new BigInteger(encoded);
                System.out.println("Data 1 : " + bi.toString(2) + " \n Nombre de bits : " + bi.toString(2).getBytes().length);
            }
        }
    }
}

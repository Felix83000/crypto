
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Crypto {
    public static void main(final String[] args) throws Exception {
        if (args.length >= 5) {
            for (int i = 0; i < args.length; i++) {
                System.out.println("args " + i + " : " + args[i]);
            }
            byte[] cipher = {0};
            byte[] encoded = Files.readAllBytes(Paths.get(args[2]));
            System.out.println(Arrays.toString(encoded));

            //Affichage binaire
            BigInteger bi = new BigInteger(encoded);
            System.out.println("Data plain : " + bi.toString(2) + " \n Nombre de bits : " + bi.toString(2).getBytes().length);

            EnDeCryption cryption = new EnDeCryption();

            if (args[5].equals("pass")) {
                switch (args[0]) {
                    case "enc":
                        cipher = cryption.encryption(encoded, args[6]);
                        break;
                    case "dec":
                        cipher = cryption.decryption(encoded, args[6]);
                        break;
                }
            } else {
                switch (args[0]) {
                    case "enc":
                        cipher = cryption.encryption(encoded, "default_pass");
                        break;
                    case "dec":
                        cipher = cryption.decryption(encoded, "default_pass");
                        break;
                }
            }
            System.out.println("main call result : " + Arrays.toString(cipher));


            cipher = cryption.decryption(cipher, args[6]);


            //Affichage binaire
            BigInteger bi1 = new BigInteger(cipher);
            System.out.println("Data cypher : " + bi.toString(2) + " \n Nombre de bits : " + bi1.toString(2).getBytes().length);

            try (FileOutputStream fos = new FileOutputStream(args[4])) {
                fos.write(cipher);
            }
        } else {
            System.out.println("Veuillez utiliser cette application avec minimum 3 arguments : –enc|-dec –in <input file> -out <output file> (-pass <password>)");
        }
    }
}

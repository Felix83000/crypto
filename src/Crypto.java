
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Crypto {
    public static void main(final String[] args) throws Exception {
        //Vérification des paramètres
        boolean clear = isClear(args);

        if (clear) {
            byte[] cipher = {0};
            byte[] encoded = Files.readAllBytes(Paths.get(args[2]));
            System.out.println(Arrays.toString(encoded));

            //Affichage binaire
            BigInteger bi = new BigInteger(encoded);
            System.out.println("Data plain : " + bi.toString(2) + " \n Nombre de bits : " + bi.toString(2).getBytes().length);

            EnDeCryption cryption = new EnDeCryption();

            switch (args[0]) {
                case "enc":
                    cipher = cryption.encryption(encoded, args[6]);
                    break;
                case "dec":
                    cipher = cryption.decryption(encoded, args[6]);
                    break;
            }

            System.out.println("main call result : " + Arrays.toString(cipher));

            //Affichage binaire
            BigInteger bi1 = new BigInteger(cipher);
            System.out.println("Data cypher : " + bi.toString(2) + " \n Nombre de bits : " + bi1.toString(2).getBytes().length);

            try (FileOutputStream fos = new FileOutputStream(args[4])) {
                fos.write(cipher);
            }
        } else {
            System.out.println("\n\nVeuillez utiliser ce pattern d'arguments : –enc|-dec –in <input file> -out <output file> -pass <password>");
        }
    }

    private static boolean isClear(String[] args) {
        String[] arguments = {"in", "out", "pass", "dec", "enc"};
        return (arguments[3].equals(args[0]) || arguments[4].equals(args[0]))
                && arguments[0].equals(args[1])
                && arguments[1].equals(args[3])
                && arguments[2].equals(args[5])
                && args.length == 7;
    }
}

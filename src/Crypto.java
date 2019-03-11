
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Crypto {

    public enum action {ENC, DEC}

    static action Action;
    static ArrayList<String> inputFiles = new ArrayList<String>();
    static HashMap<String, byte[]> encodedMap = new HashMap<String, byte[]>();
    static HashMap<String, byte[]> outputFileEnc = new HashMap<String, byte[]>();
    static String outputFilePath;
    static String password;

    public static void main(final String[] args) throws Exception {
        //Vérification des paramètres
        boolean isArgsValidate = validateArgs(args);
        if (isArgsValidate && checkInputFile(inputFiles)) {

            //Parcours tous les fichiers d'entree
            for (int i = 0; i < inputFiles.size(); i++) {
                File file = new File(inputFiles.get(i));
                String extension = getExtension(inputFiles.get(i));

                //Pour un zip
                if (extension.equals("zip")) {
                    unzip(file.getPath().toString());
                } else if (extension.equals("")) {

                } else {
                    byte[] encoded = Files.readAllBytes(Paths.get(inputFiles.get(i)));
                    encodedMap.put(inputFiles.get(i), encoded);
                }

            }

            //Realise le traitement
            doEnDeCryption();


            //Range les fichiers de sortie
            if (checkOutputFile(outputFilePath)) {
                if (getExtension(outputFilePath).equals("zip")) {
                    FileOutputStream fos = new FileOutputStream(outputFilePath);
                    ZipOutputStream zipOut = new ZipOutputStream(fos);
                    outputFileEnc.forEach((path, cipher) -> {
                        try {
                            InputStream input = new ByteArrayInputStream(cipher);
                            ZipEntry zipEntry = new ZipEntry(path);
                            zipOut.putNextEntry(zipEntry);

                            byte[] bytes = new byte[1024];
                            int length;
                            while ((length = input.read(bytes)) >= 0) {
                                zipOut.write(bytes, 0, length);
                            }
                            input.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    zipOut.close();
                    fos.close();
                } else if (getExtension(outputFilePath).equals("")) {
                    File dir = new File(outputFilePath);
                    if (!dir.exists()) dir.mkdirs();
                    outputFileEnc.forEach((path, cipher) -> {
                        try (FileOutputStream fos = new FileOutputStream(outputFilePath + File.separator + path)) {
                            System.out.println("Write to file : " + outputFilePath + File.separator + path);

                            try {
                                fos.write(cipher);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });

                } else {
                    try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                        System.out.println("Write to file : " + outputFilePath);
                        outputFileEnc.forEach((path, cipher) -> {
                            try {
                                fos.write(cipher);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                }
            }

        } else {
            System.out.println("\n\nVeuillez utiliser ce pattern d'arguments : –enc|-dec –in <input file> -out <output file> -pass <password>");
        }

    }

    private static void unzip(String zipFilePath) {
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();

                encodedMap.put(fileName, outputStream.toByteArray());
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void doEnDeCryption() {
        encodedMap.forEach((path, encoded) -> {
                    EnDeCryption cryption = new EnDeCryption(password);

                    byte[] cipher = {0};
                    switch (Action) {
                        case ENC:
                            String path_out;
                            if (encodedMap.size() == 1) {
                                path_out = outputFilePath;
                            } else {
                                path_out = path;
                            }
                            cipher = cryption.encryption(encoded, path_out);
                            break;
                        case DEC:
                            cipher = cryption.decryption(encoded, path);
                            break;
                    }
                    outputFileEnc.put(path, cipher);
                }
        );
    }


    private static boolean checkInputFile(ArrayList<String> inputFiles) {
        for (String inputFile : inputFiles) {
            File file = new File(inputFile);
            if (!file.isDirectory()) {
                if (!file.exists()) {
                    System.out.println("File : " + inputFile + " doesn't exist !");
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkOutputFile(String path) {
        File outputFile = new File(path);
        boolean exists = outputFile.exists();
        if (exists) {
            System.out.println("Output file exist, do you want overwrite the file : " + path + " ? Y/n");
            Scanner input = new Scanner(System.in);
            String answer = input.nextLine();
            if (answer.equals("") || answer.equals("y") || answer.equals("Y")) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static boolean validateArgs(String[] args) {
        if ((Arrays.asList(args).contains("-enc") || Arrays.asList(args).contains("-dec"))
                && Arrays.asList(args).contains("-pass")
                && Arrays.asList(args).contains("-in")
                && Arrays.asList(args).contains("-out")) {

            for (int i = 0; i < args.length; i++) {
                if (args[i].charAt(0) == '-') {
                    switch (args[i]) {
                        case "-enc":
                            Action = action.ENC;
                            break;
                        case "-dec":
                            Action = action.DEC;
                        case "-in":
                            int index = i + 1;
                            while (args[index].charAt(0) != '-') {
                                inputFiles.add(args[index]);
                                index = index + 1;
                            }
                            break;
                        case "-out":
                            outputFilePath = args[i + 1];
                            break;
                        case "-pass":
                            password = args[i + 1];

                    }
                }
            }
            if (inputFiles.size() == 0) {
                System.out.println("No input file in parrameter");
                return false;
            } else {
                String extension = getExtension(outputFilePath);
                if (inputFiles.size() > 1) {
                    if (!extension.equals("zip") && !extension.equals("") && Action == action.ENC) {
                        System.out.println("File's extension have to be 'zip' for multiple files!");
                        return false;
                    }
                }
            }

            return validatePassword(password);
        } else {
            return false;
        }
    }

    private static String getExtension(String path) {
        File file = new File(path);
        String fileName = file.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    private static boolean validatePassword(String password) {
        // 62^x = 2^128
        if (password.matches("[a-zA-Z0-9]+") && password.length() >= 22) {
            return true;
        } else {
            System.out.println("Your password is not valid");
            return false;
        }
    }

}

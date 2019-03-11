# Crypto AES128


## Encryption

- File to File : ``` -enc -in test.txt -out test-enc.txt -pass azertyuiopqsdfghjklmwxcvbn ```
- Multiple files to Zip : ``` -enc -in test.txt test2.txt test3.txt -out test-enc.zip -pass azertyuiopqsdfghjklmwxcvbn ```
- Multiple files to directory : ``` -enc -in test.txt test2.txt test3.txt -out test-enc -pass azertyuiopqsdfghjklmwxcvbn ```

## Decryption

- File to File : ``` -dec -in test-enc.txt -out test-dec.txt -pass azertyuiopqsdfghjklmwxcvbn ```
- File to Zip : ``` -dec -in test-enc.txt -out test-dec.zip -pass azertyuiopqsdfghjklmwxcvbn ```
- Zip to Zip : ``` -dec -in test-enc.zip -out test-dec.zip -pass azertyuiopqsdfghjklmwxcvbn ```
- Zip to directory : ``` -dec -in test-enc.zip -out test-dec -pass azertyuiopqsdfghjklmwxcvbn ```

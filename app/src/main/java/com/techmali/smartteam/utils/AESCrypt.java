package com.techmali.smartteam.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AESCrypt {

    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String ALGORITHM = "AES";
    private static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    /**
     * Instantiates a new Aes crypt.
     */
    public AESCrypt() {
    }

    /**
     * Generate key secret key spec.
     *
     * @param password the password
     * @return the secret key spec
     * @throws NoSuchAlgorithmException     the no such algorithm exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private static SecretKeySpec generateKey(final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = password.getBytes(CHARSET);
        digest.update(bytes, 0, bytes.length);

        return new SecretKeySpec(bytes, ALGORITHM);
    }

    /**
     * Encrypt string.
     *
     * @param msg    the msg
     * @param isPref the is pref
     * @return the string
     * @throws GeneralSecurityException the general security exception
     */
    public static String encrypt(String msg, boolean isPref) throws GeneralSecurityException {

        try {

            if (msg != null && msg.length() > 0) {

                String message = msg;
                if (!isPref) {
                    message = URLEncoder.encode(msg, CHARSET);
                }

                String passToSend = randomString(38);
                String password = passToSend.substring(0, 16) + passToSend.substring(22);
                final SecretKeySpec key = generateKey(password);

                Log.e("AESCrypt", "*******************************");
                Log.e("Without Encryption", message);
                Log.e("AESCrypt", "*******************************");
                Log.e("PassToSend", passToSend);
                Log.e("AESCrypt", "*******************************");
                Log.e("Password", password);

                byte[] cipherText = encrypt(key, ivBytes, message.getBytes(CHARSET));

                Log.e("AESCrypt", "*******************************");
                Log.e("With Encryption", Base64.encodeToString(cipherText, Base64.NO_WRAP));

                return stringKnitter(passToSend, Base64.encodeToString(cipherText, Base64.NO_WRAP));
            } else {
                return msg;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * Encrypt string.
     *
     * @param msg      the msg
     * @param password the password
     * @return the string
     * @throws GeneralSecurityException the general security exception
     */
    public static String encrypt(String msg, String password) throws GeneralSecurityException {

        try {
            if (msg != null && msg.length() > 0) {
                final SecretKeySpec key = generateKey(password);
                byte[] cipherText = encrypt(key, ivBytes, msg.getBytes(CHARSET));
                return Base64.encodeToString(cipherText, Base64.NO_WRAP);
            } else {
                return msg;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * Encrypt byte [ ].
     *
     * @param key     the key
     * @param iv      the iv
     * @param message the message
     * @return the byte [ ]
     * @throws GeneralSecurityException the general security exception
     */
    public static byte[] encrypt(final SecretKeySpec key, final byte[] iv, final byte[] message) throws GeneralSecurityException {
        final Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        return cipher.doFinal(message);
    }

    /**
     * Decrypt string.
     *
     * @param str the str
     * @return the string
     * @throws GeneralSecurityException the general security exception
     * @throws Exception                the exception
     */
    public static String decrypt(final String str) throws GeneralSecurityException, Exception {

        try {
            String msg;
            if (str != null && str.length() > 0) {
                msg = str;
            } else {
                return str;
            }
            if (msg.length() <= 50) {
                msg = encrypt(msg, true);
            }
            String key = msg.substring(0, 16) + msg.substring(22, 38);
            final SecretKeySpec password = generateKey(key);
            byte[] decodedCipherText = Base64.decode(digestPadding(msg), Base64.NO_WRAP);
            byte[] decryptedBytes = decrypt(password, ivBytes, decodedCipherText);
            String message = new String(decryptedBytes, CHARSET);
            Log.e("AESCrypt", "*******************************");
            Log.e("With Encryption", msg);
            Log.e("AESCrypt", "*******************************");
            Log.e("Key", msg.substring(0, 38));
            Log.e("AESCrypt", "*******************************");
            Log.e("Password", key);
            Log.e("AESCrypt", "*******************************");
            Log.e("Without Encryption", message);
            return message;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GeneralSecurityException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * Decrypt string.
     *
     * @param str      the str
     * @param password the password
     * @return the string
     * @throws GeneralSecurityException the general security exception
     */
    public static String decrypt(final String str, String password) throws GeneralSecurityException {

        try {

            String msg;
            if (str != null && str.length() > 0) {
                msg = str;
            } else {
                return str;
            }

            final SecretKeySpec key = generateKey(password);

            byte[] decodedCipherText = Base64.decode(msg, Base64.NO_WRAP);

            byte[] decryptedBytes = decrypt(key, ivBytes, decodedCipherText);

            return new String(decryptedBytes, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * String knitter string.
     *
     * @param password the password
     * @param msg      the msg
     * @return the string
     */
    private static String stringKnitter(String password, String msg) {
        return (password + msg);
    }

    /**
     * Digest padding string.
     *
     * @param msg the msg
     * @return the string
     */
    private static String digestPadding(final String msg) {

        return msg.substring(38);
    }

    /**
     * Generate passowrd string.
     *
     * @param msg    the msg
     * @param length the length
     * @return the string
     */
    private static String generatePassowrd(String msg, int length) {

        return msg.substring(0, length);
    }

    /**
     * Decrypt byte [ ].
     *
     * @param key               the key
     * @param iv                the iv
     * @param decodedCipherText the decoded cipher text
     * @return the byte [ ]
     */
    public static byte[] decrypt(final SecretKeySpec key, final byte[] iv, final byte[] decodedCipherText) {

        byte[] decryptedBytes;
        try {
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            decryptedBytes = cipher.doFinal(decodedCipherText);

        } catch (Exception e) {
            e.printStackTrace();
            String s = "null";
            decryptedBytes = s.getBytes();
        }
        return decryptedBytes;
    }

    /**
     * Random string string.
     *
     * @param length the length
     * @return the string
     */
    private static String randomString(int length) {

        char[] characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        // char[] characterSet = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ0123456789".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[length * length];
        for (int i = 0; i < result.length; i++) {
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        String str = (new String(result));
        try {
            if (str.length() > length) {
                str = generatePassowrd(str, length);
            } else {
                str += str;
                str = generatePassowrd(str, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}

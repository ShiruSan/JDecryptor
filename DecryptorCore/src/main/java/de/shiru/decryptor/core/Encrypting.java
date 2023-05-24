package de.shiru.decryptor.core;

import org.apache.commons.codec.digest.DigestUtils;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Encrypting {
    private static final List<Character> lowerCaseABC = toList("abcdefghijklmnopqrstuvwxyz".toCharArray());
    private static final List<Character> upperCaseABC = toList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());
    private static final List<Character> ABC = toList("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.!?-_ {}();'=+".toCharArray());

    private static List<Character> toList(char[] chars) {
        var characters = new ArrayList<Character>();
        for(var c : chars) {
            characters.add(c);
        }
        return characters;
    }

    private static List<Character> generateInternalEncryptionList(String seed) {
        var list = new ArrayList<Character>(ABC.size());
        list.addAll(ABC);
        var r = new Random(seed.hashCode());
        var shuffle = r.nextInt(Integer.MAX_VALUE);
        for(int i = 0; i < list.size(); i++) {
            char a = list.get(i);
            char b;
            if(i + shuffle >= (list.size() - 1)) b = list.get((i + shuffle) % (list.size() - 1));
            else b = list.get(i + shuffle);
            var index = list.indexOf(b);
            list.set(i, b);
            list.set(index, a);
            shuffle = r.nextInt(Integer.MAX_VALUE);
        }
        return list;
    }

    public static String encryptInternal(String message, String seed) {
        var builder = new StringBuilder();
        var encryptionList = generateInternalEncryptionList(seed);
        for(var c : message.toCharArray()) {
            if(!encryptionList.contains(c)) {builder.append(c); continue;}
            builder.append(encryptionList.get(ABC.indexOf(c)));
        }
        return builder.toString();
    }

    public static String decryptInternal(String message, String seed) {
        var builder = new StringBuilder();
        var encryptionList = generateInternalEncryptionList(seed);
        for(var c : message.toCharArray()) {
            if(!encryptionList.contains(c)) {builder.append(c); continue;}
            builder.append(ABC.get(encryptionList.indexOf(c)));
        }
        return builder.toString();
    }

    public static String encryptCaesar(String message, int s) {
        var builder = new StringBuilder();
        if(s > lowerCaseABC.size()) s %= lowerCaseABC.size();
        var messageArray = message.toCharArray();
        for(var c : messageArray) {
            if(!Character.isAlphabetic(c)) {builder.append(c); continue;}
            if(Character.isLowerCase(c)) {
                var index = lowerCaseABC.indexOf(c) + s;
                if(index >= lowerCaseABC.size()) index %= lowerCaseABC.size();
                builder.append(lowerCaseABC.get(index));
            } else {
                var index = upperCaseABC.indexOf(c) + s;
                if(index >= upperCaseABC.size()) index %= upperCaseABC.size();
                builder.append(upperCaseABC.get(index));
            }
        }
        return builder.toString();
    }

    public static String decryptCaesar(String message, int s) {
        var builder = new StringBuilder();
        if(s > lowerCaseABC.size()) s %= lowerCaseABC.size();
        var messageArray = message.toCharArray();
        for(var c : messageArray) {
            if(!Character.isAlphabetic(c)) {builder.append(c); continue;}
            if(Character.isLowerCase(c)) {
                var index = lowerCaseABC.indexOf(c) - s;
                if(index < 0) index += lowerCaseABC.size();
                builder.append(lowerCaseABC.get(index));
            } else {
                var index = upperCaseABC.indexOf(c) - s;
                if(index < 0) index += upperCaseABC.size();
                builder.append(upperCaseABC.get(index));
            }
        }
        return builder.toString();
    }

    public static String encryptAES(String message, String password) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException {
        var hash = DigestUtils.sha256(password);
        var iv = Arrays.copyOf(DigestUtils.sha1(hash), 16);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(hash, "AES"), new IvParameterSpec(iv));
        try {
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (BadPaddingException e) {
            return null;
        }
    }

    public static String decryptAES(String encrypted, String password) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException {
        byte[] hash = DigestUtils.sha256(password);
        var iv = Arrays.copyOf(DigestUtils.sha1(hash), 16);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(hash, "AES"), new IvParameterSpec(iv));
        try {
            return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)));
        } catch (BadPaddingException e) {
            return null;
        }
    }

    public enum Type {
        Internal,
        Caesar,
        AES;

        public static List<String> asStringList() {
            var list = new ArrayList<String>();
            for(var value : Type.values()) {
                list.add(value.name());
            }
            return list;
        }

        public static Type getByName(String name) {
            for(var value : Type.values()) {
                if(value.name().equals(name)) return value;
            }
            return null;
        }

    }

}

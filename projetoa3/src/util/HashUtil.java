package util;

import java.security.MessageDigest;
public class HashUtil{
    public static String hash(String senha){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hash) sb.append(String.format ("%02x",b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar hash", e);
        }
    }
}

package Encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
	public  String sign(String str, String type){
        String s=Encrypt(str,type);
         return s;
     }
     public  String Encrypt(String strSrc, String encName) {
         MessageDigest md = null;
         String strDes = null;
         byte[] bt = strSrc.getBytes();
         try {
             md = MessageDigest.getInstance(encName);
             md.update(bt);
             strDes = bytes2Hex(md.digest()); // to HexString
         } catch (NoSuchAlgorithmException e) {
             System.out.println("Ç©ÃûÊ§°Ü£¡");
             return null;
         }
         return strDes;
     }
     public  String bytes2Hex(byte[] bts) {
         String des = "";
         String tmp = null;
         for (int i = 0; i < bts.length; i++) {
             tmp = (Integer.toHexString(bts[i] & 0xFF));
             if (tmp.length() == 1) {
                 des += "0";
             }
             des += tmp;
         }
         return des;
     }
}


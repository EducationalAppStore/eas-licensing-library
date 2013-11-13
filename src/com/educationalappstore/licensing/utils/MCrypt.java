package com.educationalappstore.licensing.utils;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class to provide encrypt and decrypt string methods using AES 128 algorithm
 * Source code is from: http://www.androidsnippets.com/encrypt-decrypt-between-android-and-php
 * 
 * How to use:
 * 		mcrypt = new MCrypt("iv", "secretkey");
 * 		//Encrypt 
 * 		String encrypted = MCrypt.bytesToHex( mcrypt.encrypt("Text to Encrypt") );
 * 		//Decrypt 
 * 		String decrypted = new String( mcrypt.decrypt( encrypted ) );
 * 
 * 
 * @author Hoang
 *
 */
public class MCrypt {
	// identifier vector - must match with the one on server
	private String iv = "fedcba9876543210";//Dummy iv (CHANGE IT!)
    private IvParameterSpec ivspec;
    private Cipher cipher;
    
    // private key for encrypt and decrypt string - must match with the one on server
    private String SecretKey = "0123456789abcdef";//Dummy secretKey (CHANGE IT!)
    private SecretKeySpec keyspec;
    
    /**
     * 
     * @param iv - interface vector
     * @param secretKey - key used for encrypt and decrypt
     */
    public MCrypt(String iv, String secretKey)
    {
    	this.iv = iv;
        ivspec = new IvParameterSpec(iv.getBytes());
        this.SecretKey = secretKey;
        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");
        
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public byte[] encrypt(String text) throws Exception
    {
        if(text == null || text.length() == 0)
            throw new Exception("Empty string");
        
        byte[] encrypted = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {                       
            throw new Exception("[encrypt] " + e.getMessage());
        }
        
        return encrypted;
    }
    
    public byte[] decrypt(String code) throws Exception
    {
        if(code == null || code.length() == 0)
            throw new Exception("Empty string");
        
        byte[] decrypted = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] data)
    {
        if (data==null)
        {
                return null;
        }
        
        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16)
                str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i]&0xFF);
        }
        return str;
    }
            
    public static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }

    private static String padString(String source)
    {
      char paddingChar = ' ';
      int size = 16;
      int x = source.length() % size;
      int padLength = size - x;

      for (int i = 0; i < padLength; i++)
      {
          source += paddingChar;
      }

      return source;
    }
}

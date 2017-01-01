package com.ruoxu.cipher.sample;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 * <h1>对称加密算法3重DES   DESede</h1>
 * 注意: 此代码和DES算法几乎没有区别
 * 区别主要为 1.秘钥长度 2.算法名称 3.getKey中的秘钥材料类
 * 
 */

public class DESedeCoder {
	private static String KEY_ALGORITHM= "DESede";// 秘钥Key的生成算法,JDK仅支持56位秘钥
	/**
	 * CIPHER_ALGORITHM 如果简写为 "DES",则按默认工作模式和填充方式 
	 * JDK支持的工作模式和填充方式有:
	 * 
	 * 工作模式: ECB,CBC,PCBC,CTR,CTS,CFB,CFB8~CFB128,OFB,OFB8~OFB128
	 * 填充方式: NoPadding,PKCS5Padding,ISO10126Padding
	 * 注意，上面的也不能随意搭配
	 */
	private static String CIPHER_ALGORITHM= "DESede/ECB/PKCS5Padding";// 加密和解密算法/工作模式/填充方式 .
	
	public static void main(String[] args) {
		String str = "hello world";
		
		byte[] secretKey = initKey();// 每次生成的Key都不一样，注意加密和解密需要使用一样的secretKey,注意妥善保存此秘钥(Base64)

		//加密 
		byte[] encryptData = encrypt(str.getBytes(), secretKey);
		
		System.out.println("加密后的内容:"+Base64.encodeBase64String(encryptData));
		
		//解密 
		byte[] decryptData = decrypt(encryptData, secretKey);
		
		System.out.println("解密后的内容:"+new String(decryptData));
		
		
		
		
	}
	
	
	
	//生成秘钥
	public static byte[] initKey(){
		try {

			KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
			generator.init(168);//JDK仅支持112和 168位
//			generator.init(new SecureRandom());//以默认长度初始化,因为JDK仅仅支持112和168位，故同上
			
			SecretKey secretKey = generator.generateKey();
			return secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	//把 秘钥 byte字节转为 Key
	public static Key getKey(byte[] key){
			SecretKey secretKey = null;
			try {
				//实例化DES秘钥材料
				DESedeKeySpec dks = new DESedeKeySpec(key);
				//实例化秘钥工厂
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
				//生成秘钥
				secretKey = keyFactory.generateSecret(dks);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return secretKey;
		
	}
	
	
	
	//加密 
	public static byte[] encrypt(byte[] data,byte[]key){
		byte [] encryptData = null;
		try {
			Key secretKey = getKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			//初始化，设置为解密模式
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//执行操作
			encryptData = cipher.doFinal(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encryptData;
	}
	
	
	//解密
	public static byte[] decrypt(byte[]data,byte[]key){
		byte [] decryptData = null;
		try {
			Key secretKey = getKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decryptData = cipher.doFinal(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return decryptData;
	}
	
	
}


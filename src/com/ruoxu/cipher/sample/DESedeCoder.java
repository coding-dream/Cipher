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
 * <h1>�ԳƼ����㷨3��DES   DESede</h1>
 * ע��: �˴����DES�㷨����û������
 * ������ҪΪ 1.��Կ���� 2.�㷨���� 3.getKey�е���Կ������
 * 
 */

public class DESedeCoder {
	private static String KEY_ALGORITHM= "DESede";// ��ԿKey�������㷨,JDK��֧��56λ��Կ
	/**
	 * CIPHER_ALGORITHM �����дΪ "DES",��Ĭ�Ϲ���ģʽ����䷽ʽ 
	 * JDK֧�ֵĹ���ģʽ����䷽ʽ��:
	 * 
	 * ����ģʽ: ECB,CBC,PCBC,CTR,CTS,CFB,CFB8~CFB128,OFB,OFB8~OFB128
	 * ��䷽ʽ: NoPadding,PKCS5Padding,ISO10126Padding
	 * ע�⣬�����Ҳ�����������
	 */
	private static String CIPHER_ALGORITHM= "DESede/ECB/PKCS5Padding";// ���ܺͽ����㷨/����ģʽ/��䷽ʽ .
	
	public static void main(String[] args) {
		String str = "hello world";
		
		byte[] secretKey = initKey();// ÿ�����ɵ�Key����һ����ע����ܺͽ�����Ҫʹ��һ����secretKey,ע�����Ʊ������Կ(Base64)

		//���� 
		byte[] encryptData = encrypt(str.getBytes(), secretKey);
		
		System.out.println("���ܺ������:"+Base64.encodeBase64String(encryptData));
		
		//���� 
		byte[] decryptData = decrypt(encryptData, secretKey);
		
		System.out.println("���ܺ������:"+new String(decryptData));
		
		
		
		
	}
	
	
	
	//������Կ
	public static byte[] initKey(){
		try {

			KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
			generator.init(168);//JDK��֧��112�� 168λ
//			generator.init(new SecureRandom());//��Ĭ�ϳ��ȳ�ʼ��,��ΪJDK����֧��112��168λ����ͬ��
			
			SecretKey secretKey = generator.generateKey();
			return secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	//�� ��Կ byte�ֽ�תΪ Key
	public static Key getKey(byte[] key){
			SecretKey secretKey = null;
			try {
				//ʵ����DES��Կ����
				DESedeKeySpec dks = new DESedeKeySpec(key);
				//ʵ������Կ����
				SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
				//������Կ
				secretKey = keyFactory.generateSecret(dks);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return secretKey;
		
	}
	
	
	
	//���� 
	public static byte[] encrypt(byte[] data,byte[]key){
		byte [] encryptData = null;
		try {
			Key secretKey = getKey(key);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			//��ʼ��������Ϊ����ģʽ
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//ִ�в���
			encryptData = cipher.doFinal(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encryptData;
	}
	
	
	//����
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


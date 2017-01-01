package com.ruoxu.cipher.sample;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 * �ǶԳƼ����㷨RSA 
 * �ǶԳƼ��ܵ�Ч�ʽϵͣ��ʷǶԳƼ����㷨������������������  ��:�ԳƼ����㷨����Կ�� �������ݵļ���ʹ�� �ԳƼ����㷨
 * ����Կ��˽Կ�ķ������˻�ͻ��˱����ʱ��һ��ʹ��Base64�������Hex���룬Hex����ĳ���ҪС��Base64(�Ƽ�Hex����) 
 * 
 */
public class RSACoder {
	
	//��Կ�㷨 -->��Կ���ɵ��㷨
	private static final String KEY_ALGORITHM = "RSA";
	
	private static final int KEY_SIZE = 512; //RSA��Կ���ȣ�Ĭ��1024λ����Χ512~65536֮�䣬������64�ı���
	
	public static void main(String[] args) {
		
		String str = "hello world";
		
		//��ʼ����Կ 
		KeyPair keyPair = initKey();

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();
		
		System.out.println("��Կ:"+Base64.encodeBase64String(publicKey.getEncoded()));
		System.out.println("˽Կ:"+Base64.encodeBase64String(privateKey.getEncoded()));
		
		// =============>��Կ���ܣ�˽Կ����<=============
		//���� 
		byte[] encryptData = handleByPublic(str.getBytes(), publicKey.getEncoded(), true);
		//���� 
		byte[] decryptData = handleByPrivate(encryptData, privateKey.getEncoded(), false);
		
		System.out.println(new String(decryptData));
		
		
		// =============>˽Կ���ܣ���Կ����<=============

		//����
		byte[] encryptDat = handleByPrivate(str.getBytes(), privateKey.getEncoded(), true);
		//����
		byte[] decryptDat = handleByPublic(encryptDat, publicKey.getEncoded(), false);
		
		System.out.println(new String(decryptDat));
		
		
	}
	

	public static KeyPair initKey(){
		
		try {
			//ʵ������Կ�� ������
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
		
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			
			return keyPair;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	
	
	//ʹ�ù�Կ���ܻ���� 
	public static byte[] handleByPublic(byte[] data,byte[]key,boolean isEncrypt){
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//���ɹ�Կ 
			PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
			//�����ݼ��ܻ���� 
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			if(isEncrypt){
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);				
			}else{
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
			}

			return cipher.doFinal(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	//ʹ��˽Կ���ܻ���� 
	public static byte[] handleByPrivate(byte[] data,byte[]key,boolean isEncrypt){
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			//����˽Կ
			PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
			//�����ݼ��ܻ���� 
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			if(isEncrypt){
				cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			}else{
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
			}
			
			return cipher.doFinal(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	
	
	
}

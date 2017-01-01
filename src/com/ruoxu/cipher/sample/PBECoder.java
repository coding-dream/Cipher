package com.ruoxu.cipher.sample;

import java.security.Key;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 *       <h1>���ڿ���ļ����㷨PBE</h1> ���㷨��Կ�� ������� ����---->����Կ�� ���ص��� ��ϢժҪ�㷨 �� �ԳƼ����㷨�Ľ��
 *       ,����Կ��ȶԳƼ��� ���׼��� JDK֧�ֵ��㷨�� :
 * 
 *       PBEWithMD5AndDES ��Կ���� 56 ����ģʽ CBC ��䷽ʽ PKCS5Padding
 *       PBEWithMD5AndTripleDES ��Կ���� 112,168 ����ģʽ CBC ��䷽ʽ PKCS5Padding
 *       PBEWithSHA1AndDESede ��Կ���� 112,168 ����ģʽ CBC ��䷽ʽ PKCS5Padding
 *       PBEWithSHA1AndRC2_40 ��Կ���� 40��1204(8�ı���) ����ģʽ CBC ��䷽ʽ PKCS5Padding
 */

public class PBECoder {

	private static final String ALGORITHM = "PBEWITHMD5andDES";

	private static int iterationCount = 100;// ��������

	public static void main(String[] args) {

		String str = "hello world";
		
		String password = "abcd1234";
		byte[] salt  = initSalt();
		System.out.println(Base64.encodeBase64String(salt));
		
		
		//���� 
		byte[] encriptDatas = encript(str.getBytes(), password, salt);
		
		//����
		byte[] decrptDatas = decrypt(encriptDatas, password, salt);
		System.out.println(new String(decrptDatas));
		
	}

	//�εĳ�����8���ֽڼ��ɣ����ɹ�������Զ��� 
	public static byte[] initSalt() {
		
//		SecureRandom random = new SecureRandom();
		// ������
//        byte [] salt = random.getSeed(8);// ������8�ֽڵ�salt��1��Ӣ���ַ�=1���ֽ�
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		return uuid.getBytes();
		
		
	}

	public static Key getKey(String password) {
		try {
			// ��Կ����
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			// ��Կ����
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// ������Կ
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			return secretKey;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// ����
	public static byte[] encript(byte[] data, String password, byte[] salt) {
		try {
			Key key = getKey(password);
			// ʵ����PBE��������
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	//���� 
	public static byte[] decrypt(byte[] data,String password,byte[]salt){
		try {
			Key key = getKey(password);
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	

}

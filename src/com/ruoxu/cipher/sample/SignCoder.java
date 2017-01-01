package com.ruoxu.cipher.sample;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 *       <h1>RSAʵ������ǩ��</h1> RSA ����ǩ���㷨��ΪMDϵ�к�SHAϵ�С� MDϵ�� ��Ҫ���� MD5withRSA
 *       MD2withRSA 2���� SHAϵ�� ��Ҫ����SHA1withRSA
 *       SHA224withRSA��SHA256withRSA��SHA384withRSA��SHA512withRSA ��5������ǩ���㷨
 *       JDK6���ṩ�� MD2withRSA,MD5withRSA��SHA1withRSA ��3������ǩ���㷨
 * 
 *       һ���ǳ��ؼ����ص���: RSA����ǩ���㷨�� ǩ��ֵ����Կ���� ���
 * 
 */

public class SignCoder {
	private static final String KEY_ALGORITHM = "RSA";

	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final int KEY_SIZE = 512;// RSA
											// ��Կ���ȣ�Ĭ��1024λ����Χ512~65536֮�䣬������64�ı���

	public static void main(String[] args) {
		String str = "hello world";

		KeyPair keyPair = initKey();
		
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		//ǩ��
		byte[] sign = sign(str.getBytes(), privateKey.getEncoded());

		//��֤
		boolean flag = verify(str.getBytes(), publicKey.getEncoded(), sign);

		System.out.println("��ǩ:"+flag);

		System.out.println(sign.length);//��ӡ64�ֽ� ---> 64*8 = 512 bit ��ǩ���������
		

	}

	public static KeyPair initKey() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGenerator.initialize(KEY_SIZE);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			return keyPair;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ˽Կ����ǩ��
	public static byte[] sign(byte[] data, byte[] privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

			PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);// ��MD5withRSA�㷨��
																				// ʵ����Signature
			// ��ʼ��Signature(������˽Կ)
			signature.initSign(priKey);
			signature.update(data);
			return signature.sign();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// ��Կ���� У��
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) {

		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// ���ɹ�Կ
			PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);

			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			// ��ʼ��Signature(�����ǹ�Կ)
			signature.initVerify(pubKey);
			signature.update(data);

			boolean flag = signature.verify(sign);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}

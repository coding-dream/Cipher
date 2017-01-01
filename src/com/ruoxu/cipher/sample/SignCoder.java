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
 *       <h1>RSA实现数字签名</h1> RSA 数字签名算法分为MD系列和SHA系列。 MD系列 主要包括 MD5withRSA
 *       MD2withRSA 2大类 SHA系列 主要包括SHA1withRSA
 *       SHA224withRSA，SHA256withRSA，SHA384withRSA，SHA512withRSA 共5中数字签名算法
 *       JDK6仅提供了 MD2withRSA,MD5withRSA，SHA1withRSA 共3中数字签名算法
 * 
 *       一个非常关键的特点是: RSA数字签名算法的 签名值和密钥长度 相等
 * 
 */

public class SignCoder {
	private static final String KEY_ALGORITHM = "RSA";

	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final int KEY_SIZE = 512;// RSA
											// 密钥长度，默认1024位，范围512~65536之间，必须是64的倍数

	public static void main(String[] args) {
		String str = "hello world";

		KeyPair keyPair = initKey();
		
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		//签名
		byte[] sign = sign(str.getBytes(), privateKey.getEncoded());

		//验证
		boolean flag = verify(str.getBytes(), publicKey.getEncoded(), sign);

		System.out.println("验签:"+flag);

		System.out.println(sign.length);//打印64字节 ---> 64*8 = 512 bit 和签名长度相等
		

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

	// 私钥进行签名
	public static byte[] sign(byte[] data, byte[] privateKey) {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

			PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);// 以MD5withRSA算法来
																				// 实例化Signature
			// 初始化Signature(参数是私钥)
			signature.initSign(priKey);
			signature.update(data);
			return signature.sign();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 公钥进行 校检
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) {

		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			// 生成公钥
			PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);

			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			// 初始化Signature(参数是公钥)
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

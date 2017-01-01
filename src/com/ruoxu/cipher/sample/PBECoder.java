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
 *       <h1>基于口令的加密算法PBE</h1> 该算法秘钥由 口令和盐 构成---->两把钥匙 其特点是 消息摘要算法 和 对称加密算法的结合
 *       ,且秘钥相比对称加密 容易记忆 JDK支持的算法有 :
 * 
 *       PBEWithMD5AndDES 秘钥长度 56 工作模式 CBC 填充方式 PKCS5Padding
 *       PBEWithMD5AndTripleDES 秘钥长度 112,168 工作模式 CBC 填充方式 PKCS5Padding
 *       PBEWithSHA1AndDESede 秘钥长度 112,168 工作模式 CBC 填充方式 PKCS5Padding
 *       PBEWithSHA1AndRC2_40 秘钥长度 40至1204(8的倍数) 工作模式 CBC 填充方式 PKCS5Padding
 */

public class PBECoder {

	private static final String ALGORITHM = "PBEWITHMD5andDES";

	private static int iterationCount = 100;// 迭代次数

	public static void main(String[] args) {

		String str = "hello world";
		
		String password = "abcd1234";
		byte[] salt  = initSalt();
		System.out.println(Base64.encodeBase64String(salt));
		
		
		//加密 
		byte[] encriptDatas = encript(str.getBytes(), password, salt);
		
		//解密
		byte[] decrptDatas = decrypt(encriptDatas, password, salt);
		System.out.println(new String(decrptDatas));
		
	}

	//盐的长度是8个字节即可，生成规则可以自定义 
	public static byte[] initSalt() {
		
//		SecureRandom random = new SecureRandom();
		// 产生盐
//        byte [] salt = random.getSeed(8);// 必须是8字节的salt，1个英文字符=1个字节
		String uuid = UUID.randomUUID().toString().substring(0, 8);
		return uuid.getBytes();
		
		
	}

	public static Key getKey(String password) {
		try {
			// 秘钥材料
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			// 秘钥工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
			// 生成秘钥
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			return secretKey;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	// 加密
	public static byte[] encript(byte[] data, String password, byte[] salt) {
		try {
			Key key = getKey(password);
			// 实例化PBE参数材料
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	//解密 
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

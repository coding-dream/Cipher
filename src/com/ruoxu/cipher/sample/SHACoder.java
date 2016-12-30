package com.ruoxu.cipher.sample;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author wangli
 * @blog https://wangli0.github.io
 * <h1>SHA消息摘要算法</h1>
 */

public class SHACoder {

	public static void main(String[] args) {
		
		String str = "hello world";
		
		System.out.println(HexCoder.encodeHexString(encodeSHA((str.getBytes()))));
		System.out.println(HexCoder.encodeHexString(encodeSHA256((str.getBytes()))));
		System.out.println(HexCoder.encodeHexString(encodeSHA384((str.getBytes()))));
		System.out.println(HexCoder.encodeHexString(encodeSHA512((str.getBytes()))));
		
	}

	public static byte[] encodeSHA(byte[] data) {
		//JDK提供支持的算法有 SHA1(简写为SHA,摘要长度160), SHA-256(摘要长度256),SHA-384(摘要长度384),SHA-512(摘要长度512)
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			return digest.digest(data);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static byte[] encodeSHA256(byte[] data) {
		//JDK提供支持的算法有 SHA1(简写为SHA,摘要长度160), SHA-256(摘要长度256),SHA-384(摘要长度384),SHA-512(摘要长度512)
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(data);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static byte[] encodeSHA384(byte[] data) {
		//JDK提供支持的算法有 SHA1(简写为SHA,摘要长度160), SHA-256(摘要长度256),SHA-384(摘要长度384),SHA-512(摘要长度512)
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-384");
			return digest.digest(data);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static byte[] encodeSHA512(byte[] data) {
		//JDK提供支持的算法有 SHA1(简写为SHA,摘要长度160), SHA-256(摘要长度256),SHA-384(摘要长度384),SHA-512(摘要长度512)
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			return digest.digest(data);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}


}

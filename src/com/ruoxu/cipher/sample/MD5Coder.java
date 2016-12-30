package com.ruoxu.cipher.sample;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 * <h1>MD5消息摘要算法</h1>
 */

public class MD5Coder {

	public static void main(String[] args) {

		String str = "hello world";
		//实现方式1 : 得到16进制的md5字符串
		String md5HexStr1 = DigestUtils.md5Hex(str);
		System.out.println(md5HexStr1);

		//实现方式2
		String md5HexStr2 = encodeMD5(str, true);
		System.out.println(md5HexStr2);
		
		
	}

	public static String encodeMD5(String str,boolean toLowerCase) {
		//JDK 提供的实现方式
		try {
			byte[] datas = str.getBytes();
			MessageDigest digest = MessageDigest.getInstance("MD5");

			digest.update(datas);
			byte[] md5Datas = digest.digest();
			//或者直接  digest.digest(datas);
			
			char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		    char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
			
		    char[] toDigits = toLowerCase?DIGITS_LOWER:DIGITS_UPPER;
		    
			//转换为十六进制 
		        int length = md5Datas.length;
		        char[] out = new char[length << 1];
		        // two characters form the hex value.
		        for (int i = 0, j = 0; i < length; i++) {
		            out[j++] = toDigits[(0xF0 & md5Datas[i]) >>> 4];
		            out[j++] = toDigits[0x0F & md5Datas[i]];
		        }

		       return new String(out);
		        
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

}




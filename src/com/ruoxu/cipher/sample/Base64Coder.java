package com.ruoxu.cipher.sample;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 * <h1>电子邮件传输算法</h1>
 * 
 * RFC 2045 标准 
 * 电子邮件中，每行为76个字符，每行末尾需添加一个回车换行符，不论每行是否够76个字符，都要添加一个回车换行符，但实际应用中
 * 往往根据实际需要忽略这一要求。 解码的时候\r\n不会被解析，故\r\n加不加都无所谓
 * 该算法可以使用Apache提供的工具类很方便的实现
 */

public class Base64Coder {

	public static void main(String[] args) {
		
		String str = "hello world";
			
		//编码
		String base64Str = Base64.encodeBase64String(str.getBytes());
		System.out.println(base64Str);
		
		//解码
		byte[] decodeStr = Base64.decodeBase64(base64Str);
		System.out.println(new String(decodeStr));
		
		
		//============= RFC 2045标准
		
		//编码
		byte[] base64Str2 = Base64.encodeBase64(str.getBytes(), true);
		System.out.println(new String(base64Str2));
		
		//解码-->根据结果发现明显有一个 \r\n
		byte [] decodeStr2 = Base64.decodeBase64(base64Str2);
		System.out.println(new String(decodeStr2));
		
		
		
		// Url Base64 的算法实现 
		
		/**
		 * Url Base64遵循 RFC 4648 (但目前未形成标准)
		 * 因为url的要求，原本的Base64中的某些字符不能在url中出现如 '+','/'
		 * Apache  Commons Codec 放弃了填充符，使用不定长Url Base64
		 */
		//编码
		byte [] base64Url = Base64.encodeBase64URLSafe(str.getBytes());
		System.out.println(new String(base64Url));
		
		//解码
		byte [] decodebase64Url = Base64.decodeBase64(base64Url);
		System.out.println(new String(decodebase64Url));
		
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
}

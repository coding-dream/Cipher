package com.ruoxu.cipher.sample;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 * <h1>�����ʼ������㷨</h1>
 * 
 * RFC 2045 ��׼ 
 * �����ʼ��У�ÿ��Ϊ76���ַ���ÿ��ĩβ�����һ���س����з�������ÿ���Ƿ�76���ַ�����Ҫ���һ���س����з�����ʵ��Ӧ����
 * ��������ʵ����Ҫ������һҪ�� �����ʱ��\r\n���ᱻ��������\r\n�Ӳ��Ӷ�����ν
 * ���㷨����ʹ��Apache�ṩ�Ĺ�����ܷ����ʵ��
 */

public class Base64Coder {

	public static void main(String[] args) {
		
		String str = "hello world";
			
		//����
		String base64Str = Base64.encodeBase64String(str.getBytes());
		System.out.println(base64Str);
		
		//����
		byte[] decodeStr = Base64.decodeBase64(base64Str);
		System.out.println(new String(decodeStr));
		
		
		//============= RFC 2045��׼
		
		//����
		byte[] base64Str2 = Base64.encodeBase64(str.getBytes(), true);
		System.out.println(new String(base64Str2));
		
		//����-->���ݽ������������һ�� \r\n
		byte [] decodeStr2 = Base64.decodeBase64(base64Str2);
		System.out.println(new String(decodeStr2));
		
		
		
		// Url Base64 ���㷨ʵ�� 
		
		/**
		 * Url Base64��ѭ RFC 4648 (��Ŀǰδ�γɱ�׼)
		 * ��Ϊurl��Ҫ��ԭ����Base64�е�ĳЩ�ַ�������url�г����� '+','/'
		 * Apache  Commons Codec ������������ʹ�ò�����Url Base64
		 */
		//����
		byte [] base64Url = Base64.encodeBase64URLSafe(str.getBytes());
		System.out.println(new String(base64Url));
		
		//����
		byte [] decodebase64Url = Base64.decodeBase64(base64Url);
		System.out.println(new String(decodebase64Url));
		
	}
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
}

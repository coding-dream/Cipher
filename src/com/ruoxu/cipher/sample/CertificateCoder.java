package com.ruoxu.cipher.sample;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * @author wangli
 * @blog https://wangli0.github.io
 * <h1>数字证书的使用</h1>
 * 数字证书:集合多种密码学算法，自身带有公钥，同时带有数字签名，可鉴别消息来源，且自身带有消息摘要，可验证证书完整性。
 * 
 * 
 * 
 * java 工具KeyTool 
 * <h1>构建密钥库</h1>
 * keytool -genkeypair -keyalg RSA -keysize 2048 -sigalg SHA1withRSA -validity 36000 -alias www.ruulai.com -keystore ruulai.jks
 *
 * -genkeypair 表示生成密钥
 * -keyalg 指定密钥算法
 * -keysize 指定密钥长度，默认1024位，这里是2048 位
 * -sigalg 指定数字签名算法
 * -validity 证书有效期 36000天
 * -alias 指定别名 
 * -keystore 指定密钥库存储位置
 * 
 * <h1>导出数字证书</h1> 
 * keytool -exportcert -alias www.ruulai.com -keystore ruulai.keystore -file ruulai.cer -rfc
 * -exportcert 表示证书导出操纵
 * -keystore 指定密钥库文件
 * -file 证书导出路径
 * -rfc 指定以base64格式输出 
 * 
 * 
 * 用法:
 * .keystore文件是密钥库文件，里面包含了公钥和私钥 
 * .cer文件(数字证书) 发放给小伙伴，含有签名，公钥，消息摘要信息
 * 
 * Demo 案例说明:
 * 假如keystore是服务器所持有(Android 工具签名Apk时候也类似)
 * keystore既可以获取证书(证书中含有公钥)，又可以获取私钥
 * 
 * 
 * 证书发放给客户端(且证书本身含有sign，所以一般无需验证了)。
 * 客户端验证服务器的签名方法( 服务器拥有私钥keystore，私钥签名 --->客户端 公钥验证(证书) )
 * 
 */

public class CertificateCoder {

	private static final String CERT_TYPE="x.509";

	public static void main(String[] args) throws Exception {
		
		String str = "hello world";
		PrivateKey privateKey = getPrivateKeyByKeyStore("src/ruulai.keystore","xiaoming", "www.ruulai.com" );//服务器
		PublicKey publicKey = getPublicKeyByCer("src/ruulai.cer"); //客户端
		
		System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));
		System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));
		
		// =======>公钥加密，私钥解密 <=======
		//加密 
		byte[] encryptData = handleByPublic(str.getBytes(), publicKey, true);
		
		//解密 
		byte[] decryptData = handleByPrivate(encryptData, privateKey, false);
		System.out.println(new String(decryptData));

		// =======>私钥加密，公钥解密 <=======
		
		byte[] encryptDat = handleByPrivate(str.getBytes(), privateKey, true);
		
		//解密 
		byte[] decryptDat = handleByPublic(encryptDat, publicKey, false);
		System.out.println(new String(decryptDat));

		
		// =======>私钥签名 ，公钥验证 (keystore签名，证书校检) <=======
		
		byte[] sign = sign(str.getBytes(), "src/ruulai.keystore", "xiaoming", "www.ruulai.com");
		
		boolean flag = verify("src/ruulai.cer", str.getBytes(), sign);
		System.out.println("校检:"+flag);
		
		
		
	}
	
	
	//公钥 加密或解密
	public static byte[] handleByPublic(byte[] data,PublicKey publicKey,boolean isEncrypt){
		try {
			//对数据加密或解密 
			Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
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

	
	//私钥加密或解密 
	//使用私钥加密或解密 
	public static byte[] handleByPrivate(byte[] data,PrivateKey privateKey,boolean isEncrypt){
		try {
			//对数据加密或解密 
			Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
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
	
	
	// 私钥或 keystore 进行签名
		public static byte[] sign(byte[] data,String keyStorePath,String password,String alias) {
			try {
				//从 keystore中获取证书 仅仅为了获取 证书指定的  签名的算法
				X509Certificate x509Certificate = (X509Certificate) getCerByKeyStoreFile(keyStorePath,password, alias);
				PrivateKey priKey = getPrivateKeyByKeyStore(keyStorePath, password,alias);
				//由证书指定签名算法
				Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
				signature.initSign(priKey);
				signature.update(data);
				return signature.sign();

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		// 公钥或 证书 进行 校检
		public static boolean verify(String cerFilePath,byte[] data, byte[] sign) {

			try {
				X509Certificate x509Certificate= (X509Certificate) getCerByCerFile(cerFilePath);
				Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
				// 初始化Signature(参数是公钥)
				signature.initVerify(x509Certificate);//或由公钥 进行校检
				signature.update(data);

				boolean flag = signature.verify(sign);
				return flag;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}
	


		//假设是服务器 (拥有keystore)
		public static PrivateKey getPrivateKeyByKeyStore(String keyStorePath,String password,String alias) throws Exception{
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream fileInputStream  = new FileInputStream(keyStorePath);
			keyStore.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
			
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
			return privateKey;
		}

		//假设是服务器 (拥有keystore)
		private static Certificate getCerByKeyStoreFile(String keyStorePath,String password,String alias) throws Exception {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			FileInputStream fileInputStream  = new FileInputStream(keyStorePath);
			keyStore.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
			
			return keyStore.getCertificate(alias);
		}


		//假设是客户端(拥有证书)
		public static PublicKey getPublicKeyByCer(String cerFilePath) throws Exception{
			Certificate certificate = getCerByCerFile(cerFilePath);
			return certificate.getPublicKey();
		}

		//假设是客户端(拥有证书)
		private static Certificate getCerByCerFile(String cerFilePath) throws Exception {
			CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
			FileInputStream fileInputStream  = new FileInputStream(cerFilePath);
			Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
			fileInputStream.close();
			return certificate;
		}

		
	
}

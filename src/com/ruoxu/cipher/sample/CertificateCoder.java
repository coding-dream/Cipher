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
 * <h1>����֤���ʹ��</h1>
 * ����֤��:���϶�������ѧ�㷨��������й�Կ��ͬʱ��������ǩ�����ɼ�����Ϣ��Դ�������������ϢժҪ������֤֤�������ԡ�
 * 
 * 
 * 
 * java ����KeyTool 
 * <h1>������Կ��</h1>
 * keytool -genkeypair -keyalg RSA -keysize 2048 -sigalg SHA1withRSA -validity 36000 -alias www.ruulai.com -keystore ruulai.jks
 *
 * -genkeypair ��ʾ������Կ
 * -keyalg ָ����Կ�㷨
 * -keysize ָ����Կ���ȣ�Ĭ��1024λ��������2048 λ
 * -sigalg ָ������ǩ���㷨
 * -validity ֤����Ч�� 36000��
 * -alias ָ������ 
 * -keystore ָ����Կ��洢λ��
 * 
 * <h1>��������֤��</h1> 
 * keytool -exportcert -alias www.ruulai.com -keystore ruulai.keystore -file ruulai.cer -rfc
 * -exportcert ��ʾ֤�鵼������
 * -keystore ָ����Կ���ļ�
 * -file ֤�鵼��·��
 * -rfc ָ����base64��ʽ��� 
 * 
 * 
 * �÷�:
 * .keystore�ļ�����Կ���ļ�����������˹�Կ��˽Կ 
 * .cer�ļ�(����֤��) ���Ÿ�С��飬����ǩ������Կ����ϢժҪ��Ϣ
 * 
 * Demo ����˵��:
 * ����keystore�Ƿ�����������(Android ����ǩ��Apkʱ��Ҳ����)
 * keystore�ȿ��Ի�ȡ֤��(֤���к��й�Կ)���ֿ��Ի�ȡ˽Կ
 * 
 * 
 * ֤�鷢�Ÿ��ͻ���(��֤�鱾����sign������һ��������֤��)��
 * �ͻ�����֤��������ǩ������( ������ӵ��˽Կkeystore��˽Կǩ�� --->�ͻ��� ��Կ��֤(֤��) )
 * 
 */

public class CertificateCoder {

	private static final String CERT_TYPE="x.509";

	public static void main(String[] args) throws Exception {
		
		String str = "hello world";
		PrivateKey privateKey = getPrivateKeyByKeyStore("src/ruulai.keystore","xiaoming", "www.ruulai.com" );//������
		PublicKey publicKey = getPublicKeyByCer("src/ruulai.cer"); //�ͻ���
		
		System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));
		System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));
		
		// =======>��Կ���ܣ�˽Կ���� <=======
		//���� 
		byte[] encryptData = handleByPublic(str.getBytes(), publicKey, true);
		
		//���� 
		byte[] decryptData = handleByPrivate(encryptData, privateKey, false);
		System.out.println(new String(decryptData));

		// =======>˽Կ���ܣ���Կ���� <=======
		
		byte[] encryptDat = handleByPrivate(str.getBytes(), privateKey, true);
		
		//���� 
		byte[] decryptDat = handleByPublic(encryptDat, publicKey, false);
		System.out.println(new String(decryptDat));

		
		// =======>˽Կǩ�� ����Կ��֤ (keystoreǩ����֤��У��) <=======
		
		byte[] sign = sign(str.getBytes(), "src/ruulai.keystore", "xiaoming", "www.ruulai.com");
		
		boolean flag = verify("src/ruulai.cer", str.getBytes(), sign);
		System.out.println("У��:"+flag);
		
		
		
	}
	
	
	//��Կ ���ܻ����
	public static byte[] handleByPublic(byte[] data,PublicKey publicKey,boolean isEncrypt){
		try {
			//�����ݼ��ܻ���� 
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

	
	//˽Կ���ܻ���� 
	//ʹ��˽Կ���ܻ���� 
	public static byte[] handleByPrivate(byte[] data,PrivateKey privateKey,boolean isEncrypt){
		try {
			//�����ݼ��ܻ���� 
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
	
	
	// ˽Կ�� keystore ����ǩ��
		public static byte[] sign(byte[] data,String keyStorePath,String password,String alias) {
			try {
				//�� keystore�л�ȡ֤�� ����Ϊ�˻�ȡ ֤��ָ����  ǩ�����㷨
				X509Certificate x509Certificate = (X509Certificate) getCerByKeyStoreFile(keyStorePath,password, alias);
				PrivateKey priKey = getPrivateKeyByKeyStore(keyStorePath, password,alias);
				//��֤��ָ��ǩ���㷨
				Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
				signature.initSign(priKey);
				signature.update(data);
				return signature.sign();

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		// ��Կ�� ֤�� ���� У��
		public static boolean verify(String cerFilePath,byte[] data, byte[] sign) {

			try {
				X509Certificate x509Certificate= (X509Certificate) getCerByCerFile(cerFilePath);
				Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
				// ��ʼ��Signature(�����ǹ�Կ)
				signature.initVerify(x509Certificate);//���ɹ�Կ ����У��
				signature.update(data);

				boolean flag = signature.verify(sign);
				return flag;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}
	


		//�����Ƿ����� (ӵ��keystore)
		public static PrivateKey getPrivateKeyByKeyStore(String keyStorePath,String password,String alias) throws Exception{
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream fileInputStream  = new FileInputStream(keyStorePath);
			keyStore.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
			
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
			return privateKey;
		}

		//�����Ƿ����� (ӵ��keystore)
		private static Certificate getCerByKeyStoreFile(String keyStorePath,String password,String alias) throws Exception {
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			FileInputStream fileInputStream  = new FileInputStream(keyStorePath);
			keyStore.load(fileInputStream, password.toCharArray());
			fileInputStream.close();
			
			return keyStore.getCertificate(alias);
		}


		//�����ǿͻ���(ӵ��֤��)
		public static PublicKey getPublicKeyByCer(String cerFilePath) throws Exception{
			Certificate certificate = getCerByCerFile(cerFilePath);
			return certificate.getPublicKey();
		}

		//�����ǿͻ���(ӵ��֤��)
		private static Certificate getCerByCerFile(String cerFilePath) throws Exception {
			CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
			FileInputStream fileInputStream  = new FileInputStream(cerFilePath);
			Certificate certificate = certificateFactory.generateCertificate(fileInputStream);
			fileInputStream.close();
			return certificate;
		}

		
	
}

package com.cby.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.net.URLEncoder;
import java.security.Key;

public class DESEncrypt {

	public static final String ALGORITHM = "DES"; // ???

	public static final String DES_KEY = "fd@GWd900af&*3BH6$fdsE=";

	/**
	 *
	 * ????
	 *
	 * @param data
	 *
	 * @return
	 *
	 * @throws Exception
	 */

	public static String encrypt(String data) throws Exception {

		return URLEncoder.encode(new String(DESEncrypt.encryptBASE64(DESEncrypt

		.encrypt(data.getBytes("GBK"), DES_KEY))), "UTF-8");

	}

	/**
	 *
	 * BASE64 ????
	 * 
	 * @param key
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	public static String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);

	}

	public static byte[] encrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);

	}

	public static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);

		return secretKey;

	}

}

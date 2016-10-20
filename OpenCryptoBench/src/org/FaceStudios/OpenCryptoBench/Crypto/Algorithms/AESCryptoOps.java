package org.FaceStudios.OpenCryptoBench.Crypto.Algorithms;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.FaceStudios.OpenCryptoBench.OpenCryptoBench;
import org.FaceStudios.OpenCryptoBench.Crypto.CryptoObject;
import org.apache.commons.codec.binary.Hex;

import com.google.common.base.Stopwatch;

public class AESCryptoOps {
	//This is the redirect implementation for AES
	//This will allow data to be logged and processed
	//All methods should be static
	//Logger Implementaion
	public static final Logger LOGGER =  OpenCryptoBench.GLOBALLOG.getLogger(AESCryptoOps.class.getName());
	private static Stopwatch stopwatch;
	private static Stopwatch s2;
	private static long keygenTime;
	private static long encryptTime;
	private static long encryptAgTime;
	private static long decryptAgTime;
	private static long decryptTime;
	private static long cryptoTime;
	private static long totalTime;
	private static Cipher c;
	private static Cipher c1;
	private static SecretKey secret;
	protected static KeyGenerator gen;
	
	public static void performAES(int bitlen,CryptoObject thing){
		LOGGER.setUseParentHandlers(true);
		encryptTime = 0;
		encryptAgTime = 0;
		decryptTime = 0;
		decryptAgTime = 0;
		keygenTime = 0;
		cryptoTime=  0;
		totalTime=  0;
		/*try {
			LOGGER.addHandler(new FileHandler(file));
		} catch (SecurityException | IOException e2) {
			e2.printStackTrace();
		}*/
		LOGGER.info("##############################################################");
		LOGGER.info("BEGIN AES PROCEDURE");
		LOGGER.info("##############################################################");
		LOGGER.info("Starting Stopwatch");
		stopwatch = Stopwatch.createStarted();
		LOGGER.info("Starting Encryption procedures for AES");
		LOGGER.config("Creating a SecretKey Generator");
		try {
			gen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.severe("ERROR: Could not find Algorithm AES");
			e1.printStackTrace();
		}
		LOGGER.config("Initializing the generator for bitlength of "+bitlen+" bits");
	
		gen.init(bitlen);
		
		LOGGER.config("Generating Key");
		s2 = Stopwatch.createStarted();
		secret = gen.generateKey();
		s2.stop();
		keygenTime = s2.elapsed(TimeUnit.MILLISECONDS);
		cryptoTime = cryptoTime+keygenTime;
		LOGGER.config("Key Generation took "+keygenTime+" ms");
		s2.reset();
		LOGGER.config("CryptoObject's input string is "+thing.getInput());
		LOGGER.config("CryptoObject's SecretKey Object is "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Initializing Cipher as AES");
		try {
			c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, secret);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.severe("ERROR: Cipher object could not initialize with given algorithm and parameter");
			e.printStackTrace();
		}
		LOGGER.config("Success in initializing Cipher with given params");
		LOGGER.info("Creating an output String");
		byte[] outBytes = null;
		String out = "";
		LOGGER.config("Starting Encryption");
		s2.start();
		try {
			outBytes = c.doFinal(thing.getInput().getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.severe("ERROR: Cipher could not execute encryption");
			e.printStackTrace();
		}
		s2.stop();
		encryptTime = s2.elapsed(TimeUnit.MILLISECONDS);
		cryptoTime = cryptoTime+encryptTime;
		LOGGER.info("Success");
		LOGGER.info("Encryption Operation took "+encryptTime+" ms");
		s2.reset();
		LOGGER.info("Output string is " +Hex.encodeHexString(outBytes));
		LOGGER.info("Stopping Stopwatch for Encryption");
		stopwatch.stop();
		encryptAgTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		totalTime = totalTime+encryptAgTime;
		LOGGER.info("Time Elapsed for Encryption is "+encryptAgTime+" ms" );
		LOGGER.info("Resetting Stopwatch");
		stopwatch.reset();
		LOGGER.config("Success in resetting stopwatch");
		LOGGER.info("Restarting Stopwatch");
		stopwatch.start();		
		LOGGER.info("Using Output string "+out+" for decryption");
		LOGGER.info("Using SecretKey "+Hex.encodeHexString(secret.getEncoded())+" as SecretKey for decryption");
		LOGGER.info("Starting Decryption process for AES");
		try {
			c1 = Cipher.getInstance("AES");
			c1.init(Cipher.DECRYPT_MODE, secret);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.severe("ERROR: Could not initialize the cipher object with given parameters");
			e.printStackTrace();
		}
		LOGGER.config("Success in initializing a Cipher");
		LOGGER.config("Creating an output String");
		byte[] out1Bytes = null;
		LOGGER.info("Starting Decryption");
		s2.start();
		try {
			out1Bytes = c1.doFinal(outBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.severe("ERROR: Could Not Decrypt Data");
			e.printStackTrace();
		}
		s2.stop();
		decryptTime = s2.elapsed(TimeUnit.MILLISECONDS);
		cryptoTime = cryptoTime+decryptTime;
		LOGGER.info("Success");
		LOGGER.info("Output string is"+out1Bytes.toString());
		LOGGER.info("Stopping Stopwatch");
		stopwatch.stop();
		decryptAgTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
		totalTime = totalTime+decryptAgTime;
		LOGGER.info("Time elapsed is "+ decryptAgTime+" ms");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("RESULTS");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("Key Generation Time: "+keygenTime+" ms");
		LOGGER.info("Encryption Time: "+encryptTime+" ms");
		LOGGER.info("Encryption Aggregate Time: "+encryptAgTime+" ms");
		LOGGER.info("Decryption Time: "+decryptTime+" ms");
		LOGGER.info("Decryption Aggregate Time: "+decryptAgTime+" ms");
		LOGGER.info("Cryptography Operation Time: "+cryptoTime+" ms");
		LOGGER.info("Total Operation Time: "+totalTime+" ms");
		LOGGER.info("Input String: "+thing.getInput());
		LOGGER.info("Key: "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Encrypted Output: "+outBytes.toString());
		LOGGER.info("#################################################################");
		LOGGER.info("END AES PROCEDURE");
		LOGGER.info("#################################################################");
	}
}
	

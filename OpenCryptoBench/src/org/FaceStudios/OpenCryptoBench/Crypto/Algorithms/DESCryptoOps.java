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

public class DESCryptoOps {
	//This is the redirect implementation for DES
	//This will allow data to be logged and processed
	//All methods should be static
	
	//Logger Implementaion
	@SuppressWarnings("static-access")
	public static final Logger LOGGER =  OpenCryptoBench.GLOBALLOG.getLogger(DESCryptoOps.class.getName());
	//Stopwatch Implementation
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

	public static void performDES(int bitlen,CryptoObject thing){
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
		LOGGER.info("BEGIN DES PROCEDURE");
		LOGGER.info("##############################################################");
		LOGGER.info("Starting Stopwatch");
		stopwatch = Stopwatch.createStarted();
		LOGGER.info("Starting Encryption procedures for DES");
		LOGGER.config("Creating a SecretKey Generator");
		try {
			gen = KeyGenerator.getInstance("DES");
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.severe("ERROR: Could not find Algorithm DES");
			e1.printStackTrace();
		}
		LOGGER.config("Initializing the generator for bitlength of "+bitlen+" bits");
	
		gen.init(bitlen);
		
		LOGGER.config("Generating Key");
		s2 = Stopwatch.createStarted();
		secret = gen.generateKey();
		s2.stop();
		keygenTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+keygenTime;
		LOGGER.config("Key Generation took "+keygenTime+" ns");
		s2.reset();
		LOGGER.config("CryptoObject's input string is "+thing.getInput());
		LOGGER.config("CryptoObject's SecretKey Object is "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Initializing Cipher as DES");
		try {
			c = Cipher.getInstance("DES");
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
		encryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+encryptTime;
		LOGGER.info("Success");
		LOGGER.info("Encryption Operation took "+encryptTime+" ns");
		s2.reset();
		LOGGER.info("Output string is " +Hex.encodeHexString(outBytes));
		LOGGER.info("Stopping Stopwatch for Encryption");
		stopwatch.stop();
		encryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+encryptAgTime;
		LOGGER.info("Time Elapsed for Encryption is "+encryptAgTime+" ns" );
		LOGGER.info("Resetting Stopwatch");
		stopwatch.reset();
		LOGGER.config("Success in resetting stopwatch");
		LOGGER.info("Restarting Stopwatch");
		stopwatch.start();		
		LOGGER.info("Using Output string "+out+" for decryption");
		LOGGER.info("Using SecretKey "+Hex.encodeHexString(secret.getEncoded())+" as SecretKey for decryption");
		LOGGER.info("Starting Decryption process for DES");
		try {
			c1 = Cipher.getInstance("DES");
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
		decryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+decryptTime;
		LOGGER.info("Success");
		LOGGER.info("Output string is"+out1Bytes.toString());
		LOGGER.info("Stopping Stopwatch");
		stopwatch.stop();
		decryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+decryptAgTime;
		LOGGER.info("Time elapsed is "+ decryptAgTime+" ns");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("RESULTS");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("Key Generation Time: "+keygenTime+" ns");
		LOGGER.info("Encryption Time: "+encryptTime+" ns");
		LOGGER.info("Encryption Aggregate Time: "+encryptAgTime+" ns");
		LOGGER.info("Decryption Time: "+decryptTime+" ns");
		LOGGER.info("Decryption Aggregate Time: "+decryptAgTime+" ns");
		LOGGER.info("Cryptography Operation Time: "+cryptoTime+" ns");
		LOGGER.info("Total Operation Time: "+totalTime+" ns");
		LOGGER.info("Input String: "+thing.getInput());
		LOGGER.info("Key: "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Encrypted Output: "+outBytes.toString());
		LOGGER.info("#################################################################");
		LOGGER.info("END DES PROCEDURE");
		LOGGER.info("#################################################################");
		
	}
	
	public static void perform3DES(int bitlen,CryptoObject thing){
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
		LOGGER.info("BEGIN DESede PROCEDURE");
		LOGGER.info("##############################################################");
		LOGGER.info("Starting Stopwatch");
		stopwatch = Stopwatch.createStarted();
		LOGGER.info("Starting Encryption procedures for DESede");
		LOGGER.config("Creating a SecretKey Generator");
		try {
			gen = KeyGenerator.getInstance("DESede");
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.severe("ERROR: Could not find Algorithm DESede");
			e1.printStackTrace();
		}
		LOGGER.config("Initializing the generator for bitlength of "+bitlen+" bits");
	
		gen.init(bitlen);
		
		LOGGER.config("Generating Key");
		s2 = Stopwatch.createStarted();
		secret = gen.generateKey();
		s2.stop();
		keygenTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+keygenTime;
		LOGGER.config("Key Generation took "+keygenTime+" ns");
		s2.reset();
		LOGGER.config("CryptoObject's input string is "+thing.getInput());
		LOGGER.config("CryptoObject's SecretKey Object is "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Initializing Cipher as DESede");
		try {
			c = Cipher.getInstance("DESede");
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
		encryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+encryptTime;
		LOGGER.info("Success");
		LOGGER.info("Encryption Operation took "+encryptTime+" ns");
		s2.reset();
		LOGGER.info("Output string is " +Hex.encodeHexString(outBytes));
		LOGGER.info("Stopping Stopwatch for Encryption");
		stopwatch.stop();
		encryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+encryptAgTime;
		LOGGER.info("Time Elapsed for Encryption is "+encryptAgTime+" ns" );
		LOGGER.info("Resetting Stopwatch");
		stopwatch.reset();
		LOGGER.config("Success in resetting stopwatch");
		LOGGER.info("Restarting Stopwatch");
		stopwatch.start();		
		LOGGER.info("Using Output string "+out+" for decryption");
		LOGGER.info("Using SecretKey "+Hex.encodeHexString(secret.getEncoded())+" as SecretKey for decryption");
		LOGGER.info("Starting Decryption process for DESede");
		try {
			c1 = Cipher.getInstance("DESede");
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
		decryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+decryptTime;
		LOGGER.info("Success");
		LOGGER.info("Output string is"+out1Bytes.toString());
		LOGGER.info("Stopping Stopwatch");
		stopwatch.stop();
		decryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+decryptAgTime;
		LOGGER.info("Time elapsed is "+ decryptAgTime+" ns");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("RESULTS");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("Key Generation Time: "+keygenTime+" ns");
		LOGGER.info("Encryption Time: "+encryptTime+" ns");
		LOGGER.info("Encryption Aggregate Time: "+encryptAgTime+" ns");
		LOGGER.info("Decryption Time: "+decryptTime+" ns");
		LOGGER.info("Decryption Aggregate Time: "+decryptAgTime+" ns");
		LOGGER.info("Cryptography Operation Time: "+cryptoTime+" ns");
		LOGGER.info("Total Operation Time: "+totalTime+" ns");
		LOGGER.info("Input String: "+thing.getInput());
		LOGGER.info("Key: "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Encrypted Output: "+outBytes.toString());
		LOGGER.info("#################################################################");
		LOGGER.info("END DESede PROCEDURE");
		LOGGER.info("#################################################################");
		
	}

}

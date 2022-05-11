package enc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrpytion {
	public byte[] encmessgage(String messgage,SecretKey deskey) {
		byte[] et = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] t = messgage.getBytes();
			et = cipher.doFinal(t);
			
		} catch (Exception e) {

		}
		return et;
	}
	public static byte[] getByte(File file) throws IOException {
		byte[] b = new byte[(int) file.length()];
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		int read;
		while ((read = fis.read(b)) != -1) {
			os.write(b, 0, read);
		}
		os.close();
		fis.close();
		return b;

	}
	public static byte[] encfile(File f,SecretKeySpec deskey) {
		byte[] ef = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] t = getByte(f);
			ef = cipher.doFinal(t);
			
		} catch (Exception e) {

		}
		return ef;
	}
	
}

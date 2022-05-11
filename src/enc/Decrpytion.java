package enc;

import java.io.File;
import java.nio.file.Files;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Decrpytion {
	public String decmessgage(byte[] enmessgage,SecretKeySpec deskey) {
		byte[] dt = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			dt = cipher.doFinal(enmessgage);
		} catch (Exception e) {

		}
		return new String(dt);
	}
	public static void deccfile(byte[] encfile,SecretKey deskey,String filename) {
		byte[] df = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			df = cipher.doFinal(encfile);
			File x = new File("src/" + filename);
			Files.write(x.toPath(), df);
		} catch (Exception e) {

		}
	}
}


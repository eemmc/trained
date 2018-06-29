import java.io.IOException;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Formatter;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

public class DecryptTest {
	
	public static byte[] encrypt(byte[] data) {
		
		try {
			String key = "1234567890123456";
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec spec = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec);
			
            return cipher.doFinal(data);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String decrypt(String body) {
        try {
            //byte[] data = Base64.decode(URLDecoder.decode(body, "UTF-8"), Base64.DEFAULT);
        	byte[] data=Base64.getDecoder().decode(URLDecoder.decode(body,"UTF-8"));
            String key = "1234567890123456";
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec spec = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            return new String(cipher.doFinal(data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
	
	@Test
	public void demo01() {
		String value=decrypt("82bu11RliV%2FP6v14bCbk12cJ7wtb%2FcsgbZlxEIHGc3wCMAWhywlsCYEwLT9B1N2ykHEj6PrmOOHg7QmwSMmyon8hlM7VVoIY1nlLLbOcgTRqtHV%2FoqtAnG9nNqCRcxPKsJbKSCqtoiTClXtYMB9cOGOK4Uxr5mOnINLMXI4fe4rIaZjKq3DXqLkPvt42C093m6AO7894j%2BTfjZ0hYrP5kU0xITz9t%2BZeV1gLs8UOLiIBmwvUHXXCLKvAiFe06Zhya%2Fbhj2P%2BYulRvy6HuJLjBj38LMhr1Mwpgn9OITKGBVQJbX3njZ554DGhw9M29qGUisnzeSdTRUVqhxcupJbvj8xYOiJej7QfxfHKP6CwjZ62PJ%2FkLZqIHQepzPwewCtYlVXHrSvgJWQTln2KZFGmsdxq%2FsTmj6DpfKkmkDmzCOPloD7U1TrroDeoN1iKOR51obv2nnMhi5a%2BH3HVPNzggBIImk4cJBlmD%2F1ekLDpIaVGr4u4uPqLqRTt4gerVK6vJMb8JkPhkvzr4ONRt7vxdCGoMYbIHci5oCIIbCCZvXvFom%2FPVTK%2B%2FqJUZwooqwHQVx5kJ9PjyGnCKqpazyfN5xQgWxnnalPBddb1p2vuWcp63chu8opLTLW715Q05QB48GU2jNmebGyQ6mxmDZ7G9mvLFPrd2XRaYiVYyX%2BEl1aVnVDbQsMfXBVFLHE2WnI%2BngMRDfYTGiWNuHMoH6LJaiSbSqJc8XqWOOJ9WbIgLWK7w3ZNtFtpr5VBKPrqZ1B7Vpy8z09NigzSXLaJO7agE%2Fs6611kgZlPjcw1UUOWEUo4OEe1sAlbcXOJdn5fob0Hu0Qeu8gcNWmhPV%2BfHvhn6FA6szoLWlKYwK%2FbPWYIbQ7e3kaSXYKUuJtSv1vJZpvGwaW%2B%2BZvlMF2PkSOYS6qO75TbA7%2BMSl4NcqB0Ni%2FqgZ4x6ZZ%2BXgUGOGsPSaRUnSGBynmowq3iU7FCKlXqv3VZF9okTmKAzzlmQWX4g6Rlm0bw1MmMnNLzB9lh%2B2Zzn8vGvNAl5s0w2RXrzF680hhyY2wIDJ9oO%2BoOljKWD9KQ%2BuFKKXrTcxnrXRwi9RyK4J88Sl%2FRShG9N0z1xp2nkgCiVNvaoXjulUD5idD3AnJIDXBaszPVXWUNnk%2BzWPWM3ZKtzG2G0%2BS3Njjfb2AN9sofSBFr3sEghc5njCoK%2BFscvEhAmWX3Y6uM9NyxNoiHM4rPbvFXNiMFrR0Ofc2czxmedwX5PFl6MJZStOZxym6EqmvRxOnsurOuiXu%2FArSI6%2FT%2FnECIsx%2B6zWXqvPgnbNnh83TSlBWx66eWzKhgssNAw13DOysTz%2FYAPlNk7tXcz6BH85CLb1M5d05IvxxaTyE6kPyBu3G3bb94W0M8aQuZ8kW6lsRGud9O702bTixcvNSD2SfydASLwl5jIAGdcmVIkxkrfCBTW28EZRXU7EKCIlGVPuyZSLj2RS2CYeux8fCa8p%2FwWO%2BIizhsGRXVB%2BbFtbUQAIotVgGzy1ILSCFEwEjIPfVKOF6dDWd7j3DWk4tGYoVwa9j29aUTvWvUgneZMnibtZd6IEnsJJkUJWZByfMMuyqcfAotu0aNbPiSUCrW5SBxgLqtZkbxfJ6nu3FleejvCTP06hugvex1lqRTIxBrNAr6MoorL7fkB2CdW%2BMzhG5%2FmMapZDGKHMP5rXpG2NNIb5M6zxzfnxJHmZj%2FITu9og%2FKHRVgQnRCMsTCro7R0RrNHxdbCD1HTmMmOE%2F404JFEYxeTZ0mxAlo322QLUNGbcQ0ydJRtUN63ktd53UMzhqhy9tgIon%2BPq7It021sA3zJb0H65XejC8CYmqSq6US5RWacUkyNJsX5o28gDc2c%2BV32iTwIHWNQujrEbz1hmfzvTla4mi7w%2B04x6Lslrme5n0lK9FGuxu3Irz%2BSG2Zg4UVgtDceIzgy%2FygS7A0OGo%2FBt5tRCALAQ4uTtBBJH04PBFDXwsJs6gFXfTdHxlWqlgNd317Nokneuxbrnbda7e9K8wYpxxfPFNVlY2PvmdRpU82wcMofNVy0OsXcoFFvn4z2tkZ5ii3HjoJyppUIbDgTk9fE639rWc7nH0MC6d6lvhDnT3LeorHAFWcIIovPpwSFZPrJ29P8X6xLqTas5aAs0TR1M6EJKeo2%2BROaI97H1EHl1XcBzghVp%2BgoF7imlTiJdJnQ7H9cnSTTES9NVpIcwo738O7JpycgbslDw0%3D");
		System.err.println(value);
	}
	
	
	@Test
	public void demo02() throws IOException, GeneralSecurityException {
		String body="QU8hCBoWbWVeV0ihTIImseBg8mhn8WiH9lAm6fxBLQk%3D";
		byte[] result=Base64.getDecoder().decode(URLDecoder.decode(body,"UTF-8"));
		System.err.println(hex(result));
		//String result=AESCrypt.decrypt("1234567890123456", data);
		
		//System.err.println(result);
		System.err.println();
		System.err.println();///1234567890123456
		System.err.println(hex("12345678901234561234567890123456".getBytes()));
		System.err.println();
		System.err.println();///0102030405060708
		System.err.println(hex("0102030405060708".getBytes()));
		System.err.println();
		System.err.println();
		
	}
	
	@SuppressWarnings("resource")
	public static String hex(byte[] data) {
		Formatter formatter = new Formatter(Locale.getDefault());
		int index=0;
        for (byte bit : data) {
            formatter.format(Locale.getDefault(), "\\x%02x", bit);
            if(index==15) {
            	formatter.format("\n");
            	index=0;
            }else {
            	++index;
            }
            
        }
        
        return formatter.toString();
	}
	
	@Test
	public void demo03() {
		
		String source="{\"data\":[1,2,3,4],\"message\":\"This is success result.\"}";
		
		
		System.err.println(source);
		System.err.println();
		System.err.println();///1234567890123456
		System.err.println(hex("1234567890123456".getBytes()));
		System.err.println();
		System.err.println();///0102030405060708
		System.err.println(hex("0102030405060708".getBytes()));
		System.err.println();
		System.err.println();
		byte[] result=encrypt(source.getBytes());
		System.err.println(hex(result));
		
	}
}

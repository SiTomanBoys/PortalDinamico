package cl.portaldinamico.utils;


import java.util.Arrays;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

@Stateless(name="Ejb3Utils")
public class Ejb3Utils implements Ejb3UtilsLocal,Ejb3UtilsRemote
{
	static final Logger log = Logger.getLogger(Ejb3Utils.class);
	public String obtenerParametroString(HashMap<String,Object> Parametros,String llave)
	{
		String valor="";
		if(Parametros.containsKey(llave))
		{
			String[] value = (String[]) Parametros.get(llave);
			if(value.length==1)
				valor=value[0].toString();
		}
		return valor;
	}
	
	public String[] obtenerParametroArregloString(HashMap<String,Object> Parametros,String llave)
	{
		String[] valor= new String[0];
		if(Parametros.containsKey(llave))
			valor = (String[]) Parametros.get(llave);
		return valor;
	}
	
	public String encrypt(String password, String strKey) throws Exception
	{
		byte[] keyBytes = Arrays.copyOf(strKey.getBytes("ASCII"), 16);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cleartext = password.getBytes("UTF-8");
        byte[] ciphertextBytes = cipher.doFinal(cleartext);
        return new String(Hex.encodeHex(ciphertextBytes));
	}

	public String decrypt(String passwordhex, String strKey) throws Exception 
	{
		    
        byte[] keyBytes = Arrays.copyOf(strKey.getBytes("ASCII"), 16);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher decipher = Cipher.getInstance("AES");
        decipher.init(Cipher.DECRYPT_MODE, key);
        char[] cleartext = passwordhex.toCharArray();
        byte[] decodeHex = Hex.decodeHex(cleartext);
        byte[] ciphertextBytes = decipher.doFinal(decodeHex);
        return new String(ciphertextBytes);
   
	}
	public String decodificarHexa(String contenido) throws Exception
	{
		char[] cleartext = contenido.toCharArray();
        byte[] decodeHex = Hex.decodeHex(cleartext);
        return new String(decodeHex);
	}
	
}

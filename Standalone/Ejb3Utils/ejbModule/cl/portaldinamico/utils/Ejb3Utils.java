package cl.portaldinamico.utils;


import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

@Stateless(name="Ejb3Utils")
public class Ejb3Utils implements Ejb3UtilsLocal,Ejb3UtilsRemote
{
	static final Logger log = Logger.getLogger(Ejb3Utils.class);
	public String generarDocumento(String XML,String XSL) throws Exception
	{
		System.setProperty("javax.xml.transform.TransformerFactory","net.sf.saxon.TransformerFactoryImpl");
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer(new StreamSource(new StringReader(XSL)));
		tf.setOutputProperty( OutputKeys.ENCODING, "ISO-8859-1");
		tf.setOutputProperty( OutputKeys.INDENT, "no");
		tf.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes");
		StreamSource ss = new StreamSource(new StringReader(XML));
		StreamResult sr = new StreamResult(new StringWriter());
		tf.transform(ss,sr);
		return sr.getWriter().toString();
	}
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
	
	public String codificarBase64(String contenido) throws Exception
	{
        byte[] encode64 = Base64.encodeBase64(contenido.getBytes());
        return new String(encode64);
	}
	public String decodificarBase64(String contenido) throws Exception
	{
        byte[] decode64 = Base64.decodeBase64(contenido.getBytes());
        return new String(decode64);
	}
	public String hashMapAXml(HashMap<String,Object> map,String nombre)
	{
		String xml="<"+nombre+">";
		for ( String llave : map.keySet() )
		{
			xml+="<"+llave+">"+map.get(llave)+"</"+llave+">";
		}
		xml+="</"+nombre+">";
		return xml;
	}
	public void impLog(Logger logg,int logLevel,HashMap<String,Object> datosConf,String mensaje)
	{
		impLog(logg, logLevel, datosConf, mensaje,null);
	}
	public void impLog(Logger logg,int logLevel,HashMap<String,Object> datosConf,String mensaje,Throwable t)
	{
		try
		{
			int intLog = Integer.parseInt(datosConf.get("nivelLog").toString());
			if(logLevel >= intLog)
			{
				String[] nombreClase = logg.getName().split("\\.");
				String clase = nombreClase[nombreClase.length-1];
				log.log(Level.toPriority(logLevel),"["+clase+"] "+ mensaje ,t);
			}
		}catch(Exception e)
		{
			log.error("La variable 'nivelLog' del portal.properties no es un numero o no existe",e);
		}
	}
}

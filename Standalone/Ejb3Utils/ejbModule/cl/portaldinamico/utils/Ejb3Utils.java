package cl.portaldinamico.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

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

import cl.portaldinamico.Exception.PortalException;

@Stateless(name="Ejb3Utils")
public class Ejb3Utils implements Ejb3UtilsLocal,Ejb3UtilsRemote
{
	static final Logger log = Logger.getLogger(Ejb3Utils.class);
	public HashMap<String,Object> cargarPropiedades() throws Exception
	{
		Properties portalProperties = new Properties();
    	//Obtengo las propiedades generales del portal dinamico
		portalProperties.load(new FileInputStream(System.getProperty("jboss.home.dir")+File.separatorChar+"portalConf"+File.separatorChar+"portal.properties"));
		if(!portalProperties.containsKey("apacheDir"))
			throw new PortalException("El parametro 'apacheDir' no existe en el archivo 'portal.properties'");
		if(!portalProperties.containsKey("carpetaConf"))
			throw new PortalException("El parametro 'carpetaConf' no existe en el archivo 'portal.properties'");
		if(!portalProperties.containsKey("nombreArchivo"))
			throw new PortalException("El parametro 'nombreArchivo' no existe en el archivo 'portal.properties'");
		if(!portalProperties.containsKey("carpetaXsl"))
			throw new PortalException("El parametro 'carpetaXsl' no existe en el archivo 'portal.properties'");
		String raizApache = portalProperties.getProperty("apacheDir");
		String carpetaConf = portalProperties.getProperty("carpetaConf");
		String carpetaXsl = portalProperties.getProperty("carpetaXsl");
		String nombreArchivoConf = portalProperties.getProperty("nombreArchivo");
		log.info("DIRECTORIO RAIZ APACHE: "+ raizApache);
		log.info("NOMBRE DE PARPERTA DE XSL POR PORTAL: "+carpetaXsl);
		log.info("NOMBRE CARPETA DE CONFIGURACIONES POR PORTAL: "+carpetaConf);
		log.info("NOMBRE DEL ARCHIVO PROPERTIES: "+nombreArchivoConf);
		HashMap<String,Object> portalProp = new HashMap<String,Object>();
		portalProp.put("raizApache", raizApache);
		portalProp.put("carpetaXsl", carpetaXsl);
		portalProp.put("carpetaConf", carpetaConf);
		portalProp.put("nombreArchivoConf", nombreArchivoConf);
		return portalProp;
	
	}
	public String generarDocumento(String XML,String XSL) throws Exception
	{
		return generarDocumento( XML, XSL, null);
	}
	public String generarDocumento(String XML,String XSL,HashMap<String,String> parametros) throws Exception
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
		return Html2Simbolos(sr.getWriter().toString());
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
	private String Html2Simbolos(String html) {
		return html.replace("&amp;", "&").replace("&quot;", "\"").replace("&lt;", "<").replace("&gt;", ">");
	}
}

package cl.portaldinamico.utils;
import java.util.HashMap;

import javax.ejb.Local;

import org.apache.log4j.Logger;

@Local
public interface Ejb3UtilsLocal 
{
	public abstract String obtenerParametroString(HashMap<String,Object> Parametros,String llave);
	public abstract String[] obtenerParametroArregloString(HashMap<String,Object> Parametros,String llave);
	public abstract String encrypt(String value, String key) throws Exception;
	public abstract String decrypt(String key, String encrypted) throws Exception;
	public abstract String decodificarHexa(String contenido) throws Exception;
	public abstract String codificarBase64(String contenido) throws Exception;
	public abstract String decodificarBase64(String contenido) throws Exception;
	public abstract String hashMapAXml(HashMap<String,Object> map,String nombre);
	public abstract void impLog(Logger logg,int logLevel,HashMap<String,Object> datosConf,String mensaje,Throwable t);
	public abstract void impLog(Logger logg,int logLevel,HashMap<String,Object> datosConf,String mensaje);
}

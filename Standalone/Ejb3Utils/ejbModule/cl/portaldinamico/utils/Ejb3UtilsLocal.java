package cl.portaldinamico.utils;
import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface Ejb3UtilsLocal 
{
	public String obtenerParametroString(HashMap<String,Object> Parametros,String llave);
	public String[] obtenerParametroArregloString(HashMap<String,Object> Parametros,String llave);
	public String encrypt(String value, String key) throws Exception;
	public String decrypt(String key, String encrypted) throws Exception;
	public String decodificarHexa(String contenido) throws Exception;
}

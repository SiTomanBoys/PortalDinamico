package cl.portaldinamico.utils;

import java.util.HashMap;

import javax.ejb.Stateless;
@Stateless(name="Ejb3Utils")
public class Ejb3Utils implements Ejb3UtilsLocal,Ejb3UtilsRemote
{
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
}

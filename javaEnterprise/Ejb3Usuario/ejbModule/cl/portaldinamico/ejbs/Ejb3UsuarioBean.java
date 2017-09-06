package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

@Stateless(name="Ejb3UsuarioBean")
public class Ejb3UsuarioBean implements Ejb3UsuarioBeanLocal,Ejb3UsuarioBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3UsuarioBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	public HashMap<String, Object> lstUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML ="";
		retorno.put("XML", XML);
		return retorno;
	}
	
	public HashMap<String, Object> addUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML ="";
		retorno.put("XML", XML);
		return retorno;
	}
	
	public HashMap<String, Object> updUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML ="";
		retorno.put("XML", XML);
		return retorno;
	}
}

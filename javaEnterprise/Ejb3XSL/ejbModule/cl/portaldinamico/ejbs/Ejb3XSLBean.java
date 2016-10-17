package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;
@Stateless(name="Ejb3MenuBean")
public class Ejb3XSLBean implements Ejb3XSLBeanLocal,Ejb3XSLBeanRemote
{
	public HashMap<String,Object> lstXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
	public HashMap<String,Object> addXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
	public HashMap<String,Object> updXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
	public HashMap<String,Object> delXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
}

package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Remote;
@Remote
public interface Ejb3XSLBeanRemote 
{
	public HashMap<String,Object> lstXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> addXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> updXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> delXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Local;
@Local
public interface Ejb3XSLBeanLocal
{
	public HashMap<String,Object> lstXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> addXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> updXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> delXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

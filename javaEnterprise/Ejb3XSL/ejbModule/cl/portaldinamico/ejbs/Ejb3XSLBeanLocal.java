package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Local;
@Local
public interface Ejb3XSLBeanLocal
{
	public abstract HashMap<String,Object> lstXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public abstract HashMap<String,Object> addXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public abstract HashMap<String,Object> updXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public abstract HashMap<String,Object> delXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

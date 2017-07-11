package cl.portaldinamico.ejbs;

import java.util.HashMap;
import javax.ejb.Remote;
@Remote
public interface Ejb3UrlBeanRemote 
{
	public abstract HashMap<String,Object> lstUrl(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public abstract HashMap<String,Object> addUrl(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public abstract HashMap<String,Object> updUrl(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

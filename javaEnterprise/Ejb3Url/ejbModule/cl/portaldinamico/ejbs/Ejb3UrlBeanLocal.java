package cl.portaldinamico.ejbs;

import java.util.HashMap;
import javax.ejb.Local;
@Local
public interface Ejb3UrlBeanLocal {
	public HashMap<String,Object> lstUrl(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> addUrl(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> updUrl(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

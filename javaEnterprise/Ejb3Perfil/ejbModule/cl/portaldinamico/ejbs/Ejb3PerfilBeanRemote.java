package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Remote;

@Remote
public interface Ejb3PerfilBeanRemote {
	public abstract HashMap<String, Object> lstPerfil(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
	public abstract HashMap<String, Object> addPerfil(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
	public abstract HashMap<String, Object> updPerfil(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
}

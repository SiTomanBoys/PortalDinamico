package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Remote;

@Remote
public interface Ejb3UsuarioBeanRemote {
	public abstract HashMap<String, Object> lstUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
	public abstract HashMap<String, Object> addUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
	public abstract HashMap<String, Object> updUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
}

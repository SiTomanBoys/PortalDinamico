package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface Ejb3UsuarioBeanLocal {
	public abstract HashMap<String, Object> lstUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
	public abstract HashMap<String, Object> addUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
	public abstract HashMap<String, Object> updUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
}

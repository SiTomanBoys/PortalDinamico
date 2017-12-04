package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Local;

@Local
public interface Ejb3AlbumesBeanLocal {
	public HashMap<String, Object> lstAlbumes(HashMap<String, Object> datosConf, HashMap<String, Object> parametros);
}

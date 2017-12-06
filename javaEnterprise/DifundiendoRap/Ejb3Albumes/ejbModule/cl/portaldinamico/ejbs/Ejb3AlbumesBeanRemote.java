package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Remote;

@Remote
public interface Ejb3AlbumesBeanRemote 
{
	public abstract HashMap<String,Object> lstAlbumes(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

package cl.portaldinamico.ejbs;
import java.util.HashMap;

import javax.ejb.Remote;

@Remote 
public interface Ejb3PruebaBeanRemote {
	public abstract HashMap<String,Object> prueba(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

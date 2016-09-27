package cl.portaldinamico.ejbs;
import java.util.HashMap;

import javax.ejb.Local;
@Local
public interface Ejb3PruebaBeanLocal {
	public abstract HashMap<String,Object> prueba(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);

}

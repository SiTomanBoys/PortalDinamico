package cl.portaldinamico.ejbs;
import java.util.HashMap;
import javax.ejb.Local;
@Local
public interface Ejb3MenuBeanLocal 
{
	public HashMap<String,Object> lstMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> addMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> updMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

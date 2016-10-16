package cl.portaldinamico.ejbs;
import java.util.HashMap;
import javax.ejb.Remote;
@Remote
public interface Ejb3MenuBeanRemote 
{
	public HashMap<String,Object> lstMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> addMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> updMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
	public HashMap<String,Object> delMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros);
}

package cl.portaldinamico.ejbs;
import java.util.HashMap;
//
import javax.ejb.Stateless;
import org.apache.log4j.Logger;
//
import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
//
//
@Stateless(name="Ejb3MenuBean")
public class Ejb3MenuBean implements Ejb3MenuBeanLocal,Ejb3MenuBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3MenuBean.class);
	//
	//
	public HashMap<String,Object> lstMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		p.put("id_menu", parametros.get("id_menu").toString());
		p.put("nombre", parametros.get("nombre").toString());
		String listaMenu = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		listaMenu.replace("<Data", "<listaMenu").replace("</Data", "</listaMenu");
		String XML= listaMenu;
		retorno.put("XML", XML);
		return retorno;
	}
	//
	public HashMap<String,Object> addMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
	//
	public HashMap<String,Object> updMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
	//
	public HashMap<String,Object> delMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
}


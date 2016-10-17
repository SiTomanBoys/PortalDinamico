package cl.portaldinamico.ejbs;
import java.util.HashMap;
//
import javax.ejb.Stateless;
import org.apache.log4j.Logger;
//
import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;
//
//
@Stateless(name="Ejb3MenuBean")
public class Ejb3MenuBean implements Ejb3MenuBeanLocal,Ejb3MenuBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3MenuBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	//
	//
	public HashMap<String,Object> lstMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String listaMenu="";
		if("buscar".equalsIgnoreCase(accion))
		{
			String idMenu = utils.obtenerParametroString(parametros,"id_menu");
			String nombre = utils.obtenerParametroString(parametros,"nombre");
			p.put("id_menu", ( "".equals(idMenu) ) ? null : idMenu );
			p.put("nombre",  ( "".equals(nombre) ) ? null : nombre );
			listaMenu = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
			listaMenu = listaMenu.replaceAll("<Data", "<listaMenu").replaceAll("</Data>", "</listaMenu>");
		}
		
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


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
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		//Eliminar Menu
		String xmlEliminar="";
		if("eliminar".equalsIgnoreCase(accion))
		{
			String idMenu = utils.obtenerParametroString(parametros,"del_id_menu");
			p.put("id_menu", idMenu);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.delMenu", p, "estado");
			if("0".equals(resultado))
				xmlEliminar+="<delMenu><respuesta><codigo>0</codigo><mensaje>Opcion Eliminada Del Menu</mensaje></respuesta></delMenu>";
			else
				xmlEliminar+="<delMenu><respuesta><codigo>1</codigo><mensaje>Error Eliminar Opcion Del Menu</mensaje></respuesta></delMenu>";
			p.clear();
		}	
		String listaMenu="";
		//Buscar Menu
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
		XML += xmlEliminar;
		retorno.put("XML", XML);
		return retorno;
	}
	//
	public HashMap<String,Object> addMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String listaUrl = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.listarUrl", p);
		listaUrl = listaUrl.replaceAll("<Data", "<listaUrl").replaceAll("</Data>", "</listaUrl>");
		String xmlAgregar="";
		if("agregar".equalsIgnoreCase(accion))
		{
			String nombre = utils.obtenerParametroString(parametros,"nombre");
			String idPadre = utils.obtenerParametroString(parametros,"id_menu");
			String idUrl = utils.obtenerParametroString(parametros,"id_url");
			p.clear();
			p.put("idPadre", ("".equals(idPadre))? 0 : Long.parseLong(idPadre));
			p.put("nombre",nombre);
			p.put("id_url",("".equals(idUrl))? 0 : Long.parseLong(idUrl));
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.addMenu", p, "estado");
			if("0".equals(resultado))
				xmlAgregar+="<addMenu><respuesta><codigo>0</codigo><mensaje>Opcion Agregada Al Menu</mensaje></respuesta></addMenu>";
			else
				xmlAgregar+="<addMenu><respuesta><codigo>1</codigo><mensaje>Error Al Agregar Opcion al Menu</mensaje></respuesta></addMenu>";
			p.clear();
		}
		String listaMenu = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		listaMenu = listaMenu.replaceAll("<Data", "<listaMenu").replaceAll("</Data>", "</listaMenu>");
		//
		String XML= listaMenu + listaUrl;
		XML += xmlAgregar;
		retorno.put("XML", XML);
		return retorno;
	}
	//
	public HashMap<String,Object> updMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String updIdMenu = utils.obtenerParametroString(parametros, "upd_id_menu");
		String listaUrl = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.listarUrl", p);
		listaUrl = listaUrl.replaceAll("<Data", "<listaUrl").replaceAll("</Data>", "</listaUrl>");
		p.put("id_menu", updIdMenu);
		HashMap<String,Object> menu = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		String xmlGetMenu = utils.hashMapAXml(menu, "menu");
		p.clear();
		String xmlModificar="";
		if("modificar".equalsIgnoreCase(accion))
		{
			String nombre = utils.obtenerParametroString(parametros,"nombre");
			String idPadre = utils.obtenerParametroString(parametros,"id_padre");
			String idUrl = utils.obtenerParametroString(parametros,"id_url");
			p.clear();
			p.put("id_menu",updIdMenu);
			p.put("idPadre", ("".equals(idPadre))? 0 : Long.parseLong(idPadre));
			p.put("nombre",nombre);
			p.put("id_url",("".equals(idUrl))? 0 : Long.parseLong(idUrl));
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.updMenu", p,"estado");
			if("0".equals(resultado))
				xmlModificar+="<updMenu><respuesta><codigo>0</codigo><mensaje>Menu Modificado</mensaje></respuesta></updMenu>";
			else
				xmlModificar+="<updMenu><respuesta><codigo>1</codigo><mensaje>Error Al Modificar Menu</mensaje></respuesta></updMenu>";
			p.clear();
		}
		String listaMenu = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		listaMenu = listaMenu.replaceAll("<Data", "<listaMenu").replaceAll("</Data>", "</listaMenu>");
		//
		String XML= listaMenu + listaUrl;
		XML += xmlModificar;
		XML +=xmlGetMenu;
		retorno.put("XML", XML);
		return retorno;
	}
}


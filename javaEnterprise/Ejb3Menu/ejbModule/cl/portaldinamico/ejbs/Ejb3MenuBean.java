package cl.portaldinamico.ejbs;
import java.util.HashMap;
import java.util.List;

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
			List<HashMap<String,Object>> resultado = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.delMenu", p);
			if("0".equals(resultado.get(0).get("estado").toString()))
				xmlEliminar+="<delMenu><respuesta><codigo>0</codigo><mensaje>Opcion Eliminada Del Menu</mensaje></respuesta></delMenu>";
			else
				xmlEliminar+="<delMenu><respuesta><codigo>1</codigo><mensaje>Error Eliminar Opcion Del Menu</mensaje></respuesta></delMenu>";
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
		String xmlAgregar="";
		if("agregar".equalsIgnoreCase(accion))
		{
			String nombre = utils.obtenerParametroString(parametros,"nombre");
			String idPadre = utils.obtenerParametroString(parametros,"id_menu");
			p.clear();
			p.put("idPadre", ("".equals(idPadre))? 0 : Integer.parseInt(idPadre));
			p.put("nombre",nombre);
			List<HashMap<String,Object>> resultado = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.addMenu", p);
			if("0".equals(resultado.get(0).get("estado").toString()))
				xmlAgregar+="<addMenu><respuesta><codigo>0</codigo><mensaje>Opcion Agregada Al Menu</mensaje></respuesta></addMenu>";
			else
				xmlAgregar+="<addMenu><respuesta><codigo>1</codigo><mensaje>Error Al Agregar Opcion al Menu</mensaje></respuesta></addMenu>";
		}
		String listaMenu = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		listaMenu = listaMenu.replaceAll("<Data", "<listaMenu").replaceAll("</Data>", "</listaMenu>");
		//
		String XML= listaMenu;
		XML += xmlAgregar;
		retorno.put("XML", XML);
		return retorno;
	}
	//
	public HashMap<String,Object> updMenu(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
}


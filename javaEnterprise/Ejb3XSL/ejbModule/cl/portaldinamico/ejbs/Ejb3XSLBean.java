package cl.portaldinamico.ejbs;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;
@Stateless(name="Ejb3XSLBean")
public class Ejb3XSLBean implements Ejb3XSLBeanLocal,Ejb3XSLBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3XSLBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	public HashMap<String,Object> lstXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String listaXSL="";
		if("buscar".equalsIgnoreCase(accion))
		{
			ConsultaMyBatis ex = new ConsultaMyBatis();
			String idXSL = utils.obtenerParametroString(parametros,"id_xsl");
			String url = utils.obtenerParametroString(parametros,"url");
			String idIdioma = utils.obtenerParametroString(parametros,"id_idioma");
			p.put("id_xsl", ( "".equals(idXSL) ) ? null : idXSL );
			p.put("url",  ( "".equals(url) ) ? null : url );
			p.put("id_idioma",  ( "".equals(idIdioma) ) ? null : idIdioma );
			p.put("verCont","0");
			listaXSL = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreXSLMapper.xml", "coreXSL.getXSL", p);
			listaXSL = listaXSL.replaceAll("<Data", "<listaXSL").replaceAll("</Data>", "</listaXSL>");
		}
		String XML= listaXSL;
		retorno.put("XML", XML);
		return retorno;
	}
	public HashMap<String,Object> addXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
	public HashMap<String,Object> updXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String idXSL = utils.obtenerParametroString(parametros,"id_xsl");
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		log.info("ID XSL PARA MODIFICAR "+idXSL);
		p.put("id_xsl", idXSL );
		List<HashMap<String,Object>> listaXSL = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreXSLMapper.xml", "coreXSL.getXSL", p);
		if("modificar".equalsIgnoreCase(accion))
		{
			
		}
		String XML="<pagXSL>";
		XML+="<idXSL>"+listaXSL.get(0).get("id_xsl").toString()+"</idXSL>";
		XML+="<nombreEjb>"+listaXSL.get(0).get("nombre_ejb").toString()+"</nombreEjb>";
		XML+="<contenido>"+listaXSL.get(0).get("contenido").toString()+"</contenido>";
		XML+="</pagXSL>";
		retorno.put("XML",XML);
		return retorno;
	}
	public HashMap<String,Object> delXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
}

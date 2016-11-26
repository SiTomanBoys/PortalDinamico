package cl.portaldinamico.ejbs;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.mybatis.utils.MyBatisUtils;
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
			MyBatisUtils utilsMB= new MyBatisUtils();
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
		String xmlModificar="";
		String accion = utils.obtenerParametroString(parametros,"accion");
		String idXSL = utils.obtenerParametroString(parametros,"id_xsl");
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		if("modificar".equalsIgnoreCase(accion))
		{
			String contenido = utils.obtenerParametroString(parametros,"contenido");
			String nombre_ejb = utils.obtenerParametroString(parametros,"metodo_ejb");
			String id_idioma = utils.obtenerParametroString(parametros, "id_idioma");
			try 
			{
				contenido = utils.decodificarBase64(contenido);
			} catch (Exception e) 
			{
				log.error("[updXSL] ERROR AL DECODIFICAR CONTENIDO XSL",e);
			}
			p.put("id_xsl", idXSL);
			p.put("contenido",contenido);
			p.put("nombre_ejb",nombre_ejb);
			p.put("id_idioma",("".equals(id_idioma)) ? null : id_idioma);
			List<HashMap<String,Object>> resultado = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreXSLMapper.xml", "coreXSL.updXSL", p);
			if("0".equals(resultado.get(0).get("estado").toString()))
				xmlModificar+="<updXSL><respuesta><codigo>0</codigo><mensaje>Pagina XSL Modificada Correctamente</mensaje></respuesta></updXSL>";
			else
				xmlModificar+="<updXSL><respuesta><codigo>1</codigo><mensaje>Error Al Modificar Pagina XSL</mensaje></respuesta></updXSL>";
		}
		p.clear();
		p.put("id_xsl", idXSL );
		List<HashMap<String,Object>> listaXSL = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreXSLMapper.xml", "coreXSL.getXSL", p);		
		HashMap<String,Object> xsl = listaXSL.get(0);
		String contenido =(xsl.containsKey("contenido")) ? xsl.get("contenido").toString() : "";
		try 
		{
			contenido = utils.codificarBase64(contenido);
		} catch (Exception e) 
		{
			log.error("[updXSL] ERROR AL CODIFICAR CONTENIDO XSL",e);
		}
		String XML="<pagXSL>";
		XML+="<idXSL>"+xsl.get("id_xsl").toString()+"</idXSL>";
		XML+="<nombreEjb>"+((xsl.containsKey("nombre_ejb")) ? xsl.get("nombre_ejb").toString() : "")+"</nombreEjb>";
		XML+="<contenido>"+contenido+"</contenido>";
		XML+="</pagXSL>";
		
		XML+=xmlModificar;
		retorno.put("XML",XML);
		return retorno;
	}
	public HashMap<String,Object> delXSL(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		return retorno;
	}
}

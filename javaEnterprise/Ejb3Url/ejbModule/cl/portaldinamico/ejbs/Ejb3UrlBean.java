package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

@Stateless(name="Ejb3UrlBean")
public class Ejb3UrlBean implements Ejb3UrlBeanLocal,Ejb3UrlBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3UrlBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	@Override
	public HashMap<String, Object> lstUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		HashMap<String,Object> p = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);	
		String accion = utils.obtenerParametroString(parametros,"accion");
		String xmlEliminar="";
		if("eliminar".equalsIgnoreCase(accion))
		{
			String idUrl = utils.obtenerParametroString(parametros,"del_id_url");
			p.put("id_url", idUrl);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.delUrl", p, "estado");
			if("0".equals(resultado))
				xmlEliminar+="<delUrl><respuesta><codigo>0</codigo><mensaje>Url Eliminada</mensaje></respuesta></delUrl>";
			else
				xmlEliminar+="<delUrl><respuesta><codigo>1</codigo><mensaje>Error Eliminar Url</mensaje></respuesta></delUrl>";
			p.clear();
		}	
		String listaXSL="";
		if("buscar".equalsIgnoreCase(accion))
		{
			String idUrl = utils.obtenerParametroString(parametros,"id_url");
			String url = utils.obtenerParametroString(parametros,"url");
			String regXpag = datosConf.get(Constants.regXpag).toString();
			String pagina = utils.obtenerParametroString(parametros,"pagina");
			p.put("id_url", ( "".equals(idUrl) ) ? null : idUrl );
			p.put("url",  ( "".equals(url) ) ? null : url );
			p.put("numReg", regXpag);
			p.put("pagina", ("".equals(pagina) ? null : pagina));
			listaXSL = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.listarUrl", p);
			listaXSL = listaXSL.replaceAll("<Data", "<listaUrl").replaceAll("</Data>", "</listaUrl>");
			listaXSL +="<TOTAL_REGISTROS>"+p.get("totReg")+"</TOTAL_REGISTROS>";
		}
		String XML= listaXSL;
		XML+= xmlEliminar;
		retorno.put("XML", XML);
		return retorno;
	}

	@Override
	public HashMap<String, Object> addUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String xmlAgregar="";
		if("agregar".equalsIgnoreCase(accion))
		{
			String nombre = utils.obtenerParametroString(parametros,"nombre_url");
			p.clear();
			p.put("nombre",nombre);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.addUrl", p, "estado");
			if("0".equals(resultado))
				xmlAgregar+="<addUrl><respuesta><codigo>0</codigo><mensaje>Url agregada correctamente</mensaje></respuesta></addUrl>";
			else
				xmlAgregar+="<addUrl><respuesta><codigo>1</codigo><mensaje>Error al agregar Url</mensaje></respuesta></addUrl>";
			p.clear();
		}
		String XML=xmlAgregar;
		retorno.put("XML", XML);
		return retorno;
	}

	@Override
	public HashMap<String, Object> updUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String updIdUrl = utils.obtenerParametroString(parametros, "upd_id_url");
		p.put("id_url", updIdUrl);
		HashMap<String,Object> url = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.listarUrl", p);
		String xmlGetUrl = utils.hashMapAXml(url, "url");
		p.clear();
		String xmlModificar="";
		if("modificar".equalsIgnoreCase(accion))
		{
			String nombre = utils.obtenerParametroString(parametros,"nombre_url");
			String id_url = utils.obtenerParametroString(parametros,"id_url");
			p.clear();
			p.put("id_url", id_url);
			p.put("nombre",nombre);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreUrlMapper.xml", "coreUrl.updUpd", p,"estado");
			if("0".equals(resultado))
				xmlModificar+="<updUrl><respuesta><codigo>0</codigo><mensaje>Url Modificada</mensaje></respuesta></updUrl>";
			else
				xmlModificar+="<updUrl><respuesta><codigo>1</codigo><mensaje>Error Al Modificar Url</mensaje></respuesta></updUrl>";
			p.clear();
		}
		String XML=xmlModificar;
		XML +=xmlGetUrl;
		retorno.put("XML", XML);
		return retorno;
	}

}

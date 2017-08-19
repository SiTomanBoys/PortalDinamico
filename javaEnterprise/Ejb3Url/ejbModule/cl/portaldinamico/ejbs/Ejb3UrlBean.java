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
		String accion = utils.obtenerParametroString(parametros,"accion");
		String listaXSL="";
		if("buscar".equalsIgnoreCase(accion))
		{
			String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
			ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);	
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
		String XML="<data></data>";
		retorno.put("XML", XML);
		return retorno;
	}

}

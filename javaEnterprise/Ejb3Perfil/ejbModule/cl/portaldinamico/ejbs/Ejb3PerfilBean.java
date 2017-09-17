package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

@Stateless(name="Ejb3PerfilBean")
public class Ejb3PerfilBean implements Ejb3PerfilBeanLocal,Ejb3PerfilBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3PerfilBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	public HashMap<String, Object> lstPerfil(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		HashMap<String,Object> p = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);	
		String accion = utils.obtenerParametroString(parametros,"accion");
		String xmlEliminar="";
		if("eliminar".equalsIgnoreCase(accion))
		{
			String idPerfil = utils.obtenerParametroString(parametros,"del_id_perfil");
			p.put("id_perfil", idPerfil);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.delPerfil", p, "estado");
			if("0".equals(resultado))
				xmlEliminar+="<delPerfil><respuesta><codigo>0</codigo><mensaje>Perfil eliminado</mensaje></respuesta></delPerfil>";
			else
				xmlEliminar+="<delPerfil><respuesta><codigo>1</codigo><mensaje>Error al eliminar Perfil</mensaje></respuesta></delPerfil>";
			p.clear();
		}	
		String listaPerfil="";
		if("buscar".equalsIgnoreCase(accion))
		{
			String idPerfil = utils.obtenerParametroString(parametros,"id_perfil");
			String perfil = utils.obtenerParametroString(parametros,"perfil");
			String descripcion = utils.obtenerParametroString(parametros,"descripcion");
			String regXpag = datosConf.get(Constants.regXpag).toString();
			String pagina = utils.obtenerParametroString(parametros,"pagina");
			p.put("id_perfil", ( "".equals(idPerfil) ) ? null : idPerfil );
			p.put("perfil",  ( "".equals(perfil) ) ? null : perfil );
			p.put("dsc_perfil",  ( "".equals(descripcion) ) ? null : descripcion );
			p.put("numReg", regXpag);
			p.put("pagina", ("".equals(pagina) ? null : pagina));
			listaPerfil = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.listarPerfil", p);
			listaPerfil = listaPerfil.replaceAll("<Data", "<listaPerfil").replaceAll("</Data>", "</listaPerfil>");
			listaPerfil +="<TOTAL_REGISTROS>"+p.get("totReg")+"</TOTAL_REGISTROS>";
		}
		String XML= listaPerfil;
		XML+= xmlEliminar;
		retorno.put("XML", XML);
		return retorno;
	}
	public HashMap<String, Object> addPerfil(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
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
			String perfil = utils.obtenerParametroString(parametros,"perfil");
			String descripcion = utils.obtenerParametroString(parametros,"descripcion");
			p.clear();
			p.put("dsc_perfil",descripcion);
			p.put("perfil",perfil);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.addPerfil", p, "estado");
			if("0".equals(resultado))
				xmlAgregar+="<addPerfil><respuesta><codigo>0</codigo><mensaje>Perfil agregado correctamente</mensaje></respuesta></addPerfil>";
			else
				xmlAgregar+="<addPerfil><respuesta><codigo>1</codigo><mensaje>Error al agregar Perfil</mensaje></respuesta></addPerfil>";
			p.clear();
		}
		String XML=xmlAgregar;
		retorno.put("XML", XML);
		return retorno;
	}
	public HashMap<String, Object> updPerfil(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String updIdPerfil = utils.obtenerParametroString(parametros, "upd_id_perfil");
		p.put("id_perfil", updIdPerfil);
		HashMap<String,Object> mapPerfil = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.listarPerfil", p);
		String xmlPerfil = utils.hashMapAXml(mapPerfil, "perfil");
		p.clear();
		String xmlModificar="";
		if("modificar".equalsIgnoreCase(accion))
		{
			String perfil = utils.obtenerParametroString(parametros,"perfil");
			String id_perfil = utils.obtenerParametroString(parametros,"id_perfil");
			String descripcion = utils.obtenerParametroString(parametros,"descripcion");
			p.clear();
			p.put("id_perfil", id_perfil);
			p.put("perfil",perfil);
			p.put("dsc_perfil",descripcion);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfilMapper.updPerfil", p,"estado");
			if("0".equals(resultado))
				xmlModificar+="<updPerfil><respuesta><codigo>0</codigo><mensaje>Perfil Modificado</mensaje></respuesta></updPerfil>";
			else
				xmlModificar+="<updPerfil><respuesta><codigo>1</codigo><mensaje>Error Al Modificar Perfil</mensaje></respuesta></updPerfil>";
			p.clear();
		}
		String XML=xmlModificar;
		XML +=xmlPerfil;
		retorno.put("XML", XML);
		return retorno;
	}
}

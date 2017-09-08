package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

@Stateless(name="Ejb3UsuarioBean")
public class Ejb3UsuarioBean implements Ejb3UsuarioBeanLocal,Ejb3UsuarioBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3UsuarioBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	public HashMap<String, Object> lstUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		HashMap<String,Object> p = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);	
		String accion = utils.obtenerParametroString(parametros,"accion");
		String lstPerfil = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.listarPerfil", p);
		lstPerfil = lstPerfil.replaceAll("<Data", "<listaPerfil").replaceAll("</Data>", "</listaPerfil>");
		String xmlEliminar="";
		if("eliminar".equalsIgnoreCase(accion))
		{
			String idUrl = utils.obtenerParametroString(parametros,"del_id_usuario");
			p.put("id_url", idUrl);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreUsuarioMapper.xml", "coreUsuario.delUsr", p, "estado");
			if("0".equals(resultado))
				xmlEliminar+="<delUsr><respuesta><codigo>0</codigo><mensaje>Usuario eliminado</mensaje></respuesta></delUsr>";
			else
				xmlEliminar+="<delUsr><respuesta><codigo>1</codigo><mensaje>Error al eliminar usuario</mensaje></respuesta></delUsr>";
			p.clear();
		}	
		String listaUsr="";
		if("buscar".equalsIgnoreCase(accion))
		{
			String idUsuario = utils.obtenerParametroString(parametros,"id_usuario");
			String nombre = utils.obtenerParametroString(parametros,"nombre");
			String perfil = utils.obtenerParametroString(parametros,"perfil");
			String regXpag = datosConf.get(Constants.regXpag).toString();
			String pagina = utils.obtenerParametroString(parametros,"pagina");
			p.put("id_usuario", ( "".equals(idUsuario) ) ? null : idUsuario );
			p.put("nombre",  ( "".equals(nombre) ) ? null : nombre );
			p.put("perfil",  ( "".equals(perfil) ) ? null : perfil );
			p.put("numReg", regXpag);
			p.put("pagina", ("".equals(pagina) ? null : pagina));
			listaUsr = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreUsuarioMapper.xml", "coreUsuario.listarUsuario", p);
			listaUsr = listaUsr.replaceAll("<Data", "<listaUsuario").replaceAll("</Data>", "</listaUsuario>");
			listaUsr +="<TOTAL_REGISTROS>"+p.get("totReg")+"</TOTAL_REGISTROS>";
		}
		String XML= lstPerfil+listaUsr+xmlEliminar;
		retorno.put("XML", XML);
		return retorno;
	}
	
	public HashMap<String, Object> addUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String lstPerfil = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.listarPerfil", p);
		lstPerfil = lstPerfil.replaceAll("<Data", "<listaPerfil").replaceAll("</Data>", "</listaPerfil>");
		String xmlAgregar="";
		if("agregar".equalsIgnoreCase(accion))
		{
			String nombre_usuario = utils.obtenerParametroString(parametros,"nombre_usuario");
			String contra_usuario = utils.obtenerParametroString(parametros,"contra_usuario");
			String perfil = utils.obtenerParametroString(parametros,"perfil");
			p.clear();
			p.put("nombre_usuario",nombre_usuario);
			p.put("contra_usuario",contra_usuario);
			p.put("perfil",perfil);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreUsuarioMapper.xml", "coreUsuario.addUsr", p, "estado");
			if("0".equals(resultado))
				xmlAgregar+="<addUsr><respuesta><codigo>0</codigo><mensaje>Usuario agregado correctamente</mensaje></respuesta></addUsr>";
			else
				xmlAgregar+="<addUsr><respuesta><codigo>1</codigo><mensaje>Error al agregar Usuario</mensaje></respuesta></addUsr>";
			p.clear();
		}
		String XML=xmlAgregar+lstPerfil;
		retorno.put("XML", XML);
		return retorno;
	}
	
	public HashMap<String, Object> updUsr(HashMap<String, Object> datosConf, HashMap<String, Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		String accion = utils.obtenerParametroString(parametros,"accion");
		String updIdUsuario = utils.obtenerParametroString(parametros, "upd_id_usuario");
		p.put("id_usuario", updIdUsuario);
		HashMap<String,Object> usuario = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "coreUsuarioMapper.xml", "coreUsuario.listarUsuario", p);
		String xmlUsuario = utils.hashMapAXml(usuario, "usuario");
		p.clear();
		String lstPerfil = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "corePerfilMapper.xml", "corePerfil.listarPerfil", p);
		lstPerfil = lstPerfil.replaceAll("<Data", "<listaPerfil").replaceAll("</Data>", "</listaPerfil>");
		String xmlModificar="";
		if("modificar".equalsIgnoreCase(accion))
		{
			String nombre_usuario = utils.obtenerParametroString(parametros,"nombre_usuario");
			String id_usuario = utils.obtenerParametroString(parametros,"id_usuario");
			String perfil = utils.obtenerParametroString(parametros,"perfil");
			p.clear();
			p.put("id_usuario", id_usuario);
			p.put("nombre_usuario",nombre_usuario);
			p.put("perfil",perfil);
			String resultado = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreUsuarioMapper.xml", "coreUsuario.updUsr", p,"estado");
			if("0".equals(resultado))
				xmlModificar+="<updUsr><respuesta><codigo>0</codigo><mensaje>Url Modificada</mensaje></respuesta></updUsr>";
			else
				xmlModificar+="<updUsr><respuesta><codigo>1</codigo><mensaje>Error Al Modificar Url</mensaje></respuesta></updUsr>";
			p.clear();
		}
		String XML=xmlModificar+lstPerfil;
		XML +=xmlUsuario;
		retorno.put("XML", XML);
		return retorno;
	}
}

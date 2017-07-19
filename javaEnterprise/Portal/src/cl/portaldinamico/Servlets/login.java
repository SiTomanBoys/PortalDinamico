package cl.portaldinamico.Servlets;
import java.io.File;
import java.io.FileInputStream;
//
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
//
import org.apache.log4j.Logger;

import cl.portaldinamico.Exception.PortalException;
import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;


public class login extends base 
{
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(login.class);
	private HashMap<String,Object> datosConf = new HashMap<String,Object>();
    public login() 
    {
        super();
    }
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Ejb3UtilsLocal utils = new Ejb3Utils();
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		String dominio = url.substring(url.indexOf("://")+3,url.length());
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		dominio = dominio.replace(uri, "");
		log.info("DOMINIO: "+dominio);
		Properties portalProperties = new Properties();
		Properties portalConf = new Properties();
		datosConf.clear();
		//Obtengo las propiedades generales del portal dinamico
		try
		{
			portalProperties.load(new FileInputStream(System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"portalConf"+File.separatorChar+"portal.properties"));
			if(!portalProperties.containsKey("apacheDir"))
				throw new PortalException("El parametro 'apacheDir' no existe en el archivo 'portal.properties'");
			if(!portalProperties.containsKey("carpetaConf"))
				throw new PortalException("El parametro 'carpetaConf' no existe en el archivo 'portal.properties'");
			if(!portalProperties.containsKey("nombreArchivo"))
				throw new PortalException("El parametro 'nombreArchivo' no existe en el archivo 'portal.properties'");
			String apacheDir = portalProperties.getProperty("apacheDir");
			String carpetaConf = portalProperties.getProperty("carpetaConf");
			String nombreArchivo = portalProperties.getProperty("nombreArchivo");
			log.info("DIRECTORIO WWW APACHE: "+ apacheDir);
			log.info("NOMBRE CARPETA DE CONFIGURACIONES POR PORTAL: "+carpetaConf);
			log.info("NOMBRE DEL ARCHIVO PROPERTIES: "+nombreArchivo);
			try
			{
				//Obtengo las configuraciones designadas en cada portal guardandolas en datosConf.
				portalConf.load(new FileInputStream(apacheDir+dominio+carpetaConf+nombreArchivo));
				for(Object key : portalConf.keySet())
				{
					datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
				}
				
			}catch(Exception e)
			{
				log.error("ERROR AL LEER LA CONFIGURACION DEL PORTAL",e);
			}
		}
		catch(Exception e)
		{
			log.error("ERROR AL LEER LAS PROPIEDADES DEL PORTAL",e);
		}	
		//Obtengo todos los parametros que provienen del request
		for(Object key : request.getParameterMap().keySet())
		{
			parametros.put(key.toString(), request.getParameter(key.toString()));
		}
		
		if(validaLogin(parametros))
		{
			HttpSession session= request.getSession(true);
			session.setAttribute("datosConf", datosConf);
			response.sendRedirect("frameset");
		}
		else
		{
			utils.impLog(log, Level.INFO_INT, datosConf, "USUARIO O CONTRASEÑA INCORRECTA");
			response.sendRedirect("/Portal/error.jsp?Id=10");
		}
	}
	
	private boolean validaLogin(HashMap<String,Object> parametros)
	{
		Ejb3UtilsLocal utils = new Ejb3Utils();
		try
		{
			String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
			ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("usuario", parametros.get("txtUsuario"));
			p.put("contrasena", parametros.get("txtPassword"));
			HashMap<String,Object> usuario = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "coreUsuarioMapper.xml", "coreUsuario.validarUsuario", p);
			if(usuario!=null)
			{
				if("1".equals(usuario.get("estado").toString()))
				{
					datosConf.put("idUsuario",usuario.get("id_usuario").toString());
					datosConf.put("nombreUsuario",usuario.get("nombre_usuario").toString());
					datosConf.put("idPerfil",usuario.get("id_perfil").toString());
					datosConf.put("nombrePerfil",usuario.get("perfil").toString());
					datosConf.put("descripcionPerfil",usuario.get("descripcion").toString());
					return true;
				}
				else
					return false;
			}
			else 
				return false;
		}
		catch(Exception e)
		{
			utils.impLog(log, Level.ERROR_INT, datosConf, "[validaLogin] Error",e);
			return false;
		}
	}

}

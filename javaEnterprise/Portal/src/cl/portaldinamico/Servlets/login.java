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
	
    public login() 
    {
        super();
    }
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Ejb3UtilsLocal utils = new Ejb3Utils();
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		String dominio = request.getLocalName();
		Properties portalProperties = new Properties();
		log.info("DOMINIO: "+dominio);
		try
		{
			portalProperties.load(new FileInputStream(System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"portalConf"+File.separatorChar+"portal.properties"));
			if(!portalProperties.containsKey("apacheDir"))
				throw new PortalException("El parametro 'apacheDir' no existe en el archivo 'portal.properties'");
			if(!portalProperties.containsKey("carpetaConf"))
				throw new PortalException("El parametro 'carpetaConf' no existe en el archivo 'portal.properties'");
			if(!portalProperties.containsKey("nombreArchivo"))
				throw new PortalException("El parametro 'nombreArchivo' no existe en el archivo 'portal.properties'");
			if(!portalProperties.containsKey("carpetaXsl"))
				throw new PortalException("El parametro 'carpetaXsl' no existe en el archivo 'portal.properties'");
			String raizApache = portalProperties.getProperty("apacheDir");
			String carpetaConf = portalProperties.getProperty("carpetaConf");
			String carpetaXsl = portalProperties.getProperty("carpetaXsl");
			String nombreArchivoConf = portalProperties.getProperty("nombreArchivo");
			Properties portalConf = new Properties();
			datosConf.put("raizApache", raizApache);
			datosConf.put("carpetaConf", carpetaConf);
			datosConf.put("carpetaXsl", carpetaXsl);
			datosConf.put("nombreArchivoConf", nombreArchivoConf);
			try
			{
				//Obtengo las configuraciones designadas en cada portal guardandolas en datosConf.
				portalConf.load(new FileInputStream(raizApache+dominio+carpetaConf+nombreArchivoConf));
				for(Object key : portalConf.keySet())
				{
					datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
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
					utils.impLog(log, Level.INFO_INT, datosConf, "USUARIO O CONTRASEŅA INCORRECTA");
					response.sendRedirect("/Portal/error.jsp?Id=10");
				}
				
			}catch(Exception e)
			{
				log.error("ERROR AL LEER LA CONFIGURACION DEL PORTAL",e);
				response.sendRedirect("/Portal/error.jsp?Id=13");
			}
		}
		catch(Exception e)
		{
			log.error("ERROR AL LEER LAS PROPIEDADES DEL PORTAL",e);
			response.sendRedirect("/Portal/error.jsp?Id=14");
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

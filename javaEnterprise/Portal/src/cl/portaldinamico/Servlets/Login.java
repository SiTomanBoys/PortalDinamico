package cl.portaldinamico.Servlets;
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


public class Login extends Base 
{
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Login.class);
	
    public Login() 
    {
        super();
    }
    @SuppressWarnings("unchecked")
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Ejb3UtilsLocal utils = new Ejb3Utils();
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		String dominio = request.getLocalName();
		log.info("DOMINIO: "+dominio);
		try
		{
			HttpSession session= request.getSession(true);
			if(session.getAttribute("portalProp")== null)
				throw new PortalException("El parametro 'portalProp' no existe en session");
			HashMap<String,Object> portalProp = new HashMap<String,Object>();
			portalProp=(HashMap<String,Object>) session.getAttribute("portalProp");
			String raizApache = portalProp.get("raizApache").toString();
			String carpetaConf = portalProp.get("carpetaConf").toString();
			String nombreArchivoConf = portalProp.get("nombreArchivoConf").toString();
			Properties portalConf = new Properties();
			datosConf.putAll(portalProp);
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
					session.setAttribute("datosConf", datosConf);
					response.sendRedirect("frameset");
				}
				else
				{
					utils.impLog(log, Level.INFO_INT, datosConf, "USUARIO O CONTRASEÑA INCORRECTA");
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

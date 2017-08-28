package cl.portaldinamico.Servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
//
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
//
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

public class Modulo extends Base {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Modulo.class);
    public Modulo() 
    {
    }
	@SuppressWarnings("unchecked")
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Obtengo los datos de session
		HttpSession session= request.getSession(true);
		Ejb3UtilsLocal utils = new Ejb3Utils();
		if(!session.getId().equals(request.getParameter("idSession")))
		{
			log.error("ID DE SESSION EXPIRADA");
			response.sendRedirect("/Portal/error?Id=7");
		}
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		if(session.getAttribute("datosConf")!= null)
			datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
		else
		{
			utils.impLog(log, Level.ERROR_INT, datosConf, "NO SE ENCONTRARON LOS DATOS DE CONFIGURACION DEL PORTAL");
			response.sendRedirect("/Portal/error?Id=6");
		}
		//Obtengo el Catalogo y los Servidores del Portal.
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
//		String url = request.getAttribute("javax.servlet.forward.request_uri").toString();
		String url = request.getAttribute("javax.servlet.error.request_uri").toString();
		url = url.substring(7, url.length());
		utils.impLog(log, Level.INFO_INT, datosConf, "URL Recivida: "+url);
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
		HashMap<String,Object> p = new HashMap<String,Object>();
		p.put("url", url);
		p.put("id_idioma", "1");
		HashMap<String,Object> pagina = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "coreXSLMapper.xml", "coreXSL.getXSL", p);
		String XML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String nombreEjb="";
		String metodoEjb="";
		String XSL="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		//Obtengo los parametros
		HashMap<String,Object> Parametros = new HashMap<String,Object>();
		Parametros = (HashMap<String, Object>) request.getParameterMap();
		//Genero La Cabecera del XML
		if(pagina!=null)
		{
			try
			{
				XSL += pagina.get("contenido").toString();
				XSL = XSL.replace("[[raiz_xsl]]", datosConf.get("raizApache")+request.getLocalName()+datosConf.get("carpetaXsl"));
			}
			catch(Exception e)
			{
				utils.impLog(log, Level.ERROR_INT, datosConf, "ERROR AL DECODIFICAR EL CONTENIDO",e);
				response.sendRedirect("/Portal/error?Id=11");
			}
			String nomEjb = (pagina.containsKey("nombre_ejb")) ? pagina.get("nombre_ejb").toString() : "";
			String nombre_ejb [] = nomEjb.toString().split("\\."); 
			if(nombre_ejb.length>1)
			{
				nombreEjb=nombre_ejb[0];
				metodoEjb=nombre_ejb[1];
				Object[] parameters = new Object[] {datosConf,Parametros};
				Class<?>[] paramtypes = {HashMap.class,HashMap.class};
				Method mthdRemote = null;
				Object objRemote = null;
				Context ejbContext = null;	
				String ejbNameRemote = "";
				String ejbNameLocal = "";	
				try
				{
					ejbContext = new InitialContext();
					Object objRef = ejbContext.lookup(nombreEjb);
					Class<?> clazz = objRef.getClass();
					Method mthd = clazz.getMethod("create");
					objRemote = (Object) mthd.invoke(objRef,nombreEjb);
					utils.impLog(log, Level.DEBUG_INT, datosConf, "NOMBRE EJB  ["+nombreEjb+"]");
					utils.impLog(log, Level.INFO_INT, datosConf, "ENCONTRO EJB 2");
				}catch(Throwable ejb2)
				{
					try 
					{
						//Jboss 5
//						ejbNameRemote = nombreEjb+"Bean/remote";
						//Jboss 7
						ejbNameRemote = "java:global/"+nombreEjb+"/"+nombreEjb+"Bean!cl.portaldinamico.ejbs."+nombreEjb+"BeanRemote";
						objRemote = ejbContext.lookup(ejbNameRemote);
						utils.impLog(log, Level.INFO_INT, datosConf, "ENCONTRO EJB 3 REMOTO");
					} catch (Throwable f) 
					{
						try 
						{
							//Jboss 5
//							ejbNameLocal = nombreEjb + "Bean/local";
							//Jboss 7
							ejbNameLocal = "java:global/"+nombreEjb+"/"+nombreEjb+"Bean!cl.portaldinamico.ejbs."+nombreEjb+"BeanLocal";
							objRemote = ejbContext.lookup(ejbNameLocal);
							utils.impLog(log, Level.INFO_INT, datosConf, "ENCONTRO EJB 3 LOCAL");
						} catch (Throwable g) 
						{
							utils.impLog(log, Level.ERROR_INT, datosConf, "NO SE ENCONTRO EJB2, EJB3 REMOTE O EJB3 LOCAL");
							response.sendRedirect("/Portal/error?Id=5");
						}
					}
				}
				Class<?> clazzRemote = objRemote.getClass();
				Method[] mets = clazzRemote.getDeclaredMethods();
				boolean encontro = false;
				for (int i = 0; i < mets.length; i++) 
				{
		            Method method = mets[i];
		            if(method.getName().equals(metodoEjb))encontro=true;
		        }
				if(encontro)
				{
					try 
					{
						for(String key : datosConf.keySet())
						{
							utils.impLog(log, Level.DEBUG_INT, datosConf, "DATOSCONF: ["+key+"]["+datosConf.get(key).toString()+"]");
						}
						for(String key : Parametros.keySet())
						{
							utils.impLog(log, Level.DEBUG_INT, datosConf, "PARAMETROS: ["+key+"]"+Arrays.toString((String[]) Parametros.get(key)));
						}
						mthdRemote = clazzRemote.getMethod(metodoEjb, paramtypes);
						utils.impLog(log, Level.DEBUG_INT, datosConf, "EJECUTANDO EL METODO [" + mthdRemote.getName() + "] DEL OBJETO [" + objRemote.getClass().getName()+"]");
						utils.impLog(log, Level.INFO_INT, datosConf, "ANTES DEL INVOKE");
						HashMap<String,Object> Respuesta = (HashMap<String,Object>) mthdRemote.invoke(objRemote, parameters);
						XML+="<Documento>";
						XML+=ArmarCabeceraXML(Parametros,datosConf,pagina);
						XML+="<Cuerpo>";
						XML += Respuesta.get("XML");
						XML +="</Cuerpo>";
						XML +="</Documento>";
						utils.impLog(log, Level.INFO_INT, datosConf, "DESPUES DEL INVOKE");
						utils.impLog(log, Level.DEBUG_INT, datosConf, "XML OBTENIDO: "+XML);
						ejbContext.close();
						try
						{
						    PrintWriter out = response.getWriter();
				            String html = utils.generarDocumento(XML, XSL);
				            out.println(html);
						}
						catch(Exception e)
						{
							utils.impLog(log, Level.ERROR_INT, datosConf, "ERROR AL TRANSFORMAR XSL: ",e);
							response.sendRedirect("/Portal/error?Id=3");
						}
					} catch (Throwable e) 
					{
						utils.impLog(log, Level.ERROR_INT, datosConf, "ERROR AL LLAMAR EJB",e);
						response.sendRedirect("/Portal/error?Id=4");
					}
				}	
				else
				{	
					utils.impLog(log, Level.ERROR_INT, datosConf, "NO SE ENCONTRO EL METODO ["+metodoEjb+"]");
					response.sendRedirect("/Portal/error?Id=2");
				}
			}
			else
			{
				utils.impLog(log, Level.ERROR_INT, datosConf, "FALTA EL NOMBRE DEL EJB O EL METODO EN LA BASE DE DATOS: VALOR ACTUAL: ["+pagina.get("nombre_ejb")+"]");
				response.sendRedirect("/Portal/error?Id=1");
			}
		}
		else
		{
			utils.impLog(log, Level.ERROR_INT, datosConf, "LA URL NO EXISTE");
			response.sendRedirect("/Portal/error?Id=0");
		}
	}
	
	private String ArmarCabeceraXML(HashMap<String,Object> Parametros, HashMap<String,Object> datosConf,HashMap<String,Object> pagina)
	{
		String XML="<Cabecera>";
		XML+="<pagina>"+pagina.get("id_xsl")+"</pagina>";
		XML+="<Parametros>";
		for(String key : Parametros.keySet())
		{
			String[] value = (String[]) Parametros.get(key);
			if(value.length>1)
			{    
				XML+="<"+key+">";
				for (int i = 0; i < value.length; i++) 
					XML+="<valor>"+value[i].toString()+"</valor>";
				XML+="</"+key+">";
			}
			else
				XML+="<"+key+">"+value[0].toString()+"</"+key+">";
		}
		XML+="</Parametros>";
		XML+="<DatosConf>";
		for(String key : datosConf.keySet())
			XML+="<"+key+">"+datosConf.get(key).toString()+"</"+key+">";
		XML+="</DatosConf>";
		XML+="</Cabecera>";
		return XML;
	}
}

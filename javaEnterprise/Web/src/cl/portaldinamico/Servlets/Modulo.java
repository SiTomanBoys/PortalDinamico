package cl.portaldinamico.Servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

public class Modulo extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	RequestDispatcher rd = null; 
	static final Logger log = Logger.getLogger(Modulo.class);
    
    public Modulo() 
    {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String dominio = request.getLocalName();
		log.info("DOMINIO: "+dominio);
		Ejb3UtilsLocal utils = new Ejb3Utils();
		HttpSession session= request.getSession(true);
		Properties portalConf = new Properties();
		HashMap<String,Object> portalProp = new HashMap<String,Object>();
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		try
		{
			if(session.getAttribute("portalProp")!=null)
				portalProp = (HashMap<String,Object>) session.getAttribute("portalProp");
			else
			{
				portalProp = utils.cargarPropiedades();
				session.setAttribute("portalProp", portalProp);
			}
			try
			{
				//Obtengo las configuraciones designadas en cada portal guardandolas en datosConf.
				if(session.getAttribute("datosConf")!=null)
					datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
				else
				{
					portalConf.load(new FileInputStream(portalProp.get("raizApache")+dominio+portalProp.get("carpetaConf")+portalProp.get("nombreArchivoConf")));
					datosConf.clear();
					for(Object key : portalConf.keySet())
					{
						datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
					}
					datosConf.putAll(portalProp);
					session.setAttribute("datosConf", datosConf);
				}
			}catch(Exception e)
			{
				log.error("ERROR AL LEER LA CONFIGURACION DEL PORTAL",e);
				rd = request.getRequestDispatcher("error");
				request.setAttribute("codError", 13);
				rd.forward(request, response);
			}
			//Obtengo el Catalogo y los Servidores del Portal.
			String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
	//		String url = request.getAttribute("javax.servlet.forward.request_uri").toString();
			String url = request.getAttribute("javax.servlet.error.request_uri").toString();
			url = url.substring(4, url.length());
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
					rd = request.getRequestDispatcher("error");
					request.setAttribute("codError", 11);
					rd.forward(request, response);
					//response.sendRedirect("/Web/error?Id=11");
				}
				String nomEjb = (pagina.containsKey("nombre_ejb")) ? pagina.get("nombre_ejb").toString() : "";
				String nombre_ejb [] = nomEjb.toString().split("\\."); 
				if(nombre_ejb.length<2)
				{
					nombreEjb="";
					metodoEjb="";
					XML+="<Documento>";
					XML+=ArmarCabeceraXML(Parametros,datosConf,pagina);
					XML+="<Cuerpo>";
					XML +="</Cuerpo>";
					XML +="</Documento>";
				}
				else
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
								rd = request.getRequestDispatcher("error");
								request.setAttribute("codError", 5);
								rd.forward(request, response);
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
						} catch (Throwable e) 
						{
							utils.impLog(log, Level.ERROR_INT, datosConf, "ERROR AL LLAMAR EJB",e);
							rd = request.getRequestDispatcher("error");
							request.setAttribute("codError", 4);
							rd.forward(request, response);
						}
					}	
					else
					{	
						utils.impLog(log, Level.ERROR_INT, datosConf, "NO SE ENCONTRO EL METODO ["+metodoEjb+"]");
						rd = request.getRequestDispatcher("error");
						request.setAttribute("codError", 2);
						rd.forward(request, response);
					}
				}
				try
				{
				    PrintWriter out = response.getWriter();
		            String html = utils.generarDocumento(XML, XSL);
		            out.println(html);
				}
				catch(Exception e)
				{
					utils.impLog(log, Level.ERROR_INT, datosConf, "ERROR AL TRANSFORMAR XSL: ",e);
					rd = request.getRequestDispatcher("error");
					request.setAttribute("codError", 3);
					rd.forward(request, response);
				}
			}
			else
			{
				utils.impLog(log, Level.ERROR_INT, datosConf, "LA URL NO EXISTE");
				rd = request.getRequestDispatcher("error");
				request.setAttribute("codError", 0);
				rd.forward(request, response);
			}
		}
		catch(Exception e)
		{
			log.error("ERROR AL LEER LAS PROPIEDADES DEL PORTAL",e);
			rd = request.getRequestDispatcher("error");
			request.setAttribute("codError", 14);
			rd.forward(request, response);
		}
	
	}
	
	private String ArmarCabeceraXML(HashMap<String,Object> Parametros, HashMap<String,Object> datosConf,HashMap<String,Object> pagina)
	{
		Calendar fechaCalendar = Calendar.getInstance();
		Date f = fechaCalendar.getTime();
		String XML="<Cabecera>";
		XML+="<pagina>"+pagina.get("id_xsl")+"</pagina>";
		XML+="<fecha>"+new SimpleDateFormat("yyyyMMdd").format(f)+"</fecha>";
		XML+="<hora>"+new SimpleDateFormat("HHmmss").format(f)+"</hora>";
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

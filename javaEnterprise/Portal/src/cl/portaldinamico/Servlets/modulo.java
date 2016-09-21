package cl.portaldinamico.Servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
//
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.apache.log4j.Logger;
//
import cl.portaldinamico.mybatis.ConsultaMyBatis;
/**
 * Servlet implementation class modulo
 */
public class modulo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(modulo.class);
    /**
     * Default constructor. 
     */
    public modulo() {
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Logica Para BD	
		String url = request.getAttribute("javax.servlet.forward.request_uri").toString();
		url = url.substring(7, url.length());
		log.info("URL Recivida: "+url);
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		p.put("url", url);
		p.put("id_idioma", "1");
		List<HashMap<String,Object>> lista = ex.Select("local", "coreXSLMapper.xml", "coreXSL.getXSL", p);
		String XML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		String nombreEjb="";
		String metodoEjb="";
		String XSL="";
		//Obtengo los datos de session
		HttpSession session= request.getSession(true);
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		if(session.getAttribute("datosConf")!= null)
			datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
		//Obtengo los parametros
		HashMap<String,Object> Parametros = new HashMap<String,Object>();
		Parametros = (HashMap<String, Object>) request.getParameterMap();
		//Genero La Cabecera del XML
		XML+=ArmarCabeceraXML(Parametros,datosConf);
		XML+="<Cuerpo>";
		if(lista.size()>0)
		{
			XSL = lista.get(0).get("contenido").toString();
			String nombre_ejb [] = lista.get(0).get("nombre_ejb").toString().split("\\."); 
			if(nombre_ejb.length>1)
			{
				nombreEjb=nombre_ejb[0];
				metodoEjb=nombre_ejb[1];
				Object[] parameters = new Object[] {  };
				Class<?>[] paramtypes = {};
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
					log.info("NOMBRE EJB  ["+nombreEjb+"]");
					log.info("ENCONTRO EJB 2");
				}catch(Throwable ejb2)
				{
					try 
					{
						ejbNameRemote = nombreEjb+"Bean/remote";
						objRemote = ejbContext.lookup(ejbNameRemote);
						log.info("ENCONTRO EJB 3 REMOTO");

					} catch (Throwable f) 
					{
						try 
						{
							ejbNameLocal = nombreEjb + "Bean/local";
							objRemote = ejbContext.lookup(ejbNameLocal);
							log.info("ENCONTRO EJB 3 LOCAL");

						} catch (Throwable g) 
						{
							log.error("NO SE ENCONTRO EJB2, EJB3 REMOTE O EJB3 LOCAL");
							response.sendRedirect("error.jsp?Id=5");
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
						//log.info("METODO ["+metodoEjb+"]");
						//log.info("PARAMETROS ["+paramtypes+"]");
						mthdRemote = clazzRemote.getMethod(metodoEjb, paramtypes);
						log.debug("EJECUTANDO EL METODO [" + mthdRemote.getName() + "] DEL OBJETO [" + objRemote.getClass().getName()+"]");
						log.info("ANTES DEL INVOKE");
						XML +=(String) mthdRemote.invoke(objRemote, parameters);
						XML +="</Cuerpo>";
						XML +="</Documento>";
						log.info("DESPUES DEL INVOKE");
						log.info("XML OBTENIDO: "+XML);
						ejbContext.close();
					} catch (Throwable e) 
					{
						log.error("ERROR AL LLAMAR EJB",e);
						response.sendRedirect("error.jsp?Id=4");
					}
					
					try
					{
						PrintWriter out = response.getWriter();
						TransformerFactory tff = TransformerFactory.newInstance();
						Transformer tf = tff.newTransformer(new StreamSource(new StringReader(XSL)));
						StreamSource ss = new StreamSource(new StringReader(XML));
						StreamResult sr = new StreamResult(out);
						response.getWriter();
						tf.transform(ss,sr);
					}
					catch(Exception e)
					{
						log.error("ERROR: ",e);
						response.sendRedirect("error.jsp?Id=3");
					}
				}	
				else
				{	log.error("NO SE ENCONTRO EL METODO ["+metodoEjb+"]");
					response.sendRedirect("error.jsp?Id=2");
				}
			}
			else
			{
				log.info("FALTA EL NOMBRE DEL EJB O EL METODO EN LA BASE DE DATOS: VALOR ACTUAL: ["+lista.get(0).get("nombre_ejb")+"]");
				response.sendRedirect("error.jsp?Id=1");
			}
		}
		else
		{
			log.info("LA URL NO EXISTE");
			response.sendRedirect("error.jsp?Id=0");
		}
	}
	
	private String ArmarCabeceraXML(HashMap<String,Object> Parametros, HashMap<String,Object> datosConf)
	{
		String XML="<Cabecera>";
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
package cl.portaldinamico.Servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cl.portaldinamico.Exception.PortalException;
import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;
public class index extends base {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(index.class);
    public index() {
        super();
    }
    protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	Ejb3UtilsLocal utils = new Ejb3Utils();
    	String dominio = request.getLocalName();
		log.info("DOMINIO: "+dominio);
    	Properties portalProperties = new Properties();
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
			if(!portalProperties.containsKey("carpetaXsl"))
				throw new PortalException("El parametro 'carpetaXsl' no existe en el archivo 'portal.properties'");
			String raizApache = portalProperties.getProperty("apacheDir");
			String carpetaConf = portalProperties.getProperty("carpetaConf");
			String carpetaXsl = portalProperties.getProperty("carpetaXsl");
			String nombreArchivoConf = portalProperties.getProperty("nombreArchivo");
			log.info("DIRECTORIO RAIZ APACHE: "+ raizApache);
			log.info("NOMBRE DE PARPERTA DE XSL POR PORTAL: "+carpetaXsl);
			log.info("NOMBRE CARPETA DE CONFIGURACIONES POR PORTAL: "+carpetaConf);
			log.info("NOMBRE DEL ARCHIVO PROPERTIES: "+nombreArchivoConf);
			HashMap<String,Object> portalProp = new HashMap<String,Object>();
			portalProp.put("raizApache", raizApache);
			portalProp.put("carpetaXsl", carpetaXsl);
			portalProp.put("carpetaConf", carpetaConf);
			portalProp.put("nombreArchivoConf", nombreArchivoConf);
			HttpSession session= request.getSession(true);
			session.setAttribute("portalProp", portalProp);
			Properties portalConf = new Properties();		
			try
			{
				//Obtengo las configuraciones designadas en cada portal guardandolas en datosConf.
				portalConf.load(new FileInputStream(raizApache+dominio+carpetaConf+nombreArchivoConf));
				datosConf.clear();
				for(Object key : portalConf.keySet())
				{
					datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
				}
				
			}catch(Exception e)
			{
				log.error("ERROR AL LEER LA CONFIGURACION DEL PORTAL",e);
				response.sendRedirect("/Portal/error.jsp?Id=13");
			}
			try
			{
				String catalogo = datosConf.get(Constants.catalogoBase).toString();
				String servidores = datosConf.get(Constants.servidoresBase).toString();
				ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
				HashMap<String,Object> p = new HashMap<String,Object>();
				p.put("nombre", "index");
				p.put("id_idioma", 1);
				//Obtengo el XSL
				String XSL = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreXSLPrincipalMapper.xml", "coreXSLPrincipal.getXSL", p, "contenido");
		    	String XML = getXMLIndex(datosConf);
		    	PrintWriter out = response.getWriter();
	            String html = utils.generarDocumento(XML, XSL);
	            out.println(html);
			}
			catch(Exception ex)
			{
				utils.impLog(log,Level.ERROR_INT, datosConf, "ERROR AL GENERAR INDEX", ex);
				response.sendRedirect("/Portal/error.jsp?Id=12");
			}
			
    	}
		catch(Exception e)
		{
			log.error("ERROR AL LEER LAS PROPIEDADES DEL PORTAL",e);
			response.sendRedirect("/Portal/error.jsp?Id=14");
		}
    };
    
    private String getXMLIndex(HashMap<String,Object> datosConf)
	{
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="<Titulo>"+datosConf.get(Constants.TituloPortal)+"</Titulo>";
		XML+="</Documento>";
		return XML;
	}

}

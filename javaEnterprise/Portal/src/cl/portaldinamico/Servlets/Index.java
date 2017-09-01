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
import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;
public class Index extends Base {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Index.class);
    public Index() {
        super();
    }
    protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	Ejb3UtilsLocal utils = new Ejb3Utils();
    	String dominio = request.getLocalName();
		log.info("DOMINIO: "+dominio);
    	//Obtengo las propiedades generales del portal dinamico
		try
		{
			HttpSession session= request.getSession(true);
			Properties portalConf = new Properties();
			HashMap<String,Object> portalProp = new HashMap<String,Object>();
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
				portalConf.load(new FileInputStream(portalProp.get("raizApache")+dominio+portalProp.get("carpetaConf")+portalProp.get("nombreArchivoConf")));
				datosConf.clear();
				for(Object key : portalConf.keySet())
				{
					datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
				}
				
			}catch(Exception e)
			{
				log.error("ERROR AL LEER LA CONFIGURACION DEL PORTAL",e);
				rd = request.getRequestDispatcher("error");
				request.setAttribute("codError", 13);
				rd.forward(request, response);
				//response.sendRedirect("/Portal/error?Id=13");
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
				response.sendRedirect("/Portal/error?Id=12");
			}
			
    	}
		catch(Exception e)
		{
			log.error("ERROR AL LEER LAS PROPIEDADES DEL PORTAL",e);
			response.sendRedirect("/Portal/error?Id=14");
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

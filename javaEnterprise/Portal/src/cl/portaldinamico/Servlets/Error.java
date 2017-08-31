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

public class Error extends Base 
{
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Error.class);
    public Error() 
    {
        super();
    }
	@SuppressWarnings("unchecked")
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		Ejb3UtilsLocal utils = new Ejb3Utils();
		HttpSession session= request.getSession(true);
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		try
		{
			if(session.getAttribute("datosConf")!= null)
				datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
			else
			{
				String raizApache="";
				String carpetaConf="";
				String nombreArchivoConf="";
				Properties portalConf = new Properties();
				Properties portalProperties = new Properties();
				if(session.getAttribute("portalProp")!= null)
				{
					HashMap<String,Object> portalProp = new HashMap<String,Object>();
					portalProp=(HashMap<String,Object>) session.getAttribute("portalProp");
					raizApache = portalProp.get("raizApache").toString();
					carpetaConf = portalProp.get("carpetaConf").toString();
					nombreArchivoConf = portalProp.get("nombreArchivoConf").toString();
					datosConf.putAll(portalProp);
				}
				else
				{
					portalProperties.load(new FileInputStream(System.getProperty("jboss.home.dir")+File.separatorChar+"portalConf"+File.separatorChar+"portal.properties"));
					if(!portalProperties.containsKey("apacheDir"))
						throw new PortalException("El parametro 'apacheDir' no existe en el archivo 'portal.properties'");
					if(!portalProperties.containsKey("carpetaConf"))
						throw new PortalException("El parametro 'carpetaConf' no existe en el archivo 'portal.properties'");
					if(!portalProperties.containsKey("nombreArchivo"))
						throw new PortalException("El parametro 'nombreArchivo' no existe en el archivo 'portal.properties'");
					if(!portalProperties.containsKey("carpetaXsl"))
						throw new PortalException("El parametro 'carpetaXsl' no existe en el archivo 'portal.properties'");
					raizApache = portalProperties.getProperty("apacheDir");
					carpetaConf = portalProperties.getProperty("carpetaConf");
					nombreArchivoConf = portalProperties.getProperty("nombreArchivo");
				}
			}
			String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
			ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("nombre", "error");
			p.put("id_idioma", 1);
			//Obtengo el XSL
			String XSL = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreXSLPrincipalMapper.xml", "coreXSLPrincipal.getXSL", p, "contenido");
			String XML = getXMLError(datosConf,request.getParameter("Id"));
			PrintWriter out = response.getWriter();
            String html = utils.generarDocumento(XML, XSL);
            out.println(html);
		}catch(Exception ex)
		{
			
			utils.impLog(log,Level.ERROR_INT, datosConf, "ERROR AL GENERAR ERROR", ex);
			request.setAttribute("codError", 15);
			request.setAttribute("dscError", "Error inesperado, porfavor intente mas tarde");
			response.sendRedirect("/Portal/error.jsp");
		}
	}
	
	private String getXMLError(HashMap<String,Object> datosConf,String codError)
	{
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="<Titulo>"+datosConf.get(Constants.TituloPortal)+"</Titulo>";
		XML+="<codError>"+codError+"</codError>";
		XML+="<dscError>Error al crear pagina XSL</dscError>";
		XML+="</Documento>";
		return XML;
	}
}

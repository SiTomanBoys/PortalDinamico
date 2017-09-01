package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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

public class Logout extends Base 
{
	static final Logger log = Logger.getLogger(Logout.class);
	private static final long serialVersionUID = 1L;
      
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	HttpSession session = request.getSession();
    	Ejb3UtilsLocal utils = new Ejb3Utils();
    	HashMap<String,Object> datosConf = new HashMap<String,Object>();
		if(session.getAttribute("datosConf")!= null)
			datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
    	try
    	{
    		String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
			ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("nombre", "logout");
			p.put("id_idioma", 1);
			//Obtengo el XSL
    		session.invalidate();
        	response.setContentType("text/html");
    		response.setCharacterEncoding("iso-8859-1");
    		response.addHeader("Pragma", "No-cache");
    		response.addHeader("Cache-Control", "no-cache");
    		response.addDateHeader("Expires", 0);
    		String contextPath = response.encodeRedirectURL(request.getContextPath());
    		String XSL = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreXSLPrincipalMapper.xml", "coreXSLPrincipal.getXSL", p, "contenido");
			String XML = getXMLLogout(datosConf,contextPath);
			PrintWriter out = response.getWriter();
            String html = utils.generarDocumento(XML, XSL);
            out.println(html);
    	}catch(Exception ex)
    	{
    		utils.impLog(log,Level.ERROR_INT, datosConf, "ERROR AL GENERAR LOGOUT", ex);
    		rd = request.getRequestDispatcher("error");
			request.setAttribute("codError", 16);
			rd.forward(request, response);
			//response.sendRedirect("/Portal/error.jsp?Id=16");
    	}
	}
    private String getXMLLogout(HashMap<String,Object> datosConf,String context)
	{
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="<Titulo>"+datosConf.get(Constants.TituloPortal)+"</Titulo>";
		XML+="<URL>"+context+"</URL>";
		XML+="</Documento>";
		return XML;
	}

}

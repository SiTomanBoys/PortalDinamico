package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

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

/**
 * Servlet implementation class central
 */
public class Central extends Base {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Central.class);
    public Central() {
        super();
    }

    protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	Ejb3UtilsLocal utils = new Ejb3Utils();
		HttpSession session= request.getSession(true);
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		if(session.getAttribute("datosConf")!= null)
			datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
		try
		{
			String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
			ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("nombre", "central");
			p.put("id_idioma", 1);
			//Obtengo el XSL
			String XSL = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreXSLPrincipalMapper.xml", "coreXSLPrincipal.getXSL", p, "contenido");
			String XML = getXMLCentral(datosConf);
			PrintWriter out = response.getWriter();
            String html = utils.generarDocumento(XML, XSL);
            out.println(html);
		}catch(Exception ex)
		{
			utils.impLog(log,Level.ERROR_INT, datosConf, "ERROR AL GENERAR PIE DE PAGINA", ex);
			response.sendRedirect("/Portal/error?Id=18");
		}
	}

    private String getXMLCentral(HashMap<String,Object> datosConf)
	{
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="<Titulo>"+datosConf.get(Constants.TituloPortal)+"</Titulo>";
		XML+="</Documento>";
		return XML;
	}

}

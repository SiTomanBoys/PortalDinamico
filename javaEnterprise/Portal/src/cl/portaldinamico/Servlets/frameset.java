package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;

/**
 * Servlet implementation class frameset
 */
public class frameset extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(frameset.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public frameset() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try
		{
			
			HttpSession session= request.getSession(true);
			HashMap<String,Object> datosConf = new HashMap<String,Object>();
			if(session.getAttribute("datosConf")!= null)
				datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
			ConsultaMyBatis ex = new ConsultaMyBatis();
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("nombre", "frameset");
			p.put("id_idioma", 1);
			//Obtengo el XSL
			List<HashMap<String,Object>> lista = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreXSLPrincipalMapper.xml", "coreXSLPrincipal.getXSL", p);
			String XSL = (String) lista.get(0).get("contenido");
			String XML = getXMLFrame(datosConf);
			PrintWriter out = response.getWriter();
			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer(new StreamSource(new StringReader(XSL)));
			StreamSource ss = new StreamSource(new StringReader(XML));
			StreamResult sr = new StreamResult(out);
			response.getWriter();
			tf.transform(ss,sr);
		}catch(Exception ex)
		{
			log.error("Error Al Generar FrameSet", ex);
			response.sendRedirect("/Portal/error.jsp?Id=8");
		}
	}
	
	private String getXMLFrame(HashMap<String,Object> datosConf)
	{
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="<Titulo>"+datosConf.get(Constants.TituloPortal)+"</Titulo>";
		XML+="</Documento>";
		return XML;
	}

}

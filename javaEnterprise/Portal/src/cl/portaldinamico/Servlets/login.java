package cl.portaldinamico.Servlets;
import java.io.File;
import java.io.FileInputStream;
//
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//
import org.apache.log4j.Logger;

/**
 * Servlet implementation class login
 */
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(login.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public login() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub	
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		String dominio = url.substring(url.indexOf("://")+3,url.length());
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		dominio = dominio.replace(uri, "");
		log.info("DOMINIO: "+dominio);
		Properties portalProperties = new Properties();
		Properties portalConf = new Properties();
		//Obtengo las propiedades generales del portal dinamico
		try
		{
			portalProperties.load(new FileInputStream(System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"portalConf"+File.separatorChar+"portal.properties"));
			String apacheDir = portalProperties.getProperty("apacheDir");
			String carpetaConf = portalProperties.getProperty("carpetaConf");
			String nombreArchivo = portalProperties.getProperty("nombreArchivo");
			log.info("DIRECTORIO WWW APACHE: "+ apacheDir);
			log.info("NOMBRE CARPETA DE CONFIGURACIONES POR PORTAL: "+carpetaConf);
			log.info("NOMBRE DEL ARCHIVO PROPERTIES: "+nombreArchivo);
			try
			{
				//Obtengo las configuraciones designadas en cada portal guardandolas en datosConf.
				portalConf.load(new FileInputStream(apacheDir+dominio+carpetaConf+nombreArchivo));
				for(Object key : portalConf.keySet())
				{
					datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
				}
				
			}catch(Exception e)
			{
				log.error("ERROR AL LEER LA CONFIGURACION DEL PORTAL",e);
			}
		}
		catch(Exception e)
		{
			log.error("ERROR AL LEER LAS PROPIEDADES DEL PORTAL",e);
		}	
		//Obtengo todos los parametros que provienen del request
		for(Object key : request.getParameterMap().keySet())
		{
			parametros.put(key.toString(), request.getParameter(key.toString()));
		}
		
		HttpSession session= request.getSession(true);
		session.setAttribute("datosConf", datosConf);
		response.sendRedirect("frameset");
	}

}

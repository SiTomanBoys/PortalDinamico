package cl.portaldinamico.Servlets;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

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

public class Menu extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Menu.class);
	RequestDispatcher rd = null; 
    public Menu() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String dominio = request.getLocalName();
		log.info("DOMINIO: "+dominio);
		if(dominio.contains("www."))
		{
			dominio = dominio.replace("www.", "");
			log.info("DOMINIO SIN WWW: "+dominio);
		}
		Ejb3UtilsLocal utils = new Ejb3Utils();
		HttpSession session= request.getSession(true);
		Properties portalConf = new Properties();
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		HashMap<String,Object> portalProp = new HashMap<String,Object>();
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
			try
			{
				// TODO Auto-generated method stub
				String catalogo = datosConf.get(Constants.catalogoBase).toString();
				String servidores = datosConf.get(Constants.servidoresBase).toString();
				ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
				HashMap<String,Object> p = new HashMap<String,Object>();
				p.put("url", "/menu");
				p.put("id_idioma", "1");
				//Obtengo el XSL
				String XSL="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
				XSL += ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreXSLMapper.xml", "coreXSL.getXSL", p, "contenido");
				p.clear();
				//Listo las opciones del menu.
				List<HashMap<String,Object>> lista = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
				//
				String XML=generarMenu(lista,session.getId(),datosConf);
				PrintWriter out = response.getWriter();
	            String html = utils.generarDocumento(XML, XSL);
	            out.println(html);
			}
			catch(Exception ex)
			{
				utils.impLog(log, Level.ERROR_INT, datosConf, "ERROR AL GENERAR EL MENU", ex);
				rd = request.getRequestDispatcher("error");
				request.setAttribute("codError", 8);
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

	private String generarMenu(List<HashMap<String,Object>> lista,String idsession,HashMap<String,Object> datosConf) 
	{
		Ejb3UtilsLocal utils = new Ejb3Utils();
		utils.impLog(log, Level.INFO_INT, datosConf,"********GENERANDO MENU *******");
		String [] nombre = new String[lista.size()+1];
		String [] url = new String[lista.size()+1];
		long [] nivel = new long[lista.size()+1];
		long diferencia_nivel = 0;
		int i=0;
		for(HashMap<String,Object> reg : lista)
		{
			nombre[i]= (String) reg.get("nombre");
			url[i] = (String) reg.get("url");
			nivel[i] = (Long) reg.get("nivel");
			i++;
		}
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="<Menu>";
		for(int j=0; j < i; j++)
		{
			if(nivel[j + 1] > nivel[j])
			{
				XML+="";
				XML+="<li class='mabierto' id='menuopcion'><a href='javascript:expandir("+j+")'>" + nombre[j] + "</a></li>";
				XML+="<ul class='mcerrado' id='"+j+"' >";
			}
			if (nivel[j + 1] <= nivel[j])
			{
				if(url[j] != null)
				{
					if("/".equals(url[j].substring(0, 1)))
						XML+="<li class='mseleccion' id='menuenlace' ><a href='/Web"+url[j]+"' target=\"central\">" + nombre[j] + "</a></li>";
					else
						XML+="<li class='mseleccion' id='menuenlace' ><a href='"+url[j]+"' target=\"central\" >" + nombre[j] + "</a></li>";
					
				}
				else
				{
					XML+="<li class='mabierto' id='menuenlace'> <a> " + nombre[j] + "</a></li>";
				}
			} 
			
			if (nivel[j + 1] < nivel[j]) 
			{
				if (j != i ) {
					diferencia_nivel = nivel[j] - nivel[j + 1];
					for (int n = 0; n < diferencia_nivel; n++) 
					{
						XML+="</ul>";
					}
				}
			}
			
		}
		XML+="</Menu>";
		XML+="</Documento>";
		utils.impLog(log, Level.DEBUG_INT, datosConf,"XML MENU:::"+XML);
		return XML;
	}
}

package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;

/**
 * Servlet implementation class menu
 */
public class menu extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(menu.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public menu() {
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Obtengo los datos de session
		HttpSession session= request.getSession(true);
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		if(session.getAttribute("datosConf")!= null)
			datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
		// TODO Auto-generated method stub
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		List<HashMap<String,Object>> lista = ex.Select(datosConf.get(Constants.jndiBase).toString(), "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		String HTML="<!DOCTYPE html><html><head><meta charset=\"ISO-8859-1\"><title>Insert title here</title></head><body>";
		HTML+=generarMenu(lista);
		HTML+="</body></html>";
		PrintWriter out = response.getWriter();
		out.write(HTML);
	}
	private String generarMenu(List<HashMap<String,Object>> lista )
	{
		log.info("********GENERANDO MENU *******");
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
		String XML ="<ul id='menu'>";
		for(int j=0; j < i; j++)
		{
			if(nivel[j + 1] > nivel[j])
			{
				XML+="";
				XML+="<li><a href='javascript:expandir("+j+")'>" + nombre[j] + "</a></li>";
				XML+="<ul>";
			}
			if (nivel[j + 1] <= nivel[j])
			{
				if(url[j] != null)
				{
					if("\\/".equals(url[j].substring(0, 1)))
					
					XML+="<li><a href='"+url[j]+"'>" + nombre[j] + "</a></li>";
				}
				else
				{
					XML+="<li>" + nombre[j] + "</li>";
				}
			} 
			
			if (nivel[j + 1] < nivel[j]) 
			{
				if (j != i - 1) {
					diferencia_nivel = nivel[j] - nivel[j + 1];
					for (int n = 0; n < diferencia_nivel; n++) 
					{
						XML+="</ul>";
					}
				}
			}
			
		}
		XML+="</ul>";
		log.info("XML DEL MENU:::: "+XML);
		
		return XML;
	}
	
	
	
}

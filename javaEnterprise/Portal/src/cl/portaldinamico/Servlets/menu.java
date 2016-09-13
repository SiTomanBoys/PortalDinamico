package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		int ContadorSUB=0;
		// TODO Auto-generated method stub
		ConsultaMyBatis ex = new ConsultaMyBatis();
		HashMap<String,Object> p = new HashMap<String,Object>();
		p.put("id_padre", 0);
		p.put("id_raiz",0);
		List<HashMap<String,Object>> lista = ex.Select("local", "coreMenuMapper.xml", "coreMenu.listarMenu", p);
		List<HashMap<String,Object>> tmpLst;
		HashMap<String,Object> tmpHs;
		log.info("********GENERANDO MENU *******");
		if(lista.size()>0)
		{
			for(HashMap<String,Object> Raices : lista)
			{
				log.info("RAIZ :" +Raices);
				p.clear();
				p.put("id_raiz", Raices.get("id_menu"));
				tmpHs = ex.Select("local", "coreMenuMapper.xml", "coreMenu.obtenerNivelesRaiz", p).get(0);
				int numLvl = (tmpHs != null) ? Integer.parseInt(tmpHs.get("cantidad").toString()) : 0;
				//
				for(int i = 1 ; i<=numLvl ;i++)
				{
					p.clear();
					p.put("id_raiz",Raices.get("id_menu"));
					p.put("nivel", i);
					tmpLst = ex.Select("local", "coreMenuMapper.xml", "coreMenu.listarMenu", p);
					log.info(Raices.get("nombre")+" NIVEL "+i);
					for(HashMap<String,Object> opciones : tmpLst)
					{
						log.info(opciones.get("nombre"));
					}
				}
				
			}
		}
	}
	
	
	
}

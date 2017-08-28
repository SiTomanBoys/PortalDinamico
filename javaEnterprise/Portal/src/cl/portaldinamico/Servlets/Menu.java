package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

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

public class Menu extends Base 
{
	private static final long serialVersionUID = 1L;
	static final Logger log = Logger.getLogger(Menu.class);
    public Menu() 
    {
        super();
    }
	@SuppressWarnings("unchecked")
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//Obtengo los datos de session
		Ejb3UtilsLocal utils = new Ejb3Utils();
		HttpSession session= request.getSession(true);
		HashMap<String,Object> datosConf = new HashMap<String,Object>();
		if(session.getAttribute("datosConf")!= null)
			datosConf = (HashMap<String,Object>) session.getAttribute("datosConf");
		try
		{
			// TODO Auto-generated method stub
			String catalogo = datosConf.get(Constants.catalogoBase).toString();
			String servidores = datosConf.get(Constants.servidoresBase).toString();
			ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);
			HashMap<String,Object> p = new HashMap<String,Object>();
			p.put("nombre", "Menu");
			p.put("id_idioma", 1);
			//Obtengo el XSL
			String XSL = ex.SelectValor(datosConf.get(Constants.jndiBase).toString(), "coreXSLPrincipalMapper.xml", "coreXSLPrincipal.getXSL", p, "contenido");
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
			response.sendRedirect("/Portal/error?Id=8");
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
						XML+="<li class='mseleccion' id='menuenlace' ><a href='/Portal"+url[j]+"?idSession="+idsession+"' target=\"central\">" + nombre[j] + "</a></li>";
					else
						XML+="<li class='mseleccion' id='menuenlace' ><a href='"+url[j]+"?idSession="+idsession+"' target=\"central\" >" + nombre[j] + "</a></li>";
					
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

package cl.portaldinamico.Servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class base
 */
public class Base extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	RequestDispatcher rd = null;
	protected static HashMap<String,Object> datosConf = new HashMap<String,Object>();
    public Base() 
    {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		procesarPeticion(request,response);
	}
	
	protected void procesarPeticion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{};

}

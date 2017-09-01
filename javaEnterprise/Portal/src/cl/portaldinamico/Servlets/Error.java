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
				HashMap<String,Object> portalProp = new HashMap<String,Object>();
				if(session.getAttribute("portalProp")!= null)
					portalProp=(HashMap<String,Object>) session.getAttribute("portalProp");
				else
				{
					portalProp = utils.cargarPropiedades();
					session.setAttribute("portalProp", portalProp);
				}
				String dominio = request.getLocalName();
				datosConf.putAll(portalProp);
				Properties portalConf = new Properties();
				portalConf.load(new FileInputStream(portalProp.get("raizApache")+dominio+portalProp.get("carpetaConf")+portalProp.get("nombreArchivoConf")));
				for(Object key : portalConf.keySet())
				{
					datosConf.put(key.toString(), portalConf.getProperty(key.toString()));
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
			
			
			
			int codError = ("".equals(request.getAttribute("codError")) || request.getAttribute("codError") == null) ? -1 : Integer.parseInt(request.getAttribute("codError").toString());
			HashMap<String,String> param = new HashMap<String,String>();
			param.put("Titulo",datosConf.get(Constants.TituloPortal).toString());
			param.put("codError",String.valueOf(codError));
			param.put("dscError",getMensajeError(codError));
			String XML = getXMLError();
			PrintWriter out = response.getWriter();
            String html = utils.generarDocumento(XML, XSL, param);
            out.println(html);
		}catch(Exception ex)
		{
			
			utils.impLog(log,Level.ERROR_INT, datosConf, "ERROR AL GENERAR ERROR", ex);
			rd = request.getRequestDispatcher("error");
			request.setAttribute("codError", 15);
			rd.forward(request, response);
			//response.sendRedirect("/Portal/error");
		}
	}
	
	private String getXMLError()
	{
		String XML ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		XML+="<Documento>";
		XML+="</Documento>";
		return XML;
	}
	private String getMensajeError(int codError)
	{
		String dscError="";
		switch(codError)
		{
			case 0: dscError="La URL no existe";
			break;
			case 1: dscError="Falta el nombre del EJB o el metodo en la base de datos";
			break;
			case 2: dscError="No se encontro metodo";
			break;
			case 3: dscError="Error al transformar XSL";
			break;
			case 4: dscError="Error al llamar EJB";
			break;
			case 5: dscError="No se encontro EJB2, EJB3 Remote o EJB3 Local";
			break;
			case 6: dscError="La pagina solicitada no existe o no se ha iniciado ninguna sesion para ser visualizada";
			break;
			case 7: dscError="Id de session expirada";
			break;
			case 8: dscError="Error al generar Menu";
			break;
			case 9: dscError="Error ar generar Frameset";
			break;
			case 10: dscError="Usuario o contraseña incorrecta";
			break;
			case 11: dscError="Error al decodificar el contenido";
			break;
			case 12: dscError="Error al generar Index";
			break;
			case 13: dscError="Error al leer la configuracion del portal";
			break;
			case 14: dscError="Error al leer las propiedades del portal";
			break;
			case 15: dscError="Error al generar pagina de Error";
			break;
			case 16: dscError="Error al generar Logout";
			break;
			case 17: dscError="Error al generar cabecera";
			break;
			case 18: dscError="Error al generar pie de pagina";
			break;
			case 19: dscError="Error al generar pagina central";
			break;
			default: dscError="Error inesperado, porfavor intente mas tarde";
			break;
			
		}
		return dscError;
	}
}

package cl.portaldinamico.ejbs;
import java.util.HashMap;

import javax.ejb.Stateless;
//
//
@Stateless(name="Ejb3PruebaBean")
public class Ejb3PruebaBean implements Ejb3PruebaBeanLocal,Ejb3PruebaBeanRemote
{
	public HashMap<String,Object> prueba(HashMap<String,Object> datosConf,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML=""
				+"<company>"
				+"	<name>"+datosConf.get("TituloPortal")+"</name>"
				+"	<address1>DIR 1</address1>"
				+"	<address1>DIR 2</address1>"
				+"	<city>CIUDAD</city>"
				+"	<country>PAIS</country>"
				+"</company>";
		retorno.put("XML", XML);
		return retorno;
	}
}

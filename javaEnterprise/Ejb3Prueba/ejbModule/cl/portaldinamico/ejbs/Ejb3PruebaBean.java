package cl.portaldinamico.ejbs;
import javax.ejb.Stateless;
//
//
@Stateless(name="Ejb3PruebaBean")
public class Ejb3PruebaBean implements Ejb3PruebaBeanLocal,Ejb3PruebaBeanRemote
{
	public String prueba()
	{
		String XML=""
				+"<company>"
				+"	<name>NOMBRE</name>"
				+"	<address1>DIR 1</address1>"
				+"	<address1>DIR 2</address1>"
				+"	<city>CIUDAD</city>"
				+"	<country>PAIS</country>"
				+"</company>";
		return XML;
	}
}

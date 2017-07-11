package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

@Stateless(name="Ejb3UrlBean")
public class Ejb3UrlBean implements Ejb3UrlBeanLocal,Ejb3UrlBeanRemote
{

	@Override
	public HashMap<String, Object> lstUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML="<data></data>";
		retorno.put("XML", XML);
		return retorno;
	}

	@Override
	public HashMap<String, Object> addUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML="<data></data>";
		retorno.put("XML", XML);
		return retorno;
	}

	@Override
	public HashMap<String, Object> updUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		String XML="<data></data>";
		retorno.put("XML", XML);
		return retorno;
	}

}

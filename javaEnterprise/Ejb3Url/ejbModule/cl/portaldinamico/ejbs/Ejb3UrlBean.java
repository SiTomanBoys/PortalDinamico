package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

@Stateless(name="Ejb3UrlBean")
public class Ejb3UrlBean implements Ejb3UrlBeanLocal,Ejb3UrlBeanRemote
{

	@Override
	public HashMap<String, Object> lstUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> addUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, Object> updUrl(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		// TODO Auto-generated method stub
		return null;
	}

}

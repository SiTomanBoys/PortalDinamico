package cl.portaldinamico.ejbs;

import java.util.HashMap;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import cl.portaldinamico.constants.Constants;
import cl.portaldinamico.mybatis.ConsultaMyBatis;
import cl.portaldinamico.utils.Ejb3Utils;
import cl.portaldinamico.utils.Ejb3UtilsLocal;

@Stateless(name="Ejb3AlbumesBean")
public class Ejb3AlbumesBean implements Ejb3AlbumesBeanLocal,Ejb3AlbumesBeanRemote
{
	static final Logger log = Logger.getLogger(Ejb3AlbumesBean.class);
	Ejb3UtilsLocal utils = new Ejb3Utils();
	public HashMap<String, Object> lstAlbumes(HashMap<String, Object> datosConf, HashMap<String, Object> parametros) {
		HashMap<String,Object> retorno = new HashMap<String,Object>();
		HashMap<String,Object> p = new HashMap<String,Object>();
		String catalogo = datosConf.get(Constants.catalogoBase).toString();
		String servidores = datosConf.get(Constants.servidoresBase).toString();
		ConsultaMyBatis ex = new ConsultaMyBatis(servidores,catalogo);	
		String accion = utils.obtenerParametroString(parametros,"accion");
		String xmlEliminar="";
		String lista="";
		String xmlGetAlbum="";
		if("buscar".equalsIgnoreCase(accion))
		{
			String album = utils.obtenerParametroString(parametros,"album");
			String artista = utils.obtenerParametroString(parametros,"artista");
			String anho = utils.obtenerParametroString(parametros,"anho");
			String regXpag = datosConf.get(Constants.regXpag).toString();
			String pagina = utils.obtenerParametroString(parametros,"pagina");
			p.put("album", ( "".equals(album) ) ? null : album );
			p.put("artista",  ( "".equals(artista) ) ? null : artista );
			p.put("anho",  ( "".equals(anho) ) ? null : anho );
			p.put("numReg", regXpag);
			p.put("pagina", ("".equals(pagina) ? null : pagina));
			lista = ex.SelectXML(datosConf.get(Constants.jndiBase).toString(), "coreAlbumesMapper.xml", "coreAlbumes.listarAlbumes", p);
			lista = lista.replaceAll("<Data", "<listaAlbumes").replaceAll("</Data>", "</listaAlbumes>");
			lista +="<TOTAL_REGISTROS>"+p.get("totReg")+"</TOTAL_REGISTROS>";
		}
		if("ver".equalsIgnoreCase(accion))
		{
			String id_album = utils.obtenerParametroString(parametros,"id_album");
			p.put("idAlbum", ( "".equals(id_album) ) ? "0" : id_album );
			HashMap<String,Object> album = ex.SelectUno(datosConf.get(Constants.jndiBase).toString(), "coreAlbumesMapper.xml", "coreAlbumes.listarAlbumes", p);
			xmlGetAlbum = utils.hashMapAXml(album, "album");
		}
		String XML= lista;
		XML+= xmlEliminar;
		XML+= xmlGetAlbum;
		retorno.put("XML", XML);
		return retorno;
	}
}

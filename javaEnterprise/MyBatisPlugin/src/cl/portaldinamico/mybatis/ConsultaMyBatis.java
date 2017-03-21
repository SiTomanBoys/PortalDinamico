package cl.portaldinamico.mybatis;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import cl.portaldinamico.mybatis.utils.MyBatisUtils;

public class ConsultaMyBatis 
{
	static final Logger log = Logger.getLogger(ConsultaMyBatis.class);
	private static String MyBatisConfig;
	private static String dirServidores;
	private static String dirCatalogo;
	private static boolean habilitarLog;
	
	MyBatisUtils mbu = new MyBatisUtils();
	public ConsultaMyBatis(String Servidores,String catalogo)
	{
		Properties MyBatisProperties = new Properties();
		try
		{
			MyBatisProperties.load(new FileInputStream(System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"mybatis"+File.separatorChar+"mybatis.properties"));
			MyBatisConfig= mbu.archivoAString(MyBatisProperties.getProperty("MyBatisConfig"));
			habilitarLog = Boolean.parseBoolean(MyBatisProperties.getProperty("habilitarLog"));
			dirServidores = Servidores;
			dirCatalogo = catalogo;
		}catch(Exception ex)
		{
			log.error("Error al leer el archivo de propiedades hubicado en: "+ System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"mybatis"+File.separatorChar+"mybatis.properties",ex);
		}
	}
	
	
	public ConsultaMyBatis()
	{
		Properties MyBatisProperties = new Properties();
		try
		{
			MyBatisProperties.load(new FileInputStream(System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"mybatis"+File.separatorChar+"mybatis.properties"));
			MyBatisConfig= mbu.archivoAString(MyBatisProperties.getProperty("MyBatisConfig"));
			habilitarLog = Boolean.parseBoolean(MyBatisProperties.getProperty("habilitarLog"));
			dirServidores = MyBatisProperties.getProperty("ServidoresXml");
			dirCatalogo = MyBatisProperties.getProperty("CatalogoDir");
		}catch(Exception ex)
		{
			log.error("Error al leer el archivo de propiedades hubicado en: "+ System.getProperty("user.dir")+File.separatorChar+".."+File.separatorChar+"mybatis"+File.separatorChar+"mybatis.properties",ex);
		}
	}
	
	public List<HashMap<String,Object>> Select(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		List<HashMap<String,Object>> lista = mbu.ejecutarConsulta(Conexion,NameSpace,parametros);
		return lista;
	}
	
	public String SelectXML(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		List<HashMap<String,Object>> lista = mbu.ejecutarConsulta(Conexion,NameSpace,parametros);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		String XML = mbu.ListaHashMapAXML(lista);
		return XML;
	}
	
	public HashMap<String,Object> SelectUno(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		List<HashMap<String,Object>> lista = mbu.ejecutarConsulta(Conexion,NameSpace,parametros);
		HashMap<String,Object> retorno = null;
		if(lista.size()>0)
			retorno = lista.get(0);
		return retorno;
	}
	public String SelectValor(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros,String Columna)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		List<HashMap<String,Object>> lista = mbu.ejecutarConsulta(Conexion,NameSpace,parametros);
		String retorno = null;
		if(lista.size()>0)
		{
			if(lista.get(0).containsKey(Columna))
				retorno = lista.get(0).get(Columna).toString();
		}
		return retorno;
	}
	public void Insertar(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		mbu.ejecutarInsertar(Conexion, NameSpace, parametros);
	}
	
	public void Modificar(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		mbu.ejecutarModificar(Conexion, NameSpace, parametros);
	}
	
	
	public void Eliminar(String JNDI ,String Mapper,String NameSpace ,HashMap<String,Object> parametros)
	{
		HashMap<String,Object> DatosConexion = mbu.obtenerDatosDeConexion(dirServidores,JNDI);
		DatosConexion.put("mapper",dirCatalogo+Mapper);
		String Conexion = mbu.realizarConexion(MyBatisConfig, DatosConexion);
		String logBatis = "\n \t Se ejecutara la siguiente consulta: "+NameSpace;
		logBatis += "\n \t Parametros Enviados: "+parametros;
		if(habilitarLog)
			log.info(logBatis);
		mbu.ejecutarEliminar(Conexion, NameSpace, parametros);	
	}
	

}

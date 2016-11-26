package cl.portaldinamico.mybatis.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class MyBatisUtils 
{
	static final Logger log = Logger.getLogger(MyBatisUtils.class);
	private SqlSession session = null;
	
	public MyBatisUtils()
	{
		
	}
	
	
	public List<HashMap<String,Object>> ejecutarConsulta(String configMyBatis,String NameSpace,HashMap<String,Object> parametros)
	{
		List<HashMap<String,Object>> lista =null;
		SqlSession session = getSession(configMyBatis);

		if(session != null)
		{
			try
			{
				lista = session.selectList(NameSpace,parametros);
			}
			finally
			{
				session.close();
			}
		}else
		{
			log.error("[ejecutarConsulta] ERROR al crear la session para ejecutar la consulta: "+configMyBatis);
		}
		
		return lista;
	}
	
	public void ejecutarInsertar(String configMyBatis,String NameSpace,HashMap<String,Object> parametros)
	{
		
		SqlSession session = getSession(configMyBatis);
		if(session != null)
		{
			try
			{
				session.insert(NameSpace,parametros);
				session.commit();
			}
			finally
			{
				session.close();
			}
		}else
		{
			log.error("[ejecutarInsertar] ERROR al crear la session para insertar: "+configMyBatis);
		}
		
	}
	
	
	public void ejecutarModificar(String configMyBatis,String NameSpace,HashMap<String,Object> parametros)
	{
		
		SqlSession session = getSession(configMyBatis);
		if(session != null)
		{
			try
			{
				session.update(NameSpace,parametros);
				session.commit();
			}
			finally
			{
				session.close();
			}
		}else
		{
			log.error("[ejecutarModificar] ERROR al crear la session para modificar: "+configMyBatis);
		}
		
	}
	
	
	public void ejecutarEliminar(String configMyBatis,String NameSpace,HashMap<String,Object> parametros)
	{
		
		SqlSession session = getSession(configMyBatis);
		if(session != null)
		{
			try
			{
				session.delete(NameSpace,parametros);
				session.commit();
			}
			finally
			{
				session.close();
			}
		}else
		{
			
			log.error("[ejecutarEliminar] ERROR al crear la session para Eliminar "+configMyBatis);
		}
		
	}
	
	
	
	
	
	public String realizarConexion(String MyBatisConfig,HashMap<String,Object> parametros)
	{
		for ( String llave : parametros.keySet() )
		{
			MyBatisConfig=MyBatisConfig.replace("[["+llave+"]]", parametros.get(llave).toString());
		}
		return MyBatisConfig;
	}
	
	public String ListaHashMapAXML(List<HashMap<String,Object>> ListaHashMap)
	{
		String XML="";
		String Data="";
		int i=0;
		for(HashMap<String,Object> h: ListaHashMap)
		{
			Data+="<fila>";
			for ( String llave : h.keySet() )
			{
				Data+="<"+llave+">"+h.get(llave)+"</"+llave+">";
			}
			Data+="</fila>";
			i++;
		}
		XML="<Data cantidad='"+i+"'>"+Data+"</Data>";
		return XML;
	}
	
	
	public SqlSession getSession(String resource)
	{
		try
		{
			Reader reader = new StringReader(resource);
			SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
			session = sqlMapper.openSession();
			
		}catch(Exception e)
		{
			log.error("[getSession] ERROR al obtener la session, READER: "+resource,e);
		}
		
		return session;
	}
	
	
	
	
	
	//Funcion que genera la conexion del MyBatis
	public HashMap<String,Object> obtenerDatosDeConexion(String Servidores,String id) 
	{
		SAXBuilder builder = new SAXBuilder();
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		try
		{
			Document documento = (Document) builder.build(Servidores); 
			Element raiz = documento.getRootElement();
			
			List<Element> listaConexiones = raiz.getChildren("conexion");
			for(Element e : listaConexiones)
			{
				if(e.getChildTextTrim("id").equals(id))
				{
					parametros.put("id",e.getChildTextTrim("id"));
					parametros.put("driver",e.getChildTextTrim("driver"));
					parametros.put("url", e.getChildTextTrim("url"));
					parametros.put("username", e.getChildTextTrim("username"));
					parametros.put("password", e.getChildTextTrim("password"));
				}
				
			}
			
		
		}
		catch(Exception e)
		{
			log.error("[obtenerDatosDeConexion] Error al obtener datos de conexion",e);
		}
		
		return parametros;
	}
	
	
	//Funcion que lee los archivos y los convierte en String
	public String archivoAString( String ruta )
	{
        FileReader fr = null;
        BufferedReader br = null;
        String contenido = "";
        try
        {
            fr = new FileReader(ruta);
            br = new BufferedReader( fr );
            
            String linea;
            //Obtenemos el contenido del archivo linea por linea
            while( ( linea = br.readLine() ) != null )
            { 
                contenido += linea + "\n";
            }
 
        }catch( Exception e )
        {
        	log.error("[archivoAString] Error al leer Archivo");
        }
        finally
        {
            try{
                br.close();
            }catch( Exception ex )
            {
            	log.error("[archivoAString] Error al cerrar archivo",ex);
            }
        }
        return contenido;
    }
	
}

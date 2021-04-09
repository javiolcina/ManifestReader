package es.jaolve.manifestreader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Classe que permet llegir MANIFEST.MF amb el format Standard en el projectes de la UV
 * - MANIFEST.MF ha d'estar en format UTF8 per a que es vegent correctament els accents
 * - Soporta , al menys, a partir de la versió de Java J2SE 1.5
 * - soporta lectura de MANIFEST.MF de un Jar, WAR en Tomcat
 * 
 * 
 * @author jaolve
 *
 */
public class ManifestReader {

	private String manifestPath = "/META-INF/MANIFEST.MF";
	
	/**
	 * Constructor public
	 */
	public ManifestReader()
	{}
	
	/**
	 * Constructor que permet especificar la ruta interna i el fitxer manifest
	 * 
	 * @param manifestPath a especificar en el cas de que siga diferent a /META-INF/MANIFEST.MF
	 */
	public ManifestReader(String manifestPath)
	{
		this.manifestPath = manifestPath;
	}
	
	/**
	 * A partir d'un classe es torna el Manifest
	 * 
	 * @param clz Classe que es gastarà per a localitzar un MANIFEST.MF en el jar que la continga
	 * @return Manifest
	 * @throws ManifestAccesException en el cas de que no trobe el jar
	 */
	public Manifest getManifest(Class<?> clz) throws ManifestAccesException
	{
		String resource = "/" + clz.getName().replace(".", "/") + ".class";
		 
		String fullPath = clz.getResource(resource).toString();
		
		String archivePath = fullPath.substring(0, fullPath.length() - resource.length());
		    
		//Per a entorns d'execusió local
	    if (archivePath.endsWith("\\bin") || archivePath.endsWith("/bin") )
		{
	    	archivePath = archivePath.substring(0, archivePath.length() - "/bin".length());
		}
	    // Required for wars
	    if (archivePath.endsWith("\\WEB-INF\\classes") || archivePath.endsWith("/WEB-INF/classes"))  
	    {
	    	archivePath = archivePath.substring(0, archivePath.length() - "/WEB-INF/classes".length());  
	    }

	    try{
	    	InputStream input = new URL(archivePath + manifestPath).openStream();
	    	return new Manifest(input);
	    } catch (Exception e) {
	    	throw new ManifestAccesException("Loading MANIFEST for class " + clz + " failed!");
	    }
	}
	  
	/**
	 * A partir de la classe ManifestReader es torna el Manifest del propi Manifest Reader
	 * Aquest mètode no s'ha d'usar a no ser que es vullga traure la versió del propi Manifest Reader.
	 * 
	 *
	 * @return Manifest
	 * @throws ManifestAccesException en el cas de que no trobe el jar
	 */
	public Manifest getManifest() throws ManifestAccesException
	{
		return getManifest(ManifestReader.class);
	}	
	  
	/**
	 * Donat un jar torna el arxiu que conté
	 * 
	 * @param archiveJar arxiu jar sobre el que buscarà el MANIFEST.MF
	 * @return Manifest
	 * @throws IOException en el cas de que no trobe el jar
	 */
	public Manifest getManifestFromJar(String archiveJar) throws IOException
	{
		archiveJar= archiveJar.replace("jar:file:/", "");
		archiveJar= archiveJar.replace("!", "");
		Manifest manifest;
		JarFile jar = null;
		try 
		{
			jar = new JarFile(archiveJar);
			manifest = jar.getManifest();
			return manifest;
		} finally {
			  if (jar != null)
				  jar.close();
		}
	  }
	
	/**
	 * Torna un inputStream per a que fitxer puga ser llegit
	 * 
	 * @return InputStream al fitxer
	 */
	public InputStream accessFile() {
		String resource = manifestPath;
	
	    // this is the path within the jar file
	    InputStream input = ManifestReader.class.getResourceAsStream("/resources/" + resource);
	    if (input == null) {
	        // this is how we load file within editor (eg eclipse)
	        input = ManifestReader.class.getClassLoader().getResourceAsStream(resource);
	    }
	    if (input == null) {
	        input = ManifestReader.class.getClassLoader().getResourceAsStream("."+resource);
	    }
	    if (input == null) {
	        input = ManifestReader.class.getClassLoader().getResourceAsStream("MANIFEST.MF");
	    }
	    if (input == null) {
	    	try {
	    		input = new FileInputStream( "."+resource);
			} catch (Exception e) {
				return null;
			}
	    }
	
	    return input;
	}
}

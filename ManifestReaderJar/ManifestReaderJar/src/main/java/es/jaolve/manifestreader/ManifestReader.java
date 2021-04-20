package es.jaolve.manifestreader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Classe que permet llegir MANIFEST.MF amb el format Standard en el projectes de la UV<br>
 * - MANIFEST.MF ha d'estar en format UTF8 per a que es vegent correctament els accents<br>
 * - Soporta , al menys, a partir de la versió de Java J2SE 1.5<br>
 * - soporta lectura de MANIFEST.MF de un Jar, WAR en Tomcat en EAR en WAS <br><br>
 *
 * Un exemple típic d'utilització: <br>
 *    <pre><code>
 *           ManifestReader manifestReader = new ManifestReader(); <br>
 *           Manifest m = manifestReader.getManifest(); <br>
 *           System.out.println("\tSPECIFICATION_VERSION:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VERSION)); <br>
 *           System.out.println("\tBuild-date:"+m.getMainAttributes().getValue("Build-date")); <br>
 *    </code></pre>
 *
 * @author jaolve
 *
 */
public class ManifestReader {

	private String manifestPath = "/META-INF/MANIFEST.MF";
	
	/**
	 * Constructor public per defecte
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
	 * A partir d'una classe es torna el Manifest que estiga a l'ambit d'ixa classe. <br>
	 * La classe típicament serà qualsevol classe de la aplicació. <br>
	 * O si es vol saber la versió d'un jar s'hauria passar com a parámetre una classe d'ixe Jar. <br>
	 * 
	 * @param clz Classe que es gastarà per a localitzar un MANIFEST.MF en el jar o war(ear) que la continga
	 * @return Manifest
	 * @throws ManifestAccesException en el cas de que no es trobe el manifest o qualsevol altre problema.
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
	 * Es torna el Manifest del propi Manifest Reader.
	 * Aquest mètode no s'ha d'usar a no ser que es vullga traure la versió del propi Manifest Reader.
	 * 
	 *
	 * @return Manifest
	 * @throws ManifestAccesException en el cas de que no es trobe el manifest o qualsevol altre problema.
	 */
	public Manifest getManifest() throws ManifestAccesException
	{
		return getManifest(ManifestReader.class);
	}	
	  
	/**
	 * Donat un jar torna l'arxiu de MANIFEST que conté
	 * 
	 * @param archiveJar arxiu jar sobre el que buscarà el MANIFEST.MF
	 * @return Manifest
	 * @throws IOException en el cas de que no trobe el jar, no es trobe el manifest o qualsevol altre problema.
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
	 * Torna un inputStream per a que fitxer de MANIFEST puga ser llegit de forma genèrica
	 * 
	 * @return InputStream al fitxer MANIFEST.MF
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

package es.jaolve.manifestreader;

import java.beans.XMLDecoder;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
/**
 * Printer class per a comprovar el funcionament de ManifestReader
 * Per a executar la prova es pot executar:  java  -cp <jar amb les classes del ManifestReader + classes de test>  es.jaolve.manifestreader.ManifestPrinterTest
 * 
 * @author jaolve
 *
 */
public class ManifestPrinterTest {

	public static void main(String[] args)  {
		
		try {
			System.out.println("java.version: " + System.getProperty("java.version"));
			System.out.println("java.specification.version: " + System.getProperty("java.specification.version"));
			
			System.out.println("\n\nLlegint el propi Manifest com a Manifest .... ");
			ManifestReader manifestReader = new ManifestReader();
			Manifest m = manifestReader.getManifest();

			System.out.println("\tSPECIFICATION_TITLE:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_TITLE));
			System.out.println("\tSPECIFICATION_VERSION:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VERSION));
			System.out.println("\tSPECIFICATION_VENDOR:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VENDOR));

			System.out.println("\n\tIMPLEMENTATION_TITLE (Aplicació):"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE));
			System.out.println("\tIMPLEMENTATION_VERSION (Versió):"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION));
			System.out.println("\tIMPLEMENTATION_VENDOR:"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR));

			System.out.println("\n\tCreated-By:"+m.getMainAttributes().getValue("Created-By"));
			System.out.println("\tBuild-Jdk:"+m.getMainAttributes().getValue("Build-Jdk"));
			System.out.println("\tBuild-date:"+m.getMainAttributes().getValue("Build-date"));
			System.out.println("\tBuilt-By:"+m.getMainAttributes().getValue("Built-By"));
			
			Map <String, Attributes> entries = m.getEntries();
			
		      for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
		          System.out.println("\tAttribute : " + entry.getKey() + ", Value : " + entry.getValue());
		      }
		} catch (Exception e) {
			System.out.println("Error llegint el Manifest "+e);
		}

		
		try {
			System.out.println("\n\nLlegint el Manifest de la classe String com a Manifest .... ");
			ManifestReader manifestReader = new ManifestReader();
			Manifest m = manifestReader.getManifest(XMLDecoder.class);

			System.out.println("\tSPECIFICATION_TITLE:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_TITLE));
			System.out.println("\tSPECIFICATION_VERSION:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VERSION));
			System.out.println("\tSPECIFICATION_VENDOR:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VENDOR));

			System.out.println("\n\tIMPLEMENTATION_TITLE (Aplicació):"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE));
			System.out.println("\tIMPLEMENTATION_VERSION (Versió):"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION));
			System.out.println("\tIMPLEMENTATION_VENDOR:"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR));

			System.out.println("\n\tCreated-By:"+m.getMainAttributes().getValue("Created-By"));
			System.out.println("\tBuild-Jdk:"+m.getMainAttributes().getValue("Build-Jdk"));
			System.out.println("\tBuild-date:"+m.getMainAttributes().getValue("Build-date"));
			System.out.println("\tBuilt-By:"+m.getMainAttributes().getValue("Built-By"));
			
			Map <String, Attributes> entries = m.getEntries();
			
	      for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
	          System.out.println("\tAttribute : " + entry.getKey() + ", Value : " + entry.getValue());
	      }
				
			
		} catch (Exception e) {
			System.out.println("Error llegint el Manifest "+e);
		}

		
		System.out.println("\n\nLlegint un Manifest com a fitxer .... ");
		
		try {
			printManifestAsAFile();
		} catch (Exception e) {
			System.out.println("Error llegint el Manifest com a fitxer "+e);
		}
		
		try {
			System.out.println("\n\nLlegint un Manifest d'un jar ....  ");
			ManifestReader manifestReader = new ManifestReader();
			Manifest m = manifestReader.getManifestFromJar("TestManifestVersion.jar");
			System.out.println("\tSPECIFICATION_TITLE:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_TITLE));
			System.out.println("\tSPECIFICATION_VERSION:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VERSION));
			System.out.println("\tSPECIFICATION_VENDOR:"+m.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VENDOR));

			System.out.println("\n\tIMPLEMENTATION_TITLE (Aplicació):"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE));
			System.out.println("\tIMPLEMENTATION_VERSION (Versió):"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION));
			System.out.println("\tIMPLEMENTATION_VENDOR:"+m.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR));

			System.out.println("\n\tCreated-By:"+m.getMainAttributes().getValue("Created-By"));
			System.out.println("\tBuild-Jdk:"+m.getMainAttributes().getValue("Build-Jdk"));
			System.out.println("\tBuild-date:"+m.getMainAttributes().getValue("Build-date"));
			System.out.println("\tBuilt-By:"+m.getMainAttributes().getValue("Built-By"));
		} catch (Exception e) {
			System.out.println("Error llegint el Manifest d'un jar "+e);
		}

		
	}
	
	/**
	 * Printeja un fitxer
	 */
	public static void printManifestAsAFile()
	{
		ManifestReader manifestReader = new ManifestReader();
		InputStream inputStream = manifestReader.accessFile();
		if (inputStream == null)
			System.out.println("No trobat el Manifest com a fitxer ");
		else {
			Scanner myReader = new Scanner(inputStream, "UTF8");
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println(data);
			}
			myReader.close();
		}
	}
}

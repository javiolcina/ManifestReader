package es.jaolve.manifestreader;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.util.jar.Manifest;

/**
 * Unit test for simple App.
 */
public class ManifestReaderTest
{
    /**
     * Test to return a Manifest
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        ManifestReader manifestReader = new ManifestReader();
        Manifest manifest = null;
        try {
            manifest = manifestReader.getManifest();

        } catch (ManifestAccesException e) {
            e.printStackTrace();
        }
        assertNotNull(manifest);

    }
}

package eu.unifiedviews.plugins.loader.sparqltovirtuoso;

import junit.framework.Assert;
import org.junit.Test;

public class SparqlToVirtuosoTest {

    @Test
    public void testDefaultConfiguration() throws Exception {
        SparqlToVirtuoso dpu = new SparqlToVirtuoso();
        String defaultConfiguration = dpu.getDefaultConfiguration();
        Assert.assertNotNull(defaultConfiguration);
    }

}
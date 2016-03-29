package eu.unifiedviews.plugins.loader.sparqltovirtuoso;

import eu.unifiedviews.dataunit.DataUnit;
import eu.unifiedviews.dataunit.rdf.RDFDataUnit;
import eu.unifiedviews.dataunit.rdf.WritableRDFDataUnit;
import eu.unifiedviews.dpu.DPU;
import eu.unifiedviews.dpu.DPUException;
import eu.unifiedviews.helpers.dpu.config.ConfigHistory;
import eu.unifiedviews.helpers.dpu.context.ContextUtils;
import eu.unifiedviews.helpers.dpu.exec.AbstractDpu;
import eu.unifiedviews.helpers.dpu.extension.rdf.RdfConfiguration;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoso.sesame2.driver.VirtuosoRepository;

import java.util.Map;

@DPU.AsLoader
public class SparqlToVirtuoso extends AbstractDpu<SparqlToVirtuosoConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(SparqlToVirtuoso.class);

    @RdfConfiguration.ContainsConfiguration
    @DataUnit.AsInput(name = "config", optional = true)
    public RDFDataUnit rdfConfiguration;

    @DataUnit.AsOutput(name = "rdfOutput", optional = true)
    public WritableRDFDataUnit rdfOutput;

    static final String CLEAR_QUERY = "CLEAR GRAPH <%s>";

    public static final String CONFIGURATION_VIRTUOSO_USERNAME = "dpu.l-sparqlToVirtuoso.username";

    public static final String CONFIGURATION_VIRTUOSO_PASSWORD = "dpu.l-sparqlToVirtuoso.password";

    public static final String CONFIGURATION_VIRTUOSO_JDBC_URL = "dpu.l-sparqlToVirtuoso.jdbc.url";

    public SparqlToVirtuoso() {
        super(SparqlToVirtuosoVaadinDialog.class, ConfigHistory.noHistory(SparqlToVirtuosoConfig.class));
    }

    @Override
    protected void innerExecute() throws DPUException {

        Map<String, String> environment = ctx.getExecMasterContext().getDpuContext().getEnvironment();
        String username = environment.get(CONFIGURATION_VIRTUOSO_USERNAME);
        if (config.getUsername() == null || config.getUsername().isEmpty()) {
            config.setUsername(username);
        }
        String password = environment.get(CONFIGURATION_VIRTUOSO_PASSWORD);
        if (config.getPassword() == null || config.getPassword().isEmpty()) {
            config.setPassword(password);
        }
        String virtuosoJdbcUrl = environment.get(CONFIGURATION_VIRTUOSO_JDBC_URL);
        if (config.getVirtuosoUrl() == null || config.getVirtuosoUrl().isEmpty()) {
            config.setVirtuosoUrl(virtuosoJdbcUrl);
        }

        final String longMessage = String.format("Configuration: \nVirtuosoUrl: %s\nusername: %s",
                config.getVirtuosoUrl(),
                config.getUsername());
        ContextUtils.sendInfo(ctx, "Configuration", longMessage);
        try {
            Class.forName("virtuoso.jdbc4.Driver");
        } catch (ClassNotFoundException ex) {
            throw new DPUException("Error loading driver", ex);
        }

//        if (config.isClearDestinationGraph()) {
//            executeUpdate(String.format(CLEAR_QUERY, config.getTargetGraphName()));
//        }
        executeUpdate(config.getQuery());
    }

    protected void executeUpdate(String updateQuery) throws DPUException {
        VirtuosoRepository virtuosoRepository = null;
        RepositoryConnection repositoryConnection = null;
        try {
            virtuosoRepository = new VirtuosoRepository(config.getVirtuosoUrl(), config.getUsername(), config.getPassword());
            virtuosoRepository.initialize();
            repositoryConnection = virtuosoRepository.getConnection();
            ContextUtils.sendInfo(ctx, "Update", "Started");
            Update update = repositoryConnection.prepareUpdate(QueryLanguage.SPARQL,updateQuery);
            update.execute();
            ContextUtils.sendInfo(ctx, "Update", "Finished");
        } catch (MalformedQueryException | RepositoryException | UpdateExecutionException ex) {
            throw new DPUException("Error working with Virtuoso using Repository API", ex);
        } finally {
            if (repositoryConnection != null) {
                try {
                    repositoryConnection.close();
                } catch (RepositoryException ex) {
                    LOG.warn("Error closing repository connection", ex);
                }
            }
            if (virtuosoRepository != null) {
                try {
                    virtuosoRepository.shutDown();
                } catch (RepositoryException ex) {
                    LOG.warn("Error shutdown repository", ex);
                }
            }
        }
    }

}

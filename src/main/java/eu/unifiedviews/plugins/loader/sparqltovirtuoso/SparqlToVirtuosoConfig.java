package eu.unifiedviews.plugins.loader.sparqltovirtuoso;

public class SparqlToVirtuosoConfig {

    private String query = "DEFINE sql:log-enable 3\nADD <http://localhost:8890/pilot3> TO <http://mu.semte.ch/application>\n";

    private String virtuosoUrl = "jdbc:virtuoso://localhost:1111/charset=UTF-8/";

    private String username = "dba";

    private String password = "dba";

    public SparqlToVirtuosoConfig() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getVirtuosoUrl() {
        return virtuosoUrl;
    }

    public void setVirtuosoUrl(String virtuosoUrl) {
        this.virtuosoUrl = virtuosoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

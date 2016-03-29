package eu.unifiedviews.plugins.loader.sparqltovirtuoso;

import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.UserDialogContext;

/**
 * DPU's configuration dialog. User can use this dialog to configure DPU
 * configuration.
 */
public class SparqlToVirtuosoVaadinDialog extends AbstractDialog<SparqlToVirtuosoConfig> {

    private static final long serialVersionUID = -5666076909428L;

    private TextArea txtQuery;

    private ObjectProperty<String> virtuosoUrl = new ObjectProperty<>("");

    private ObjectProperty<String> username = new ObjectProperty<>("");

    private ObjectProperty<String> password = new ObjectProperty<>("");

    public SparqlToVirtuosoVaadinDialog() {
        super(SparqlToVirtuoso.class);
    }

    public void outerBuildDialogLayout(UserDialogContext ctx) {
        this.ctx = ctx;
        buildDialogLayout();
    }

    @Override
    protected void buildDialogLayout() {
        setSizeFull();
        FormLayout mainLayout = new FormLayout();

        // top-level component properties
        setWidth("100%");
        setHeight("100%");

        final PasswordField passwordField = new PasswordField(ctx.tr("SparqlToVirtuosoVaadinDialog.password"), password);
        passwordField.setWidth("100%");

        mainLayout.addComponent(createTextField(ctx.tr("SparqlToVirtuosoVaadinDialog.virtuosoUrl"), virtuosoUrl));
        mainLayout.addComponent(createTextField(ctx.tr("SparqlToVirtuosoVaadinDialog.username"), username));
        mainLayout.addComponent(passwordField);

        txtQuery = new TextArea(ctx.tr("SparqlConstructVaadinDialog.constructQuery"));
        txtQuery.setSizeFull();
        txtQuery.addValidator(createSparqlQueryValidator());
        txtQuery.setImmediate(true);
        mainLayout.addComponent(txtQuery);
        mainLayout.setExpandRatio(txtQuery, 1);

        setCompositionRoot(mainLayout);
    }

    private <T> TextField createTextField(String caption, ObjectProperty<T> property) {
        final TextField result = new TextField(caption, property);
        result.setWidth("100%");
        return result;
    }

    @Override
    public void setConfiguration(SparqlToVirtuosoConfig conf) throws DPUConfigException {
        txtQuery.setValue(conf.getQuery());
        virtuosoUrl.setValue(conf.getVirtuosoUrl());
        username.setValue(conf.getUsername());
        password.setValue(conf.getPassword());
    }

    @Override
    public SparqlToVirtuosoConfig getConfiguration() throws DPUConfigException {
        SparqlToVirtuosoConfig conf = new SparqlToVirtuosoConfig();


        if (txtQuery.getValue().isEmpty()) {
            throw new DPUConfigException(ctx.tr("SparqlConstructVaadinDialog.emptyQuery"));
        }
        if (!txtQuery.isValid()) {
            throw new DPUConfigException(ctx.tr("sparqlvalidator.invalidQuery"));
        }
        conf.setQuery(txtQuery.getValue());
        conf.setVirtuosoUrl(virtuosoUrl.getValue());
        conf.setUsername(username.getValue());
        conf.setPassword(password.getValue());

        return conf;
    }

    private Validator createSparqlQueryValidator() {
        Validator validator = new Validator() {
            private static final long serialVersionUID = 1L;

            @Override
            public void validate(Object value) throws InvalidValueException {
                final String valueStr = (String) value;
                if (value == null || valueStr.isEmpty()) {
                    throw new InvalidValueException(ctx.tr("sparqlvalidator.emptyQuery"));
                }
//
//                try {
//                    QueryParserUtil.parseQuery(QueryLanguage.SPARQL, valueStr, null);
//                } catch (MalformedQueryException ex) {
//                    throw new InvalidValueException(ctx.tr("sparqlvalidator.invalidQuery") + " " + ex.getMessage());
//                }
            }
        };
        return validator;
    }

}

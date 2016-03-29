package eu.unifiedviews.plugins.loader.sparqltovirtuoso;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.*;
import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;

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

    @Override
    protected void buildDialogLayout() {
        setSizeFull();

        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(false);
        mainLayout.setSizeFull();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);

        final PasswordField passwordField = new PasswordField(ctx.tr("SparqlToVirtuosoVaadinDialog.password"), password);
        passwordField.setWidth(100, Unit.PERCENTAGE);

        mainLayout.addComponent(createTextField(ctx.tr("SparqlToVirtuosoVaadinDialog.virtuosoUrl"), virtuosoUrl));
        mainLayout.addComponent(createTextField(ctx.tr("SparqlToVirtuosoVaadinDialog.username"), username));
        mainLayout.addComponent(passwordField);

        txtQuery = new TextArea();
        txtQuery.setSizeFull();
        txtQuery.setCaption(ctx.tr("SparqlConstructVaadinDialog.constructQuery"));
        txtQuery.setImmediate(true);
        mainLayout.addComponent(txtQuery);

        mainLayout.setExpandRatio(txtQuery, 1);
        setCompositionRoot(mainLayout);
    }

    private <T> TextField createTextField(String caption, ObjectProperty<T> property) {
        final TextField result = new TextField(caption, property);
        result.setWidth(100, Unit.PERCENTAGE);
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
        conf.setQuery(txtQuery.getValue());
        conf.setVirtuosoUrl(virtuosoUrl.getValue());
        conf.setUsername(username.getValue());
        conf.setPassword(password.getValue());

        return conf;
    }

}

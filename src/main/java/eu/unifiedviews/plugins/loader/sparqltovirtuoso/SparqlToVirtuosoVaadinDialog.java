package eu.unifiedviews.plugins.loader.sparqltovirtuoso;

import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.util.ObjectProperty;

import eu.unifiedviews.dpu.config.DPUConfigException;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.AbstractDialog;
import eu.unifiedviews.helpers.dpu.vaadin.dialog.UserDialogContext;
import eu.unifiedviews.helpers.dpu.vaadin.validator.UrlValidator;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.parser.QueryParserUtil;

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

    private ObjectProperty<Boolean> clearDestinationGraph = new ObjectProperty<>(false);

    private ObjectProperty<String> targetGraphName = new ObjectProperty<>("");

    private ObjectProperty<Integer> threadCount = new ObjectProperty<>(1);

    private ObjectProperty<Boolean> perGraph = new ObjectProperty<>(
            false);

    private ObjectProperty<Boolean> skipOnError = new ObjectProperty<>(
            false);

    TextField targerGraphNameTextField;

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
        mainLayout.addComponent(new CheckBox(ctx.tr("SparqlToVirtuosoVaadinDialog.clearDestinationGraph"), clearDestinationGraph));
        targerGraphNameTextField = createTextField(ctx.tr("SparqlToVirtuosoVaadinDialog.targetGraphName"), targetGraphName);
        final CheckBox perGraphCheckbox = new CheckBox(ctx.tr("SparqlToVirtuosoVaadinDialog.perGraph"), perGraph);
        perGraphCheckbox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                targerGraphNameTextField.setEnabled(!perGraphCheckbox.getValue());
            }
        });
        targerGraphNameTextField.addValidator(new Validator() {

            @Override
            public void validate(Object value) throws InvalidValueException {
                if (value == null || StringUtils.isBlank((String) value)) {
                    if (!perGraphCheckbox.getValue()) {
                        throw new InvalidValueException(ctx.tr(SparqlToVirtuosoVaadinDialog.this.getClass().getSimpleName() + ".exception.target.graph.name.empty"));
                    }
                }
            }

        });
        targerGraphNameTextField.addValidator(new UrlValidator(true, ctx.getDialogMasterContext().getDialogContext().getLocale()));
        targerGraphNameTextField.setImmediate(true);

        mainLayout.addComponent(perGraphCheckbox);
        mainLayout.addComponent(targerGraphNameTextField);
        mainLayout.addComponent(createTextField(ctx.tr(this.getClass().getSimpleName() + ".threadCount"), threadCount));
        mainLayout.addComponent(new CheckBox(ctx.tr(this.getClass().getSimpleName() + ".skipOnError"), skipOnError));


        txtQuery = new TextArea(ctx.tr("SparqlConstructVaadinDialog.constructQuery"));
        txtQuery.setSizeFull();
        txtQuery.setRequired(true);
        txtQuery.addValidator(createSparqlQueryValidator());
        txtQuery.setImmediate(true);
        mainLayout.addComponent(txtQuery);
        mainLayout.setExpandRatio(txtQuery, 1.0f);


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
        clearDestinationGraph.setValue(conf.isClearDestinationGraph());
        perGraph.setValue(StringUtils.isEmpty(conf.getTargetGraphName()));
        targetGraphName.setValue(conf.getTargetGraphName());
        threadCount.setValue(conf.getThreadCount());
        skipOnError.setValue(conf.isSkipOnError());
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
        conf.setClearDestinationGraph(clearDestinationGraph.getValue());
        if (perGraph.getValue()) {
            conf.setTargetGraphName("");
        } else {
            if (!targerGraphNameTextField.isValid()) {
                throw new DPUConfigException(ctx.tr(this.getClass().getSimpleName() + ".validation.exception"));
            }
            conf.setTargetGraphName(targetGraphName.getValue());
        }
        conf.setThreadCount(threadCount.getValue());
        conf.setSkipOnError(skipOnError.getValue());
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

                try {
                    QueryParserUtil.parseQuery(QueryLanguage.SPARQL, valueStr, null);
                } catch (MalformedQueryException ex) {
                    throw new InvalidValueException(ctx.tr("sparqlvalidator.invalidQuery") + " " + ex.getMessage());
                }
            }
        };
        return validator;
    }

}

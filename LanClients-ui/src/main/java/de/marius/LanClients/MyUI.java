package de.marius.LanClients;

import javax.servlet.annotation.WebServlet;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import de.marius.LanClients.backend.services.LanSampleService;
import de.marius.LanClients.ui.LanSampleTable;
import de.marius.LanClients.ui.SamplesCombo;
import de.marius.LanClientsCore.domain.LanSample;

import java.io.IOException;
import java.util.Comparator;

/**
 *
 */
@Theme("mytheme")
public class MyUI extends UI {

//    private CrudService<Person> service = new CrudService<>();
//    private DataProvider<Person, String> dataProvider = new CallbackDataProvider<>(
//                    query -> service.findAll().stream(),
//                    query -> service.findAll().size());
//
//    @Override
//    protected void init(VaadinRequest vaadinRequest) {
//        final VerticalLayout layout = new VerticalLayout();
//        final TextField name = new TextField();
//        name.setCaption("Type your name here:");
//
//        final Button button = new Button("Click Me");
//        button.addClickListener(e -> {
//            service.save(new Person(name.getValue()));
//            dataProvider.refreshAll();
//        });
//
//        final Grid<Person> grid = new Grid<>();
//        grid.addColumn(Person::getName).setCaption("Name");
//        grid.setDataProvider(dataProvider);
//        grid.setSizeFull();
//
//        // This is a component from the LanClients-addon module
//        //layout.addComponent(new MyComponent());
//        layout.addComponents(name, button, grid);
//        layout.setSizeFull();
//        layout.setExpandRatio(grid, 1.0f);
//
//        setContent(layout);
//    }

    private LanSampleTable lanSampleTable;
    private static final String CURRENTVERSION = "0.1_2017-04-17_10";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        GridLayout rootLayout = new GridLayout(1, 2);
        VerticalLayout mainContentLayout = new VerticalLayout();

        SamplesCombo lanSampleComboBox = new SamplesCombo();
        lanSampleComboBox.setDataProvider(new CallbackDataProvider(query -> new LanSampleService().getAll().stream().sorted(Comparator.comparing(LanSample::getSampleDate).reversed()),
                query -> new LanSampleService().getAll().size()));
        lanSampleComboBox.setWidth(410.0f, Unit.PIXELS);
        lanSampleComboBox.addValueChangeListener(getLanSampleSelectedListener());
        mainContentLayout.addComponent(lanSampleComboBox);
        mainContentLayout.setComponentAlignment(lanSampleComboBox, Alignment.TOP_CENTER);

        try {
            lanSampleTable = new LanSampleTable();
            lanSampleTable.setWidth(480.0f, Unit.PIXELS);
            mainContentLayout.addComponent(lanSampleTable);
            mainContentLayout.setComponentAlignment(lanSampleTable, Alignment.TOP_CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }

        rootLayout.addComponent(mainContentLayout, 0, 0);

        Label currentVersion = new Label(CURRENTVERSION);
        rootLayout.addComponent(currentVersion, 0, 1);
        rootLayout.setComponentAlignment(currentVersion, Alignment.BOTTOM_CENTER);

        rootLayout.setSizeFull();
//        rootLayout.setSpacing(false);

        setContent(rootLayout);
    }

    private HasValue.ValueChangeListener<LanSample> getLanSampleSelectedListener() {
        return valueChangeEvent -> {
            lanSampleTable.showSample(valueChangeEvent.getValue());
        };

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

package de.marius.LanClients;

import javax.servlet.annotation.WebServlet;

import com.vaadin.data.HasValue;
import com.vaadin.ui.*;
import de.marius.LanClients.backend.CrudService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.DataProvider;
import de.marius.LanClients.backend.services.LanSampleService;
import de.marius.LanClientsCore.domain.LanSample;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

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
    private static final String CURRENTVERSION = "0.1_2017-04-13";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();


        ComboBox<LanSample> lanSampleComboBox = new ComboBox<>();
        List<LanSample> samples = new LanSampleService().getAll();
        lanSampleComboBox.setItems(samples.stream().sorted(Comparator.comparing(LanSample::getSampleDate).reversed()));
        lanSampleComboBox.setWidth(380.0f, Unit.PIXELS);
        lanSampleComboBox.addValueChangeListener(getLanSampleSelectedListener());
        layout.addComponent(lanSampleComboBox);
        layout.setComponentAlignment(lanSampleComboBox, Alignment.MIDDLE_CENTER);

        try {
            lanSampleTable = new LanSampleTable();
            lanSampleTable.setWidth(480.0f, Unit.PIXELS);
            layout.addComponent(lanSampleTable);
            layout.setComponentAlignment(lanSampleTable, Alignment.MIDDLE_CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Label currentVersion = new Label(CURRENTVERSION);
        layout.addComponent(currentVersion);
        layout.setComponentAlignment(currentVersion, Alignment.BOTTOM_CENTER);
        setContent(layout);
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

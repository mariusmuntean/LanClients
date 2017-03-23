package de.marius.LanClientsViewer;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.HasValue;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import de.marius.LanClientsViewer.domain.LanSample;
import de.marius.LanClientsViewer.services.LanSampleService;
import de.marius.LanClientsViewer.ui.LanSampleTable;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private LanSampleTable lanSampleTable;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();


        ComboBox<LanSample> lanSampleComboBox = new ComboBox<>();
        List<LanSample> samples = new LanSampleService().getAll();
        lanSampleComboBox.setItems(samples.stream().sorted(Comparator.comparing(LanSample::getSampleDate).reversed()));
//        lanSampleComboBox.setWidth(320.0f, Unit.PIXELS);
        lanSampleComboBox.addValueChangeListener(getLanSampleSelectedListener());
        layout.addComponent(lanSampleComboBox);

        try {
            lanSampleTable = new LanSampleTable();
            lanSampleTable.addStyleName("dead");
            layout.addComponent(lanSampleTable);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

package de.marius.LanClients.ui;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import de.marius.LanClientsCore.domain.LanSample;

/**
 * Created by marius on 17.04.17.
 */
public class SamplesCombo extends CustomComponent {


    private final ComboBox<LanSample> lanSampleComboBox;
    private final Button refreshButton;

    public SamplesCombo() {
        HorizontalLayout rootLayout = new HorizontalLayout();

        lanSampleComboBox = new ComboBox<>();
        lanSampleComboBox.setSizeFull();
//        lanSampleComboBox.setSizeUndefined();
        refreshButton = new Button();
        refreshButton.setIcon(VaadinIcons.REFRESH);
        refreshButton.addClickListener(event -> {
            if (lanSampleComboBox != null && lanSampleComboBox.getDataProvider() != null) {
                lanSampleComboBox.getDataProvider().refreshAll();
            }
        });

        rootLayout.addComponent(lanSampleComboBox);
        rootLayout.setComponentAlignment(lanSampleComboBox, Alignment.MIDDLE_CENTER);
        rootLayout.setExpandRatio(lanSampleComboBox, 9.0f);
        rootLayout.addComponent(refreshButton);
        rootLayout.setComponentAlignment(refreshButton, Alignment.MIDDLE_CENTER);
        rootLayout.setExpandRatio(refreshButton, 1.0f);

        rootLayout.setSpacing(true);
        rootLayout.setSizeFull();
//        rootLayout.addStyleName("newdevice");

//        setSizeUndefined();
        setCompositionRoot(rootLayout);
    }


    public void setDataProvider(CallbackDataProvider dataProvider) {
        lanSampleComboBox.setDataProvider(dataProvider);
    }


    public void addValueChangeListener(HasValue.ValueChangeListener<LanSample> lanSampleSelectedListener) {
        lanSampleComboBox.addValueChangeListener(lanSampleSelectedListener);
    }
}

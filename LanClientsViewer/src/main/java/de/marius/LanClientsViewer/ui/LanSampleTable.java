package de.marius.LanClientsViewer.ui;


import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import de.marius.LanClientsViewer.domain.LanClient;
import de.marius.LanClientsViewer.domain.LanSample;

import java.util.Map;

/**
 * Created by marius on 05/03/2017.
 */
public class LanSampleTable extends Grid<LanClient> {

    private LanSample lanSample;

    public LanSampleTable() {
        this.addColumn(LanClient::getIpAddress).setCaption("IP Address");
        this.addColumn(LanClient::getMacAddress).setCaption("MAC Address");
    }

    public void showSample(LanSample lanSample) {
        this.lanSample = lanSample;

        this.setDataProvider(getLanSampleClientsProvider());
    }

    private DataProvider<LanClient, ?> getLanSampleClientsProvider() {
        return new ListDataProvider<>(this.lanSample.getClients());
    }
}

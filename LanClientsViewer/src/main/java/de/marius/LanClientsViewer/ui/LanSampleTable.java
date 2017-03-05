package de.marius.LanClientsViewer.ui;


import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import de.marius.LanClientsViewer.domain.LanSample;

import java.util.Map;

/**
 * Created by marius on 05/03/2017.
 */
public class LanSampleTable extends Grid<Map.Entry<String, String>> {

    private LanSample lanSample;


    public LanSampleTable(){
        this.addColumn(Map.Entry::getKey).setCaption("IP");
        this.addColumn(Map.Entry::getValue).setCaption("MAC");
    }

    public void showSample(LanSample lanSample){
        this.lanSample = lanSample;

        this.setDataProvider(getLanSampleClientsProvider());
    }

    private DataProvider<Map.Entry<String, String>, ?> getLanSampleClientsProvider() {
        return new ListDataProvider<>(this.lanSample.getClients().entrySet());
    }
}

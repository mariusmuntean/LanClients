package de.marius.LanClientsViewer.ui;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.StyleGenerator;
import de.marius.LanClientsViewer.domain.LanClient;
import de.marius.LanClientsViewer.domain.LanSample;
import de.marius.LanClientsViewer.services.WhitelistService;

import java.io.IOException;

/**
 * Created by marius on 05/03/2017.
 */
public class LanSampleTable extends Grid<LanClient> {

    private LanSample lanSample;
    private WhitelistService whitelistService;
    private final Column<LanClient, String> ipColumn;
    private final Column<LanClient, String> macColumn;

    public LanSampleTable() throws IOException {
        whitelistService = new WhitelistService();
        ipColumn = this.addColumn(LanClient::getIpAddress);
        ipColumn.setCaption("IP Address");
        macColumn = this.addColumn(LanClient::getMacAddress);
        macColumn.setCaption("MAC Address");
        setStyleGenerator(getRowStyleGenerator());

//        macColumn.setStyleGenerator(getCellStyleGenerator());
//        ipColumn.setStyleGenerator(getCellStyleGenerator());
    }

    private StyleGenerator<LanClient> getCellStyleGenerator() {
        return item ->
        {
            return "newdevice";
        };
    }

    private StyleGenerator<LanClient> getRowStyleGenerator() {
        return lanClient -> {
            try {
                String style = whitelistService.isClientWhitelisted(lanClient) ? null : "newdevice";
                return style;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    public void showSample(LanSample lanSample) {
        this.lanSample = lanSample;

        this.setDataProvider(getLanSampleClientsProvider());
    }

    private DataProvider<LanClient, ?> getLanSampleClientsProvider() {
        return new ListDataProvider<>(this.lanSample.getClients());
    }
}

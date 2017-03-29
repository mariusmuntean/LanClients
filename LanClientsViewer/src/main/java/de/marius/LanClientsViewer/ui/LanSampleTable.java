package de.marius.LanClientsViewer.ui;

import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.contextmenu.Menu;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.DetailsGenerator;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ClickableRenderer;
import de.marius.LanClientsCore.domain.LanClient;
import de.marius.LanClientsCore.domain.LanSample;
import de.marius.LanClientsViewer.services.WhitelistService;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by marius on 05/03/2017.
 */
public class LanSampleTable extends Grid<LanClient> {

    private LanSample lanSample;
    private WhitelistService whitelistService;
    private final Column<LanClient, String> nameColumn;
    private final Column<LanSample, ButtonRenderer> expandColumn;
    private final Column<LanClient, String> ipColumn;
    private final Column<LanClient, String> macColumn;

    public LanSampleTable() throws IOException {
        whitelistService = new WhitelistService();
        ipColumn = this.addColumn(LanClient::getIpAddress);
        ipColumn.setCaption("IP Address");
        macColumn = this.addColumn(LanClient::getMacAddress);
        macColumn.setCaption("MAC Address");
        nameColumn = this.addColumn(LanClient::getName);
        nameColumn.setCaption("Name");
        expandColumn = this.addColumn("Details", getDetailsButtonRenderer());

        setStyleGenerator(getRowStyleGenerator());

        addContextMenu();

        this.setDetailsGenerator(getDetailsGenerator());
    }

    private ButtonRenderer getDetailsButtonRenderer() {
        return new ButtonRenderer((ClickableRenderer.RendererClickEvent event) -> {
            LanSampleTable.this.setDetailsVisible((LanClient) event.getItem(), true);
        });
    }


    private DetailsGenerator<LanClient> getDetailsGenerator() {
        return lanClient -> {
            Label nameLbl = new Label("Client name: ");
            TextField nameTxt = new TextField("Client name");
            nameTxt.setValue(lanClient.getName());
            Button update = new Button("Update");
            update.addClickListener(event -> {
                Notification.show("Updated name");
            });

            HorizontalLayout detailsLayout = new HorizontalLayout(nameLbl, nameTxt, update);
            return detailsLayout;
        };
    }

    private void addContextMenu() {
        GridContextMenu menu = new GridContextMenu(this);
        menu.addGridBodyContextMenuListener(event -> {
            menu.removeItems();
            LanClient lanClient = (LanClient) event.getItem();
            try {
                boolean clientWhitelisted = whitelistService.isClientWhitelisted(lanClient);
                String menuItemName = clientWhitelisted ? "Blacklist" : "Whitelist";
                Menu.Command menuItemCommand = clientWhitelisted ? getBlacklistCommand(lanClient) : getWhitelistCommand(lanClient);
                menu.addItem(menuItemName, menuItemCommand);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Menu.Command getBlacklistCommand(LanClient lanClient) {
        return (menuItem) -> {
            try {
                whitelistService.removeClientFromWhitelist(lanClient);
                Notification.show("Blacklisted " + lanClient.getMacAddress());
            } catch (IOException e) {
                e.printStackTrace();
                Notification.show("Error blacklisting " + e.getMessage());
            }
            LanSampleTable.this.reload();
        };
    }

    private Menu.Command getWhitelistCommand(LanClient lanClient) {
        return (menuItem) -> {
            try {
                whitelistService.whitelistClient(lanClient);
                Notification.show("Whitelisted " + lanClient.getMacAddress());
            } catch (IOException e) {
                e.printStackTrace();
                Notification.show("Error whitelisting " + e.getMessage());
            }
            LanSampleTable.this.reload();
        };
    }

    private StyleGenerator<LanClient> getRowStyleGenerator() {
        return lanClient -> {
            try {
                return whitelistService.isClientWhitelisted(lanClient) ? null : "newdevice";
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

    public void reload() {
        if (this.lanSample != null) {
            showSample(this.lanSample);
        }
    }

    private DataProvider<LanClient, ?> getLanSampleClientsProvider() {
        if (this.lanSample == null) {
            return new ListDataProvider<>(new ArrayList<>());
        }
        return new ListDataProvider<>(this.lanSample.getClients());
    }
}

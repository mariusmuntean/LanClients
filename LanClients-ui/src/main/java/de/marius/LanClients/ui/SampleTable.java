package de.marius.LanClients.ui;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextArea;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class SampleTable extends VerticalLayout {
    protected TextField search;
    protected Button add;
    protected Grid list;
    protected TextField firstName;
    protected TextField lastName;
    protected TextField jobTitle;
    protected TextField phoneNumber;
    protected TextField email;
    protected TextField company;
    protected TextField url;
    protected TextArea notes;
    protected Button update;
    protected Button cancel;
    protected Button delete;

    public SampleTable() {
        Design.read(this);
    }
}

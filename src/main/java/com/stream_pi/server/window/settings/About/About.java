package com.stream_pi.server.window.settings.About;

import com.stream_pi.action_api.ActionAPI;
import com.stream_pi.server.info.ServerInfo;
import com.stream_pi.server.Main;
import javafx.application.HostServices;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public class About extends VBox
{

    private HostServices hostServices;

    public About(HostServices hostServices)
    {
        getStyleClass().add("about");
        this.hostServices = hostServices;

        setAlignment(Pos.TOP_CENTER);

        Image appIcon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("app_icon.png")));
        ImageView appIconImageView = new ImageView(appIcon);
        appIconImageView.setFitHeight(196);
        appIconImageView.setFitWidth(182);

        TabPane tabPane = new TabPane();
        tabPane.addEventFilter(SwipeEvent.ANY, Event::consume);
        tabPane.getStyleClass().add("settings_about_tab_internal");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setMaxWidth(600);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Tab licenseTab = new Tab("License");
        licenseTab.setContent(new LicenseTab());


        Tab contributorsTab = new Tab("Contributors");
        contributorsTab.setContent(new ContributorsTab(hostServices));

        Tab contactTab = new Tab("Contact");
        contactTab.setContent(new ContactTab(hostServices));

        tabPane.getTabs().addAll(licenseTab, contributorsTab, contactTab);


        Hyperlink donateButton = new Hyperlink("DONATE");
        donateButton.setOnAction(event -> openWebpage("https://www.patreon.com/streampi"));
        donateButton.getStyleClass().add("about_donate_hyperlink");

        ServerInfo serverInfo = ServerInfo.getInstance();

        Label versionLabel = new Label(serverInfo.getVersion().getText() + " - "+ serverInfo.getPlatform().getUIName() + " - "+ serverInfo.getReleaseStatus().getUIName());
        versionLabel.getStyleClass().add("about_version_label");

        Label commStandardLabel = new Label("Comm Standard "+serverInfo.getCommStandardVersion().getText());
        commStandardLabel.getStyleClass().add("about_comm_standard_label");

        Label minThemeAPILabel = new Label("Min ThemeAPI "+serverInfo.getMinThemeSupportVersion().getText());
        minThemeAPILabel.getStyleClass().add("about_min_theme_api_label");

        Label minActionAPILabel = new Label("Min ActionAPI "+serverInfo.getMinPluginSupportVersion().getText());
        minActionAPILabel.getStyleClass().add("about_min_action_api_label");

        Label currentActionAPILabel = new Label("ActionAPI "+ ActionAPI.API_VERSION.getText());
        currentActionAPILabel.getStyleClass().add("about_current_action_api_label");

        HBox hBox1 = new HBox(versionLabel, getSep(),
                commStandardLabel, getSep(),
                minThemeAPILabel, getSep(),
                minActionAPILabel, getSep(),
                currentActionAPILabel);

        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(10);

        Label javaVersionLabel = new Label("Java "+System.getProperty("java.version"));
        javaVersionLabel.getStyleClass().add("about_java_version");

        Label javafxVersionLabel = new Label("JavaFX "+System.getProperty("javafx.version"));
        javafxVersionLabel.getStyleClass().add("about_javafx_version");

        Label javaGCLabel = new Label("GC: "+ManagementFactory.getGarbageCollectorMXBeans().get(0).getName());
        javaGCLabel.getStyleClass().add("about_java_gc");

        HBox hBox2 = new HBox(javaVersionLabel, getSep(),
                javafxVersionLabel, getSep(),
                javaGCLabel);

        hBox2.setAlignment(Pos.CENTER);
        hBox2.setSpacing(10);


        getChildren().addAll(appIconImageView, tabPane, donateButton, hBox1, hBox2);


        InputStream inputStream = Main.class.getResourceAsStream("build-date");
        if(inputStream != null)
        {
            try
            {
                Logger.getLogger(getClass().getName()).info("build-date not present");
                Label buildDateLabel = new Label("Build date/time: " +  new String(inputStream.readAllBytes()));
                buildDateLabel.getStyleClass().add("about_build_date_label");
                hBox2.getChildren().addAll(getSep(), buildDateLabel);
            }
            catch (IOException e)
            {
                Logger.getLogger(getClass().getName()).info("build-date not present");
            }
        }
    }

    public void openWebpage(String url)
    {
        hostServices.showDocument(url);
    }

    private Label getSep()
    {
        Label label = new Label("|");
        label.getStyleClass().add("separator_ui_label");
        return label;
    }
}

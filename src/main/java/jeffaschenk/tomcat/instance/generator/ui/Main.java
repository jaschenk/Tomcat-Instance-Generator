package jeffaschenk.tomcat.instance.generator.ui;

import jeffaschenk.tomcat.util.ResourceHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

/**
 * JavaFX Main UI
 */
public class Main extends Application {
    private static final String FXML_RESOURCE =
            "jeffaschenk/tomcat/instance/generator/ui/TomcatInstanceGenerator.fxml";
    /**
     * Start Primary JavaFX Stage View
     *
     * @param primaryStage Stage
     * @throws Exception If issues arise during Bootstrap...
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        /**
         * Obtain FXML Resource...
         */
        StringBuilder FXML = ResourceHelper.readResourceToStringBuffer(FXML_RESOURCE);
        /**
         * Load the Anchor Pane
         */
        AnchorPane layout =  loader.load(
                new ByteArrayInputStream(FXML.toString().getBytes())
        );
        /**
         * Initialize the Scene
         */
        Scene scene = new Scene(layout);
        /**
         * Establish StyleSheets
         */
        scene.getStylesheets().add(
                getClass().getClassLoader().getResource("css/log-view.css").toExternalForm()
        );

        /**
         * Prepare remainder of Stage ...
         */
        primaryStage.setTitle("Tomcat Instance Generator");
        Image logo = new Image(getClass().getClassLoader().getResourceAsStream("images/tomcat.png"));
        primaryStage.getIcons().add(logo);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Main Launcher ...
     * @param args Supplied Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

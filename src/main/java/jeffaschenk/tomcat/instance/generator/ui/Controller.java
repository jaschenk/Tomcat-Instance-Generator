package jeffaschenk.tomcat.instance.generator.ui;

import jeffaschenk.tomcat.instance.generator.builders.TomcatInstance;
import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceBuilderForUI;
import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceBuilderHelper;
import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceProperty;
import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import jeffaschenk.tomcat.instance.generator.logger.Level;
import jeffaschenk.tomcat.instance.generator.logger.Log;
import jeffaschenk.tomcat.instance.generator.logger.LogView;
import jeffaschenk.tomcat.knowledgebase.DefaultDefinitions;
import jeffaschenk.tomcat.util.ValidationHelper;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller
 */
public class Controller implements Initializable {

    /**
     * Logging
     */
    static final GenerationLogger GENERATION_LOGGER = new GenerationLogger(new Log(), "main");

    /**
     * Task State Indicators
     */
    private static final String TASK_STATE_DONE = "DONE";
    private static final String TASK_STATE_FAILURE = "FAILURE";

    /**
     * Generation Task Worker
     */
    private Task generationWorker;

    /**
     * FXML Wiring
     * Logo Areas ....
     */
    @FXML
    private VBox logoArea1;

    @FXML
    private VBox logoArea2;

    @FXML
    private TabPane tabPane;
    private String currentTabId;
    private String previousTabId;

    /**
     * Primary Buttons
     */
    @FXML
    Button generateButton;

    @FXML
    Button cancelButton;

    @FXML
    Button resetButton;

    /**
     * Primary Message Areas
     */
    @FXML
    TextField messageResponse;

    /**
     * Primary Generation Progress Bar
     */
    @FXML
    ProgressBar progressBar;

    /**
     * Generation Elements
     * Instance Definition Tab
     */
    private static final int TAB_INSTANCE_DEFINITIONS = 0;

    @FXML
    TextField instanceName;

    @FXML
    ChoiceBox<String> environmentChoice;
    private int selectedEnvironmentChoice = 0;

    @FXML
    ChoiceBox<String> tcVersionChoice;
    private int selectedTomcatVersionChoice = 0;

    @FXML
    TextField destinationFolder;

    @FXML
    Button fileDirectorySelectionButton;

    @FXML
    CheckBox compressFolderOutput;

    /**
     * Generation Elements
     * Instance Ports Tab
     */
    private static final int TAB_INSTANCE_PORTS = 1;

    @FXML
    TextField primaryPort;

    @FXML
    ChoiceBox<String> protocolPrimaryPort;

    @FXML
    TextField shutdownPort;

    @FXML
    TextField ajpPort;

    /**
     * Generation Elements
     * Instance Security Tab
     */
    private static final int TAB_INSTANCE_SECURITY = 2;

    @FXML
    CheckBox secureInstance;

    @FXML
    Label secureInstanceInfoLabel;

    @FXML
    Label keystoreSourceLabel;

    @FXML
    TextField keystoreSourceFilename;

    @FXML
    Button keystoreSourceSelectionButton;

    @FXML
    Label keystoreCredentialsLabel;

    @FXML
    TextField keystoreCredentials;

    @FXML
    Label securePortLabel;

    @FXML
    TextField securePort;

    @FXML
    Label protocolSecurePortLabel;

    @FXML
    ChoiceBox<String> protocolSecurePort;

    /**
     * Generation Elements
     * JVM Options Tab
     */
    private static final int TAB_JVM_OPTIONS = 3;

    @FXML
    ChoiceBox<String> jvmOptionXms;

    @FXML
    ChoiceBox<String> jvmOptionXmx;

    @FXML
    ChoiceBox<String> jvmOptionXss;

    @FXML
    ChoiceBox<String> jvmOptionXXMaxMetaspaceSize;

    /**
     * Instance JVM Options Table
     */
    @FXML
    TableView<InstanceJVMOptionRow> instanceJVMOptionsTableView;

    @FXML
    TableColumn instanceJVMOptionsNameColumn;

    /**
     * JVM Options Row Data
     */
    ObservableList<InstanceJVMOptionRow> instanceJVMOptionsData = FXCollections.observableArrayList();

    @FXML
    private TextField newInstanceJVMOptionsName;

    @FXML
    private Button newInstanceJVMOptionsButton;

    /**
     * Generation Elements
     * Instance Properties Tab
     */
    private static final int TAB_INSTANCE_PROPERTIES = 4;

    /**
     * Instance Property Table
     */
    @FXML
    TableView<InstancePropertyRow> instancePropertyTableView;
    @FXML
    TableColumn instancePropertyNameColumn;
    @FXML
    TableColumn instancePropertyValueColumn;

    /**
     * Instance Property Row Data
     */
    ObservableList<InstancePropertyRow> instancePropertyData = FXCollections.observableArrayList();

    @FXML
    private TextField newInstancePropertyName;

    @FXML
    private TextField newInstancePropertyValue;

    @FXML
    private Button newInstancePropertyButton;

    /**
     * Generation Elements
     * Instance Management Tab
     */
    private static final int TAB_INSTANCE_MANAGEMENT = 5;

    @FXML
    CheckBox instanceManagement;

    @FXML
    Label instanceManagementInfoLabel;

    /**
     * Instance JVM Options Table
     */
    @FXML
    TableView<InstancePropertyRow> instanceManagementTableView;
    @FXML
    TableColumn instanceManagementNameColumn;
    @FXML
    TableColumn instanceManagementValueColumn;

    /**
     * Instance Property Row Data
     */
    ObservableList<InstancePropertyRow> instanceManagementData = FXCollections.observableArrayList();

    @FXML
    private Label instanceManagementPropertyNameLabel;

    @FXML
    private TextField newInstanceManagementName;

    @FXML
    private Label instanceManagementPropertyValueLabel;

    @FXML
    private TextField newInstanceManagementValue;

    @FXML
    private Button newInstanceManagementButton;


    /**
     * Generation Elements
     * Load YAML Configuration File Tab
     */
    private static final int TAB_LOAD_YAML_CONFIGIGURATION_FILE = 6;

    @FXML
    TextField yamlConfigurationFilename;

    @FXML
    Button yamlFileSelectionButton;

    @FXML
    Button loadYAMLButton;

    @FXML
    Button saveYAMLButton;


    /**
     * Generation Elements
     * The 'Generation Logging' Tab ...
     */
    private static final int TAB_LOGGER = 7;

    @FXML
    AnchorPane generationLogAnchorPane;

    /**
     * Initialization of our Rendered View Elements ...
     *
     * @param url URL
     * @param rb  Resource Bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * Initialize the Logger Frame First ...
         */
        initializeGenerationLoggerPane();
        GENERATION_LOGGER.info("Welcome to Tomcat Instance Generation Utility ...");
        GENERATION_LOGGER.info("Initializing ...");

        /**
         * Add Change Listener for when Tabs Change, si we know where the User is in relationship
         * to a button or function requested.
         */
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            //GENERATION_LOGGER.debug("Moved From Tab: "+oldTab.getId()+" to Tab: "+newTab.getId());
            currentTabId = newTab.getId();
            previousTabId = oldTab.getId();
            /**
             * Perform specific Visibility and Enablement Options based upon Tab and existing Data ...
             */
            if (currentTabId.equals("yamlConfigurationManagementTab")) {
                if (haveAllRequiredFieldsBeenSet()) {
                    saveYAMLButton.setDisable(false);
                } else {
                    saveYAMLButton.setDisable(true);
                }
            }
        });

        /**
         * Set Applicable Buttons as off and not visible ...
         */
        cancelButton.setDisable(true);
        cancelButton.setVisible(false);

        /**
         * Set the Load and Save YAML Buttons as Off to begin with ...
         */
        loadYAMLButton.setDisable(true);
        saveYAMLButton.setDisable(true);

        /**
         * Do not show Progress Bar ...
         */
        progressBar.setVisible(false);

        /**
         * Establish a Default Instance Name
         */
        String defaultInstanceName = TomcatInstanceBuilderHelper.getThisDefaultInstanceHostName();
        instanceName.setText(defaultInstanceName+"-001");

        /**
         * Set Default for Environment
         */
        environmentChoice.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_ENVIRONNMENT));

        environmentChoice.setValue(DefaultDefinitions.DEFAULT_ENVIRONMENT_SELECTED);

        /**
         * Set Default for Tomcat Versions
         */
        tcVersionChoice.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_TOMCAT_VERSIONS));

        tcVersionChoice.setValue(DefaultDefinitions.DEFAULT_TOMCAT_VERSION_SELECTED);

        /**
         * Set Default for Primary Port Protocol
         */
        protocolPrimaryPort.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_CATALINA_PROTOCOLS));

        protocolPrimaryPort.setValue(DefaultDefinitions.DEFAULT_CATALINA_PROTOCOL_SELECTED);

        /**
         * Set Default for Secure Port Protocol
         */
        protocolSecurePort.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_CATALINA_PROTOCOLS));

        protocolSecurePort.setValue(DefaultDefinitions.DEFAULT_CATALINA_SECURE_PROTOCOL_SELECTED);

        /**
         * Initialize the Secure Instance Tab
         */
        setSecureInstanceVisibility(false);
        setInstanceManagementVisibility(false);

        /**
         * Clear and Ready Message Status Area ...
         */
        messageResponse.setFont(Font.font("Verdana", 14));
        messageResponse.setStyle("-fx-text-inner-color: green;");
        messageResponse.clear();
        messageResponse.setText("Ready ...");

        /**
         * Set Environment Choices
         */
        environmentChoice.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue ov, Number value, Number new_value) {
                        selectedEnvironmentChoice = new_value.intValue();
                    }
                });
        /**
         * Set Tomcat Version Choices
         */
        tcVersionChoice.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    public void changed(ObservableValue ov, Number value, Number new_value) {
                        selectedTomcatVersionChoice = new_value.intValue();
                    }
                });

        /**
         * Initialize Images on Banner
         */
        Image tomcat_logo = new Image(getClass().getClassLoader().getResourceAsStream("images/tomcat.png"));
        ImageView imgView = new ImageView(tomcat_logo);
        logoArea2.getChildren().add(imgView);

        /**
         * Set Defaults for Primary Port
         */
        setDefaultPorts();

        /**
         * Initialize TableView for Instance Properties.
         */
        initializeInstancePropertiesTableView();
        /**
         * Initialize the JVM Options ...
         */
        initializeInstanceJVMOptions();
        /**
         * Initialize the Management Options ...
         */
        initializeInstanceManagementPropertiesTableView();
        
        /**
         * Position to correct Tab
         */
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(TAB_INSTANCE_DEFINITIONS);

        /**
         * Indicate we are Initialized
         */
        GENERATION_LOGGER.info("Initialization Completed ...");
        GENERATION_LOGGER.info("Ready ...");
    }


    /**
     * Generate button pressed,
     * We Generate an Instance Based upon Properties specified.
     *
     * @param event Action which Triggered getting to here...
     */
    @FXML
    private void handleGenerateButtonAction(ActionEvent event) {
        if (!isGenerationReady()) {
            return;
        }

        /**
         * Proceed ...
         */
        progressBar.setProgress(0);

        messageResponse.clear();
        messageResponse.setStyle("-fx-text-inner-color: green;");
        messageResponse.setText("Will be start Generating Tomcat Instance Named: " + instanceName.getText() + " for Environment " +
                environmentChoice.getItems().get(selectedEnvironmentChoice)
                + ", Running Tomcat " + tcVersionChoice.getItems().get(selectedTomcatVersionChoice));

        fileDirectorySelectionButton.setDisable(true);

        generateButton.setDisable(true);
        cancelButton.setDisable(false);
        cancelButton.setVisible(true);
        resetButton.setVisible(false);
        /**
         * Create Worker Task
         */
        generationWorker = createWorker();

        /**
         * Initialize the Progress Bar ...
         */
        progressBar.setVisible(true);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(generationWorker.progressProperty());
        /**
         * Set up our Listener for when the generation process state changes ...
         */
        generationWorker.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                //GENERATION_LOGGER.debug("Generation State: " + newValue);
                if (newValue != null && !newValue.isEmpty() && newValue.equalsIgnoreCase(TASK_STATE_DONE)) {
                    /**
                     * Child Thread Task is Completed ...
                     */
                    resetStateAfterGenerationProcessEnded();
                    /**
                     * Show a Message
                     */
                    messageResponse.setStyle("-fx-text-inner-color: green;");
                    messageResponse.clear();
                    messageResponse.setText("Done, Generated Tomcat Instance for Environment " +
                            environmentChoice.getItems().get(selectedEnvironmentChoice)
                            + ", Running Tomcat " + tcVersionChoice.getItems().get(selectedTomcatVersionChoice)
                            + ", Located at " + destinationFolder.getText());

                } else if (newValue != null && !newValue.isEmpty() && newValue.equalsIgnoreCase(TASK_STATE_FAILURE)) {
                    /**
                     * Child Thread Task is Completed ...
                     */
                    resetStateAfterGenerationProcessEnded();
                    /**
                     * Show a Message
                     */
                    messageResponse.setStyle("-fx-text-inner-color: red;");
                    messageResponse.clear();
                    messageResponse.setText("Generation Process has Failed, please see Logs to determine cause!");
                }
            }
        });
        /**
         * Begin the Generation Thread ...
         */
        Thread generationWorkerThread = new Thread(generationWorker);
        generationWorkerThread.start();
    }

    /**
     * Reset State After Generation process has Ended ...
     */
    private void resetStateAfterGenerationProcessEnded() {
        /**
         * Reset Buttons After Task ...
         */
        generateButton.setDisable(false);
        cancelButton.setDisable(true);
        cancelButton.setVisible(false);
        resetButton.setDisable(false);
        resetButton.setVisible(true);
        /**
         * Clear the Progress Bar ...
         */
        progressBar.progressProperty().unbind();
        progressBar.setVisible(false);

        /**
         * Re-Enable the File Directory Selection Button.
         */
        fileDirectorySelectionButton.setDisable(false);

        // TODO :: Perform a True Reset all Data Values to Defaults and clear any and all existing values..
    }

    /**
     * Cancel button pressed,
     * We Cancel All Operations back to Defaults ...
     *
     * @param event Action which Triggered getting to here...
     */
    @FXML
    private void handleCancelButtonAction(ActionEvent event) {

        generationWorker.cancel(true);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);

        messageResponse.clear();
        messageResponse.setStyle("-fx-text-inner-color: blue;");
        messageResponse.setText("Generation process for Tomcat Instance has been Cancelled.");

        generateButton.setDisable(false);
        resetButton.setDisable(false);
        resetButton.setVisible(true);
        cancelButton.setDisable(true);
        cancelButton.setVisible(false);

        fileDirectorySelectionButton.setDisable(false);
    }

    /**
     * Reset button pressed,
     * We Reset all Properties back to Defaults ...
     *
     * @param event Action which Triggered getting to here...
     */
    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        messageResponse.clear();
        messageResponse.setText("Resetting values to all Defaults ...");
        messageResponse.setStyle("-fx-text-inner-color: blue;");
        fileDirectorySelectionButton.setDisable(false);
    }

    /**
     * File Directory Selection Button Selected
     *
     * @param event Reference
     */
    @FXML
    private void handleFileDirButtonAction(ActionEvent event) {
        messageResponse.clear();
        messageResponse.setText("Obtaining File Directory Selector ...");
        messageResponse.setStyle("-fx-text-inner-color: blue;");
        fileDirectorySelectionButton.setDisable(false);

        showDirectoryChooser();

    }

    /**
     * KeyStore File Selection Button Selected
     *
     * @param event Reference
     */
    @FXML
    private void handleKeystoreFileButtonAction(ActionEvent event) {
        messageResponse.clear();
        messageResponse.setText("Obtaining Keystore File Selector ...");
        messageResponse.setStyle("-fx-text-inner-color: blue;");
        keystoreSourceSelectionButton.setDisable(false);

        showKeystoreFileChooser();
    }

    /**
     * Handle the Instance Management Check Box has Changed....
     *
     * @param event Triggered Event
     */
    @FXML
    private void handleInstanceManagementCheckBoxAction(ActionEvent event) {
        setInstanceManagementVisibility(instanceManagement.selectedProperty().getValue());
    }

    /**
     * Yaml File Selection Button Selected
     *
     * @param event Reference
     */
    @FXML
    private void handleYamlConfigurationFileButtonAction(ActionEvent event) {
        messageResponse.clear();
        messageResponse.setText("Obtaining Yaml Configuration File Selector ...");
        messageResponse.setStyle("-fx-text-inner-color: blue;");
        yamlFileSelectionButton.setDisable(false);

        showYamlFileChooser();
    }

    @FXML
    private void handleLoadYAMLButtonAction(ActionEvent event) {
        messageResponse.clear();
        messageResponse.setText("Attempt Loading of requested Yaml Configuration File: "+
                yamlConfigurationFilename.getText());
        messageResponse.setStyle("-fx-text-inner-color: blue;");

        /**
         * Load the YAML File
         */
        try {
            TomcatInstance tomcatInstance =
                    TomcatInstanceBuilderHelper.loadYAMLConfigurationForInstance(yamlConfigurationFilename.getText());
            /**
             * Now that we have the Loaded Tomcat Instance,
             * turn the Loaded Configuration into our current State for UI.
             */
             tomcatInstanceObjectToUIState(tomcatInstance);
            /**
             * Show a Message on UI, that we have Loaded ...
             */
            messageResponse.clear();
            messageResponse.setText("Loaded Yaml Configuration File: "+
                    yamlConfigurationFilename.getText());
            messageResponse.setStyle("-fx-text-inner-color: green;");
            /**
             * Position to correct Tab
             */
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(TAB_INSTANCE_DEFINITIONS);
        } catch (IOException ioe) {
            messageResponse.clear();
            messageResponse.setText("Error Loading Yaml Configuration File: "+
                    yamlConfigurationFilename.getText()+", see Log!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
        }
        /**
         * Clear the Filename once we have it loaded...
         */
        yamlConfigurationFilename.clear();
        loadYAMLButton.setDisable(true);
    }

    /**
     * Save our Configuration to YAML File for later use ...
     * @param event
     */
    @FXML
    private void handleSaveYAMLButtonAction(ActionEvent event) {
        messageResponse.clear();
        messageResponse.setText("Attempt Saving of current state to Yaml Configuration File.");
        messageResponse.setStyle("-fx-text-inner-color: blue;");
        /**
         * Save the YAML File
         */
        if (TomcatInstanceBuilderHelper.generateYAMLConfigurationForInstance(GENERATION_LOGGER,
                this.buildTomcatInstanceObject())) {
            messageResponse.clear();
            messageResponse.setText("Saved of current state to Yaml Configuration File.");
            messageResponse.setStyle("-fx-text-inner-color: green;");
        } else {
            messageResponse.clear();
            messageResponse.setText("Unable to save current state to Yaml Configuration File, see Log!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
        }
    }

    /**
     * Helper to Set the Instance Management Visibility...
     *
     * @param visible indicator
     */
    private void setInstanceManagementVisibility(boolean visible) {
        GENERATION_LOGGER.info("Turning " + (visible ? "On" : "Off") + " Instance Management Features.");
        instanceManagementInfoLabel.setVisible(visible);
        instanceManagementTableView.setVisible(visible);
        instanceManagementPropertyNameLabel.setVisible(visible);
        newInstanceManagementName.setVisible(visible);
        instanceManagementPropertyValueLabel.setVisible(visible);
        newInstanceManagementValue.setVisible(visible);
        newInstanceManagementButton.setVisible(visible);
    }

    /**
     * Helper Method to set Visibility of Secure Instance Parameters.
     *
     * @param event Triggered Event
     */
    @FXML
    private void handleSecureInstanceCheckBoxAction(ActionEvent event) {
        setSecureInstanceVisibility(secureInstance.selectedProperty().getValue());
    }

    /**
     * Helper to Set the Instance Security Visibility...
     *
     * @param visible indicator
     */
    private void setSecureInstanceVisibility(boolean visible) {
        GENERATION_LOGGER.info("Turning " + (visible ? "On" : "Off") + " Instance Secure Features.");
        secureInstanceInfoLabel.setVisible(visible);
        keystoreSourceLabel.setVisible(visible);
        keystoreSourceFilename.setVisible(visible);
        keystoreSourceSelectionButton.setVisible(visible);
        keystoreCredentialsLabel.setVisible(visible);
        keystoreCredentials.setVisible(visible);
        securePortLabel.setVisible(visible);
        securePort.setVisible(visible);
        protocolSecurePortLabel.setVisible(visible);
        protocolSecurePort.setVisible(visible);
    }

    /**
     * Handler for New Instance property Button Pushed...
     *
     * @param event Triggered Event
     */
    @FXML
    private void handleNewInstancePropertyButtonAction(ActionEvent event) {
        if (newInstancePropertyName == null || newInstancePropertyName.getText() == null ||
                newInstancePropertyName.getText().trim().isEmpty()) {
            messageResponse.clear();
            messageResponse.setText("You need to specify a Valid Property Name to Add a new Property!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Validate the Property Name ...
         */
        if (!ValidationHelper.validatePropertyName(newInstancePropertyName.getText().trim())) {
            messageResponse.clear();
            messageResponse.setText("Invalid Instance Property Name, Must be Alpha-Numeric, including dots and underscores and no Whitespace!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Create New Instance Property
         */
        InstancePropertyRow instancePropertyRow = new InstancePropertyRow();
        instancePropertyRow.propertyName.setValue(newInstancePropertyName.getText());
        instancePropertyRow.propertyValue.setValue(newInstancePropertyValue.getText());
        /**
         * Check if Property Already Exists...
         */
        if (instancePropertyData.contains(instancePropertyRow)) {
            messageResponse.clear();
            messageResponse.setText("Property Named " + newInstancePropertyName.getText() + " already exists as an Instance Property!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Add the new Property Row ...
         */
        instancePropertyData.add(instancePropertyRow);
        /**
         * Clear the Added Property Name and Value ...
         */
        newInstancePropertyName.clear();
        newInstancePropertyValue.clear();
        /**
         * Indicate Addition ...
         */
        messageResponse.clear();
        messageResponse.setText("Instance Property Added ...");
        messageResponse.setStyle("-fx-text-inner-color: green;");
    }

    /**
     * Handler for New JVM Option Button Pushed...
     *
     * @param event Triggered Event
     */
    @FXML
    private void handleNewInstanceJVMOptionsButtonAction(ActionEvent event) {
        /**
         * Validate our New JVM Option
         */
        if (newInstanceJVMOptionsName == null || newInstanceJVMOptionsName.getText() == null ||
                !ValidationHelper.validateJVMOption(newInstanceJVMOptionsName.getText())) {
            messageResponse.clear();
            messageResponse.setText("You need to specify a Valid JVM Option, which must begin with a dash(-) to Add a new JVM Option!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Create New Instance Property
         */
        InstanceJVMOptionRow instanceJVMOptionRow = new InstanceJVMOptionRow();
        instanceJVMOptionRow.jvmOption.setValue(newInstanceJVMOptionsName.getText());
        /**
         * Check if Property Already Exists...
         */
        if (instanceJVMOptionsData.contains(instanceJVMOptionRow)) {
            messageResponse.clear();
            messageResponse.setText("JVM Option " + newInstanceJVMOptionsName.getText() + " already exists as an Instance JVM Option!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Add the new Property Row ...
         */
        instanceJVMOptionsData.add(instanceJVMOptionRow);
        /**
         * Clear the Added Property Name and Value ...
         */
        newInstanceJVMOptionsName.clear();
        /**
         * Indicate Addition ...
         */
        messageResponse.clear();
        messageResponse.setText("Instance JVM Option Added ...");
        messageResponse.setStyle("-fx-text-inner-color: green;");
    }

    /**
     * Handler for New Instance Management Property Button Pushed...
     *
     * @param event Triggered Event
     */
    @FXML
    private void handleNewInstanceManagementButtonAction(ActionEvent event) {
        if (newInstanceManagementName == null || newInstanceManagementName.getText() == null ||
                newInstanceManagementName.getText().trim().isEmpty()) {
            messageResponse.clear();
            messageResponse.setText("You need to specify a Valid Instance Management Property Name, to Add a new Property!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Validate the Property Name ...
         */
        if (!ValidationHelper.validatePropertyName(newInstanceManagementName.getText().trim())) {
            messageResponse.clear();
            messageResponse.setText("Invalid Instance Management Property Name, Must be Alpha-Numeric, including dots and underscores and no Whitespace!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }

        /**
         * Create New Instance Property
         */
        InstancePropertyRow instancePropertyRow = new InstancePropertyRow();
        instancePropertyRow.propertyName.setValue(newInstanceManagementName.getText());
        instancePropertyRow.propertyValue.setValue(newInstanceManagementValue.getText());
        /**
         * Check if Property Already Exists...
         */
        if (instanceManagementData.contains(instancePropertyRow)) {
            messageResponse.clear();
            messageResponse.setText("Instance Management Property Named " + newInstanceManagementName.getText() + " already exists as an Property!");
            messageResponse.setStyle("-fx-text-inner-color: red;");
            return;
        }
        /**
         * Add the new Property Row ...
         */
        instanceManagementData.add(instancePropertyRow);
        /**
         * Clear the Added Property Name and Value ...
         */
        newInstanceManagementName.clear();
        newInstanceManagementValue.clear();
        /**
         * Indicate Addition ...
         */
        messageResponse.clear();
        messageResponse.setText("Instance Management Property Added ...");
        messageResponse.setStyle("-fx-text-inner-color: green;");
    }
    
    /**
     * Check to validate we can proceed with a generation based upon what has been
     * specified at this time.
     *
     * @return boolean indicating if Generation Process if Ready to Roll...
     */
    private boolean isGenerationReady() {
        /**
         * Check for an Instance Name ...
         */
        if (instanceName == null || instanceName.getText() == null || instanceName.getText().trim().isEmpty() ||
                !ValidationHelper.validateInstanceName(instanceName.getText().trim())) {
            messageResponse.clear();
            messageResponse.setText("Unable to Generate, as Instance Name has not been specified or Invalid!");
            messageResponse.setStyle("-fx-text-inner-color: red;");

            /**
             * Position to correct Tab
             */
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(TAB_INSTANCE_DEFINITIONS);

            return false;
        }

        /**
         * Check for Destination Folder ...
         */
        if (destinationFolder == null || destinationFolder.getText() == null || destinationFolder.getText().trim().isEmpty()) {
            messageResponse.clear();
            messageResponse.setText("Unable to Generate, as Destination Folder has not been specified!");
            messageResponse.setStyle("-fx-text-inner-color: red;");

            /**
             * Position to correct Tab
             */
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(TAB_INSTANCE_DEFINITIONS);

            return false;
        }
        /**
         * Check for Valid Ports
         */
        if (!validatePrimaryPortsSpecified()) {
            /**
             * Position to correct Tab
             */
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(TAB_INSTANCE_PORTS);

            return false;
        }
        /**
         * Check for Valid Ports
         */
        if (!validateSecurePortSpecified()) {
            /**
             * Position to correct Tab
             */
            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
            selectionModel.select(TAB_INSTANCE_SECURITY);

            return false;
        }
        /**
         * Position to Generation Log to begin Generation Process ...
         */
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.select(TAB_LOGGER);
        return true;
    }

    /**
     * Directory Chooser
     */
    private void showDirectoryChooser() {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (destinationFolder.getText() != null && !destinationFolder.getText().isEmpty()) {
            File startingPosition = new File(destinationFolder.getText());
            if (startingPosition.exists() && startingPosition.isDirectory()) {
                directoryChooser.setInitialDirectory(startingPosition);
            }
        }
        File selectedDirectory = directoryChooser.showDialog(null);

        if (selectedDirectory != null) {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: green;");
            messageResponse.setText("Destination Folder selected: " + selectedDirectory.getAbsolutePath());

            destinationFolder.setText(selectedDirectory.getAbsolutePath());
        } else {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("Destination Folder selection cancelled.");
        }
    }

    /**
     * Keystore File Chooser
     */
    private void showKeystoreFileChooser() {

        FileChooser fileChooser = new FileChooser();
        if (keystoreSourceFilename.getText() != null && !keystoreSourceFilename.getText().isEmpty()) {
            File startingPosition = new File(keystoreSourceFilename.getText());
            if (startingPosition.exists() && startingPosition.isFile()) {
                fileChooser.setInitialDirectory(startingPosition.getParentFile());
            }
        }
        File selectedKeystoreFile = fileChooser.showOpenDialog(null);

        if (selectedKeystoreFile != null) {

            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: green;");
            messageResponse.setText("KeyStore File selected: " + selectedKeystoreFile.getName());

            keystoreSourceFilename.setText(selectedKeystoreFile.getAbsolutePath());
        } else {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("Keystore File selection cancelled.");
        }
    }

    /**
     * Yaml File Chooser
     */
    private void showYamlFileChooser() {

        FileChooser fileChooser = new FileChooser();
        if (yamlConfigurationFilename.getText() != null && !yamlConfigurationFilename.getText().isEmpty()) {
            File startingPosition = new File(yamlConfigurationFilename.getText());
            if (startingPosition.exists() && startingPosition.isFile()) {
                fileChooser.setInitialDirectory(startingPosition.getParentFile());
            }
        }
        File selectedYamlFile = fileChooser.showOpenDialog(null);

        if (selectedYamlFile != null) {

            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: green;");
            messageResponse.setText("Yaml File selected: " + selectedYamlFile.getName());

            yamlConfigurationFilename.setText(selectedYamlFile.getAbsolutePath());
            // Enable the Load Button now that we have a File dialed in....
            loadYAMLButton.setDisable(false);
        } else {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("Yaml File selection cancelled.");
        }
    }

    /**
     * Reset the instance to all Default Ports.
     */
    private void setDefaultPorts() {
        primaryPort.setText(DefaultDefinitions.DEFAULT_PRIMARY_PORT.toString());

        securePort.setText(DefaultDefinitions.DEFAULT_SECURE_PORT.toString());

        shutdownPort.setText(DefaultDefinitions.DEFAULT_SHUTDOWN_PORT.toString());

        ajpPort.setText(DefaultDefinitions.DEFAULT_AJP_PORT.toString());
    }

    /**
     * Worker Task for Generation process.
     *
     * @return Task Child Thread Generation Process ...
     */
    private Task<Void> createWorker() {
        /**
         * Instantiate a Tomcat Instance and Builder Based upon Parameters Derived from GUI.
         */
        final TomcatInstance tomcatInstance = buildTomcatInstanceObject();
        /**
         * Now Create a Builder with the Instance Information to Drive the Generation Process.
         */
        final TomcatInstanceBuilderForUI tomcatInstanceBuilderForUI =
                new TomcatInstanceBuilderForUI(GENERATION_LOGGER, tomcatInstance);

        /**
         * Return Concurrent Generation Task
         */
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                /**
                 * Set our Reference to Bind the Progress of the Task ...
                 */
                tomcatInstanceBuilderForUI.progressProperty().addListener((obs, oldProgress, newProgress) ->
                        updateProgress(newProgress.doubleValue(), 10));

                /**
                 * Perform the Generation Process ...
                 */
                updateMessage("Beginning Generation Process ...");
                if (tomcatInstanceBuilderForUI.performGenerationProcess()) {
                    /**
                     * All Done ...
                     */
                    updateMessage(TASK_STATE_DONE);
                } else {
                    /**
                     * All Done, but Failure ...
                     */
                    updateMessage(TASK_STATE_FAILURE);
                }
                return null;
            }
        };
        /**
         * Return Instantiated Task
         */
        return task;
    }

    /**
     * Validate currently Specified Ports ...
     *
     * @return boolean indicates if Ports are Valid or Not ...
     */
    private boolean validatePrimaryPortsSpecified() {
        Integer selectedPrimaryPortChoice = ValidationHelper.validatePort(primaryPort.getText());
        if (selectedPrimaryPortChoice == null) {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("Primary Port is not Valid, please re-specify!");
            return false;
        }
        Integer selectedShutdownPortChoice = ValidationHelper.validatePort(shutdownPort.getText());
        if (selectedShutdownPortChoice == null) {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("Shutdown Port is not Valid, please re-specify!");
            return false;
        }
        Integer selectedAjpPortChoice = ValidationHelper.validatePort(ajpPort.getText());
        if (selectedAjpPortChoice == null) {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("AJP Port is not Valid, please re-specify!");
            return false;
        }
        /**
         * Now Check for Duplicates
         */
        if (ValidationHelper.validatePortsUnique(getListOfPrimaryPorts())) {
            return true;
        } else {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("A Port is Duplicate with another Port, please re-specify!");
            return false;
        }
    }

    /**
     * Validate currently Specified Secure Port ...
     *
     * @return boolean indicates if Ports are Valid or Not ...
     */
    private boolean validateSecurePortSpecified() {
        if (secureInstance.selectedProperty().getValue()) {
            Integer selectedSecurePortChoice = ValidationHelper.validatePort(securePort.getText());
            if (selectedSecurePortChoice == null) {
                messageResponse.clear();
                messageResponse.setStyle("-fx-text-inner-color: red;");
                messageResponse.setText("Secure Port is not Valid, please re-specify!");
                return false;
            }
        } else {
           securePort.setText(DefaultDefinitions.DEFAULT_CATALINA_SECURE_PROTOCOL_SELECTED);
        }
        /**
         * Now Check for Duplicates
         */
        List<Integer> ports = getListOfPrimaryPorts();
        ports.add(ValidationHelper.validatePort(securePort.getText()));
        if (ValidationHelper.validatePortsUnique(ports)) {
            return true;
        } else {
            messageResponse.clear();
            messageResponse.setStyle("-fx-text-inner-color: red;");
            messageResponse.setText("Secure Port is Duplicate with another Port, please re-specify!");
            return false;
        }
    }

    /**
     * Obtain a List of Primary Ports.
     * @return List of Primary Ports for Instance.
     */
    private List<Integer> getListOfPrimaryPorts() {
        List<Integer> ports = new ArrayList<>();
        ports.add(ValidationHelper.validatePort(primaryPort.getText()));
        ports.add(ValidationHelper.validatePort(shutdownPort.getText()));
        ports.add(ValidationHelper.validatePort(ajpPort.getText()));
        return ports;
    }

    /**
     * Initialize Instance JVM Options ...
     */
    private void initializeInstanceJVMOptions() {
        /**
         * Initialize Choice Items ...
         */
        jvmOptionXms.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS));
        jvmOptionXms.setValue(DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS_SELECTED);

        jvmOptionXmx.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS));
        jvmOptionXmx.setValue(DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS_SELECTED);

        jvmOptionXss.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS));
        jvmOptionXss.setValue(DefaultDefinitions.DEFAULT_JVM_MEMORY_SS_OPTION_SELECTED);

        jvmOptionXXMaxMetaspaceSize.setItems(
                FXCollections.observableArrayList(DefaultDefinitions.DEFAULT_JVM_MEMORY_OPTIONS));
        jvmOptionXXMaxMetaspaceSize.setValue(DefaultDefinitions.DEFAULT_JVM_MEMORY_METASPACE_OPTION_SELECTED);

        /**
         * Initialize the JVM Options Table View ...
         */
        instanceJVMOptionsTableView.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingPropertyCell();
                    }
                };

        instanceJVMOptionsNameColumn.setCellFactory(cellFactory);
        instanceJVMOptionsNameColumn.setCellValueFactory(
                new PropertyValueFactory<InstanceJVMOptionRow, String>("jvmOption")
        );
        instanceJVMOptionsNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<InstanceJVMOptionRow, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<InstanceJVMOptionRow, String> t) {
                        /**
                         * Validate the Property Name change, and if the Name is now
                         * Empty or Null, remove the Property.
                         */
                        if (t.getNewValue() == null || t.getNewValue().isEmpty()) {

                            instanceJVMOptionsData.remove(t.getTablePosition().getRow());

                            messageResponse.clear();
                            messageResponse.setStyle("-fx-text-inner-color: blue;");
                            messageResponse.setText("Instance JVM Property has been removed.");

                        } else if (t.getNewValue().startsWith("-")) {

                            ((InstanceJVMOptionRow) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setJvmOption(t.getNewValue());

                            messageResponse.clear();
                            messageResponse.setStyle("-fx-text-inner-color: green;");
                            messageResponse.setText("Instance JVM Property has been Updated.");

                        } else {
                            messageResponse.clear();
                            messageResponse.setText("You need to specify a Valid JVM Option, which must begin with a dash(-) to Add a new JVM Option!");
                            messageResponse.setStyle("-fx-text-inner-color: red;");
                        }

                    }
                }
        );

        instanceJVMOptionsNameColumn.setStyle(
                "-fx-font-size: 12 Courier New; -fx-font-weight: bold;");

        /**
         * Establish Data Items.
         */
        instanceJVMOptionsTableView.setItems(instanceJVMOptionsData);

        /**
         * Set Default JVM Options
         */
        for (InstanceJVMOptionRow instanceJVMOptionRow : DefaultDefinitions.DEFAULT_JVM_OPTION_ROWS) {
            instanceJVMOptionsData.add(instanceJVMOptionRow);
        }
    }

    /**
     * Initialize the Instance Properties Constructs ...
     */
    private void initializeInstancePropertiesTableView() {
        /**
         * Set up the Instance Property TableView
         */
        instancePropertyTableView.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingPropertyCell();
                    }
                };

        instancePropertyNameColumn.setCellFactory(cellFactory);
        instancePropertyNameColumn.setCellValueFactory(
                new PropertyValueFactory<InstancePropertyRow, String>("propertyName")
        );
        instancePropertyNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<InstancePropertyRow, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<InstancePropertyRow, String> t) {
                        /**
                         * Validate the Property Name change, and if the Name is now
                         * Empty or Null, remove the Property.
                         */
                        if (t.getNewValue() == null || t.getNewValue().isEmpty()) {

                            instancePropertyData.remove(t.getTablePosition().getRow());

                            messageResponse.clear();
                            messageResponse.setStyle("-fx-text-inner-color: blue;");
                            messageResponse.setText("Instance Property has been removed.");

                        } else {

                            ((InstancePropertyRow) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setPropertyName(t.getNewValue());

                            messageResponse.clear();
                            messageResponse.setStyle("-fx-text-inner-color: green;");
                            messageResponse.setText("Instance Property has been Updated.");

                        }

                    }
                }
        );

        instancePropertyValueColumn.setCellFactory(cellFactory);
        instancePropertyValueColumn.setCellValueFactory(
                new PropertyValueFactory<InstancePropertyRow, String>("propertyValue")
        );
        instancePropertyValueColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<InstancePropertyRow, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<InstancePropertyRow, String> t) {
                        ((InstancePropertyRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPropertyValue(t.getNewValue());
                    }
                }
        );

        instancePropertyNameColumn.setStyle(
                "-fx-font-size: 12 Courier New; -fx-font-weight: bold;");

        instancePropertyValueColumn.setStyle(
                "-fx-font-size: 12 Courier New; -fx-font-weight: bold;");


        /**
         * Establish Data Items.
         */
        instancePropertyTableView.setItems(instancePropertyData);

        /**
         * Set Default Management Instance Level Properties...
         */
        for (InstancePropertyRow instancePropertyRow : DefaultDefinitions.DEFAULT_INSTANCE_PROPERTY_ROWS) {
            instancePropertyData.add(instancePropertyRow);
        }
    }

    /**
     * Initialize Instance Management Options ...
     */
    private void initializeInstanceManagementPropertiesTableView() {
        /**
         * Initialize the Management Properties Table View ...
         */
        instanceManagementTableView.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingPropertyCell();
                    }
                };

        instanceManagementNameColumn.setCellFactory(cellFactory);
        instanceManagementNameColumn.setCellValueFactory(
                new PropertyValueFactory<InstancePropertyRow, String>("propertyName")
        );
        instanceManagementNameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<InstancePropertyRow, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<InstancePropertyRow, String> t) {
                        /**
                         * Validate the Property Name change, and if the Name is now
                         * Empty or Null, remove the Property.
                         */
                        if (t.getNewValue() == null || t.getNewValue().isEmpty()) {

                            instanceManagementData.remove(t.getTablePosition().getRow());

                            messageResponse.clear();
                            messageResponse.setStyle("-fx-text-inner-color: blue;");
                            messageResponse.setText("Instance Management Property has been removed.");

                        } else {

                            ((InstancePropertyRow) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setPropertyName(t.getNewValue());

                            messageResponse.clear();
                            messageResponse.setStyle("-fx-text-inner-color: green;");
                            messageResponse.setText("Instance Management Property has been Updated.");

                        }
                    }
                }
        );

        instanceManagementValueColumn.setCellFactory(cellFactory);
        instanceManagementValueColumn.setCellValueFactory(
                new PropertyValueFactory<InstancePropertyRow, String>("propertyValue")
        );
        instanceManagementValueColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<InstancePropertyRow, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<InstancePropertyRow, String> t) {
                        ((InstancePropertyRow) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPropertyValue(t.getNewValue());
                    }
                }
        );

        instanceManagementNameColumn.setStyle(
                "-fx-font-size: 12 Courier New; -fx-font-weight: bold;");

        instanceManagementValueColumn.setStyle(
                "-fx-font-size: 12 Courier New; -fx-font-weight: bold;");

        /**
         * Establish Data Items.
         */
        instanceManagementTableView.setItems(instanceManagementData);

        /**
         * Set Default Management Instance Level Properties...
         */
        for (InstancePropertyRow instancePropertyRow : DefaultDefinitions.DEFAULT_MANAGEMENT_PROPERTY_ROWS) {
            instanceManagementData.add(instancePropertyRow);
        }

    }

    /**
     * Helper Method to simple build the Tomcat Instance
     * Object for driving the generation processing ...
     */
    private TomcatInstance buildTomcatInstanceObject() {
        /**
         * Convert Instance Properties
         */
        List<TomcatInstanceProperty> instanceProperties = new ArrayList<>(instancePropertyData.size());
        for (InstancePropertyRow instancePropertyRow : instancePropertyData) {
            instanceProperties.add(new TomcatInstanceProperty(instancePropertyRow.getPropertyName(),
                    instancePropertyRow.getPropertyValue()));
        }
        /**
         * Convert Instance Management Properties
         */
        List<TomcatInstanceProperty> instanceManagementProperties = new ArrayList<>(instanceManagementData.size());
        for (InstancePropertyRow instancePropertyRow : instanceManagementData) {
            instanceManagementProperties.add(new TomcatInstanceProperty(instancePropertyRow.getPropertyName(),
                    instancePropertyRow.getPropertyValue()));
        }
        /**
         * Convert Instance JVM Options
         */
        List<String> instanceJVMOptions = new ArrayList<>(instanceJVMOptionsData.size());
        for (InstanceJVMOptionRow instanceJVMOptionRow : instanceJVMOptionsData) {
            instanceJVMOptions.add(instanceJVMOptionRow.getJvmOption());
        }

        /**
         * Instantiate and return the Tomcat Instance POJO.
         */
        return new TomcatInstance(
                instanceName.getText(),
                environmentChoice.getValue(),
                tcVersionChoice.getItems().get(selectedTomcatVersionChoice),
                new File(destinationFolder.getText()),
                compressFolderOutput.selectedProperty().getValue(),
                ValidationHelper.validatePort(primaryPort.getText().trim()),
                protocolPrimaryPort.getValue(),
                ValidationHelper.validatePort(shutdownPort.getText().trim()),
                ValidationHelper.validatePort(ajpPort.getText().trim()),
                secureInstance.selectedProperty().getValue(),
                ValidationHelper.validatePort(securePort.getText().trim()),
                protocolSecurePort.getValue(),
                keystoreSourceFilename.getText(),
                keystoreCredentials.getText(),
                jvmOptionXms.getValue(),
                jvmOptionXmx.getValue(),
                jvmOptionXss.getValue(),
                jvmOptionXXMaxMetaspaceSize.getValue(),
                instanceJVMOptions,
                instanceManagement.selectedProperty().getValue(),
                instanceManagementProperties,
                instanceProperties
        );
    }

    /**
     * Helper Method to simple build the Tomcat Instance
     * Object for driving the generation processing ...
     */
    private void tomcatInstanceObjectToUIState(TomcatInstance tomcatInstance) {
        /**
         * Set UI State from YAML
         */
        instanceName.setText(tomcatInstance.getInstanceName());
        environmentChoice.setValue(tomcatInstance.getEnvironmentName());
        tcVersionChoice.setValue(tomcatInstance.getTomcatVersion());
        destinationFolder.setText(tomcatInstance.getDestinationFolder().getAbsolutePath());
        compressFolderOutput.selectedProperty().setValue(tomcatInstance.isCompressed());

        primaryPort.setText(tomcatInstance.getPrimaryPort().toString());
        protocolPrimaryPort.setValue(tomcatInstance.getProtocolPrimaryPort());
        shutdownPort.setText(tomcatInstance.getShutdownPort().toString());
        ajpPort.setText(tomcatInstance.getAjpPort().toString());

        secureInstance.selectedProperty().setValue(tomcatInstance.isSecureInstance());
        securePort.setText(tomcatInstance.getSecurePort().toString());
        protocolSecurePort.setValue(tomcatInstance.getProtocolSecurePort());
        keystoreSourceFilename.setText(tomcatInstance.getKeystoreSourceFilename());
        keystoreCredentials.setText(tomcatInstance.getKeystoreCredentials());

        jvmOptionXms.setValue(tomcatInstance.getJvmOptionXms());
        jvmOptionXmx.setValue(tomcatInstance.getJvmOptionXmx());
        jvmOptionXss.setValue(tomcatInstance.getJvmOptionXss());
        jvmOptionXXMaxMetaspaceSize.setValue(tomcatInstance.getJvmOptionXXMaxMetaspaceSize());

        instanceManagement.selectedProperty().setValue(tomcatInstance.isInstanceManagement());

        /**
         * Load Instance Properties
         */
        instancePropertyData.clear();
        for(TomcatInstanceProperty tomcatInstanceProperty : tomcatInstance.getInstanceProperties()) {
            InstancePropertyRow instancePropertyRow = new InstancePropertyRow();
            instancePropertyRow.setPropertyName(tomcatInstanceProperty.getPropertyName());
            instancePropertyRow.setPropertyValue(tomcatInstanceProperty.getPropertyValue());
            instancePropertyData.add(instancePropertyRow);
        }
        /**
         * Load Instance Management Properties
         */
        instanceManagementData.clear();
        for(TomcatInstanceProperty tomcatInstanceProperty : tomcatInstance.getInstanceManagementProperties()) {
            InstancePropertyRow instancePropertyRow = new InstancePropertyRow();
            instancePropertyRow.setPropertyName(tomcatInstanceProperty.getPropertyName());
            instancePropertyRow.setPropertyValue(tomcatInstanceProperty.getPropertyValue());
            instanceManagementData.add(instancePropertyRow);
        }
        /**
         * Load Instance JVM Options
         */
        instanceJVMOptionsData.clear();
        for(String jvmOption : tomcatInstance.getJvmOptions()) {
            InstanceJVMOptionRow instanceJVMOptionRow = new InstanceJVMOptionRow();
            instanceJVMOptionRow.setJvmOption(jvmOption);
            instanceJVMOptionsData.add(instanceJVMOptionRow);
        }
        /**
         * Now set UI widgets per setting we just rendered ...
         */
        setInstanceManagementVisibility(instanceManagement.selectedProperty().getValue());
        setSecureInstanceVisibility(secureInstance.selectedProperty().getValue());
        
    }

    /**
     * Determine based upon current Required Data established, if we can write what we have
     * to a Yaml file.
     * @return boolean Indicator which indicates if all Required Data Fields have been Entered or Not...
     */
    private boolean haveAllRequiredFieldsBeenSet() {
       /**
         * Check for an Instance Name ...
         */
        if (instanceName == null || instanceName.getText() == null || instanceName.getText().trim().isEmpty() ||
                !ValidationHelper.validateInstanceName(instanceName.getText().trim())) {
            return false;
        }

        /**
         * Check for Instance Environment
         */
        if(environmentChoice.getValue() == null || environmentChoice.getValue().trim().isEmpty()) {
            return false;
        }

        /**
         * Check for Tomcat Version
         */
        if(tcVersionChoice.getValue() == null || tcVersionChoice.getValue().trim().isEmpty()) {
            return false;
        }

        /**
         * Check for Destination Folder ...
         */
        if (destinationFolder == null || destinationFolder.getText() == null || destinationFolder.getText().trim().isEmpty()) {
            return false;
        } else {
            File destFolder = new File(destinationFolder.getText());
            if (!destFolder.exists() || !destFolder.canWrite()) {
                return false;
            }
        }
        /**
         * Falling Through indicates we have all required Data Fields.
         */
        return true;

    }

    /**
     * initializeGenerationLoggerPane
     * Initialize the Logger Pane ...
     */
    private void initializeGenerationLoggerPane() {
        LogView logView = new LogView(GENERATION_LOGGER);
        logView.setPrefWidth(949);
        logView.setPrefHeight(296.0);

        ChoiceBox<Level> filterLevel = new ChoiceBox<>(
                FXCollections.observableArrayList(
                        Level.values()
                )
        );
        filterLevel.getSelectionModel().select(Level.DEBUG);
        logView.filterLevelProperty().bind(
                filterLevel.getSelectionModel().selectedItemProperty()
        );

        ToggleButton showTimestamp = new ToggleButton("Show Timestamp");
        logView.showTimeStampProperty().bind(showTimestamp.selectedProperty());

        ToggleButton tail = new ToggleButton("Tail");
        tail.selectedProperty().set(true);
        tail.setVisible(true);
        logView.tailProperty().bind(tail.selectedProperty());

        ToggleButton pause = new ToggleButton("Pause");
        pause.selectedProperty().set(false);
        pause.setVisible(true);
        logView.pausedProperty().bind(pause.selectedProperty());

        Slider rate = new Slider(0.1, 60, 60);
        logView.refreshRateProperty().bind(rate.valueProperty());
        rate.setVisible(true);
        Label rateLabel = new Label();
        rateLabel.textProperty().bind(Bindings.format("Update: %.2f fps", rate.valueProperty()));
        rateLabel.setStyle("-fx-font-family: monospace;");
        rateLabel.setVisible(true);
        VBox rateLayout = new VBox(rate, rateLabel);
        rateLayout.setAlignment(Pos.CENTER);
        rateLayout.setVisible(false);

        HBox controls = new HBox(
                10,
                filterLevel,
                showTimestamp,
                tail,
                pause,
                rateLayout
        );
        controls.setMinHeight(HBox.USE_PREF_SIZE);

        VBox layout = new VBox(
                10,
                controls,
                logView
        );
        VBox.setVgrow(logView, Priority.ALWAYS);
        layout.setLayoutX(1.0);
        layout.setLayoutY(14.0);

        /**
         * Attach the Logger VBox Viewer to our Tab Anchor Pane...
         */
        generationLogAnchorPane.getChildren().add(layout);
    }
}

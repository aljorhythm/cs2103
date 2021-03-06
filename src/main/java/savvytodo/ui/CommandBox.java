package savvytodo.ui;

import java.util.logging.Logger;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import savvytodo.commons.core.LogsCenter;
import savvytodo.commons.events.ui.NewResultAvailableEvent;
import savvytodo.commons.util.FxViewUtil;
import savvytodo.logic.Logic;
import savvytodo.logic.commands.CommandResult;
import savvytodo.logic.commands.exceptions.CommandException;

public class CommandBox extends UiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";
    public static final String ERROR_STYLE_CLASS = "error";
    //@@author A0147827U
    private Logic logic;
    private AutoCompletionBinding<String> binding;
    @FXML
    private TextField commandTextField;

    public CommandBox(AnchorPane commandBoxPlaceholder, Logic logic) {
        super(FXML);
        this.logic = logic;
        binding = TextFields.bindAutoCompletion(commandTextField, AutoCompleteDictionaryFactory.getDictionary());
        addToPlaceholder(commandBoxPlaceholder);
    }
    //@@author
    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @FXML
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());

            // process result of the command
            setStyleToIndicateCommandSuccess();
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    //@@author A0147827U
    /**
     * Executes the given string as a command as though it was from the text input
     * @author jingloon
     * @param command
     */
    public void executeExternalCommand(String command) {
        try {
            CommandResult commandResult = logic.execute(command);
            // process result of the command
            setStyleToIndicateCommandSuccess();
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        commandTextField.getStyleClass().add(ERROR_STYLE_CLASS);
    }

    //@@author A0140036X
    /**
     * Sets logic for command box
     * @param logic
     */
    public void setLogic(Logic logic) {
        this.logic = logic;
    }
    //@@author A0147827U
    /**
     * Disables the auto complete feature
     */
    public void disableAutoComplete() {
        binding.dispose();
    }
    /**
     * Enables the auto complete feature
     */
    public void enableAutoComplete() {
        binding = TextFields.bindAutoCompletion(commandTextField, AutoCompleteDictionaryFactory.getDictionary());
    }
    //@@author
}

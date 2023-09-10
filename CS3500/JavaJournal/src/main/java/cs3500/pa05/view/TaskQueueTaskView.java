package cs3500.pa05.view;

import cs3500.pa05.controller.MasterController;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.enums.CompletionStatus;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/**
 * The type Task queue task view.
 */
public class TaskQueueTaskView extends HBox implements JournalViewItem<Task, HBox> {

  /**
   * The Controller.
   */
  private final MasterController controller;

  /**
   * Instantiates a new Task queue task view.
   *
   * @param controller the controller
   */
  public TaskQueueTaskView(MasterController controller) {
    this.controller = controller;
  }

  /**
   * Set status.
   *
   * @param task       the task
   * @param isSelected the is selected
   */
  private void setStatus(Task task, boolean isSelected) {
    if (isSelected) {
      task.setCompletionStatus(CompletionStatus.COMPLETE);
    } else {
      task.setCompletionStatus(CompletionStatus.INCOMPLETE);
    }
    this.controller.updateTaskQueueStats();
    this.controller.updateCalendar();
  }

  /**
   * Create HBox based on the task.
   *
   * @param item the item to base the box on
   * @return the HBox
   */
  @Override
  public HBox create(Task item) {
    CheckBox checkBox = new CheckBox();
    checkBox.getStyleClass().add("checkbox");
    Label label = new Label(item.getEntryName());
    label.setWrapText(true);
    checkBox.setOnAction(
        event -> this.setStatus(item, checkBox.isSelected()));
    if (item.getStatus() == CompletionStatus.COMPLETE) {
      checkBox.fire();
    }
    checkBox.setPadding(new Insets(0, 10, 0, 0));
    HBox box = new HBox();
    box.getChildren().addAll(checkBox, label);
    box.setMinHeight(Region.USE_PREF_SIZE);
    return box;
  }
}

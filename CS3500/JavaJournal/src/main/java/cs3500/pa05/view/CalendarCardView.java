package cs3500.pa05.view;

import cs3500.pa05.model.CalendarEntry;
import cs3500.pa05.model.enums.CompletionStatus;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


/**
 * Represents a calendar card view.
 */
public class CalendarCardView extends VBox implements JournalViewItem<CalendarEntry, VBox> {

  /**
   * The constant IMAGE_WIDTH.
   */
  private static final int IMAGE_WIDTH = 72;
  /**
   * The constant IMAGE_HEIGHT.
   */
  private static final int IMAGE_HEIGHT = 18;

  /**
   * Create box v box.
   *
   * @return the v box
   */
  private static VBox createBox() {
    VBox box = new VBox();
    box.setSpacing(5);
    box.getStyleClass().add("event-card");
    box.setMinHeight(Region.USE_PREF_SIZE);
    return box;
  }

  /**
   * Create name label label.
   *
   * @param entryName the entry name
   * @return the label
   */
  private static Label createNameLabel(String entryName) {
    Label name = new Label();
    name.setText(entryName);
    name.setStyle("-fx-font-weight: bold");
    name.setWrapText(true);
    return name;
  }

  /**
   * Create description label label.
   *
   * @param textDescription the text description
   * @return the label
   */
  private static Label createDescriptionLabel(String textDescription) {
    Label description = new Label(textDescription);
    description.setWrapText(true);
    VBox.setVgrow(description, Priority.ALWAYS);
    return description;
  }

  /**
   * Create status image view.
   *
   * @param status the status
   * @param box    the box
   * @return the image view
   */
  private static ImageView createStatusImageView(CompletionStatus status, VBox box) {
    ImageView statusImageView = new ImageView();
    statusImageView.setFitWidth(CalendarCardView.IMAGE_WIDTH);
    statusImageView.setFitHeight(CalendarCardView.IMAGE_HEIGHT);
    statusImageView.setPreserveRatio(true);

    Image image;
    switch (status) {
      case COMPLETE -> {
        image = new Image("ybox.png");
        statusImageView.setImage(image);
        box.setStyle("-fx-background-color: #E1E1E1FF");
      }
      case INCOMPLETE -> {
        image = new Image("xbox.png");
        statusImageView.setImage(image);
      }
      default -> {
      }
      // Do nothing
    }

    return statusImageView;
  }

  /**
   * Create label box h box.
   *
   * @param nameLabel       the name label
   * @param statusImageView the status image view
   * @return the h box
   */
  private static HBox createLabelBox(Label nameLabel, ImageView statusImageView) {
    HBox labelBox = new HBox();
    labelBox.setSpacing(5);
    labelBox.setFillHeight(true);
    labelBox.getChildren().addAll(nameLabel, statusImageView);
    labelBox.setAlignment(Pos.TOP_LEFT);
    return labelBox;
  }

  /**
   * Create v box based on an entry.
   *
   * @param item the item
   * @return the v box
   */
  @Override
  public VBox create(CalendarEntry item) {
    VBox box = CalendarCardView.createBox();
    Label name = CalendarCardView.createNameLabel(item.getEntryName());
    Label description = CalendarCardView.createDescriptionLabel(item.textDescription());
    ImageView status = CalendarCardView.createStatusImageView(item.getStatus(), box);

    HBox labelBox = CalendarCardView.createLabelBox(name, status);
    box.getChildren().addAll(labelBox, description);

    return box;
  }
}

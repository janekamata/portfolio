package cs3500.pa05.controller;

import cs3500.pa05.model.JournalInteractable;

/**
 * Represents a master controller.
 */
public interface MasterController extends JournalController {

  /**
   * Update view.
   */
  void updateView();

  /**
   * Update journal.
   *
   * @param other the other journal
   */
  void updateJournal(JournalInteractable other);

  /**
   * Update calendar.
   */
  void updateCalendar();

  /**
   * Update task queue stats.
   */
  void updateTaskQueueStats();
}

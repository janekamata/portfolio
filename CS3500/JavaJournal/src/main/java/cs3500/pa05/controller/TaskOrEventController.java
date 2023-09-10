package cs3500.pa05.controller;

import cs3500.pa05.model.enums.CalendarFeature;

/**
 * Represents a task or event controller for a journal.
 */
public interface TaskOrEventController extends JournalController {

  /**
   * If true, changing task limit If false, changing event limit
   *
   * @param calendarFeatureSetting the calendar feature setting
   */
  void setTaskOrEvent(CalendarFeature calendarFeatureSetting);
}

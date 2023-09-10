module cs3500.pa05 {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.databind;
  requires org.controlsfx.controls;
  requires java.desktop;
  opens cs3500.pa05 to javafx.fxml;
  exports cs3500.pa05;
  exports cs3500.pa05.controller;
  exports cs3500.pa05.model;
  exports cs3500.pa05.view;
  opens cs3500.pa05.controller to javafx.fxml;
  exports cs3500.pa05.controller.handlers;
  opens cs3500.pa05.controller.handlers to javafx.fxml;
  exports cs3500.pa05.controller.handlers.event;
  opens cs3500.pa05.controller.handlers.event to javafx.fxml;
  exports cs3500.pa05.controller.handlers.task;
  opens cs3500.pa05.controller.handlers.task to javafx.fxml;
  opens cs3500.pa05.model to com.fasterxml.jackson.databind;
  exports cs3500.pa05.controller.subcontrollers;
  opens cs3500.pa05.controller.subcontrollers to javafx.fxml;
  exports cs3500.pa05.model.enums;
  opens cs3500.pa05.model.enums to com.fasterxml.jackson.databind;
  exports cs3500.pa05.controller.handlers.file;
  opens cs3500.pa05.controller.handlers.file to javafx.fxml;
  exports cs3500.pa05.model.comparators;
  opens cs3500.pa05.model.comparators to com.fasterxml.jackson.databind;

}
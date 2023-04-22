/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2SEModule/module-info.java to edit this template
 */

module main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires javafx.graphics;

    opens seniorproject to javafx.fxml;
    
    exports seniorproject;
}

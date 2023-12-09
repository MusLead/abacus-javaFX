module de.uniks.abacus {

    requires java.base; // Most modules will require java.base at a minimum

    requires javafx.controls;
    requires javafx.fxml;
    requires fulibYaml;
    requires java.desktop;

    opens de.uniks.abacus to javafx.fxml;
    exports de.uniks.abacus ;
}
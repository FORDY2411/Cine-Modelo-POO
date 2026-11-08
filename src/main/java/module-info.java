module com.mycompany.cine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.cine to javafx.fxml;
    exports com.mycompany.cine;

    opens com.mycompany.cine.controladores to javafx.fxml;
    exports com.mycompany.cine.controladores;

    opens com.mycompany.cine.modelo to javafx.fxml, javafx.base;
    exports com.mycompany.cine.modelo;

    opens com.mycompany.cine.persistencia to javafx.fxml;
    exports com.mycompany.cine.persistencia;

    opens com.mycompany.cine.daos to javafx.fxml;
    exports com.mycompany.cine.daos;
}

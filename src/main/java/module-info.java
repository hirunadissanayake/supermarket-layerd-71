module lk.ijse.gdse71.supermarketfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;
    requires java.mail;
    requires net.sf.jasperreports.core;
    requires mysql.connector.j;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens lk.ijse.gdse71.supermarketfx.view.tdm to javafx.base;
    opens lk.ijse.gdse71.supermarketfx.controller to javafx.fxml;
    opens lk.ijse.gdse71.supermarketfx.entity to org.hibernate.orm.core;
    opens lk.ijse.gdse71.supermarketfx.config to jakarta.persistence;
    exports lk.ijse.gdse71.supermarketfx;
}
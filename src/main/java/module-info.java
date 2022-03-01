module monopoly.monopoly_bank {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires spring.context;

    opens monopoly.monopoly_bank to javafx.fxml;
    exports monopoly.monopoly_bank;
    exports monopoly.monopoly_bank.logic.titledeeds;
    exports monopoly.monopoly_bank.logic.player;
}
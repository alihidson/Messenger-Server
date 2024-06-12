module moneyexchange.messengerserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens moneyexchange.messengerserver to javafx.fxml;
    exports messengerserver;
}
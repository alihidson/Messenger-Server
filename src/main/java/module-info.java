module moneyexchange.messenger {
    requires javafx.controls;
    requires javafx.fxml;


    opens messenger to javafx.fxml;
    exports messenger;
}
module com.example.tictactoe_l3_info {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tictactoe_l3_info to javafx.fxml;
    exports com.example.tictactoe_l3_info;
}
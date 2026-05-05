package UI;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class UI {

    public TextField createSearchBar() {
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search files...");
        return searchBar;
    }

    public Button createSearchButton() {
        return new Button("Search");
    }
}
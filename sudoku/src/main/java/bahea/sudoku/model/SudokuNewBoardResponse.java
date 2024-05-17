package bahea.sudoku.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)


public class SudokuNewBoardResponse {
    @JsonAlias("newboard")
    private NewBoard newboard;

    // Getters and setters
    public NewBoard getNewboard() {
        return newboard;
    }

    public void setNewboard(NewBoard newboard) {
        this.newboard = newboard;
    }
}
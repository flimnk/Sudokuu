package bahea.sudoku.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewBoard {
    @JsonAlias("grids")
    private List<Grid> grids;

    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }
}

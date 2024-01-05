package snake.state;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Board implements BoardEntity {
    private final Cell[][] cells;
    private final List<Snake> snakes;

    public Board(int width, int height) {
        this.cells = new Cell[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.cells[x][y] = new Cell(x, y);
            }
        }

        this.snakes = Collections.emptyList();
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    @Override
    public Iterable<CellEntity> getCells() {
        return Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .flatMap(cell -> cell.getStack().stream())
                .collect(Collectors.toList());
    }

    @Override
    public void destroy() {
        for (var row : cells) {
            for (var cell : row) {
                cell.destroy();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (var row : cells) {
            for (var cell : row) {
                builder.append(cell);
            }

            builder.append("\n");
        }

        return builder.toString();
    }
}

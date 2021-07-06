package fr.lernejo.navy_battle.entities;
import fr.lernejo.navy_battle.enums.BoatOrientationEnum;
import fr.lernejo.navy_battle.enums.FireResultEnum;
import fr.lernejo.navy_battle.enums.GameCellEnum;
import java.util.*;
public class MapEntity {
    private final Integer[] BOATS = {5, 4, 3, 3, 2};
    private final GameCellEnum[][] map = new GameCellEnum[10][10];
    private final List<List<CoordinatesEntity>> boats = new ArrayList<>();
    public MapEntity(boolean fill) {
        for (GameCellEnum[] gameCells : map) {
            Arrays.fill(gameCells, GameCellEnum.EMPTY);
        }
        if (fill) {
            buildMap();
        }
    }
    public int getHeight() {
        return map[0].length;
    }
    public int getWidth() {
        return map.length;
    }
    private void buildMap() {
        var random = new Random();
        var boats = new ArrayList<>(Arrays.asList(BOATS));
        Collections.shuffle(boats);
        while (!boats.isEmpty()) {
            int boat = boats.get(0);
            int x = Math.abs(random.nextInt()) % getWidth();
            int y = Math.abs(random.nextInt()) % getHeight();
            var orientation = random.nextBoolean() ? BoatOrientationEnum.HORIZONTAL : BoatOrientationEnum.VERTICAL;
            if (!canFit(boat, x, y, orientation))
                continue;
            addBoat(boat, x, y, orientation);
            boats.remove(0);
        }
    }
    private boolean canFit(int length, int x, int y, BoatOrientationEnum orientation) {
        if (x >= getWidth() || y >= getHeight() || getCell(x, y) != GameCellEnum.EMPTY)
            return false;
        if (length == 0)
            return true;
        return switch (orientation) {
            case HORIZONTAL -> canFit(length - 1, x + 1, y, orientation);
            case VERTICAL -> canFit(length - 1, x, y + 1, orientation);
        };
    }

    public GameCellEnum getCell(CoordinatesEntity coordinates) {
        return getCell(coordinates.getX(), coordinates.getY());
    }
    public GameCellEnum getCell(int x, int y) {
        if (x >= 10 || y >= 10)
            throw new RuntimeException("Invalidate coordinates!");
        return map[x][y];
    }
    public void setCell(CoordinatesEntity coordinates, GameCellEnum newStatus) {
        map[coordinates.getX()][coordinates.getY()] = newStatus;
    }
    public void addBoat(int length, int x, int y, BoatOrientationEnum orientation) {
        var coordinates = new ArrayList<CoordinatesEntity>();
        while (length > 0) {
            map[x][y] = GameCellEnum.BOAT;
            length--;
            coordinates.add(new CoordinatesEntity(x, y));
            switch (orientation) {
                case HORIZONTAL -> x++;
                case VERTICAL -> y++;
            }
        }
        boats.add(coordinates);
    }
    public boolean hasShipLeft() {
        for (var row : map) {
            if (Arrays.stream(row).anyMatch(s -> s == GameCellEnum.BOAT)) return true;
        }
        return false;
    }
    public CoordinatesEntity getNextPlaceToHit() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getCell(i, j) == GameCellEnum.EMPTY)
                    return new CoordinatesEntity(i, j);
            }
        }
        throw new RuntimeException("Error");
    }
    public FireResultEnum hit(CoordinatesEntity coordinates) {
        if (getCell(coordinates) != GameCellEnum.BOAT) return FireResultEnum.MISS;
        var first = boats.stream().filter(s -> s.contains(coordinates)).findFirst();
        assert (first.isPresent());
        first.get().remove(coordinates);
        setCell(coordinates, GameCellEnum.SUCCESSFUL_FIRE);
        return first.get().isEmpty() ? FireResultEnum.SUNK : FireResultEnum.HIT;
    }
}

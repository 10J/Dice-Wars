package com.shadyaardvark.map;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class OutlineRenderer {
    private static final int LINE_SIZE = 4;
    private ShapeRenderer shapeRenderer;
    private Set<Line> outlines;

    public OutlineRenderer(HexagonMap map) {
        shapeRenderer = new ShapeRenderer();
        outlines = new HashSet<>();

        Array<Hexagon> padding = new Array<>();
        for (int x = -1; x <= map.getWidth(); x++) {
            for (int y = -1; y <= map.getHeight(); y++) {
                if (x == -1 || x == map.getWidth() || y == -1 || y == map.getHeight()) {
                    padding.add(new Hexagon(x, y, map.getHexSize()));
                }
            }
        }

        for (Hexagon hexagon : map.getHexagons()) {
            if (hexagon.isValid()) {
                Array<Hexagon> neighbors = map.getNeighborsOf(hexagon);

                for (Hexagon neighbor : neighbors) {
                    if (neighbor.isValid()) {
                        if (neighbor.getRegion() != hexagon.getRegion()) {
                            createLines(hexagon.getPoints(), neighbor.getPoints());
                        }
                    } else {
                        createLines(hexagon.getPoints(), neighbor.getPoints());
                    }
                }
            }
        }

        for (Hexagon hexagon : padding) {
            Array<Hexagon> neighbors = map.getNeighborsOf(hexagon);
            for (Hexagon neighbor : neighbors) {
                if (neighbor.isValid()) {
                    createLines(hexagon.getPoints(), neighbor.getPoints());
                }
            }
        }
    }

    public void render(OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BLACK);

        for (Line line : outlines) {
            shapeRenderer.rectLine(line.start.x, line.start.y, line.end.x, line.end.y, LINE_SIZE);
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }

    private void createLines(Array<Vector2> p1, Array<Vector2> p2) {
        Set<GridPoint2> s1 = new HashSet<>();
        Set<GridPoint2> s2 = new HashSet<>();

        for (Vector2 v : p1) {
            s1.add(new GridPoint2((int) v.x, (int) v.y));
        }

        for (Vector2 v : p2) {
            s2.add(new GridPoint2((int) v.x, (int) v.y));
        }
        s1.retainAll(s2);

        Iterator<GridPoint2> iter = s1.iterator();
        while (iter.hasNext()) {
            Line line = new Line();
            line.start = iter.next();

            if (iter.hasNext()) {
                line.end = iter.next();
                outlines.add(line);
            }
        }
    }

    private class Line {
        private GridPoint2 start;
        private GridPoint2 end;
    }
}

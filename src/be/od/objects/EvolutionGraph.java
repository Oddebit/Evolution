package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;

public class EvolutionGraph extends GameObject {

    public int SCALE = 1;
    private final int bottom = y + 50;
    private final int left = x;
    private ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

    public EvolutionGraph(ArrayList<Integer> firstGeneration) {
        super(20, Game.HEIGHT - 100, ID.EVOLUTION_GRAPH);
        addGeneration(firstGeneration);
    }

    public void tick() {

    }

    public void render(Graphics graphics) {
        for (int i = 0; i < graph.size(); i++) {
            ArrayList <Integer> generation = graph.get(i);
            int height = 0;
            for (int j = 0; j < generation.size(); j++) {
                int creatures = generation.get(j);

                graphics.setColor(new Color(0, j * 5, j * 5));
                graphics.fillRect(left + i * 3 * SCALE, bottom - height - creatures * SCALE, 3 * SCALE, creatures * SCALE);
                height += creatures * SCALE;
            }

        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, graph.size() * SCALE, 20 * SCALE);
    }

    public void addGeneration(ArrayList<Integer> creatures) {
        this.graph.add(creatures);
    }
}

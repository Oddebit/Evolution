package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;

public class EvolutionGraph extends GameObject {

    public int SCALE = 2;
    private ArrayList<Integer> graph = new ArrayList<>();

    public EvolutionGraph(int creatures) {
        super(20, Game.HEIGHT - 55, ID.EVOLUTION_GRAPH);
        addGeneration(creatures);
    }

    public void tick() {

    }

    public void render(Graphics graphics) {
        for (int i = 0; i < graph.size(); i++) {
            int creatures = graph.get(i);
            graphics.setColor(new Color(255, 121, 0));
            graphics.fillRect(x + SCALE * i, y - creatures * SCALE, SCALE, creatures * SCALE);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, graph.size() * SCALE, 20 * SCALE);
    }

    public void addGeneration(int creatures) {
        this.graph.add(creatures);
    }
}

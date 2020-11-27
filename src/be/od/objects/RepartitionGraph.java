package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;

public class RepartitionGraph extends GameObject {

    private final int SCALE = 4;
    private ArrayList<Integer> graph = new ArrayList<>();

    public RepartitionGraph() {
        super(Game.WIDTH - 100, 50, ID.REPARTITION_GRAPH);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(new Color(255, 121, 0));
        graphics.fillRect(x - 102, y + 50, 100, 1);

        for (int i = 0; i < 3; i++) {
            graphics.drawString(String.valueOf(i * 10), x - 100 + SCALE * i * 10, y + 65);
        }

        for (int i = 0; i < graph.size(); i++) {
            int creatures = graph.get(i);
            graphics.setColor(new Color(0, i * 10, i * 10));
            graphics.fillRect(x - 100 + SCALE * i, y + 50 - creatures * SCALE/2, SCALE, creatures * SCALE/2);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, Game.WIDTH - x, 50);
    }

    public void setGraph(ArrayList<Integer> graph) {
        this.graph.clear();
        this.graph = graph;
    }
}

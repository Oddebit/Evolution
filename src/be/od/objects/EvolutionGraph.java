package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;

import static be.od.main.Game.BORDER_WIDTH;

public class EvolutionGraph extends GameObject {

    Type type;
    public double heightPerCreature = 2;
    public double widthPerGeneration = 1;
    private final int bottom = y + 50;
    private final int left = x;
    private int width;
    private ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

    public EvolutionGraph(int x, int width, Type type, ArrayList<Integer> firstGeneration) {
        super(x, Game.HEIGHT - Game.HEIGHT_BORDER - 100, ID.EVOLUTION_GRAPH);
        addGeneration(firstGeneration);
        this.type = type;
        this.width = width;
    }

    public void tick() {

    }

    public void render(Graphics graphics) {

        if (graph.size() * widthPerGeneration > width) {
            widthPerGeneration = (double) width / graph.size();
        }
        for (int i = 0; i < graph.size(); i++) {
            ArrayList<Integer> generation = graph.get(i);
            int height = 0;
            for (int j = 0; j < generation.size(); j++) {
                int creatures = generation.get(j);
                graphics.setColor(getColor(j));
                graphics.fillRect( left + (int) ((double)i * widthPerGeneration),
                        bottom - height - (int)((double)creatures * heightPerCreature),
                        (int) Math.max(1, widthPerGeneration),
                        (int)((double)creatures * heightPerCreature));
                height += creatures * heightPerCreature;
            }

        }
    }

    public Color getColor(int valueOfGene) {
        int shade;
        switch (type) {
            case SPEED:
                shade = Math.min(255, valueOfGene * 8);
                return new Color(0, shade, shade);
            case DIAMETER:
                shade = Math.min(255, valueOfGene * 6);
                return new Color(255, shade, 0);
            case SENSE:
            default:
                shade = Math.min(255, valueOfGene);
                return new Color(shade, 0, shade);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, (int)((double)graph.size() * widthPerGeneration),(int)(20d * heightPerCreature));
    }

    public void addGeneration(ArrayList<Integer> creatures) {
        this.graph.add(creatures);
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        SPEED, DIAMETER, SENSE
    }
}

package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;

import static be.od.main.Game.*;

public class EvolutionGraph extends GameObject {

    Type type;
    private ArrayList<ArrayList<Integer>> graph = new ArrayList<>();

    private final int bottom = y + 50;
    private final int left = x;
    private final int width;
    private final int height;

    public double heightPerCreature = 2;
    public double widthPerGeneration = 1;

    public EvolutionGraph(int x, int width, int height, Type type) {
        super(x, Game.HEIGHT - Game.HEIGHT_BORDER - 100, ID.EVOLUTION_GRAPH);
        this.width = width;
        this.height = height;
        this.type = type;
        addGeneration();
    }

    public void tick() {

    }

    public void render(Graphics graphics) {

        if (CREATURES > height) {
            heightPerCreature = (double) height / CREATURES;
        }
        if (graph.size() * widthPerGeneration > width) {
            widthPerGeneration = (double) width / graph.size();
        }
        for (int i = 0; i < graph.size(); i++) {
            ArrayList<Integer> generation = graph.get(i);
            int bottomOfValue = 0;
            for (int j = 0; j < generation.size(); j++) {
                int creatures = generation.get(j);
                graphics.setColor(getColor(j));
                graphics.fillRect( left + (int) ((double)i * widthPerGeneration),
                        bottom - bottomOfValue - (int)((double)creatures * heightPerCreature),
                        (int) Math.max(1, widthPerGeneration),
                        (int)((double)creatures * heightPerCreature));
                bottomOfValue += creatures * heightPerCreature;
            }

        }
    }

    public Color getColor(int valueOfGene) {
        int shade;
        switch (type) {
            case SPEED:
                shade = Math.min(255, valueOfGene * Creature.speedShade);
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

    public void addGeneration() {
        int valueOfGene = 0;

        switch (type) {
            case SPEED:
                valueOfGene = INITIAL_SPEED;
                break;
            case DIAMETER:
                valueOfGene = INITIAL_DIAMETER;
                break;
            case SENSE:
                valueOfGene = INITIAL_SENSE;
                break;
        }
        ArrayList<Integer> newGeneration = new ArrayList<>();

        for (int i = 0; i < valueOfGene; i++) {
            newGeneration.add(0);
        }
        newGeneration.add(valueOfGene, CREATURES);

        this.graph.add(newGeneration);
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        SPEED, DIAMETER, SENSE
    }
}

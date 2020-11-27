package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Handler {

    Random random = new Random();
    public static int generation = 0;
    public static int creatures = 15;
    private int maxSpeed = 0;
    public static final double foodPerCreature = 1.2;
    LinkedList<GameObject> objects = new LinkedList<>();

    public void nextGeneration() {
        generation++;
        creatures = 0;

        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);

            if (tempObject.getId() != ID.CREATURE && tempObject.getId() != ID.FOOD) {
                continue;
            }

            if (tempObject.getId() == ID.FOOD) {
                this.removeObject(tempObject);
                i--;
                continue;
            }

            if (tempObject.getFood() == 0) {
                this.removeObject(tempObject);
                i--;
            } else {
                creatures++;
                maxSpeed = Math.max(maxSpeed, ((Creature) tempObject).getSpeed());
                if (tempObject.getFood() > 1) {
                    ((Creature) tempObject).giveBirth(tempObject.getFood() - 1);
                }
            }
        }
        updateStatistics();
        resetCreatures();
        resetFood();
    }

    public void updateStatistics() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);

            if (tempObject.getId() == ID.DASHBOARD) {
                ((DashBoard) tempObject).setGeneration(generation);
                ((DashBoard) tempObject).setCreatures(creatures);
            } else if (tempObject.getId() == ID.EVOLUTION_GRAPH) {
                ((EvolutionGraph) tempObject).addGeneration(Handler.creatures);
            } else if (tempObject.getId() == ID.REPARTITION_GRAPH) {
                updateRepartition((RepartitionGraph)tempObject);
            }
        }
    }

    public void updateRepartition(RepartitionGraph repartitionGraph){
        ArrayList<Integer> graph = new ArrayList<>();
        for (int i = 0; i <= maxSpeed; i++) {
            int count = 0;
            for (int j = 0; j < objects.size(); j++) {
                GameObject tempObject = objects.get(j);
                if (tempObject.getId() == ID.CREATURE) {
                    if (((Creature) tempObject).getSpeed() == i) count ++;
                }
            }
            graph.add(count);
        }
        repartitionGraph.setGraph(graph);
    }

    public void resetCreatures() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            if (tempObject.getId() == ID.CREATURE) {
                tempObject.setFood(0);
                tempObject.setEnergy(5_000);
            }
        }
    }

    public void resetFood() {
        for (int i = 0; i < Game.CREATURES * foodPerCreature; i++) {
            this.addObject(new Food(
                    random.nextInt(Game.WIDTH - 48),
                    random.nextInt(Game.HEIGHT - 70)));
        }
    }


    public void tick() {
        int count = 0;
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            if (tempObject.getEnergy() > 0) {
                tempObject.tick();
                count++;
            }
        }
        if (count == 0) nextGeneration();
    }

    public void render(Graphics graphics) {

        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            tempObject.render(graphics);
        }
    }

    public void addObject(GameObject object) {
        this.objects.add(object);
    }

    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }
}

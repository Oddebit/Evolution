package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Handler {

    Random random = new Random();
    LinkedList<GameObject> objects = new LinkedList<>();

    public static int generation = 0;
    public static int creatures = 15;

    private int maxSpeed = 0;
    private int maxDiameter = 0;
    private int maxSense = 0;

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
                maxDiameter = Math.max(maxDiameter, ((Creature) tempObject).getDiameter());
                maxSense = Math.max(maxSense, ((Creature) tempObject).getSense());

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

        ArrayList<Integer> speedStatistics = getSpeedStatistics();
        ArrayList<Integer> diameterStatistics = getDiameterStatistics();
        ArrayList<Integer> senseStatistics = getSenseStatistics();
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);

            if (tempObject.getId() == ID.DASHBOARD) {
                ((DashBoard) tempObject).setGeneration(generation);
                ((DashBoard) tempObject).setCreatures(creatures);
            } else if (tempObject.getId() == ID.EVOLUTION_GRAPH) {
                switch (((EvolutionGraph) tempObject).getType()) {
                    case SPEED:
                        ((EvolutionGraph) tempObject).addGeneration(speedStatistics);
                        break;
                    case DIAMETER:
                        ((EvolutionGraph) tempObject).addGeneration(diameterStatistics);
                        break;
                    case SENSE:
                        ((EvolutionGraph) tempObject).addGeneration(senseStatistics);
                        break;
                }

            }
        }
    }

    public ArrayList<Integer> getSpeedStatistics(){
        ArrayList<Integer> speedStatistics = new ArrayList<>();
        for (int i = 0; i <= maxSpeed; i++) {
            int count = 0;
            for (int j = 0; j < objects.size(); j++) {
                GameObject tempObject = objects.get(j);
                if (tempObject.getId() == ID.CREATURE) {
                    if (((Creature) tempObject).getSpeed() == i) count ++;
                }
            }
            speedStatistics.add(count);
        }
        return speedStatistics;
    }

    public ArrayList<Integer> getDiameterStatistics(){
        ArrayList<Integer> diameterStatistics = new ArrayList<>();
        for (int i = 0; i <= maxDiameter; i++) {
            int count = 0;
            for (int j = 0; j < objects.size(); j++) {
                GameObject tempObject = objects.get(j);
                if (tempObject.getId() == ID.CREATURE) {
                    if (((Creature) tempObject).getDiameter() == i) count ++;
                }
            }
            diameterStatistics.add(count);
        }
        return diameterStatistics;
    }

    public ArrayList<Integer> getSenseStatistics(){
        ArrayList<Integer> senseStatistics = new ArrayList<>();
        for (int i = 0; i <= maxSense; i++) {
            int count = 0;
            for (int j = 0; j < objects.size(); j++) {
                GameObject tempObject = objects.get(j);
                if (tempObject.getId() == ID.CREATURE) {
                    if (((Creature) tempObject).getSense() == i) count ++;
                }
            }
            senseStatistics.add(count);
        }
        return senseStatistics;
    }

    public void resetCreatures() {
        for (int i = 0; i < objects.size(); i++) {
            GameObject tempObject = objects.get(i);
            if (tempObject.getId() == ID.CREATURE) {
                tempObject.setFood(0);
                tempObject.setEnergy(Creature.INITIAL_ENERGY);
                ((Creature)tempObject).setDestination();
            }
        }
    }

    public void resetFood() {
        for (int i = 0; i < Game.CREATURES; i++) {
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

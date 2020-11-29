package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.Random;

import static be.od.main.Game.*;

public class Creature extends GameObject {


    Random random = new Random();
    Handler handler;

    private final int speed;
    private final int sense;
    public static int INITIAL_ENERGY = 1_000_000;

    Food target;
    private int destinationX;
    private int destinationY;


    public Creature(int x, int y, int speed, int diameter, int sense, Handler handler) {
        super(x, y, ID.CREATURE);
        this.speed = speed;
        this.diameter = diameter;
        this.sense = sense;

        this.handler = handler;
        this.energy = INITIAL_ENERGY;
        setDestination();
    }

    @Override
    public void tick() {

        velocityX = 0;
        velocityY = 0;

        sense();
        if (destinationX > x - speed && destinationX < x + speed && destinationY > y - speed && destinationY < y + speed) {
            setDestination();
        }
        if (destinationX < x - speed) {
            velocityX -= speed;
        } else if (destinationX > x + speed) {
            velocityX += speed;
        } else if (destinationY < y - speed) {
            velocityY -= speed;
        } else if (destinationY > y + speed) {
            velocityY += speed;
        }

        x += velocityX;
        y += velocityY;

        collision();
        energy -= (Math.pow(speed, 2.5) * Math.pow(diameter, 1.5) + sense);
    }


    public void setDestination() {
        destinationX = random.nextInt(WIDTH - 48);
        destinationY = random.nextInt(HEIGHT - 70);
    }

    public void setDestination(int x, int y) {
        destinationX = x;
        destinationY = y;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(getColor());
        graphics.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        graphics.fillOval(x, y, diameter, diameter);
        graphics.drawOval( (x + diameter / 2 - sense / 2), (y + diameter / 2 - sense / 2), sense, sense);
//        graphics.drawString(St ring.valueOf(food), x, y);
    }

    public void giveBirth(int times) {
        int mutationProb = 3;

        for (int i = 0; i < times; i++) {
            int probability = random.nextInt(99);

            int speed = this.speed;
            int diameter = this.diameter;
            int sense = this.sense;

            if (probability < mutationProb) {
                speed = this.speed + 1;
            } else if (probability < 2 * mutationProb) {
                speed = this.speed - 1;
            } else if (probability < 3 * mutationProb) {
                diameter = this.diameter + 1;
            } else if (probability < 4 * mutationProb) {
                diameter = this.diameter - 1;
            } else if (probability < 5 * mutationProb) {
                sense = this.sense + 10;
            } else if (probability < 6 * mutationProb) {
                sense = this.sense - 10;
            }

            Creature child = new Creature(random.nextInt(REAL_WIDTH), random.nextInt(REAL_HEIGHT),
                    speed, diameter, sense, handler);
            child.setFood(1);
            handler.addObject(child);
        }
    }

    public void sense() {
        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            if (tempObject.getId() == ID.FOOD && tempObject != target) {

                if (distanceTo(tempObject) < sense) {
                    target = (Food) tempObject;
                    setDestination(target.getX(), target.getY());
                }
            }
        }
    }

    private double distanceTo(GameObject gameObject) {
        double deltaX = x - gameObject.getX();
        double deltaY = y - gameObject.getY();
        return Math.sqrt(Math.pow(deltaX, 2) + (Math.pow(deltaY, 2)));
    }

    public void collision() {
        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            if (tempObject.getId() == ID.FOOD) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    food++;
                    handler.removeObject(tempObject);
                }

            } else if (tempObject.getId() == ID.CREATURE && tempObject.getEnergy() > 0) {
                if (getBounds().intersects(tempObject.getBounds()) && diameter > 1.2 * tempObject.getDiameter()) {
                    this.food += tempObject.getFood();
                    handler.removeObject(tempObject);
                }
            }
        }
    }


    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public int getDiameter() {
        return diameter;
    }

    public int getSense() {
        return sense;
    }

    public int getSpeed() {
        return speed;
    }

    public Color getColor() {
        int shade = Math.min(255, speed * 8);
        return new Color(0, shade, shade);
    }
}

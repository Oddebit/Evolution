package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.Random;

import static be.od.main.Game.HEIGHT;
import static be.od.main.Game.WIDTH;

public class Creature extends GameObject {


    Random random = new Random();
    Handler handler;

    private int speed;
    public int diameter;
    public static int INITIAL_ENERGY = 1_000_000;

    private int destinationX;
    private int destinationY;


    public Creature(int x, int y, int speed, int diameter, Handler handler) {
        super(x, y, ID.CREATURE);
        this.speed = speed;
        this.diameter = diameter;
        this.handler = handler;
        this.energy = INITIAL_ENERGY;
        setDestination();
    }

    @Override
    public void tick() {

        velocityX = 0;
        velocityY = 0;

        if(destinationX > x - speed && destinationX < x + speed && destinationY > y - speed && destinationY < y + speed) {
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
        energy -= (Math.pow(speed, 4) + Math.pow(diameter, 3));
    }

    public void setDestination() {
        destinationX = random.nextInt(WIDTH - 48);
        destinationY = random.nextInt(HEIGHT - 70);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(new Color(0, speed * 5, speed * 5));
        graphics.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        graphics.fillOval(x, y, diameter, diameter);
//        graphics.drawString(String.valueOf(food), x, y);
    }

    public void giveBirth(int times){
        int mutationProb = 5;

        for (int i = 0; i < times; i++) {
            int probability = random.nextInt(99);

            int speed = this.speed;
            int diameter = this.diameter;
            if (probability < mutationProb) {
                speed = this.speed + 1;
            } else if (probability < 2 * mutationProb) {
                speed = this.speed - 1;
            } else if (probability < 4 * mutationProb) {
                diameter = this.diameter + 1;
            } else if (probability < 6 * mutationProb) {
                diameter = this.diameter - 1;
            }

            Creature child = new Creature(random.nextInt(WIDTH - 48), random.nextInt(Game.HEIGHT - 70), speed, diameter, handler);
            child.setFood(1);
            handler.addObject(child);
        }
    }

    public void collision() {
        for (int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
            if (tempObject.getId() == ID.FOOD) {
                if (getBounds().intersects(tempObject.getBounds())) {
                    food++;
                    handler.removeObject(tempObject);
                }
            }
        }
    }



    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public int getSpeed() {
        return speed;
    }
}

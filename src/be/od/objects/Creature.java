package be.od.objects;

import be.od.main.Game;

import java.awt.*;
import java.util.Random;

public class Creature extends GameObject {

    Random random = new Random();
    Handler handler;

    private int speed;
    public int diameter = 16;
    private static int INITIAL_ENERGY = 20_000;

    private int DIRECTION;
    private final int DIRECTION_UP = 0;
    private final int DIRECTION_DOWN = 1;
    private final int DIRECTION_LEFT = 2;
    private final int DIRECTION_RIGHT = 3;


    public Creature(int x, int y, int speed, Handler handler) {
        super(x, y, ID.CREATURE);
        this.handler = handler;
        this.energy = INITIAL_ENERGY;
        this.speed = speed;
        this.DIRECTION = 1;
    }

    @Override
    public void tick() {

        velocityX = 0;
        velocityY = 0;

        if (x <= 0) {
            DIRECTION = 3;
        }
        if (x >= Game.WIDTH - 48) {
            DIRECTION = 2;
        }
        if (y <= 0) {
            DIRECTION = 1;
        }
        if (y >= Game.HEIGHT - 70) {
            DIRECTION = 0;
        }

        switch (DIRECTION) {
            case DIRECTION_UP:
                velocityY -= speed;
                break;
            case DIRECTION_DOWN:
                velocityY += speed;
                break;
            case DIRECTION_LEFT:
                velocityX -= speed;
                break;
            case DIRECTION_RIGHT:
                velocityX += speed;
                break;
        }

        x += velocityX;
        y += velocityY;

        collision();
        energy -= Math.pow(speed, 1.8);

        changeDirection();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(new Color(0, speed * 5, speed * 5));
        graphics.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        graphics.fillOval(x, y, diameter, diameter);
//        graphics.drawString(String.valueOf(food), x, y);
    }

    public void giveBirth(int times){
        for (int i = 0; i < times; i++) {
            int probability = random.nextInt(99);

            int speed = this.getSpeed();
            if (probability < 5) {
                speed = this.getSpeed() + 1;
            } else if (probability < 10) {
                speed = this.getSpeed() - 1;
            }

            Creature child = new Creature(random.nextInt(Game.WIDTH - 48), random.nextInt(Game.HEIGHT - 70), speed, handler);
            child.setFood(1);
            handler.addObject(child);
        }
    }

    public void changeDirection() {
        int turn = random.nextInt(4);

        switch (turn) {
            case 0:
                if (++this.DIRECTION > 3) this.DIRECTION = 0;
            case 1:
                if (--this.DIRECTION < 0) this.DIRECTION = 3;
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

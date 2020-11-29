package be.od.objects;

import java.awt.*;

public class Food extends GameObject {


    public Food(int x, int y) {
        super(x, y, ID.FOOD);
        this.diameter = 16;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.ORANGE);
        graphics.fillOval(x, y, diameter, diameter);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }

}

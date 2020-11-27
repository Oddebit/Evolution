package be.od.objects;

import java.awt.*;

public class Food extends GameObject {

    public int diameter = 8;

    public Food(int x, int y) {
        super(x, y, ID.FOOD);
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

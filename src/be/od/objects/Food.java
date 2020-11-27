package be.od.objects;

import java.awt.*;

public class Food extends GameObject {

    public Food(int x, int y) {
        super(x, y, ID.FOOD);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(Color.ORANGE);
        graphics.fillOval(x, y, 16, 16);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 16, 16);
    }

}

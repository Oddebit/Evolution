package be.od.objects;


import java.awt.*;

import static be.od.main.Game.*;

public class DashBoard extends GameObject {

    private int generation;
    private int creatures;
    private static final int FONT_SIZE = 32;

    public DashBoard(int creatures) {
        super(WIDTH_BORDER, HEIGHT_BORDER, ID.DASHBOARD);
        this.generation = 0;
        this.creatures = creatures;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {

        graphics.setColor(new Color(255, 121, 0));
        graphics.setFont(new Font(Font.DIALOG, Font.BOLD, FONT_SIZE));
        graphics.drawString("Generation : " + generation, x, (int) ((double)y + 1.2 * FONT_SIZE));
        graphics.drawString("Creatures : " + creatures, x, (int) ((double)y + 2.4 * FONT_SIZE));
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 100, 50);
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setCreatures(int creatures) {
        this.creatures = creatures;
    }
}

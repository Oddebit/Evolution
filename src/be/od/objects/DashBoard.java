package be.od.objects;


import java.awt.*;

public class DashBoard extends GameObject {

    private int generation;
    private int creatures;

    public DashBoard(int creatures) {
        super(20, 15, ID.DASHBOARD);
        this.generation = 0;
        this.creatures = creatures;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics graphics) {

        graphics.setColor(new Color(255, 121, 0));
        graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
        graphics.drawString("Generation : " + generation, x + 5, y + 30);
        graphics.drawString("Creatures : " + creatures, x + 5, y + 60);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public void setCreatures(int creatures) {
        this.creatures = creatures;
    }
}

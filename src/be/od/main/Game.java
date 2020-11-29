package be.od.main;

import be.od.objects.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1550691097823471818L;

    public static final int WIDTH = 1306, HEIGHT = 708;
    public static final int REAL_WIDTH = WIDTH - 48, REAL_HEIGHT = HEIGHT - 70;
    public static final double BORDER_PROPORTION = 0.03;
    public static final int WIDTH_BORDER = (int) (REAL_WIDTH * BORDER_PROPORTION), HEIGHT_BORDER = (int) (REAL_HEIGHT * BORDER_PROPORTION);
    public static final int BORDER_WIDTH = REAL_WIDTH - 2 * WIDTH_BORDER, BORDER_HEIGHT = REAL_HEIGHT - 2 * HEIGHT_BORDER;

    private Thread thread;
    private boolean running = false;
    public static final int CREATURES = 100;
    private final int INITIAL_SPEED = 12;
    private final int INITIAL_DIAMETER = 16;
    private final int INITIAL_SENSE = 100;

    private Handler handler;

    public Game() {
        handler = new Handler();
        new Window(WIDTH, HEIGHT, "Evolution", this);
        Random random = new Random();

        ArrayList<Integer> speedStatistics = new ArrayList<>();
        ArrayList<Integer> diameterStatistics = new ArrayList<>();
        ArrayList<Integer> senseStatistics = new ArrayList<>();

        for (int i = 0; i <= INITIAL_SPEED; i++) {
            speedStatistics.add(0);
        }
        for (int i = 0; i <= INITIAL_DIAMETER; i++) {
            diameterStatistics.add(0);
        }
        for (int i = 0; i <= INITIAL_SENSE; i++) {
            senseStatistics.add(0);
        }

        for (int i = 0; i < CREATURES; i++) {
            handler.addObject(new Creature(random.nextInt(WIDTH - 48),
                    random.nextInt(HEIGHT - 70),
                    INITIAL_SPEED, INITIAL_DIAMETER, INITIAL_SENSE,  handler));
            speedStatistics.set(INITIAL_SPEED, speedStatistics.get(INITIAL_SPEED) + 1);
            diameterStatistics.set(INITIAL_DIAMETER, diameterStatistics.get(INITIAL_DIAMETER) + 1);
            senseStatistics.set(INITIAL_SENSE, senseStatistics.get(INITIAL_SENSE) + 1);
        }

        handler.resetFood();
        final int graphs = 3;
        final int graphWidth = (BORDER_WIDTH - (graphs + 1) * WIDTH_BORDER) / graphs;
        handler.addObject(new EvolutionGraph(Game.WIDTH_BORDER, graphWidth, EvolutionGraph.Type.SPEED, speedStatistics));
        handler.addObject(new EvolutionGraph(2 * WIDTH_BORDER + graphWidth, graphWidth, EvolutionGraph.Type.DIAMETER, diameterStatistics));
        handler.addObject(new EvolutionGraph(3 * WIDTH_BORDER + 2 * graphWidth, graphWidth, EvolutionGraph.Type.SENSE, senseStatistics));
        handler.addObject(new DashBoard(CREATURES));
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1_000_000_000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }

            if (running) {
                render();
            }
            frames++;

            // Affiche les FPS chaque seconde
            if (System.currentTimeMillis() - timer > 1_000) {
                timer += 1_000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        handler.tick();
    }

    private void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        handler.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    public static void main(String[] args) {
        new Game();
    }
}

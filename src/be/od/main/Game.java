package be.od.main;

import be.od.objects.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;

import static be.od.objects.Handler.foodPerCreature;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1550691097823471818L;


    public static final int WIDTH = 1306, HEIGHT = 708;

    private Thread thread;
    private boolean running = false;
    public static final int CREATURES = 200;

    private Handler handler;

    public Game() {
        handler = new Handler();
        new Window(WIDTH, HEIGHT, "Evolution", this);
        Random random = new Random();

        ArrayList<Integer> speedStatistics = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            speedStatistics.add(0);
        }
        for (int i = 0; i < CREATURES; i++) {
            int speed = (random.nextInt(4) + 1) * 4;
            handler.addObject(new Creature(random.nextInt(WIDTH - 48), random.nextInt(HEIGHT - 70), speed, handler));
            speedStatistics.set(speed, speedStatistics.get(speed) + 1);
        }

        for (int i = 0; i < CREATURES * foodPerCreature; i++) {
            handler.addObject(new Food(random.nextInt(WIDTH - 48), random.nextInt(HEIGHT - 70)));
        }
//        handler.addObject(new RepartitionGraph());
        handler.addObject(new EvolutionGraph(speedStatistics));
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

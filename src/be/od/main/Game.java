package be.od.main;

import be.od.objects.*;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable {

    private static final long serialVersionUID = 1550691097823471818L;

    private Handler handler;
    private Menu menu;
    private Thread thread;
    private boolean running = false;

    public static final int WIDTH = 1306, HEIGHT = 708;
    public static final int REAL_WIDTH = WIDTH - 48, REAL_HEIGHT = HEIGHT - 70;
    public static final int WIDTH_CENTER = REAL_WIDTH / 2, HEIGHT_CENTER = REAL_HEIGHT / 2;
    public static final double BORDER_PROPORTION = 0.03;
    public static final int WIDTH_BORDER = (int) (REAL_WIDTH * BORDER_PROPORTION), HEIGHT_BORDER = (int) (REAL_HEIGHT * BORDER_PROPORTION);
    public static final int BORDER_WIDTH = REAL_WIDTH - 2 * WIDTH_BORDER, BORDER_HEIGHT = REAL_HEIGHT - 2 * HEIGHT_BORDER;
    public static final int GRAPH_HEIGHT = 50;


    public static final int CREATURES = 100;
    public static final int INITIAL_SPEED = 12;
    public static final int INITIAL_DIAMETER = 16;
    public static final int INITIAL_SENSE = 100;

    public enum State {
        MENU, GAME, GAME_OVER
    }

    public State gameState = State.MENU;

    public Game() {
        handler = new Handler();
        menu = new Menu(this, handler);
        this.addMouseListener(menu);

        new Window(WIDTH, HEIGHT, "Evolution", this);
        Random random = new Random();

        for (int i = 0; i < CREATURES; i++) {
            handler.addObject(new Creature(random.nextInt(WIDTH - 48),
                    random.nextInt(HEIGHT - 70),
                    INITIAL_SPEED, INITIAL_DIAMETER, INITIAL_SENSE, handler));
        }

        handler.resetFood();
        final int graphs = 3;
        final int graphWidth = (BORDER_WIDTH - (graphs + 1) * WIDTH_BORDER) / graphs;
        handler.addObject(new EvolutionGraph(Game.WIDTH_BORDER, graphWidth, GRAPH_HEIGHT, EvolutionGraph.Type.SPEED));
        handler.addObject(new EvolutionGraph(2 * WIDTH_BORDER + graphWidth, graphWidth, GRAPH_HEIGHT, EvolutionGraph.Type.DIAMETER));
        handler.addObject(new EvolutionGraph(3 * WIDTH_BORDER + 2 * graphWidth, graphWidth, GRAPH_HEIGHT, EvolutionGraph.Type.SENSE));
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

        if(gameState == State.GAME) {
            handler.tick();
        } else {
            menu.tick();
        }
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

        if (gameState == State.GAME) {
            graphics.setColor(new Color(250, 120, 0));
            graphics.setFont(new Font(Font.DIALOG, Font.BOLD, 32));
            String string = "Menu";
            int stringHeight = graphics.getFontMetrics().getHeight();
            int stringWidth = graphics.getFontMetrics().stringWidth(string);
            graphics.drawString("Menu", BORDER_WIDTH - stringWidth, HEIGHT_BORDER + stringHeight);
        }
        if (gameState == State.GAME) {
            handler.render(graphics);
        } else if (gameState == State.MENU) {
            menu.render(graphics);
        }

        graphics.dispose();
        bufferStrategy.show();
    }

    public static void main(String[] args) {
        new Game();
    }
}

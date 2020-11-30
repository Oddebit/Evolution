package be.od.main;

import be.od.objects.Handler;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static be.od.main.Game.WIDTH_CENTER;

public class Menu extends MouseAdapter {

    private final Game game;
    private final Handler handler;

    private static final int menuHEIGHT = 64;
    private static final int menuWIDTH = 300;
    private static final int menuX = WIDTH_CENTER - menuWIDTH / 2;
    private static final int menuY = Game.HEIGHT / 4;
    private static final int distanceToButton = 70;
    private static final int TITLE_FONTSIZE = 32;
    private static final int BUTTON_FONTSIZE = 24;

    Font titleFont = new Font(Font.DIALOG, Font.BOLD, TITLE_FONTSIZE);
    Font buttonFont = new Font(Font.DIALOG, 1, BUTTON_FONTSIZE);

    public Menu(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (game.gameState == Game.State.MENU) {
            // play button
            if (mouseOver(mouseX, mouseY, menuX, menuY + distanceToButton)) {
                game.gameState = Game.State.GAME;
            }
            // play button
            if (mouseOver(mouseX, mouseY, menuX, menuY + 2 * distanceToButton)) {
                System.exit(1);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    private boolean mouseOver(int mouseX, int mouseY, int x, int y) {
        if (mouseX > x && mouseX < x + menuWIDTH) {
            if (mouseY > y && mouseY < y + menuWIDTH) {
                return true;
            }
        }
        return false;
    }

    public void tick() {

    }

    public void render(Graphics graphics) {


        graphics.setColor(new Color(0, 120, 120));
        graphics.fillRect(menuX, menuY, menuWIDTH, menuHEIGHT);
        graphics.setColor(Color.BLACK);
        graphics.setFont(titleFont);
        String title = "MENU";
        int titleWidth = graphics.getFontMetrics().stringWidth(title);
        int titleHeight = titleFont.getSize();
        graphics.drawString(title, menuX + menuWIDTH / 2 - titleWidth / 2, menuY + menuHEIGHT / 2 + titleHeight / 2);

        graphics.setFont(buttonFont);
        graphics.setColor(new Color(255, 120, 0));
        Button play = new Button(1, "Play", graphics);
        Button exit = new Button(2, "Exit", graphics);

        graphics.drawString(play.display, play.x, play.y);
        graphics.drawString(exit.display, exit.x, exit.y);
    }

    public class Button {

        private int x;
        private int y;
        private String display;
        private Color color = new Color(255, 120, 0);

        public Button(int rank, String display, Graphics graphics) {
            graphics.setFont(buttonFont);
            int buttonWidth = graphics.getFontMetrics().stringWidth(display);
            int buttonHeight = graphics.getFontMetrics().getHeight();
            this.x = menuX + menuWIDTH /2 - buttonWidth/2;
            this.y = menuY + menuHEIGHT + 2*rank*buttonHeight;
            this.display = display;
        }
    }
}
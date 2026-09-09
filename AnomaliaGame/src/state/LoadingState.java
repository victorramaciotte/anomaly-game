package state;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import level.Campaign;
import main.GameConfig;

public class LoadingState implements GameState {
    private GameStateManager stateManager;
    private Campaign campaign;
    private volatile boolean loadingDone = false;
    private int dotCount = 0;
    private int dotTickCounter = 0;
    private static final int TICKS_PER_DOT = 50; // ajusta a velocidade
    private double spinnerAngle = 0;

    public LoadingState(GameStateManager stateManager, Campaign campaign) {
        this.stateManager = stateManager;
        this.campaign = campaign;
        startLoading();
    }

    private void startLoading() {
        Thread loaderThread = new Thread(() -> {
            campaign.preloadAllImages();
            loadingDone = true;
        });
        loaderThread.start();
    }

    @Override
    public void update() {
        dotTickCounter++;
        if (dotTickCounter >= TICKS_PER_DOT) {
            dotTickCounter = 0;
            dotCount = (dotCount + 1) % 4; // cicla entre 0, 1, 2, 3 pontos
        }
        
        spinnerAngle += 2; // graus por tick — ajusta a velocidade de rotação
        if (spinnerAngle >= 360) spinnerAngle -= 360;

        if (loadingDone) {
            stateManager.setState(new PlayingState(campaign, stateManager));
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);

        String dots = ".".repeat(dotCount);
        String text = "Carregando" + dots;

        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.setColor(Color.WHITE);
        FontMetrics metrics = g.getFontMetrics();

        // usa a largura do texto MAIOR possível pra centralizar sem "tremer" conforme os pontos mudam
        int maxWidth = metrics.stringWidth("Carregando...");
        int textX = (GameConfig.SCREEN_WIDTH - maxWidth) / 2;
        int textY = GameConfig.SCREEN_HEIGHT / 2;

        g.drawString(text, textX, textY);

        renderSpinner(g);
    }
    

    private void renderSpinner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int centerX = GameConfig.SCREEN_WIDTH / 2;
        int centerY = GameConfig.SCREEN_HEIGHT / 2 + 60; // um pouco abaixo do texto
        int radius = 20;

        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(Color.WHITE);
        g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, (int) spinnerAngle, 90);
        // reseta stroke pro padrão, caso outra coisa desenhe depois
        g2d.setStroke(new BasicStroke(1));
    }

    @Override
    public void onKeyPressed(int keyCode) {}

    @Override
    public void onKeyReleased(int keyCode) {}
}
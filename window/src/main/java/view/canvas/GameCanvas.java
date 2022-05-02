package view.canvas;

import drawable.Drawable;
import lombok.Getter;
import model.WindowModel;
import tools.Vector2D;
import view.window.Window;
import view.drawTools.GameDrawer;
import view.drawTools.GameScaler;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class GameCanvas extends JPanel {
    private JFrame frame;
    private WindowModel WINDOW_MODEL;
    @Getter
    GameScaler gameScaler;
    GameDrawer gameDrawer;

    public GameCanvas(Window window, WindowModel viewModel) {
        WINDOW_MODEL = viewModel;
        setBackground(WINDOW_MODEL.COLOR_SETTINGS.BLACK_SPACE_COLOR);
        setLayout(null);

        setSize(window.getSize().width, window.getSize().height);

        frame = new JFrame("ELEVATOR SYS");
        frame.setSize(window.getSize().width, window.getSize().height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);

        gameScaler = new GameScaler(getSize(), viewModel.getSettings().BUILDING_SIZE);
        gameDrawer = new GameDrawer(gameScaler);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resize(Dimension newSize) {
        gameScaler.updateSizes(newSize, WINDOW_MODEL.getSettings().BUILDING_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameDrawer.startDraw(g);

        var drawableObjets = WINDOW_MODEL.getDrawableOjects();
        drawableObjets.sort(Comparator.comparingInt(Drawable::GetDrawPrioritet));
        drawableObjets.forEach(drawable -> drawable.draw(gameDrawer));
    }

    public boolean zoomedIn() {
        return gameScaler.getAdditionalZoomFinishValue() == 1.;
    }

    public void zoomOut() {
        gameScaler.zoomOut();
    }

    public void zoomIn(Vector2D point, double zoomScale) {
        gameScaler.zoomIn(point, zoomScale);
    }

    public void update() {
        gameScaler.tick();
        frame.repaint();
    }
}
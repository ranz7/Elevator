package view.canvas;

import configs.WindowSettings;
import drawable.drawableBase.creatureWithTexture.Drawable;
import lombok.Getter;
import model.GuiModel;
import architecture.tickable.Tickable;
import tools.Vector2D;
import view.gui.windowListeners.WindowResizeListener;
import view.drawTools.GameDrawer;
import view.drawTools.GameScaler;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class GameWindow extends JPanel implements Tickable {
    private final JFrame jframe;
    private GuiModel windowModel;

    @Getter
    private GameScaler gameScaler = new GameScaler();
    private GameDrawer gameDrawer = new GameDrawer(gameScaler);

    public void setModel(GuiModel model) {
        windowModel = model;
        setBackground(windowModel.colorSettings.windowBackGround);
    }

    public GameWindow() {
        setLayout(null);
        setVisible(false);

        setSize(WindowSettings.WindowStartSize.width, WindowSettings.WindowStartSize.height);

        jframe = new JFrame("ELEVATOR SYS");
        jframe.setSize(WindowSettings.WindowStartSize.width, WindowSettings.WindowStartSize.height);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(this);
    }

    public void start() {
        setVisible(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resize(Dimension newSize) {
            gameScaler.updateSizes(newSize, windowModel.getMainInitializationSettings().BUILDING_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameDrawer.startDraw(g);

        var drawableObjets = windowModel.getDrawableOjects();
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

    @Override
    public void tick(long deltaTime) {
        gameScaler.tick(deltaTime);
        jframe.repaint();
    }

    public void addResizeListener(WindowResizeListener windowResizeListener) {
        this.addComponentListener(windowResizeListener);
    }

}
package view.canvas;

import configs.WindowSettings;
import drawable.drawableBase.creatureWithTexture.Drawable;
import lombok.Getter;
import model.GuiModel;
import tools.tools.Vector2D;
import view.gui.WindowResizeListener;
import view.drawTools.GameDrawer;
import view.drawTools.GameScaler;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class GameWindow extends JPanel {
    private final JFrame FRAME;
    private GuiModel windowModel;

    @Getter
    private GameScaler gameScaler;
    private GameDrawer gameDrawer;

    public void setModel(GuiModel model) {
        windowModel = model;
        setBackground(windowModel.COLOR_SETTINGS.GUI_BACK_GROUND_COLOR);
    }

    public GameWindow() {
        setLayout(null);
        setVisible(false);

        setSize(WindowSettings.WindowStartSize.width, WindowSettings.WindowStartSize.height);

        FRAME = new JFrame("ELEVATOR SYS");
        FRAME.setSize(WindowSettings.WindowStartSize.width, WindowSettings.WindowStartSize.height);
        FRAME.setVisible(true);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.add(this);
    }

    public void start() {
        setVisible(true);
        gameScaler = new GameScaler(getSize(), windowModel.getSettings().BUILDING_SIZE);
        gameDrawer = new GameDrawer(gameScaler);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resize(Dimension newSize) {
        gameScaler.updateSizes(newSize, windowModel.getSettings().BUILDING_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(gameDrawer==null){
            return;
        }
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

    public void update() {
        gameScaler.tick();
        FRAME.repaint();
    }

    public void addResizeListener(WindowResizeListener windowResizeListener) {
        this.addComponentListener(windowResizeListener);
    }

}
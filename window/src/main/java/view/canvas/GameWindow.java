package view.canvas;

import databases.configs.WindowConfig;
import drawable.abstracts.DrawableCreature;
import lombok.Getter;
import model.GuiModel;
import architecture.tickable.Tickable;
import tools.Pair;
import tools.Vector2D;
import view.FPScounter;
import view.gui.windowListeners.WindowResizeListener;
import view.graphics.GameGraphics;
import view.graphics.GameScaler;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.LinkedList;

public class GameWindow extends JPanel implements Tickable {
    private final JFrame jframe;
    private GuiModel windowModel;
    private FPScounter counter = new FPScounter("42");

    @Getter
    private GameScaler gameScaler = new GameScaler();
    private GameGraphics gameDrawer = new GameGraphics(gameScaler);

    public void setModel(GuiModel model) {
        windowModel = model;
        setBackground(windowModel.getCombienedDrawDataBase().backGroundColor());
    }

    public GameWindow() {
        setLayout(null);
        setVisible(false);

        setSize(WindowConfig.WindowStartSize.width, WindowConfig.WindowStartSize.height);

        jframe = new JFrame("ELEVATOR SYS");
        jframe.setSize(WindowConfig.WindowStartSize.width, WindowConfig.WindowStartSize.height);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(counter);
        jframe.add(this);
    }

    public void start() {
        setVisible(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void resize(Dimension newSize) {
        gameScaler.updateSizes(newSize, windowModel.getCombienedDrawDataBase().buildingSize());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameDrawer.prepareDrawer(g);

        LinkedList<Pair<Vector2D, DrawableCreature>> objectsAndRelativePositions = windowModel.getDrawableOjects();
        objectsAndRelativePositions.sort(Comparator.comparingInt(drawableObjet -> drawableObjet.getSecond().GetDrawPrioritet()));
        objectsAndRelativePositions.forEach(
                positionAndObject -> {
                    positionAndObject.getSecond().draw(positionAndObject.getThirst(), gameDrawer);
                });
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
    public void tick(double deltaTime) {
        gameScaler.tick(deltaTime);
        counter.tick(deltaTime);
        jframe.repaint();
    }

    public void addResizeListener(WindowResizeListener windowResizeListener) {
        this.addComponentListener(windowResizeListener);
    }

}
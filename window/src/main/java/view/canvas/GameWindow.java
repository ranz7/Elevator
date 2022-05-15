package view.canvas;

<<<<<<< Updated upstream
import configs.WindowSettings;
import drawable.drawableBase.creatureWithTexture.Drawable;
=======
import settings.configs.WindowConfig;
import drawable.abstracts.DrawableCreature;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        setBackground(windowModel.COLOR_SETTINGS.GUI_BACK_GROUND_COLOR);
=======
        setBackground(windowModel.getCombienedDrawSettings().backGroundColor());
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        gameScaler.updateSizes(newSize, windowModel.getSettings().BUILDING_SIZE);
=======
        gameScaler.updateSizes(newSize, windowModel.getCombienedDrawSettings().buildingSize());
>>>>>>> Stashed changes
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
<<<<<<< Updated upstream
        if(gameDrawer==null){
            return;
        }
        gameDrawer.startDraw(g);

        var drawableObjets = windowModel.getDrawableOjects();
        drawableObjets.sort(Comparator.comparingInt(Drawable::GetDrawPrioritet));
        drawableObjets.forEach(drawable -> drawable.draw(gameDrawer));
=======
        gameDrawer.prepareDrawer(g);

        LinkedList<Pair<Vector2D, DrawableCreature>> objectsAndRelativePositions = windowModel.getDrawableOjects();
        objectsAndRelativePositions.sort(Comparator.comparingInt(drawableObjet -> drawableObjet.getSecond().GetDrawPrioritet()));
        objectsAndRelativePositions.forEach(
                positionAndObject -> {
                    positionAndObject.getSecond().draw(positionAndObject.getFirst(), gameDrawer);
                });
>>>>>>> Stashed changes
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
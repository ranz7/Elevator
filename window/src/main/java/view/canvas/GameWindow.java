package view.canvas;

import drawable.drawableBase.creatureWithTexture.Drawable;
import lombok.Getter;
import model.GuiModel;
import tools.Vector2D;
import view.gui.WindowResizeListener;
import view.drawTools.GameDrawer;
import view.drawTools.GameScaler;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class GameWindow extends JPanel {
    private final JFrame FRAME;
    private final GuiModel WINDOW_MODEL;

    @Getter
    private final GameScaler GAME_SCALER;
    private final GameDrawer GAME_DRAWER;

    public GameWindow(GuiModel viewModel) {
        WINDOW_MODEL = viewModel;
        setBackground(WINDOW_MODEL.COLOR_SETTINGS.GUI_BACK_GROUND_COLOR);
        setLayout(null);
        setVisible(false);

        setSize(WindowSettings.WindowStartSize.width, WindowSettings.WindowStartSize.height);

        FRAME = new JFrame("ELEVATOR SYS");
        FRAME.setSize(WindowSettings.WindowStartSize.width, WindowSettings.WindowStartSize.height);
        FRAME.setVisible(true);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.add(this);

        GAME_SCALER = new GameScaler(getSize(), WINDOW_MODEL.getSettings().BUILDING_SIZE);
        GAME_DRAWER = new GameDrawer(GAME_SCALER);
    }

    public void start(){
        this.setVisible(true);
    }
    @SuppressWarnings("deprecation")
    @Override
    public void resize(Dimension newSize) {
        GAME_SCALER.updateSizes(newSize, WINDOW_MODEL.getSettings().BUILDING_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        GAME_DRAWER.startDraw(g);

        var drawableObjets = WINDOW_MODEL.getDrawableOjects();
        drawableObjets.sort(Comparator.comparingInt(Drawable::GetDrawPrioritet));
        drawableObjets.forEach(drawable -> drawable.draw(GAME_DRAWER));
    }

    public boolean zoomedIn() {
        return GAME_SCALER.getAdditionalZoomFinishValue() == 1.;
    }

    public void zoomOut() {
        GAME_SCALER.zoomOut();
    }

    public void zoomIn(Vector2D point, double zoomScale) {
        GAME_SCALER.zoomIn(point, zoomScale);
    }

    public void update() {
        GAME_SCALER.tick();
        FRAME.repaint();
    }

    public void addResizeListener(WindowResizeListener windowResizeListener) {
        this.addComponentListener(windowResizeListener);
    }
}
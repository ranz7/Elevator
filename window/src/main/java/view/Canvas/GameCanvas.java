package view.Canvas;

import model.WindowModel;
import tools.Vector2D;
import view.Window.Window;
import view.drawTools.GameDrawer;
import view.drawTools.GameScaler;

import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JPanel {
    private JFrame frame;
    private WindowModel VIEW_MODEL;
    GameScaler gameScaler;
    GameDrawer gameDrawer;
    public GameCanvas(Window window, WindowModel viewModel) {
        VIEW_MODEL = viewModel;
        setBackground(VIEW_MODEL.COLOR_SETTINGS.BLACK_SPACE_COLOR);
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
        gameScaler.updateSizes(newSize, VIEW_MODEL.getSettings().BUILDING_SIZE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameDrawer.startDraw(g);

        drawWall(gameDrawer);
        VIEW_MODEL.getDrawableOjects().forEach(drawable -> drawable.draw(gameDrawer));
        drawBuilding(gameDrawer);
    }

    private void drawBuilding(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.BETON_COLOR);
            gameDrawer.drawRect(
                    new Vector2D(VIEW_MODEL.getSettings().BUILDING_SIZE.x / 2., i * floorHeight),
                    new Point((int) VIEW_MODEL.getSettings().BUILDING_SIZE.x, (int) floorHeight), 7);

            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.BLACK_SPACE_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0 - VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4., i * floorHeight - 2),
                    new Point(VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, (int) floorHeight)
            );
            gameDrawer.fillRect(
                    new Vector2D(
                            VIEW_MODEL.getSettings().BUILDING_SIZE.x, i * floorHeight - 2),
                    new Point(VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, (int) floorHeight)
            );
        }

    }

    private void drawWall(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.WALL_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0, i * floorHeight),
                    new Point((int) VIEW_MODEL.getSettings().BUILDING_SIZE.x, (int) floorHeight));
        }
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
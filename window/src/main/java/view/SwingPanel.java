package view;

import lombok.RequiredArgsConstructor;
import model.WindowModel;
import tools.Vector2D;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
class SwingPanel extends JPanel {
    private final WindowModel VIEW_MODEL;
    GameDrawer gameDrawer = new GameDrawer();


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameDrawer.updateGameDrawerWithSizes(
                this.getSize(), VIEW_MODEL.getSettings().BUILDING_SIZE, g);

        drawWall(gameDrawer);
        VIEW_MODEL.getDrawableOjects().forEach(drawable -> drawable.draw(gameDrawer));
        drawBuilding(gameDrawer);

        gameDrawer.restore();
    }

    private void drawBuilding(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.BETON_COLOR);
            gameDrawer.drawRect(
                    new Vector2D(VIEW_MODEL.getSettings().BUILDING_SIZE.x / 2., i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().BUILDING_SIZE.x, floorHeight), 7);

            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.BLACK_SPACE_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0 - VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4., i * floorHeight - 2),
                    new Point(VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, floorHeight)
            );
            gameDrawer.fillRect(
                    new Vector2D(
                            VIEW_MODEL.getSettings().BUILDING_SIZE.x, i * floorHeight - 2),
                    new Point(VIEW_MODEL.getSettings().CUSTOMER_SIZE.x * 4, floorHeight)
            );
        }

    }

    private void drawWall(GameDrawer gameDrawer) {
        var floorHeight = VIEW_MODEL.getSettings().BUILDING_SIZE.y / VIEW_MODEL.getSettings().FLOORS_COUNT;
        for (int i = 0; i < VIEW_MODEL.getSettings().FLOORS_COUNT; i++) {
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.WALL_COLOR);
            gameDrawer.fillRect(
                    new Vector2D(0, i * floorHeight),
                    new Point(VIEW_MODEL.getSettings().BUILDING_SIZE.x, floorHeight));
        }
    }
}
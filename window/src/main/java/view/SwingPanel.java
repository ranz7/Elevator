package view;

import lombok.RequiredArgsConstructor;
import model.WindowModel;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
class SwingPanel extends JPanel {
    private final WindowModel VIEW_MODEL;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBuilding(g);

    }

    private void drawBuilding(Graphics gameDrawer) {
        var height = 100.;
        var width = 100.;
        var floors_count = 3;
        var floorHeight = height / floors_count;

        for (int i = 0; i < floors_count; i++) {
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.BETON_COLOR);
            gameDrawer.drawRect((int) width / 2, (int) (i * floorHeight),
                    (int) width, (int) floorHeight);

            //black zones
            gameDrawer.setColor(VIEW_MODEL.COLOR_SETTINGS.BLACK_SPACE_COLOR);
            gameDrawer.fillRect(
                    0 - 15, (int) (i * floorHeight - 2),
                    15 * 4, (int) floorHeight
            );
            gameDrawer.fillRect((int) width, (int) (i * floorHeight - 2),
                    15, (int) floorHeight);
        }

    }

}
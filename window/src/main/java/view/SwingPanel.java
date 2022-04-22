package view;

import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.*;

@RequiredArgsConstructor
class SwingPanel extends JPanel {

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
            gameDrawer.setColor(new Color(83, 152, 80));
            gameDrawer.drawRect((int) width / 2, (int) (i * floorHeight),
                    (int) width, (int) floorHeight);

            //black zones
            gameDrawer.setColor(new Color(0, 0, 0));
            gameDrawer.fillRect(
                    0 - 15, (int) (i * floorHeight - 2),
                    15 * 4, (int) floorHeight
            );
            gameDrawer.fillRect((int) width, (int) (i * floorHeight - 2),
                    15, (int) floorHeight);
        }

    }

}
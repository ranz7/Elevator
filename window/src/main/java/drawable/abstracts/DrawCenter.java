package drawable.abstracts;

import tools.Vector2D;

public enum DrawCenter {
    bottomCenter, leftCenter, center, bottomRight, bottomLeft;

    public Vector2D getShiftDrawPosition(Vector2D position, Vector2D size) {
        return switch (this) {
            case bottomCenter -> position.getSubbed(new Vector2D(size.x / 2., 0));
            case leftCenter -> position.getSubbed(new Vector2D(0, size.y / 2.));
            case center -> position.getSubbed(new Vector2D(size.x / 2., size.y / 2.));
            case bottomRight -> position.getSubbed(new Vector2D(size.x, 0));
            case bottomLeft -> position.getSubbed(new Vector2D(0, 0));
        };

    }
}
// bottomLeft
// * * * * *
// * * * * *
// * * * * *
// * * * * *
// S * * * *

// bottomRight
// * * * * *
// * * * * *
// * * * * *
// * * * * *
// * * * * S

// bottomCenter
// * * * * *
// * * * * *
// * * * * *
// * * * * *
// * * S * *

// leftCenter
// * * * * *
// * * * * *
// S * * * *
// * * * * *
// * * * * *

// center
// * * * * *
// * * * * *
// * * S * *
// * * * * *
// * * * * *


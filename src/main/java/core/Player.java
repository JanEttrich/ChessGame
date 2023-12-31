package core;

import lombok.Data;

@Data
public class Player {
    private boolean white;
    private boolean human;
    private boolean castleShortAllowed;
    private boolean castleLongAllowed;

    public Player(boolean white, boolean human) {
        this.white = white;
        this.human = human;
        castleShortAllowed = true;
        castleLongAllowed = true;
    }

    public void disallowCastle() {
        castleShortAllowed = false;
        castleLongAllowed = false;
    }

    public void reAllowedCastle() {
        castleShortAllowed = true;
        castleLongAllowed = true;
    }

    public boolean canCastleOnAtLeastOneSide() {
        return castleShortAllowed || castleLongAllowed;
    }
}

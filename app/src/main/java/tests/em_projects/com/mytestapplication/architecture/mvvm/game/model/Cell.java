package tests.em_projects.com.mytestapplication.architecture.mvvm.game.model;

import tests.em_projects.com.mytestapplication.utils.StringUtils;

public class Cell {
    public Player player;


    public Cell(Player player) {
        this.player = player;
    }

    public boolean isEmpty() {
        return player == null || StringUtils.isNullOrEmpty(player.value);
    }
}

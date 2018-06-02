package tests.em_projects.com.mytestapplication.architecture.mvvm.game.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayMap;

import tests.em_projects.com.mytestapplication.architecture.mvvm.game.model.Cell;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.model.Game;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.model.Player;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

public class GameViewModel extends ViewModel {
    private static final String TAG = "GameViewModel";

    public ObservableArrayMap<String, String> cells;
    private Game game;

    public void init(String player1, String player2) {
        game = new Game(player1, player2);
        cells = new ObservableArrayMap<>();
    }

    public void onClickedCellAt(int row, int column) {
        if (game.getCells()[row][column] == null) {
            game.getCells()[row][column] = new Cell(game.getCurrentPlayer());
            cells.put(StringUtils.stringFromNumbers(row, column), game.getCurrentPlayer().value);
            if (game.hasGameEnded())
                game.reset();
            else
                game.switchPlayer();
        }
    }

    public LiveData<Player> getWinner() {
        return game.getWinner();
    }
}

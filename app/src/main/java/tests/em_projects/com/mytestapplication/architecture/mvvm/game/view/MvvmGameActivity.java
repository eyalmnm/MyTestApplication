package tests.em_projects.com.mytestapplication.architecture.mvvm.game.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.model.Player;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.view.dialogs.GameBeginDialog;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.view.dialogs.GameEndDialog;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.viewmodel.GameViewModel;
import tests.em_projects.com.mytestapplication.databinding.ActivityMvvmGameBinding;
import tests.em_projects.com.mytestapplication.utils.StringUtils;

public class MvvmGameActivity extends AppCompatActivity {
    private static final String GAME_BEGIN_DIALOG_TAG = "game_dialog_tag";
    private static final String GAME_END_DIALOG_TAG = "game_end_dialog_tag";
    private static final String NO_WINNER = "No one";
    private GameViewModel gameViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promptForPlayers();
    }

    public void promptForPlayers() {
        GameBeginDialog dialog = GameBeginDialog.newInstance(this);
        dialog.show(getFragmentManager(), GAME_BEGIN_DIALOG_TAG);
    }

    public void onPlayersSet(String player1, String player2) {
        initDataBinding(player1, player2);
    }

    private void initDataBinding(String player1, String player2) {
        // ActivityMvvmGameBinding generated automaticly by the layout xml file.
        ActivityMvvmGameBinding activityGameBinding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm_game);
        gameViewModel = ViewModelProviders.of(this).get(GameViewModel.class);
        gameViewModel.init(player1, player2);
        activityGameBinding.setGameViewModel(gameViewModel);
        setUpOnGameEndListener();
    }

    private void setUpOnGameEndListener() {
        gameViewModel.getWinner().observe(this, this::onGameWinnerChanged);
    }

    @VisibleForTesting
    public void onGameWinnerChanged(Player winner) {
        String winnerName = winner == null || StringUtils.isNullOrEmpty(winner.name) ? NO_WINNER : winner.name;
        GameEndDialog dialog = GameEndDialog.newInstance(this, winnerName);
        dialog.show(getFragmentManager(), GAME_END_DIALOG_TAG);
    }
}

package tests.em_projects.com.mytestapplication.architecture.mvvm.game.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import tests.em_projects.com.mytestapplication.R;
import tests.em_projects.com.mytestapplication.architecture.mvvm.game.view.MvvmGameActivity;

public class GameEndDialog extends DialogFragment {
    private View rootView;
    private MvvmGameActivity activity;
    private String winnerName;

    public static GameEndDialog newInstance(MvvmGameActivity activity, String winnerName) {
        GameEndDialog dialog = new GameEndDialog();
        dialog.activity = activity;
        dialog.winnerName = winnerName;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViews();
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setCancelable(false)
                .setPositiveButton(R.string.done, ((dialog, which) -> onNewGame()))
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    private void initViews() {
        rootView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_mvvm_game_end, null, false);
        ((TextView) rootView.findViewById(R.id.tv_winner)).setText(winnerName);
    }

    private void onNewGame() {
        dismiss();
        activity.promptForPlayers();
    }
}

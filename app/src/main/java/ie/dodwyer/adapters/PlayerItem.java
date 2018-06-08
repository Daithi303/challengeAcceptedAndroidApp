package ie.dodwyer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.main.ChallengeAcceptedApp;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/24/2017.
 */

public class PlayerItem {

    ChallengeAcceptedApp app;
    public View view;
    public PlayerItem(Context context, ViewGroup parent,
                      View.OnClickListener listener, Player player)
    {
        app = new ChallengeAcceptedApp();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.player_row, parent, false);
        view.setTag(player.getPlayerId());
        CheckBox playerAddRemoveToggle = (CheckBox) view.findViewById(R.id.playerAddRemoveCheckBox);
        playerAddRemoveToggle.setChecked(player.isCreateGameIsChecked());
        updateControls(player);

        playerAddRemoveToggle.setTag(player);
        playerAddRemoveToggle.setOnClickListener(listener);
    }


    private void updateControls(Player player) {
        ((TextView) view.findViewById(R.id.playerNameTextView)).setText(player.getfName()+" "+player.getlName());
        ((TextView) view.findViewById(R.id.playerEmailValueTextView)).setText(player.getEmail());


}
}
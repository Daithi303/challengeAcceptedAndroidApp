package ie.dodwyer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.GamePlayers;

/**
 * Created by User on 4/23/2017.
 */

public class ScoreboardPlayerListAdapter extends ArrayAdapter<GamePlayers> {

    private Context context;
    public List<GamePlayers> gamePlayersList;

    public ScoreboardPlayerListAdapter(Context context, List<GamePlayers> gamePlayersList) {
        super(context, R.layout.scoreboard_player_row, gamePlayersList);
        this.context = context;
        this.gamePlayersList = gamePlayersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ScoreboardPlayerItem scp = new ScoreboardPlayerItem(context,parent,gamePlayersList.get(position));
        return (scp.view);
    }
}

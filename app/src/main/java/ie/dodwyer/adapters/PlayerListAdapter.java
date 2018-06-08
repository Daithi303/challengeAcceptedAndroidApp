package ie.dodwyer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/24/2017.
 */

public class PlayerListAdapter extends ArrayAdapter<Player> {

    private Context context;
    private View.OnClickListener listener;
    public List<Player> playerList;

    public PlayerListAdapter(Context context, View.OnClickListener listener, List<Player> playerList) {
        super(context, R.layout.player_row, playerList);
        this.context = context;
        this.listener = listener;
        this.playerList = playerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

       PlayerItem c = new PlayerItem(context,parent,listener,playerList.get(position));
        return (c.view);
    }

    @Override
    public int getCount() {
        if(playerList==null){
            return 0;
        }
        return playerList.size();
    }
    public List<Player> getChallengeList() {
        return this.playerList;
    }
    @Override
    public Player getItem(int position) {
        return playerList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getPosition(Player p) {
        return playerList.indexOf(p);
    }
}

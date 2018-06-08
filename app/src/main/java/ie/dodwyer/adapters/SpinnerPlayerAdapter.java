package ie.dodwyer.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Challenge;
import ie.dodwyer.model.Player;

public class SpinnerPlayerAdapter extends BaseAdapter {
    Context context;
    public List<Player> playerList;
    LayoutInflater inflater;

    public SpinnerPlayerAdapter(Context context, List<Player> playerList) {
        this.context = context;
        this.playerList = playerList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return playerList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_player_row, null);
        //view.setTag(playerList.get(i).getPlayerId());
        view.setTag(playerList.get(i));
        ((TextView) view.findViewById(R.id.spinnerPlayerNameValueTextView)).setText(playerList.get(i).getfName()+" "+playerList.get(i).getlName());
        ((TextView) view.findViewById(R.id.spinnerPlayerEmailTextView)).setText(playerList.get(i).getEmail());
        return view;
    }
}
package ie.dodwyer.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Game;

/**
 * Created by User on 2/24/2017.
 */

public class GameListAdapter extends ArrayAdapter<Game> {

    private Context context;
    private View.OnClickListener deleteListener;
    private View.OnClickListener newChallengeListener;
    public List<Game> gameList;

    public GameListAdapter(Context context, View.OnClickListener deleteListener, View.OnClickListener newChallengeListener, List<Game> gameList) {
        super(context, R.layout.game_row, gameList);
        this.context = context;
        this.deleteListener = deleteListener;
        this.newChallengeListener = newChallengeListener;
        this.gameList = gameList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

       GameItem g = new GameItem(context,parent,deleteListener,newChallengeListener,gameList.get(position));

        return (g.view);
    }

    @Override
    public int getCount() {
        return gameList.size();
    }
    public List<Game> getGameList() {
        return this.gameList;
    }
    @Override
    public Game getItem(int position) {
        return gameList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getPosition(Game g) {
        return gameList.indexOf(g);
    }
}

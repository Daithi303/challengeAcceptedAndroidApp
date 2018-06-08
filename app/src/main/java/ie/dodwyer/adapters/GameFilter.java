package ie.dodwyer.adapters;

/**
 * Created by User on 3/3/2017.
 */

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.activities.Base;
import ie.dodwyer.model.Game;
import ie.dodwyer.model.GamePlayers;

public class GameFilter extends Filter{
    private List<Game> unfilteredGameList;
    private String playerFilter;
    private GameListAdapter 	adapter;
    protected Base activity;

    public GameFilter(List<Game> unfilteredGameList, String playerFilter, GameListAdapter adapter,Context context) {
        this.activity = (Base) context;
        this.unfilteredGameList = unfilteredGameList;
        this.playerFilter = playerFilter;
        this.adapter = adapter;
    }

    public void setPlayerFilter(String playerFilter) {
        this.playerFilter = playerFilter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (unfilteredGameList == null) {
            unfilteredGameList = new ArrayList<>();
        }
        if (constraint == null || constraint.length() == 0) {
            List<Game> newGames = new ArrayList<>();
            if (playerFilter==null) {
                results.values = unfilteredGameList;
                results.count = unfilteredGameList.size();
            } else {
                if (playerFilter.length() >0) {
                    List<GamePlayers> gamePlayers;
               gamePlayers = activity.app.dbManager.getGamePlayersConditional("playerId = '"+playerFilter+"'");
                    for(int i = 0;i<gamePlayers.size();i++){
                        newGames.add(activity.app.dbManager.getGamesConditional("gameId = "+gamePlayers.get(i).getGameId()).get(0));
                    }

                }
                results.values = newGames;
                results.count = newGames.size();
            }
        } else {
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.gameList = (ArrayList<Game>) results.values;

        if (results.count >= 0){
            adapter.notifyDataSetChanged();}
        else {
            adapter.notifyDataSetInvalidated();
            adapter.gameList = unfilteredGameList;
        }
    }
}

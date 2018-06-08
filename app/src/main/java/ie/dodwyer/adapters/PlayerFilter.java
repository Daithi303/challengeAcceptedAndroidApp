package ie.dodwyer.adapters;


/**
 * Created by User on 3/3/2017.
 */

        import android.content.Context;
        import android.view.View;
        import android.widget.Filter;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;

        import ie.dodwyer.activities.Base;
        import ie.dodwyer.fragments.PlayerItemFragment;
        import ie.dodwyer.model.Game;
        import ie.dodwyer.model.GamePlayers;
        import ie.dodwyer.model.Player;

public class PlayerFilter extends Filter {
    public List<Player> unfilteredPlayerList;
    public List<Player> conditionalList;
    private String playerFilter;
    private PlayerListAdapter adapter;
    protected Base activity;

    public PlayerFilter(List<Player> unfilteredPlayerList, String playerFilter, PlayerListAdapter adapter, Context context) {
        this.activity = (Base) context;
        this.unfilteredPlayerList = unfilteredPlayerList;
        this.playerFilter = playerFilter;
        this.adapter = adapter;
    }

    public void setPlayerFilter(String playerFilter) {
        this.playerFilter = playerFilter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence prefix) {
        FilterResults results = new FilterResults();
        activity.app.selectedPlayers= new ArrayList<>();

        if (unfilteredPlayerList == null) {
            unfilteredPlayerList = new ArrayList<>();
        }
        for(Player p:unfilteredPlayerList){
            p.setCreateGameIsChecked(false);
        }

        if(playerFilter.equals("addPlayer") && conditionalList!=null){
            unfilteredPlayerList = conditionalList;
        }
        if (prefix == null || prefix.length() == 0) {
                    results.values = unfilteredPlayerList;
                    results.count = unfilteredPlayerList.size();

            } else {
            List<Player>conditionalPlayerList =  unfilteredPlayerList;
                String prefixString = prefix.toString().toLowerCase();
                final ArrayList<Player> newPlayers = new ArrayList<Player>();
                for (Player p : conditionalPlayerList) {
                    final String fName = p.getfName().toLowerCase();
                    final String lName = p.getlName().toLowerCase();
                    final String name = fName+" "+lName;
                    if (name.contains(prefixString)) {
                        newPlayers.add(p);
                    }

                }
                results.values = newPlayers;
                results.count = newPlayers.size();

            }
        return results;
    }

    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.playerList = (ArrayList<Player>) results.values;

        if (results.count >= 0) {

            adapter.notifyDataSetChanged();
        } else {

            adapter.notifyDataSetInvalidated();
            adapter.playerList = unfilteredPlayerList;
        }
    }

}
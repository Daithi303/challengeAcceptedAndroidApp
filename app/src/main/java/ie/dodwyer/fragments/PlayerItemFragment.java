package ie.dodwyer.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.activities.Base;
import ie.dodwyer.adapters.PlayerFilter;
import ie.dodwyer.adapters.PlayerListAdapter;
import ie.dodwyer.model.Player;

/**
 * Created by User on 2/24/2017.
 */

public class PlayerItemFragment extends ListFragment implements View.OnClickListener{
    protected Base activity;
    protected static PlayerListAdapter listAdapter;
    protected ListView listView;
    public PlayerFilter playerFilter;
    public PlayerItemFragment(){
    }

    public static PlayerItemFragment newInstance() {
        PlayerItemFragment fragment = new PlayerItemFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Base) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new PlayerListAdapter(activity, this, activity.app.dbManager.getAllPlayers());
        playerFilter = new PlayerFilter((List<Player>)activity.app.dbManager.getAllPlayers(),"",listAdapter,this.getActivity());
        List<Player> p = activity.app.getPlayersNotInCurrentGame(activity.app.currentGame.getGameId());
        playerFilter.conditionalList = p;
        setListAdapter (listAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        Player player = (Player) v.getTag();
        CheckBox check = (CheckBox) v.findViewById(R.id.playerAddRemoveCheckBox);
        if(check.isChecked()){
        activity.app.selectedPlayers.add(player);
            player.setCreateGameIsChecked(true);
        }else{
            activity.app.selectedPlayers.remove(player);
            player.setCreateGameIsChecked(false);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(10);
    }
}

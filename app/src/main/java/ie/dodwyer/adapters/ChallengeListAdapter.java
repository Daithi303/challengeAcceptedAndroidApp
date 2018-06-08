package ie.dodwyer.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Challenge;

/**
 * Created by User on 2/24/2017.
 */

public class ChallengeListAdapter extends ArrayAdapter<Challenge> {
    int resource;
    private Context context;
    private View.OnClickListener listener;
    private View.OnClickListener editListener;
    private View.OnClickListener deleteListener;
    public List<Challenge> challengeList;

    public ChallengeListAdapter(Context context, View.OnClickListener listener, View.OnClickListener editListener, View.OnClickListener deleteListener, List<Challenge> challengeList,int resource) {
        super(context, resource, challengeList);
        this.resource = resource;
        this.context = context;
        this.listener = listener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
        this.challengeList = challengeList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {   ChallengeItem chall = new ChallengeItem(context,parent,listener,editListener,deleteListener,challengeList.get(position),resource);

        return chall.view;
    }

    @Override
    public int getCount() {
        return challengeList.size();
    }
    public List<Challenge> getChallengeList() {
        return this.challengeList;
    }
    @Override
    public Challenge getItem(int position) {
        return challengeList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getPosition(Challenge c) {
        return challengeList.indexOf(c);
    }
}

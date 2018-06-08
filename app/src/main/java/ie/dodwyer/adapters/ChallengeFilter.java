package ie.dodwyer.adapters;

/**
 * Created by User on 3/3/2017.
 */

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import ie.dodwyer.activities.Base;
import ie.dodwyer.model.Challenge;

public class ChallengeFilter extends Filter{
    public List<Challenge> unfilteredChallengeList;
    private String filter;
    public ChallengeListAdapter adapter;
    protected Base activity;

    public ChallengeFilter(List<Challenge> unfilteredChallengeList, String filter, ChallengeListAdapter adapter, Context context) {
        this.activity = (Base) context;
        this.unfilteredChallengeList = unfilteredChallengeList;
        this.filter = filter;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (unfilteredChallengeList == null) {
            unfilteredChallengeList = new ArrayList<>();
        }
        if (constraint == null || constraint.length() == 0) {
            List<Challenge> newChallenges = new ArrayList<>();
            if (filter.equals("")) {
                results.values = unfilteredChallengeList;
                results.count = unfilteredChallengeList.size();
            } else {
                if (filter.length() >0) {

                    newChallenges.addAll(activity.app.dbManager.getChallengesConditional(filter));
                }
                results.values = newChallenges;
                results.count = newChallenges.size();
            }
        } else {
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.challengeList = (ArrayList<Challenge>) results.values;

        if (results.count >= 0)
            adapter.notifyDataSetChanged();
        else {
            adapter.notifyDataSetInvalidated();
            adapter.challengeList = unfilteredChallengeList;
        }
    }
}

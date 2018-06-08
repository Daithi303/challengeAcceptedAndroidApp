package ie.dodwyer.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ie.dodwyer.R;
import ie.dodwyer.model.Challenge;

public class SpinnerChallengeAdapter extends BaseAdapter {
    Context context;
    public List<Challenge> challengeList;
    LayoutInflater inflater;

    public SpinnerChallengeAdapter(Context context, List<Challenge> challengeList) {
        this.context = context;
        this.challengeList = challengeList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return challengeList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.spinner_challenge_row, null);
        view.setId(challengeList.get(i).getChallengeId());
        view.setTag(challengeList.get(i));
        ((TextView) view.findViewById(R.id.spinnerChallengeSubjectValueTextView)).setText(challengeList.get(i).getSubject());
        ((TextView) view.findViewById(R.id.spinnerChallengeCreatedTextView)).setText(challengeList.get(i).getCreationDate());
        return view;
    }

    public int getPosition(Challenge challenge){
        return challengeList.indexOf(challenge);

    }
}
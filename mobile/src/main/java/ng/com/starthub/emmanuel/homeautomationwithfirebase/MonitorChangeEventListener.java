package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
modified by Emmanuel on 27/11/2019.
 */

public class MonitorChangeEventListener implements ValueEventListener {

    private final static String TAG  = MonitorChangeEventListener.class.getSimpleName();

    private  final TextView textView;
    private static final String INTRUDER = "Intruder detected";
    private static final String NO_INTRUDER = "No intruder";

    MonitorChangeEventListener(TextView textView){
        this.textView = textView;
    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Boolean status = (Boolean) dataSnapshot.getValue();
        if(status != null) {
            if (status) {
                textView.setText(INTRUDER);
            } else {
                textView.setText(NO_INTRUDER);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

package ng.com.starthub.emmanuel.homeautomationwithfirebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Emmanuel on 21/11/2017.
 */

public class InstanceIDService extends FirebaseInstanceIdService {

    private  static  final String TAG = InstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String tokn = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "The Token is: " + tokn);
    }
}

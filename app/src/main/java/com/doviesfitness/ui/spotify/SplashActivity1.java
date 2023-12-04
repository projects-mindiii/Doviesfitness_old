package com.doviesfitness.ui.spotify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.doviesfitness.R;
import com.doviesfitness.ui.spotify.Connectors.UserService;
import com.doviesfitness.ui.spotify.Model.User;
import com.doviesfitness.utils.PermissionAll;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

/**
 * Start Activity, authenticate Spotify
 */
public class SplashActivity1 extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    //private static final String CLIENT_ID = "3a7b0154a0fd4a868e41d59834f97bd5";
    private static final String CLIENT_ID = "1eee5e0d69594f2e90b3e7ecaf5915ac";
  //  private static final String REDIRECT_URI = "com.doviesfitness://callback";
    private static final String REDIRECT_URI = "com.doviesfitness.debug://callback";
    private static final int REQUEST_CODE = 1337;
  //  private static final String SCOPES = "user-read-recently-played,
    //  user-library-modify,user-library-read,playlist-modify-public,playlist-modify-private,user-read-email,user-read-private,user-read-birthdate,playlist-read-private,
    //  playlist-read-collaborative";

    private static final String SCOPES = "user-read-recently-played,user-library-modify," +
            "user-library-read," +
            "playlist-modify-public," +
            "playlist-modify-private," +
            "playlist-read-private," +
            "user-modify-playback-state," +
            "playlist-read-collaborative," +
            "user-read-email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getSupportActionBar().hide();
        setContentView(R.layout.activity_splash1);
        new PermissionAll().RequestMultiplePermission(SplashActivity1.this);


        authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }


    private void waitForUserInfo() {
        UserService userService = new UserService(queue, msharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            Log.d("STARTING", "GOT USER INFORMATION");
            // We use commit instead of apply because we need the information stored immediately
            editor.commit();
          //   String endpoint = "https://api.spotify.com/v1/users/{user_id}/playlists/{playlist_id}? ";

            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent newintent = new Intent(SplashActivity1.this, MainActivity1.class);
        startActivity(newintent);
    }


    private void authenticateSpotify() {

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            Log.d("STARTING", "GOT AUTH TOKEN..."+response.getType());

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    waitForUserInfo();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }


}
package com.doviesfitness.ui.spotify;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.doviesfitness.R;
import com.doviesfitness.ui.base.BaseActivity;
import com.doviesfitness.ui.spotify.Connectors.SongService;
import com.doviesfitness.ui.spotify.Model.Song;
import com.doviesfitness.utils.CommanUtils;
import com.google.android.exoplayer2.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity1 extends BaseActivity {

    private TextView userView;
    private TextView songView;
    private Button addBtn,play;
    private Song song;

    private SongService songService;
    private ArrayList<Song> recentlyPlayedTracks;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        songService = new SongService(getApplicationContext());
        userView = (TextView) findViewById(R.id.user);
        songView = (TextView) findViewById(R.id.song);
        addBtn = (Button) findViewById(R.id.add);
        play = (Button) findViewById(R.id.play);

         sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        String id=sharedPreferences.getString("userid", "No User");
        songService.setUserid(id);
        userView.setText(sharedPreferences.getString("userid", "No User"));

        play.setOnClickListener(view -> getlist());
        addBtn.setOnClickListener(addListener);

        //   mp.setDataSource(MainActivity1.this,);

    }


    void onPlay(){
        MediaPlayer mp=new MediaPlayer();
        try {
         // Uri uri= new Uri();
           // mp.setDataSource(MainActivity1.this,Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3"));
           // mp.setDataSource(MainActivity1.this,Uri.parse("https://open.spotify.com/artist/5f4QpKfy7ptCHwTqspnSJI"));
            mp.setDataSource(MainActivity1.this,Uri.parse("https:\\/\\/api.spotify.com\\/v1\\/artists\\/5f4QpKfy7ptCHwTqspnSJI"));
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private View.OnClickListener addListener = v -> {

        PlaySong();
        //  getTracks();

      /*  songService.addSongToLibrary(this.song);
        if (recentlyPlayedTracks.size() > 0) {
            recentlyPlayedTracks.remove(0);
        }
        updateSong();*/
    };


    private void getTracks() {
/*
        songService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateSong();
        });
*/
        songService.getPlaylistTracks(() -> {
            recentlyPlayedTracks = songService.getSongs();
            updateSong();
        });


    }

    private void updateSong() {
        if (recentlyPlayedTracks.size() > 0) {
            songView.setText(recentlyPlayedTracks.get(0).getName());
            song = recentlyPlayedTracks.get(0);
        }
    }

    private void getlist() {
        HashMap param =new  HashMap<String, String>();
           // param.put("auth_customer_id", "");
           // param.put("device_id", "");
           // param.put("device_token", "");
        HashMap header = new HashMap<String, String>();
          //  header.put("AUTHTOKEN", "");

        String token = sharedPreferences.getString("token", "");
        String auth = "Bearer " + token;
        header.put("Authorization", auth);


        getDataManager().playlistApi(param, header).getAsJSONObject(  new  JSONObjectRequestListener(){

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity1.this,"response.."+response.toString(),Toast.LENGTH_SHORT);
                Log.d("response","response..."+response.toString());
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(MainActivity1.this,"error.."+anError.getErrorBody(),Toast.LENGTH_SHORT);
                Log.d("response","response error..."+anError.getErrorBody());

            }
        });

/*
        getDataManager().feedListApi(param, header).getAsJSONObject(new JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.rltvLoader.visibility=View.GONE

                    Log.d("resonse...", "response data..." + response!!.toString(3)): Exception) {
                        binding.rltvLoader.visibility=View.GONE

                        if (mContext != null && isAdded)
                            Constant.showCustomToast(
                                    mContext,
                                    getString(R.string.something_wrong)
                            )
                    }
                }

                override fun onError(anError: ANError) {

                }

            })
*/
        }
    private void PlaySong() {
        HashMap param =new  HashMap<String, String>();
           // param.put("auth_customer_id", "");
           // param.put("device_id", "");
           // param.put("device_token", "");
        HashMap header = new HashMap<String, String>();
          //  header.put("AUTHTOKEN", "");

        String token = sharedPreferences.getString("token", "");
        String auth = "Bearer " + token;
        header.put("Authorization", auth);


        getDataManager().playSong(param, header).getAsJSONObject(  new  JSONObjectRequestListener(){

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity1.this,"response.."+response.toString(),Toast.LENGTH_SHORT);
                Log.d("response","response..."+response.toString());
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(MainActivity1.this,"error.."+anError.getErrorBody(),Toast.LENGTH_SHORT);
                Log.d("response","response error..."+anError.getErrorBody());

            }
        });

/*
        getDataManager().feedListApi(param, header).getAsJSONObject(new JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.rltvLoader.visibility=View.GONE

                    Log.d("resonse...", "response data..." + response!!.toString(3)): Exception) {
                        binding.rltvLoader.visibility=View.GONE

                        if (mContext != null && isAdded)
                            Constant.showCustomToast(
                                    mContext,
                                    getString(R.string.something_wrong)
                            )
                    }
                }

                override fun onError(anError: ANError) {

                }

            })
*/
        }




}


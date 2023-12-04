package com.doviesfitness.ui.spotify;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.doviesfitness.R;
import com.doviesfitness.ui.base.BaseActivity;
import com.doviesfitness.ui.spotify.Connectors.SongService;
import com.doviesfitness.ui.spotify.Model.Song;
import com.doviesfitness.utils.PermissionAll;
import com.google.android.exoplayer2.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.sql.DriverManager.println;

public class MainActivity2 extends BaseActivity {

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
        addBtn = (Button) findViewById(R.id.add);
        play = (Button) findViewById(R.id.play);
     //  play.setOnClickListener(view -> getlist());
       addBtn.setOnClickListener(view -> showSecondPlaylist());

         new PermissionAll().RequestMultiplePermission(getActivity());

        //   mp.setDataSource(MainActivity1.this,);

    }

    private void getPlaylist() {
      /*  String[] proj = {"*"};
        Uri tempPlaylistURI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        // In the next line 'this' points to current Activity.
        // If you want to use the same code in other java file then activity,
        // then use an instance of any activity in place of 'this'.

        Cursor playListCursor= this.managedQuery(tempPlaylistURI, proj, null,null,null);

        if(playListCursor == null){
            System.out.println("Not having any Playlist on phone --------------");
            return;//don't have list on phone
        }

        System.gc();

        String playListName = null;

        System.out.println(">>>>>>>  CREATING AND DISPLAYING LIST OF ALL CREATED PLAYLIST  <<<<<<");

        for(int i = 0; i <playListCursor.getCount() ; i++)
        {
            playListCursor.moveToPosition(i);
            playListName = playListCursor.getString(playListCursor.getColumnIndex("name"));
            System.out.println("> " + i + "  : " + playListName );
        }

        if(playListCursor != null)
            playListCursor.close();
*/

    }

    private void showSecondPlaylist(){
        final String[] ARG_STRING = {   MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                android.provider.MediaStore.MediaColumns.DATA };
        Uri membersUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songsWithingAPlayList = managedQuery(membersUri, ARG_STRING, null, null, null);
        int theSongIDIwantToPlay = 0; // PLAYING FROM THE FIRST SONG
        if (songsWithingAPlayList != null)
        {
            songsWithingAPlayList.moveToPosition(theSongIDIwantToPlay);
            String DataStream = songsWithingAPlayList.getString(4);
//PlayMusic(DataStream)
            println(DataStream + " datastream");
            PlayMusic(DataStream);
         //  PlayMusic("/storage/emulated/0/WhatsApp/Media/WhatsApp Audio/AUD-20200809-WA0009.aac");

        }
        String playListName="";
         for (boolean hasItem = songsWithingAPlayList.moveToFirst(); hasItem; hasItem = songsWithingAPlayList.moveToNext()) {
       // playLists.moveToFirst();
        playListName = songsWithingAPlayList.getString(4);
        String id = songsWithingAPlayList.getString(0);
        String name = songsWithingAPlayList.getString(1);

             Log.d("playlist....", "playlist...."+playListName+"......id...."+id+".....name....."+name);
           }
        songsWithingAPlayList.close();

    }
    public void playTrackFromPlaylist(final long playListID) {
        final ContentResolver resolver = this.getContentResolver();
        final Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListID);
        final String dataKey = MediaStore.Audio.Media.DATA;
        Cursor tracks = resolver.query(uri, new String[] { dataKey }, null, null, null);
        if (tracks != null) {
            tracks.moveToFirst();
            final int dataIndex = tracks.getColumnIndex(dataKey);
            final String dataPath = tracks.getString(dataIndex);
            PlayMusic(dataPath);
            tracks.close();
        }
    }
    public void PlayMusic(String DataStream) {
        MediaPlayer mpObject = new MediaPlayer();
        if (DataStream == null)
            return;
        try {
            mpObject.setDataSource(DataStream);
            mpObject.prepare();
            mpObject.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    void getlist(){
        final ContentResolver resolver = this.getContentResolver();
     //   final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        final String idKey = MediaStore.Audio.Playlists._ID;
        final String nameKey = MediaStore.Audio.Playlists.NAME;
        final String[] columns = { idKey, nameKey };
        final Cursor playLists = resolver.query(uri, columns, null, null, null);
        if (playLists == null) {
            Log.d("playlist", "Found no playlists.");
            return;
        }

        // Log a list of the playlists.
        Log.d("playlist", "Playlists:");
        String playListName = null;
      //  for (boolean hasItem = playLists.moveToFirst(); hasItem; hasItem = playLists.moveToNext()) {
        playLists.moveToFirst();
            playListName = playLists.getString(playLists.getColumnIndex(nameKey));
            Log.d("playlist", playListName);
     //   }

        // Play the first song from the first playlist.
        playLists.moveToFirst();
        final long playlistID = playLists.getLong(playLists.getColumnIndex(idKey));
        this.playTrackFromPlaylist(playlistID);

        // Close the cursor.
        if (playLists != null) {
            playLists.close();
        }


    }
*/


}


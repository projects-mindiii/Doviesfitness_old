package com.doviesfitness.ui.multipleQuality;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.doviesfitness.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static com.google.android.exoplayer2.util.Assertions.checkArgument;
public class SampleChooserActivity extends AppCompatActivity {
  private static final String TAG = "SampleChooserActivity";
  private String[] uris;
  private MenuItem preferExtensionDecodersMenuItem;
  TextView sampleTitle;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.sample_chooser_activity);
    sampleTitle =findViewById(R.id.sample_title);
    loadSample();
  }
  private void loadSample() {
    SampleListLoader loaderTask = new SampleListLoader();
    loaderTask.execute("");
  }
  private void onPlaylistGroups(final PlaylistHolder groups, boolean sawError) {
    if (sawError) {
      Toast.makeText(getApplicationContext(), R.string.sample_list_load_error, Toast.LENGTH_LONG)
          .show();
    }
    sampleTitle.setText(groups.title);
    sampleTitle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(SampleChooserActivity.this, StreamPlayerActivity.class);
        intent.putExtra(
                IntentUtil.PREFER_EXTENSION_DECODERS_EXTRA,
                isNonNullAndChecked(preferExtensionDecodersMenuItem));
        IntentUtil.addToIntent(groups.mediaItems, intent);
        startActivity(intent);
      }
    });
  }
  private static boolean isNonNullAndChecked(@Nullable MenuItem menuItem) {
    return menuItem != null && menuItem.isChecked();
  }

  private final class SampleListLoader extends AsyncTask<String, Void, PlaylistHolder> {
    private boolean sawError;
    @Override
    protected PlaylistHolder doInBackground(String... uris) {
      PlaylistHolder result = null;
      try {
        result= readEntry();
      } catch (Exception e) {
        Log.e(TAG, "Error loading sample list: " +  e);
        sawError = true;
      } finally {
      }
      return result;
    }
    @Override
    protected void onPostExecute(PlaylistHolder result) {
      onPlaylistGroups(result, sawError);
    }
    private PlaylistHolder readEntry() throws IOException {
      Uri uri = null;
      String extension = null;
      String title = null;
      MediaItem.Builder mediaItem = new MediaItem.Builder();
      title = "Dovies video";
        //  uri = Uri.parse("https://s3.us-east-2.amazonaws.com/dovies-fitness-dev/stream_video/2KOIkHRSBE8bXwfM.mp4");
          uri = Uri.parse("https://www.youtube.com/api/manifest/dash/id/3aa39fa2cc27967f/source/youtube?as=fmp4_audio_clear,fmp4_sd_hd_clear&sparams=ip,ipbits,expire,source,id,as&ip=0.0.0.0&ipbits=0&expire=19000000000&signature=A2716F75795F5D2AF0E88962FFCD10DB79384F29.84308FF04844498CE6FBCE4731507882B8307798&key=ik0");
        extension = "mpd";
        @Nullable
        String adaptiveMimeType =
            Util.getAdaptiveMimeTypeForContentType(Util.inferContentType(uri, extension));
        mediaItem
            .setUri(uri)
            .setMediaMetadata(new MediaMetadata.Builder().setTitle(title).build())
            .setMimeType(adaptiveMimeType);
        return new PlaylistHolder(title, Collections.singletonList(mediaItem.build()));
    }
  }

  private static final class PlaylistHolder {
    public final String title;
    public final List<MediaItem> mediaItems;
    private PlaylistHolder(String title, List<MediaItem> mediaItems) {
      checkArgument(!mediaItems.isEmpty());
      this.title = title;
      this.mediaItems = Collections.unmodifiableList(new ArrayList<>(mediaItems));
    }
  }
}

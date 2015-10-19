package io.caster.rxexamples;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new AsyncTask<Void, Void, Gist>() {

            public IOException error;

            @Override
            protected Gist doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient();

                // Go get this Gist: https://gist.github.com/donnfelker/db72a05cc03ef523ee74
                // via the GitHub API
                Request request = new Request.Builder()
                        .url("https://api.github.com/gists/db72a05cc03ef523ee74")
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        Gist gist = new Gson().fromJson(response.body().charStream(), Gist.class);
                        return gist;
                    }

                    return null;

                } catch (IOException e) {
                    this.error = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Gist gist) {
                super.onPostExecute(gist);

                StringBuilder sb = new StringBuilder();
                // Output
                for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                    sb.append(entry.getKey());
                    sb.append(" - ");
                    sb.append("Length of file ");
                    sb.append(entry.getValue().content.length());
                    sb.append("\n");
                }

                TextView text = (TextView) findViewById(R.id.main_message);
                text.setText(sb.toString());
            }
        }.execute();

    }

}

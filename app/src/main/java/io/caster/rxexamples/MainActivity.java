package io.caster.rxexamples;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
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

                    if(response.isSuccessful()) {
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

                // Output
                for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                    Log.d(TAG, entry.getKey());
                    Log.d(TAG, "Length of file: " + entry.getValue().content.length());
                }

            }
        }.execute();

    }


    public static void showCustomDialog(String title, String message, Context activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton(activity.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}

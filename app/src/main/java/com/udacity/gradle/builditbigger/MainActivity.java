package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

import hu.drorszagkriszaxel.jokelibrary.ShowJoke;

/**
 * This class maintains MainActivity's workflow.
 */
public class MainActivity extends AppCompatActivity {

    // An instance to help Espresso idling test.
    public final CountingIdlingResource countingIdlingResource =
            new CountingIdlingResource(JokeEndpointAsyncTask.class.getSimpleName());

    /**
     * Standard onCreate method without modification.
     *
     * @param savedInstanceState Saved instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    /**
     * Standard onCreateOptionsMenu method without modification.
     *
     * @param menu Menu to inflate
     * @return     True if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     * Standard onOptionsItemSelected method without modification.
     *
     * @param item Selected item
     * @return     True if action handled successfully
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;

        }

        return super.onOptionsItemSelected(item);

    }

    /**
     * ClickListener for the activity's button that initiates the process to get a joke.
     *
     * @param view The instance of the button but it's not handled
     */
    public void tellJoke(View view) {

        new JokeEndpointAsyncTask(this).execute();

    }

    /**
     * A static class to manage AsyncTask to get a joke without instantiation.
     */
    public static class JokeEndpointAsyncTask extends AsyncTask<Void,Void,String> {

        // Final fields to kind of class persistence.
        private MyApi myApi = null;
        private final WeakReference<MainActivity> weakReference;

        /**
         * Simple constructor that handles a weak reference to the AsyncTask's context.
         *
         * @param mainActivity Source of the best friend of an Android developer, the context
         */
        JokeEndpointAsyncTask(MainActivity mainActivity) {

            this.weakReference = new WeakReference<>(mainActivity);

        }

        /**
         * The core of the AsyncTask, this method gets the joke actually.
         *
         * @param voids Voids are always the best parameters you can ignore them they don't mind it
         * @return      Hopefully a joke, in worst cases something else if anything
         */
        @Override
        protected String doInBackground(Void... voids) {

            weakReference.get().countingIdlingResource.increment();

            if (myApi == null) {

                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer( new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                                request.setDisableGZipContent(true);
                            }
                        });

                myApi = builder.build();

            }

            String returnString = "Theres is no joke anymore.";

            try {

                returnString = myApi.tellJoke().execute().getData();

            } catch (IOException e) {

                returnString = e.getMessage();

            }

            return returnString;

        }

        /**
         * Sends the joke to the activity.
         *
         * @param s The joke
         */
        @Override
        protected void onPostExecute(String s) {

            Context context = weakReference.get();
            Intent intent = new Intent(context, ShowJoke.class);

            intent.putExtra(ShowJoke.EXTRA_JOKE_TEXT,s);
            context.startActivity(intent);

            weakReference.get().countingIdlingResource.decrement();

        }

    }

}

package io.caster.rxexamples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import io.caster.rxexamples.examples.AmbFragment;
import io.caster.rxexamples.examples.IntroFragment;
import io.caster.rxexamples.examples.ZipFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigateToMain();

    }

    private void navigateToMain() {
        changeFragment(MainFragment.newInstance());
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

}

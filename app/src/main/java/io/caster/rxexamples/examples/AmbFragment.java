package io.caster.rxexamples.examples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AmbFragment extends Fragment {

    public static AmbFragment newInstance() {
        return new AmbFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Just some view
        return new LinearLayout(getActivity());
    }


}

package io.caster.rxexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.caster.rxexamples.examples.rxjava1.operators.AmbFragment;
import io.caster.rxexamples.examples.rxjava1.subjects.AsyncSubjectFragment;
import io.caster.rxexamples.examples.rxjava1.subjects.BehaviorSubjectFragment;
import io.caster.rxexamples.examples.rxjava2.operators.JustFragment;
import io.caster.rxexamples.examples.rxjava2.operators.RepeatFragment;
import io.caster.rxexamples.examples.IntroFragment;
import io.caster.rxexamples.examples.rxjava1.subjects.PublishSubjectFragment;
import io.caster.rxexamples.examples.rxjava1.subjects.ReplaySubjectFragment;
import io.caster.rxexamples.examples.rxjava1.operators.ZipFragment;

public class MainFragment extends Fragment {

    public static Fragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    private void changeFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.root_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.just_fragment)
    public void onJustFragmentClick() {
        changeFragment(new JustFragment());
    }

    @OnClick(R.id.intro_fragment)
    public void onIntroFragmentClick() {
        changeFragment(IntroFragment.newInstance());
    }

    @OnClick(R.id.zip_fragment)
    public void onZipFragmentClick() {
        changeFragment(ZipFragment.newInstance());
    }

    @OnClick(R.id.amb_fragment)
    public void onAmbFragmentClick() {
        changeFragment(AmbFragment.newInstance());
    }

    @OnClick(R.id.repeat_fragment)
    public void onCombineLatestClick() {
        changeFragment(new RepeatFragment());
    }

    @OnClick(R.id.replay_subject_fragment)
    public void onReplaySubjectFragmentClick() {
        changeFragment(ReplaySubjectFragment.newInstance());
    }


    @OnClick(R.id.async_subject_fragment)
    public void onAsyncSubjectClick() {
        changeFragment(AsyncSubjectFragment.newInstance());
    }

    @OnClick(R.id.publish_subject_fragment)
    public void onPublishSubjectClick() {
        changeFragment(PublishSubjectFragment.newInstance());
    }

    @OnClick(R.id.behavior_subject_fragment)
    public void onBehaviorSubjectClick() {
        changeFragment(BehaviorSubjectFragment.newInstance());
    }
}

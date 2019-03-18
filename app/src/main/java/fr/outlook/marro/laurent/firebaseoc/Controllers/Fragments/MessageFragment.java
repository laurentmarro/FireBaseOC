package fr.outlook.marro.laurent.firebaseoc.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import fr.outlook.marro.laurent.firebaseoc.R;

public class MessageFragment extends Fragment {

    public static MessageFragment newInstance() {
        return (new MessageFragment());
    }

    public MessageFragment () { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
package com.example.ervand.voloappnotes.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.model.User;
import com.example.ervand.voloappnotes.view.activities.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class LoginFragment extends Fragment implements SwitchFragment {

    private Unbinder unbinder;
    private Realm realm;

    @BindView(R.id.edtLogin)
    EditText edtLogin;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.btnLogin, R.id.txtGoRegisterPage, R.id.lLayoutLoginFragment})
    void onClick(View button) {
        switch (button.getId()) {
            case R.id.btnLogin:
                if (isCheckLoginAndPassword(edtLogin.getText().toString().trim(),
                        edtPassword.getText().toString().trim())) {
                    switchFragment(new NotesFragment(), R.id.mainContainer, "Notes Page");
                    edtLogin.setText("");
                    edtPassword.setText("");
                } else {
                    Toast.makeText(getContext(), "Please input correct email or password",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtGoRegisterPage:
                switchFragment(new RegisterFragment(), R.id.mainContainer, "Register Page");
                break;
            case R.id.lLayoutLoginFragment:
                if (getActivity().getCurrentFocus() != null){
                    InputMethodManager inputMethodManagerLog = (InputMethodManager)
                            getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManagerLog.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }

    private boolean isCheckLoginAndPassword(CharSequence email, String password) {
        RealmResults<User> users = realm.where(User.class).equalTo("password", password)
                .equalTo("email", (String) email).findAll();
        if (!users.isEmpty()) {
            MainActivity.user = users.first();
        }
        return !users.isEmpty();
    }

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        realm.close();
    }

    @Override
    public void switchFragment(Fragment fragment, int containerId, String TAG) {
        getFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();
    }
}

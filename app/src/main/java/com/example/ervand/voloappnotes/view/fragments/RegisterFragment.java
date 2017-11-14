package com.example.ervand.voloappnotes.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.ervand.voloappnotes.R;
import com.example.ervand.voloappnotes.constants.Constants;
import com.example.ervand.voloappnotes.model.User;
import java.util.regex.Matcher;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import static android.content.Context.INPUT_METHOD_SERVICE;

public class RegisterFragment extends Fragment implements SwitchFragment {

    private Realm realm;
    private Unbinder unbinder;

    @BindView(R.id.edtRegisterLogin)
    EditText edtRegisterLogin;

    @BindView(R.id.edtRegisterPassword)
    EditText edtRegisterPassword;

    @BindView(R.id.btnRegisterBtn)
    Button btnRegisterBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick({R.id.btnRegisterBtn, R.id.txtGoLoginPage, R.id.lLayoutRegisterFragment})
    void onClick(View button) {
        switch (button.getId()) {
            case R.id.btnRegisterBtn:
                if (isValidFields()) {
                    if (isValidEmail(edtRegisterLogin.getText().toString().trim())) {
                        if (isValidPassword(edtRegisterPassword.getText().toString().trim())) {
                            User user = new User(edtRegisterLogin.getText().toString().trim()
                                    , edtRegisterPassword.getText().toString().trim());

                            saveUserToDB(user);
                            switchFragment(new LoginFragment(), R.id.mainContainer,
                                    "Login page");
                            edtRegisterPassword.setText("");
                            edtRegisterLogin.setText("");
                            Toast.makeText(getContext(), "User saved", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            edtRegisterPassword.setError("The password must contain 8-12 " +
                                    "characters,mandatory numbers and symbols\n");
                        }
                    } else {
                        edtRegisterLogin.setError("Email address is invalid");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Complete all fields ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txtGoLoginPage:
                switchFragment(new LoginFragment(), R.id.mainContainer, "Login page");
                break;
            case R.id.lLayoutRegisterFragment:
                if (getActivity().getCurrentFocus() != null){
                    InputMethodManager inputMethodManagerReg = (InputMethodManager)
                            getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManagerReg.hideSoftInputFromWindow(getActivity()
                            .getCurrentFocus().getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }

    private void saveUserToDB(User userRealm) {
        realm.beginTransaction();
        realm.copyToRealm(userRealm);
        realm.commitTransaction();
    }

    private boolean isValidEmail(CharSequence email) {
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private boolean isValidPassword(String password) {
        Matcher matcher = Constants.VALID_PASSWORD_REGEX.matcher(password);
        return matcher.find();
    }

    private boolean isValidFields() {
        return !edtRegisterLogin.getText().toString().trim().equals("")
                && !edtRegisterPassword.getText().toString().trim().equals("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        unbinder.unbind();
    }

    @Override
    public void switchFragment(Fragment fragment, int containerId, String TAG) {
        getFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();
    }
}

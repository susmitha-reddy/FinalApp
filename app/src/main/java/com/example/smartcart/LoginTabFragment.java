package com.example.smartcart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import io.realm.mongodb.User;

public class LoginTabFragment extends Fragment {

    EditText username,password;
    Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        username = root.findViewById(R.id.login_name);
        password = root.findViewById(R.id.login_password);
        login = root.findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                LoginService loginService = new LoginService();
                loginService.loginVerification(String.valueOf(username.getText()),String.valueOf(password.getText()), getActivity(), false);
            }
        });
        return root;
    }
}

package com.example.smartcart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.smartcart.util.PreferenceManager;

import io.realm.mongodb.User;

public class LoginTabFragment extends Fragment {

    EditText username,password;
    Button login;
    CheckBox remember_me;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        username = root.findViewById(R.id.login_name);
        password = root.findViewById(R.id.login_password);
        login = root.findViewById(R.id.login_button);
        remember_me = root.findViewById(R.id.remember_me);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                LoginService loginService = new LoginService();
                loginService.loginVerification(String.valueOf(username.getText()),String.valueOf(password.getText()), getActivity(), false);
                if(remember_me.isChecked()){
                    PreferenceManager.getInstance(getActivity()).saveString("Username",String.valueOf(username.getText()));
                    PreferenceManager.getInstance(getActivity()).saveString("Password",String.valueOf(password.getText()));
                }
            }
        });

        return root;
    }
}

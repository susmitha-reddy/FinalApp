package com.example.smartcart;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class SignUpTabFragment extends Fragment {

    EditText email,password,confirmp;
    Button signup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIsntanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_tab_fragment,container,false);

        email = root.findViewById(R.id.signup_email);
        password = root.findViewById(R.id.signup_password);
        confirmp = root.findViewById(R.id.signup_confirmp);
        signup = root.findViewById(R.id.sign_up_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup.setEnabled(false);
                LoginService loginService = new LoginService();
                loginService.registerUser(String.valueOf(email.getText()), String.valueOf(password.getText()), getActivity());
            }
        });
        return root;
    }
}

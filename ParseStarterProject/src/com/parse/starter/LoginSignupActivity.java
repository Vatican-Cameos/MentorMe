package com.parse.starter;

/**
 * Created by kai on 29/9/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;

public class LoginSignupActivity extends Activity implements AdapterView.OnItemSelectedListener {
    // Declare Variables
    ShimmerButton loginbutton;
    ShimmerButton signup;
    String usernametxt;
    String passwordtxt;
    String whoami;
    EditText password;
    EditText username;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_login_signup);
        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Mentor", "Student"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in main.xml
        loginbutton = (ShimmerButton) findViewById(R.id.login);
        signup = (ShimmerButton) findViewById(R.id.signup);
        Shimmer shimmer = new Shimmer();
        shimmer.start(loginbutton);
        shimmer.start(signup);

        // Login Button Click Listener
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // If user exist and authenticated, send user to Welcome.class
                                    if(user.getString("whoami").equals("teacher")){
                                    Intent intent = new Intent(
                                            LoginSignupActivity.this,
                                            Welcome.class);
                                    startActivity(intent);
                                    }

                                    if(user.getString("whoami").equals("student")){
                                            Intent intent = new Intent(
                                                    LoginSignupActivity.this,
                                                    Swelcome.class);
                                            startActivity(intent);
                                    }


                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Logged in",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "No such user exist, please signup",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                if (usernametxt.equals("") && passwordtxt.equals("")) {

                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_SHORT).show();

                } else {

                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.put("whoami",whoami);
                    user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up!",
                                        Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        "Sign up error", Toast.LENGTH_SHORT)
                                        .show();

                            }
                        }
                    });

                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){
            whoami = "teacher";
        }
        else{
            whoami = "student";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        whoami = "teacher";
    }
}
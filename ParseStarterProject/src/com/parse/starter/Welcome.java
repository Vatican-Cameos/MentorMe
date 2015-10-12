package com.parse.starter;

/**
 * Created by kai on 29/9/15.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerButton;
import com.romainpiel.shimmer.ShimmerTextView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends Activity {

    // Declare Variable
    ShimmerButton logout;
    ListView myList;
    List<String> populate;
    String globalUsn;
    ArrayAdapter<String> arrayAdapter;

    ParseUser currentUser;
    String struser;
    Context x;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.welcome);

       new Read().execute();
        // Retrieve current user from Parse.com
         currentUser = ParseUser.getCurrentUser();


        // Convert currentUser into String
        struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        //TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        ShimmerTextView myShimmerTextView = (ShimmerTextView)findViewById(R.id.txtuser);
        Shimmer shimmer = new Shimmer();
        shimmer.start(myShimmerTextView);
        myShimmerTextView.setText("Welcome  " + struser);

        // Locate Button in welcome.xml
        logout = (ShimmerButton) findViewById(R.id.logout);
        shimmer.start(logout);
        myList=(ListView)findViewById(R.id.slist);
        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });



        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedFromList =(myList.getItemAtPosition(i).toString());
                //Create the bundle
                Bundle bundle = new Bundle();

                bundle.putString("TAKE_NAME", selectedFromList);
                Intent in = new Intent(Welcome.this,Personal.class);
                in.putExtras(bundle);
                startActivity(in);

            }
        });
    }




    class Read extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            populate= new ArrayList<String>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("mentorDB");
          //  final ParseObject gameScore = new ParseObject("mentorDB");
            query.whereEqualTo("mentor", struser);

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (scoreList.size()>0) {

                        for (ParseObject value : scoreList) {


                            //to access the properties of the Deals object.
                            //to access the properties of the Deals object.
                            populate.add(value.getString("Name") +"\n"+ "Usn:" + value.getString("usn"));
                        }
                         arrayAdapter = new ArrayAdapter<String>(Welcome.this, R.layout.list, populate);
                        myList.setAdapter(arrayAdapter);


                    } else {
                        populate.add("No students under you at the moment");
                        arrayAdapter = new ArrayAdapter<String>(Welcome.this, R.layout.list, populate);
                        myList.setAdapter(arrayAdapter);

                    }
                }
            });
            return null;
        }
    }
}

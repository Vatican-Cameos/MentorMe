package com.parse.starter;

/**
 * Created by kai on 29/9/15.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import com.loopj.android.image.SmartImageView;
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

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Swelcome extends Activity implements AdapterView.OnItemSelectedListener {

    // Declare Variable

    TextView mentorFound;
    List<String> a = new ArrayList<String>();
    ShimmerButton logout,submit;
    EditText ename,eusn,ebranch,eaddress,ephone,eaggregate;
    SmartImageView myImage;
    String selectedPath1 = "NONE";
    ParseFile file ;
    ParseUser currentUser;
    String struser,name,usn,branch,address,phone,mentor;
    String aggregate;
    Spinner dropdown;
    Context x = this;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.swelcome);
        a.add("None");



        // Retrieve current user from Parse.com
        currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        //TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Shimmer for the name
        ShimmerTextView myShimmerTextView = (ShimmerTextView)findViewById(R.id.txtuser);
        Shimmer shimmer = new Shimmer();
        shimmer.start(myShimmerTextView);
        myShimmerTextView.setText("Hey " + struser +"!" );

        // references in welcome.xml
        logout = (ShimmerButton) findViewById(R.id.logout);
        mentorFound=(TextView) findViewById(R.id.mentorFound);
        submit = (ShimmerButton) findViewById(R.id.bSubmit);
        ename = (EditText) findViewById(R.id.ename);
        eusn = (EditText) findViewById(R.id.eusn);
        ebranch = (EditText) findViewById(R.id.ebranch);
        eaddress = (EditText) findViewById(R.id.eaddress);
        ephone = (EditText) findViewById(R.id.ephone);
        eaggregate = (EditText) findViewById(R.id.eaggregate);
         myImage = (SmartImageView) this.findViewById(R.id.my_image);
        dropdown = (Spinner)findViewById(R.id.mentorSpinner);


        shimmer.start(logout);
        shimmer.start(submit);

        dropdown.setOnItemSelectedListener(this);
        Bitmap icon = BitmapFactory.decodeResource(x.getResources(),
                R.drawable.upload);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        file= new ParseFile(struser+"image.png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();
        new Read().execute();

        myImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              name=  ename.getText().toString();
                usn=  eusn.getText().toString();
                branch=  ebranch.getText().toString();
                address=  eaddress.getText().toString();
                phone=  ephone.getText().toString();
                aggregate=  eaggregate.getText().toString();


                ParseQuery<ParseObject> query = ParseQuery.getQuery("mentorDB");
                final ParseObject gameScore = new ParseObject("mentorDB");
                query.whereEqualTo("username", currentUser);

                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if (scoreList.size()>0) {
                            Toast.makeText(x,
                                    "Updating..",
                                    Toast.LENGTH_SHORT).show();
                            try {
                                scoreList.get(0).delete();
                                gameScore.put("username",currentUser);
                                gameScore.put("Name",name);
                                gameScore.put("dp", file);
                                gameScore.put("usn", usn);
                                gameScore.put("branch", branch);
                                gameScore.put("address", address);
                                gameScore.put("phone", phone);
                                gameScore.put("aggregate", aggregate);
                                gameScore.put("mentor", mentor);

                                gameScore.saveInBackground();
                                finish();
                                startActivity(getIntent());

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                        } else {
                            Toast.makeText(x,
                                    "New Entry Success",
                                    Toast.LENGTH_SHORT).show();
                          //  ParseObject gameScore = new ParseObject("mentorDB");
                            gameScore.put("username",currentUser);
                            gameScore.put("Name",name);
                            gameScore.put("dp", file);
                            gameScore.put("usn", usn);
                            gameScore.put("branch", branch);
                            gameScore.put("address", address);
                            gameScore.put("phone", phone);
                            gameScore.put("aggregate", aggregate);
                            gameScore.put("mentor", mentor);

                            gameScore.saveInBackground();
                            finish();
                            startActivity(getIntent());

                        }
                    }
                });


            }
        });

        // Logout Button Click Listener
        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                // Logout current user
                ParseUser.logOut();
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //Case of Camera Button 1
            if (requestCode == 1) {


                Bitmap photo = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                 file = new ParseFile(struser+"camera.png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                selectedPath1 = (getRealPathFromURI(tempUri));
                myImage.setImageURI(Uri.parse(selectedPath1));


            }

            //Case of Gallery Button 1

          else  if (requestCode == 2) {
                Bitmap bitmap = null;
                Uri selectedImageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();

                // Create the ParseFile
                 file = new ParseFile(struser+"image.png", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();


                selectedPath1 = getPath(selectedImageUri);
                myImage.setImageURI(Uri.parse(selectedPath1));


            }






        }



    }


    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(Swelcome.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery"))

                {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }

        });

        builder.show();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                mentor = a.get(i);



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    class Read extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... strings) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("mentorDB");
        final ParseObject gameScore = new ParseObject("mentorDB");
        query.whereEqualTo("username", currentUser);

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (scoreList.size()>0) {

                    for (ParseObject value : scoreList) {
                        ename.setText(value.getString("Name"));
                        eusn.setText(value.getString("usn"));
                        ebranch.setText(value.getString("branch"));
                        eaddress.setText(value.getString("address"));
                        ephone.setText(value.getString("phone"));
                        eaggregate.setText(value.getString("aggregate"));
                        mentor = value.getString("mentor");

                        mentorFound.setText("My Mentor : " + value.getString("mentor"));
                       // ParseFile downloaded= value.getParseFile("dp");
                        ParseFile bum = (ParseFile) value.get("dp");
                        byte[] file1 = new byte[0];
                        try {
                            file1 = bum.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        Bitmap image = BitmapFactory.decodeByteArray(file1,0,file1.length);
                        Uri tempUri = getImageUri(getApplicationContext(), image);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        selectedPath1 = (getRealPathFromURI(tempUri));
                        myImage.setImageURI(Uri.parse(selectedPath1));
                         file = new ParseFile(struser+"camera.png", file1);
                        // Upload the image into Parse Cloud
                        file.saveInBackground();




                        Toast.makeText(x,
                                "Loading",
                                Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(x,
                            "New Entry",
                            Toast.LENGTH_SHORT).show();
                    //  ParseObject gameScore = new ParseObject("mentorDB");


                }
            }
        });

        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereEqualTo("whoami", "teacher");
        query1.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for(ParseUser values:objects) {
                        a.add(values.getUsername());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(x, android.R.layout.simple_spinner_dropdown_item, a);
                        dropdown.setAdapter(adapter);
                    }
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(x, android.R.layout.simple_spinner_dropdown_item, a);
                    dropdown.setAdapter(adapter);
                    // Something went wrong.
                }
            }
        });



        return null;
    }
}}
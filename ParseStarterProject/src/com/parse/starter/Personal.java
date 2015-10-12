package com.parse.starter;

/**
 * Created by kai on 1/10/15.
 */
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Personal extends Activity {
    TextView tname,tusn,tbranch,tadd,tph,tagg,tm;
    String part1=null;
    String part2=null,colon1,colon2;SmartImageView hisImage;
    String selectedPath1 ;


    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal);
        tname=(TextView)findViewById(R.id.tname);
        tusn = (TextView)findViewById(R.id.tusn);
        tbranch = (TextView)findViewById(R.id.tbranch);
        tadd = (TextView)findViewById(R.id.tadd);
        tph = (TextView)findViewById(R.id.tph);
        tagg = (TextView)findViewById(R.id.tagg);
        tm = (TextView)findViewById(R.id.tm);
        hisImage = (SmartImageView) this.findViewById(R.id.teacher_my_image);

        Bundle bundle = getIntent().getExtras();


        name = bundle.getString("TAKE_NAME");
        String[] parts = name.split("\n");
         part1 = parts[0];
         part2 = parts[1];


        String[] colonSplit = part2.split(":");
        colon2=colonSplit[1];
        new Read().execute();
    }


    class Read extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("mentorDB");
            //  final ParseObject gameScore = new ParseObject("mentorDB");
            query.whereEqualTo("usn", colon2);


            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (scoreList.size()>0) {

                        for (ParseObject value : scoreList) {
                            tname.setText(Html.fromHtml("<font color=aqua>" + "Name:" + "</font>"
                                    +  value.getString("Name")));

                            tusn.setText(Html.fromHtml("<font color=aqua>" + "USN:" + "</font>"
                                    +  value.getString("usn")));
                            tbranch.setText(Html.fromHtml("<font color=aqua>" + "Branch:" + "</font>"
                                    +  value.getString("branch")));
                            tadd.setText(Html.fromHtml("<font color=aqua>" + "Address:" + "</font>"
                                    +  value.getString("address")));
                            tph.setText(Html.fromHtml("<font color=aqua>" + "Phone:" + "</font>"
                                    +  value.getString("phone")));
                            tagg.setText(Html.fromHtml("<font color=aqua>" + "Aggregate:" + "</font>"
                                    +  value.getString("aggregate")));
                            tm.setText(Html.fromHtml("<font color=aqua>" + "Mentor:" + "</font>"
                                    +  value.getString("mentor")));
                            ParseFile bum = (ParseFile) value.get("dp");
                            byte[] file1 = new byte[0];
                            try {
                                file1 = bum.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            Bitmap image = BitmapFactory.decodeByteArray(file1, 0, file1.length);
                            Uri tempUri = getImageUri(getApplicationContext(), image);

                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            selectedPath1 = (getRealPathFromURI(tempUri));
                            hisImage.setImageURI(Uri.parse(selectedPath1));

                        }



                    } else {

                        Toast.makeText(getApplicationContext(),
                                "ROFL NOT WORKING ",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
            return null;


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
    }
}


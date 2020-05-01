package com.nyit.attendanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class LessonInfoActivity extends Activity {

    private String name, section, date, time;
    private AttendanceListAdapter adapter;
    private int id;
    private Dialog deleteDialog;
    private AttendanceDbHelper db;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private PendingIntent pendingIntent;
    private TagReaderWriter tagReaderWriter;
    private NfcAdapter nfcadapter;
    private Switch safeSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_info);
        db = new AttendanceDbHelper(this);
        configureSafeSignIn();
        configureBackButton();
        configureLessonHeader();
        configureAttendanceList();
        configureDeleteDialog();
        configureDeleteButton();
        configureExportButton();
        configureForegroundNFCDispatch();

    }


    @Override
    public void onPause() {
        super.onPause();
        if(nfcadapter != null) {
            nfcadapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.populateAttendanceList();
        if(nfcadapter != null) {
            nfcadapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {

        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Student s = tagReaderWriter.readTag(tagFromIntent);
        if(s != null && safeSignIn.isChecked()){
            tapDialog(s);
        }
        else if(s != null && !safeSignIn.isChecked()){
            if(adapter.setPresent(s.getId())){
                showConfirmToast(s);
            }
            else{
                showErrorToast(s);
            }

        }
        else{
            Toast toast = Toast.makeText(this,"Invalid Tag: Tag may be empty or improperly formatted.",Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private void configureSafeSignIn(){
        safeSignIn = findViewById(R.id.safeSwitch);
        safeSignIn.setChecked(true);
    }

    private void tapDialog(final Student s){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Student").setMessage(String.format("Sign Student %s: %s into class?",s.getName(),s.getId()));
        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(adapter.setPresent(s.getId())){
                    dialog.dismiss();
                }
                else{
                    showErrorToast(s);
                }

            }
        });
        builder1.setNegativeButton("cancel",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder1.create().show();
    }

    public void showErrorToast(Student s){
        Toast toast = Toast.makeText(this,String.format("Student %s is not in this class",s.getName()),Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showConfirmToast(Student s){
        Toast toast = Toast.makeText(this,String.format("Student %s was signed in",s.getName()),Toast.LENGTH_SHORT);
        toast.show();
    }


    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.sheetDoneButton);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureLessonHeader(){

        name = getIntent().getStringExtra("class_name");
        section = getIntent().getStringExtra("class_section");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        id = getIntent().getIntExtra("id",0);


        TextView lessonDate = findViewById(R.id.SheetInfoDate);
        TextView className = findViewById(R.id.SheetInfoClassName);
        TextView classSection = findViewById(R.id.SheetInfoSection);

        lessonDate.setText(date + " " + time);
        className.setText(name);
        classSection.setText(section);

    }

    private void configureAttendanceList(){
        ListView lv = findViewById(R.id.SheetInfoStudentList);
        adapter = new AttendanceListAdapter(this,id);
        lv.setAdapter(adapter);
    }

    private void configureDeleteDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Delete").setMessage("Are you sure you want to delete this attendance sheet?");
        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteLesson(id);
                finish();

            }
        });
        builder1.setNegativeButton("cancel",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        deleteDialog = builder1.create();
    }

    private void configureDeleteButton(){
        AppCompatImageButton acib = findViewById(R.id.sheetDeleteButton);
        acib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });
    }

    private void configureForegroundNFCDispatch(){

        nfcadapter = NfcAdapter.getDefaultAdapter(this);
        tagReaderWriter = new TagReaderWriter();

        pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches. */
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {ndef, };

        techListsArray = new String[][] { new String[] { Ndef.class.getName() } };
    }

    private void configureExportButton(){
        AppCompatButton acb = findViewById(R.id.exportButton);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportAttendanceSheet();
            }
        });
    }

    private void exportAttendanceSheet(){
        String fileName = String.format("%s_%s.csv",name,section);
        StringBuilder sb = new StringBuilder();
        ArrayList<Attendance> aList = db.retrieveAllAttendance(id);
        sb.append("Student,SID,Status");
        for(Attendance a: aList){
            sb.append(String.format("\n%s,%s,%s",a.getStudent().getName(),a.getStudent().getId(),a.getPaxt()));
        }

        try{
            //saving file
            FileOutputStream out = openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(sb.toString().getBytes());
            out.close();

            //exporting file
            Context context = getApplicationContext();
            File file = new File(getFilesDir(),fileName);
            Uri path = FileProvider.getUriForFile(context,"com.nyit.attendanceapp.fileprovider",file);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT,"Attendance Sheet");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM,path);
            startActivity(fileIntent);
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

}

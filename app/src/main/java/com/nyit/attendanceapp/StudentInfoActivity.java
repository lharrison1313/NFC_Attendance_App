package com.nyit.attendanceapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

public class StudentInfoActivity extends Activity {

    String name;
    String id;
    AlertDialog deleteDialog;
    private NfcAdapter adapter;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private PendingIntent pendingIntent;
    private TagReaderWriter tagReaderWriter;
    private AlertDialog nfcDialog;
    private AlertDialog writeCompleteDialog;
    private boolean writeToNFC;
    private AttendanceDbHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info);
        db = new AttendanceDbHelper(this);
        configureDeleteDialog();
        configureDeleteStudentButton();
        configureBackButton();
        configureStudentHeader();
        configureStatusMetrics();
        configureNFCButton();
        configureNFCDialog();
        configureForegroundNFCDispatch();
        configureWriteCompleteDialog();
    }

    public void onPause() {
        super.onPause();
        if(adapter != null) {
            adapter.disableForegroundDispatch(this);
        }
    }

    public void onResume() {
        super.onResume();
        if(adapter != null) {
            adapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
        }
    }

    public void onNewIntent(Intent intent) {
        if(writeToNFC) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            tagReaderWriter.writeTag(tagFromIntent, id, name);
            nfcDialog.dismiss();
            writeToNFC = false;
            writeCompleteDialog.show();
        }
        else{
            Toast toast = Toast.makeText(this,"Click write to NFC first.",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.infoDone);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureStudentHeader(){
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        TextView nameText = findViewById(R.id.infoStudentName);
        TextView idText = findViewById(R.id.infoStudentID);

        nameText.setText(name);
        idText.setText(id);
    }

    private void configureDeleteDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Confirm Delete").setMessage("Are you sure you want to delete this student? This will delete the student from all classes and attendance sheets.");
        builder1.setPositiveButton("confirm",new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteStudent();
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

    private void configureDeleteStudentButton(){
        AppCompatImageButton acb = findViewById(R.id.infoDeleteStudent);

        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.show();
            }
        });
    }

    private void configureNFCButton(){
        AppCompatButton acb = findViewById(R.id.infoNFC);
        acb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    writeToNFC = true;
                    nfcDialog.show();
            }
        });
    }

    private void configureForegroundNFCDispatch(){

        adapter = NfcAdapter.getDefaultAdapter(this);
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

    private void configureNFCDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tap NFC Now").setTitle("Writing Student To NFC");
        builder.setNeutralButton("quit", new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                writeToNFC = false;
                dialog.dismiss();
            }
        });
        nfcDialog = builder.create();
    }

    private void configureWriteCompleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Student was written to the NFC device").setTitle("NFC Written");
        builder.setNeutralButton("Done", new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        writeCompleteDialog = builder.create();
    }

    private void deleteStudent(){
        AttendanceDbHelper db = new AttendanceDbHelper(this);
        db.deleteStudent(id);
    }

    private void configureStatusMetrics(){
        int[] status = db.getAttendanceStatusCounts(new Student(name,id));
        TextView presents = findViewById(R.id.infoPresents);
        TextView absences = findViewById(R.id.infoAbsences);
        TextView excused = findViewById(R.id.infoExcused);
        TextView tardies = findViewById(R.id.infoTardies);

        presents.setText("Presents: " + status[0]);
        absences.setText("Absences: " + status[1]);
        excused.setText("Excused: " + status[2]);
        tardies.setText("Tardies: " + status[3]);


    }
}

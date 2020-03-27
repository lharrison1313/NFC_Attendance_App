package com.nyit.attendanceapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;

public class AddStudentsActivity extends Activity {

    private AttendanceDbHelper db;
    private NfcAdapter adapter;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;
    private PendingIntent pendingIntent;
    private TagReaderWriter tagReaderWriter;
    private TextInputEditText nameField;
    TextInputEditText idField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_dialog);

        //creating database helper
        db = new AttendanceDbHelper(this);

        //linking textfield objects
        nameField = findViewById(R.id.studentNameInput);
        idField = findViewById(R.id.studentIdInput);

        //configuring buttons and nfc
        configureForegroundNFCDispatch();
        configureBackButton();
        configureDoneButton();
    }

    public void onPause() {
        super.onPause();
        adapter.disableForegroundDispatch(this);
    }

    public void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    public void onNewIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        tagReaderWriter.writeTag(tagFromIntent,idField.getText().toString());
    }

    private void configureBackButton(){
        AppCompatButton acb = findViewById(R.id.cancelStudents);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configureDoneButton(){
        AppCompatButton acb = findViewById(R.id.doneStudents);
        acb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStudent(new Student(nameField.getText().toString(),idField.getText().toString()));
                finish();
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

    private void createStudent(Student s){
        db.addStudent(s);
    }




}

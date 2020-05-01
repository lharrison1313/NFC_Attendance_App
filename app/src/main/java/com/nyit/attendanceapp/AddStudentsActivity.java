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
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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
    private TextInputEditText idField;
    private AlertDialog nfcDialog;
    private AlertDialog writeCompleteDialog;
    private boolean writeToNFC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_student_dialog);

        db = new AttendanceDbHelper(this);
        writeToNFC = false;

        //linking textfield objects
        nameField = findViewById(R.id.studentNameInput);
        idField = findViewById(R.id.studentIdInput);

        //configuring buttons and nfc
        configureForegroundNFCDispatch();
        configureNFCDialog();
        configureWriteCompleteDialog();
        configureBackButton();
        configureDoneButton();
        configureNFCButton();
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
            tagReaderWriter.writeTag(tagFromIntent, idField.getText().toString(), nameField.getText().toString());
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
                String name = nameField.getText().toString();
                String id = idField.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(id)){
                    nameField.setError("Name field cannot be empty");
                    idField.setError("ID field cannot be empty");
                }
                else {
                    createStudent(new Student(name, idField.getText().toString()));
                    finish();
                }
            }
        });
    }

    private void configureNFCButton(){
        AppCompatButton acb = findViewById(R.id.NFCButton);
        acb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String id = idField.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(id)){
                    nameField.setError("Name field cannot be empty");
                    idField.setError("ID field cannot be empty");
                }
                else {
                    writeToNFC = true;
                    nfcDialog.show();
                }

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

    private void createStudent(Student s){
        db.addStudent(s);
    }




}

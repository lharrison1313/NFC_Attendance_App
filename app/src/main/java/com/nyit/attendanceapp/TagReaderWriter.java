package com.nyit.attendanceapp;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.util.Log;

import java.io.IOException;

public class TagReaderWriter {

    private static final String TAG = TagReaderWriter.class.getSimpleName();

    public void writeTag(Tag tag, String name, String studentID ){
        Ndef ndef = Ndef.get(tag);
        NdefRecord idRecord = NdefRecord.createTextRecord(null,studentID);
        NdefRecord nameRecord = NdefRecord.createTextRecord(null,name);
        NdefRecord [] payload = {nameRecord,idRecord};

        try {
            //writing student id to tag
            ndef.connect();
            ndef.writeNdefMessage(new NdefMessage(payload));

        }catch (Exception e){
            Log.e(TAG, "IOException while writing ndef", e);
        }finally {
            try {
                ndef.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing ndef", e);
            }
        }

    }

    public Student readTag(Tag tag){
        Ndef ndef = Ndef.get(tag);

        try{
            ndef.connect();
            NdefMessage message = ndef.getNdefMessage();
            NdefRecord[] records = message.getRecords();
            String id = new String(records[0].getPayload());
            String name = new String(records[1].getPayload());
            return new Student(name.substring(3),id.substring(3));


        }catch (Exception e) {
            Log.e(TAG, "IOException while reading ndef message...", e);
        } finally {
            if (ndef != null) {
                try {
                    ndef.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }

        return null;
    }

}

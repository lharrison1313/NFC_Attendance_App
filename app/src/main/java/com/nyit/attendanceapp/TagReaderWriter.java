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
            Log.e(TAG, "IOException while writing MifareUltralight...", e);
        }finally {
            try {
                ndef.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }

    }

}
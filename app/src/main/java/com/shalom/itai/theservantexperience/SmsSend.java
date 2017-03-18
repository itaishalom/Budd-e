package com.shalom.itai.theservantexperience;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

public class SmsSend extends AppCompatActivity {
    String namecsv="";
    String phonecsv="";

    String namearray[];
    String phonearray[];
    String[] sArrFull;
    int iSelectedNum;
    public static ArrayList<String> allAddedPhoneNumbers = new ArrayList<>();
    ListView lv1;
    Switch myOnOffSwitch;
    static boolean bIsOn;
    EditText inputSearch;
    ArrayAdapter<String> adapter;
    String selectContact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send);
          lv1 = (ListView) findViewById(R.id.listView1);
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {


            //Read Contact Name
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            //Read Phone Number
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(name!=null)
            {
                namecsv += name + ",";
                String text = phoneNumber;
                String digits = text.replaceAll("[^0-9.]", "");

                phonecsv += digits + ",";
            }


        }
        phones.close();


        //==============================================
        // Convert csvstrimg into array
        //==============================================
        namearray = namecsv.split(",");
        phonearray = phonecsv.split(",");
        ArrayList<String> tempString = new ArrayList<>();
        // String[] sArrFull = new String[phonearray.length];
        String newString ="";
        for (int i=0;i<phonearray.length;i++)
        {
            newString = namearray[i]+":"+phonearray[i];
            if(i>1)
            {
                if(!tempString.contains(newString))
                {
                    tempString.add( newString);
                    //     sArrFull[i] = newString;
                }
            }
        }
        java.util.Collections.sort(tempString);

        sArrFull = new String[tempString.size()];
        for(int i=0;i<sArrFull.length;i++)
        {
            sArrFull[i] = tempString.get(i);
        }

        //Create Array Adapter and Pass ArrayOfValues to it.
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2,android.R.id.text1,sArrFull);
        java.util.Collections.sort(tempString);
        //BindAdpater with our Actual ListView
        lv1.setAdapter(adapter);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                SmsSend.this.adapter.getFilter().filter(cs);
                //   lv1.setAdapter(MainActivity.this.adapter.getFilter().filter(cs));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        //Do something on click on ListView Click on Items
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //    ListView l = (ListView) arg0;
                //   l.
                Object o = lv1.getItemAtPosition(arg2);
                selectContact = o.toString();
                Toast.makeText(getBaseContext(), o.toString(), Toast.LENGTH_SHORT).show();
                //============================================
                // Display number of contact on click.
                //===========================================

                //   iSelectedNum = arg2;
                String[] vals = selectContact.split(":");
                String name = vals[0];
                String num = vals[1];
                // Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SmsSend.this);
                builder1.setMessage("Are you sure you wish to add "+name+" to your specials?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] vals = selectContact.split(":");
                                String name = vals[0];
                                String num = vals[1];
                                sendSMS(num,"i love you",name);
                             /*
                                if (allAddedPhoneNumbers.contains(num)) {
                                    Toast.makeText(getBaseContext(), "The number: " + num + " already exists", Toast.LENGTH_SHORT).show();
                                } else {
                                    allAddedPhoneNumbers.add(num);
                                    Toast.makeText(getBaseContext(), "added: " + num, Toast.LENGTH_SHORT).show();
                              //      SettingHand.saveNumbersToFile();
                                }
                               */
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });


    }
    public void sendSMS(String phoneNo, String msg, String name) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "I decided to send "+msg+" to "+name+", I hope it's ok",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}

package com.shalom.itai.theservantexperience;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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
                builder1.setMessage("You really love "+name+" ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] vals = selectContact.split(":");
                                String name = vals[0];
                                String num = vals[1];
                                sendSMS(num,"Why does he love you more than he loves me?",name);
                            //    sendSMS(num,"i love you",name);
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
            Toast.makeText(getApplicationContext(), "I send \""+msg+"\" to "+name+"!!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    public void sendWhatsAppMsg(String phoneNo, String msg, String name){
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);

            sendIntent.putExtra(Intent.EXTRA_TEXT, "hi");
            sendIntent.setType("text/plain");
            sendIntent.putExtra("smsto:", PhoneNumberUtils.stripSeparators("972543223702")+"@s.whatsapp.net");
            // Do not forget to add this to open whatsApp App specifically
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);

            /*
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setAction(Intent.ACTION_SENDTO);
            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("972543223702")+"@s.whatsapp.net");//phone number without "+" prefix

            startActivity(sendIntent);
            */
/*
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(Uri.parse("smsto:"+phoneNo));
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(sendIntent);
  */
        Toast.makeText(getApplicationContext(), "I decided to send "+msg+" to "+name+", I hope it's ok",
                Toast.LENGTH_LONG).show();
    } else {
        Toast.makeText(this, "WhatsApp not Installed",
                Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("market://details?id=com.whatsapp");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(goToMarket);

    }


    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}

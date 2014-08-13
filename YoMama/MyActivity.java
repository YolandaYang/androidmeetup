package com.shafernotes.yomama;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyActivity extends Activity {
    ArrayList YOs;
    final int PICK_CONTACT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        YOs = new ArrayList();

        YOs.add("+ New Yo");

        ListView lv;
        lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, YOs);

        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } else {
                    String selection = (String)parent.getItemAtPosition(position);
                    String phoneNumber = selection.replaceAll("\\D","") ;
                    sendSMS(phoneNumber,"YO MAMA!");
                }
            }
        });

    }


    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor c=null;
        String name = "";
        List<String> allNumbers = new ArrayList<String>();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT:
                    String phoneNumber = "";
                    try {
                        Uri contactData  = data.getData();
                        c = getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {
                            String contact_id = c.getString(c.getColumnIndex( ContactsContract.Contacts._ID ));
                            name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            // Get Phone Numbers
                            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contact_id, null, null);
                            while (phoneCursor.moveToNext()) {
                                phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                allNumbers.add(phoneNumber);
                            }
                            phoneCursor.close();
                        } else {
                            //no results actions
                        }
                    } catch (Exception e) {
                        String err = e.getMessage();
                        //error actions
                    } finally {
                        if (c != null) {
                            c.close();
                        }

                        final EditText phoneInput = new EditText(this);
                        phoneInput.setText(name);
                        final CharSequence[] items = allNumbers.toArray(new String[allNumbers.size()]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyActivity.this);
                        builder.setTitle("Choose a number");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                String selectedNumber = items[item].toString();
                                selectedNumber = selectedNumber.replace("-", "");
                                YOs.add(phoneInput.getText() + ":" + selectedNumber);
                                ListView lv;
                                lv = (ListView) findViewById(R.id.listView);
                                lv.invalidateViews();
                            }
                        });
                        AlertDialog alert = builder.create();
                        if(allNumbers.size() > 1) {
                            alert.show();
                        } else {
                            String selectedNumber = phoneNumber.toString();
                            selectedNumber = selectedNumber.replace("-", "");
                            phoneInput.setText(selectedNumber);
                            YOs.add(name + ":" + phoneInput.getText());
                            ListView lv;
                            lv = (ListView) findViewById(R.id.listView);
                            lv.invalidateViews();
                        }

                    }
                    break;
            }
        } else {
            //activity result error actions
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

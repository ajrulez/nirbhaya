package com.som_itsolutions.nirbhaya;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NirbhayaSettings extends AppCompatActivity {
	Button contactButton;
	Button setYourPreferencesButton;
	EditText phoneNumber;
	EditText SMSMessage;
	private String phoneNumberStr;
	//private String smsMessageStr;
	int PICK_CONTACT = 9;
	//public static final String settingPreferences= "settings";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences(NirbhayaActivity.settingPreferences, Context.MODE_PRIVATE);

        setContentView(R.layout.activity_main_settings);

        contactButton = (Button)findViewById(R.id.buttonContacts);
        setYourPreferencesButton = (Button)findViewById(R.id.buttonSet);
        phoneNumber = (EditText)findViewById(R.id.editTextPhoneNumber);
        SMSMessage = (EditText)findViewById(R.id.editTextMessage);

        contactButton.setOnClickListener(new View.OnClickListener() {
			/*@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});*/
        	public void onClick(View v) {
	    		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
	            startActivityForResult(intent, 1);
	        }
        });
        setYourPreferencesButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SharedPreferences sharedPref = getSharedPreferences(NirbhayaActivity.settingPreferences, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				String phoneNumberStrTemp = phoneNumber.getText().toString();
				String smsMessageStrTemp = SMSMessage.getText().toString();
				if((!(phoneNumberStrTemp.equals(""))) && (!(smsMessageStrTemp.equals("")))){
						editor.clear();
						 editor.putString("phonenumber", phoneNumber.getText().toString());
						 editor.putString("message", SMSMessage.getText().toString());
						 
						 editor.commit();
						 /*LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					        if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
					            buildAlertMessageNoGps();
					        }
					    */
						 finish();
					     
					     Intent intent = new Intent(Intent.ACTION_MAIN);
					     intent.addCategory(Intent.CATEGORY_HOME);
					     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					     startActivity(intent);
					}
				 
			}
		});
    }
	
/*	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
	  super.onActivityResult(reqCode, resultCode, data);

	  switch (reqCode) {
	    case 9:
	      if (resultCode == Activity.RESULT_OK) {
	        Uri contactData = data.getData();
	        Cursor cursor =  managedQuery(contactData, null, null, null, null);
	        while (cursor.moveToNext()) 
	        {           
	            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
	            //name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 

	            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

	            if ( hasPhone.equalsIgnoreCase("1"))
	                hasPhone = "true";
	            else
	                hasPhone = "false" ;

	            if (Boolean.parseBoolean(hasPhone)) 
	            {
	             Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
	             while (phones.moveToNext()) 
	             {
	               phoneNumberStr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	               phoneNumber.setText(phoneNumberStr);
	             }
	             phones.close();
	            }
	        }
	      }
	      break;
	  }
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (data != null) {
	        Uri uri = data.getData();

	        if (uri != null) {
	            Cursor c = null;
	            try {
	                c = getContentResolver().query(uri, new String[]{ 
	                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
	                            ContactsContract.CommonDataKinds.Phone.TYPE },
	                        null, null, null);

	                if (c != null && c.moveToFirst()) {
	                	phoneNumberStr = c.getString(0);
	                    int type = c.getInt(1);
	                    phoneNumber.setText(phoneNumberStr);
	                   
	                }
	            } finally {
	                if (c != null) {
	                    c.close();
	                }
	            }
	        }
	    }
    }
	
	
	/*
	 private void buildAlertMessageNoGps() {
	        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	               .setCancelable(false)
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	                   }
	               })
	               .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                   public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                        dialog.cancel();
	                   }
	               });
	        final AlertDialog alert = builder.create();
	        alert.show();
	    }*/
	   
}


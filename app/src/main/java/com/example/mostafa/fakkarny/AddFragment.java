package com.example.mostafa.fakkarny;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mostafa.fakkarny.data.FakkarnyContract;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mostafa on 11/19/2016.
 */

public class AddFragment extends Fragment implements View.OnClickListener/*used to listen on button click*/ {

    private static final int CAMERA_REQUEST = 1008 ;
    private static final String SPINNERS_SHARED_PREFS_NAME = "MY_SPINNERS_SHARED_PREF"; //Shared preferences for spinner view
    private static final String RADIO_SHARED_PREFs_NAME ="MY_RADIO_SHARED_PREF" ;  //Shared preferences for radio group
    //this is set to determine how fragment closes based on the activity that initialized it.
    //if twopane then its main activity, if onepane then its add activity
    private static final String ACTIVITY_TYPE = "NameActivity";
    int isSpinnerFirstItem = 0;
    String spinnerFirstItem = "Select a type" ;
    String categoryFirstItem = "Select a category" ;

    // Save a reference to the fragment manager. This is initialised in onCreate().used to initialize LocationServicesFragment
    private FragmentManager mFM;
    // Code to identify the fragment that is calling onActivityResult().
    static final int TASK_FRAGMENT = 0;
    // Tag so we can find the task fragment again, in another instance of this fragment after destroy().
    static final String TASK_FRAGMENT_TAG = "locationfinder";
    //boolean value set to false when location data has been fetched!
    // used to indicate that LocationServiceFragment has finished

    LocationServicesFragment taskFragment ;

    EditText nameOfItem;
    EditText details;
    ImageView imageCaptured;
    Button captureButton;
    Button addItemButton;
    Spinner sItems;
    Spinner radioItems;
    RadioGroup rg1;

    List<String> spinnerArray =  new ArrayList<String>();
    List<String> radioArray =  new ArrayList<String>();
    ContentValues cv;

    public AddFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        spinnerArray = getArray(SPINNERS_SHARED_PREFS_NAME);
        radioArray = getArray(RADIO_SHARED_PREFs_NAME);

        if(isSpinnerFirstItem == 0) { // to add certain value to spinner when app launches for the first time
            spinnerArray.add(spinnerFirstItem);
            radioArray.add(categoryFirstItem);
            isSpinnerFirstItem ++ ;
        }

        mFM = getFragmentManager();  //to create a LocationServicesFragment
        // Check to see if we have retained the worker fragment.
        taskFragment = (LocationServicesFragment) mFM.findFragmentByTag(TASK_FRAGMENT_TAG);
        if (taskFragment != null)
        {
            // Update the target fragment so it goes to this fragment instead of the old one.
            taskFragment.setTargetFragment(this, TASK_FRAGMENT);
        }else{

            taskFragment = new LocationServicesFragment();
            // And tell it to call onActivityResult() on this fragment.
            taskFragment.setTargetFragment(this, TASK_FRAGMENT);
            // Show the fragment.
            // I'm not sure which of the following two lines is best to use but this one works well.
            mFM.beginTransaction().add(taskFragment, TASK_FRAGMENT_TAG).commit();
        }

        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();

        if (itemid == R.id.action_add_category) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add category");
            // Set up the input
            final EditText input = new EditText(getActivity());
            // Specify the type of input expected;
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mText = input.getText().toString();
                    radioArray.add(mText);}
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();}
            });

            builder.show();
            return true;
        }
        if (itemid == R.id.action_add_type) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add type of item");
            // Set up the input
            final EditText input = new EditText(getActivity());
            // Specify the type of input expected;
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String mText = input.getText().toString();
                    spinnerArray.add(mText);}
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();}
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        cv = new ContentValues();

        View rootView = inflater.inflate(R.layout.fragment_add , container , false);
        nameOfItem = (EditText) rootView.findViewById(R.id.items_name);                   //name
        details =(EditText) rootView.findViewById(R.id.details_field);

        rg1 = (RadioGroup) rootView.findViewById(R.id.location_radio);              //category

        ArrayAdapter<String> radio_adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, radioArray);
        radio_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        radioItems = (Spinner) rootView.findViewById(R.id.planets_radio);
        radioItems.setAdapter(radio_adapter);
        radioItems.setSelection(0);

        imageCaptured = (ImageView) rootView.findViewById(R.id.camera_image);             //Photo
        captureButton = (Button) rootView.findViewById(R.id.capture_image);
        captureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems = (Spinner) rootView.findViewById(R.id.planets_spinner);
        sItems.setAdapter(adapter);
        sItems.setSelection(0);

        /*sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        addItemButton = (Button) rootView.findViewById(R.id.add_Button);
        addItemButton.setOnClickListener(this);
        return rootView ;
    }

    @Override
    public void onClick(View v){
        if(nameOfItem.getText().toString().equals("")){  //check if user added a name
            nameOfItem.setError("type a name plz!");
            return;
        }else if(radioItems == null || radioItems.getSelectedItem() == categoryFirstItem){     //check if user added a category
            Toast.makeText(getContext(),"choose a category plz!",Toast.LENGTH_SHORT).show();
            return;
        }else if(hasImage(imageCaptured)==false){        //check if user added an image, call to hasImage fn
            Toast.makeText(getContext(),"add image plz!",Toast.LENGTH_SHORT).show();
            return;
        }else if(sItems == null || sItems.getSelectedItem() == spinnerFirstItem){   //check if user selected a type
            Toast.makeText(getContext(),"choose type plz!",Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar c = Calendar.getInstance();    //getting the date automatically
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());


        int id = rg1.getCheckedRadioButtonId();   //get value of radiobutton
        View radioButton = rg1.findViewById(id);
        int radioId = rg1.indexOfChild(radioButton);
        RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
        String selection = btn.getText().toString();

        // We will create a new TaskFragment.
       switch (selection){
           case "Yes":
            cv.put(FakkarnyContract.itemsEntry.COLUMN_LATITUDE, taskFragment.latitude);
            cv.put(FakkarnyContract.itemsEntry.COLUMN_LONGITUDE, taskFragment.longitude);
               break;
           default:
        }

        cv.put(FakkarnyContract.itemsEntry.COLUMN_NAME, nameOfItem.getText().toString());
        cv.put(FakkarnyContract.itemsEntry.COLUMN_DATE_STORED, strDate);
        cv.put(FakkarnyContract.itemsEntry.COLUMN_ITEM_TYPE , sItems.getSelectedItem().toString());
        cv.put(FakkarnyContract.itemsEntry.COLUMN_PLACE_CATEGORY , radioItems.getSelectedItem().toString() );

        if(!details.getText().toString().equals(""))  //add details to contentvalues if added by user
            cv.put(FakkarnyContract.itemsEntry.COLUMN_PLACE_DETAILS , details.getText().toString());
        else
            Toast.makeText(getContext(), "no details added",Toast.LENGTH_LONG).show();

        getContext().getContentResolver().insert(FakkarnyContract.itemsEntry.CONTENT_URI, cv);
        Toast.makeText(getContext(), "added to Database", Toast.LENGTH_LONG);

        String strtext=getArguments().getString(ACTIVITY_TYPE);
        switch (strtext){
            case "onepane":
                getActivity().finish();
                break;
            case "twopane":
                Toast.makeText(getActivity(),"Item Added",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onStop() {
        saveArray(SPINNERS_SHARED_PREFS_NAME , spinnerArray);
        saveArray(RADIO_SHARED_PREFs_NAME, radioArray);
        super.onStop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TASK_FRAGMENT && resultCode == Activity.RESULT_OK)
        {

        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            cv.put(FakkarnyContract.itemsEntry.COLUMN_ITEM_PHOTO, byteArray);
            imageCaptured.setImageBitmap(photo);
        }
    }
    private boolean hasImage(@NonNull ImageView view) {   //to check if imageview has an image or not
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }
        return hasImage;
    }
    //to save data from shared prefs, used for adding gategories and types dynamically
    public boolean saveArray(String filename , List<String> mvalues) {
        SharedPreferences sp = getActivity().getSharedPreferences(filename, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();
        Set<String> set = new HashSet<String>();
        set.addAll(mvalues);
        mEdit1.putStringSet("list", set);
        return mEdit1.commit();
    }
    //to get data from shared prefs, used for adding gategories and types dynamically
    public ArrayList<String> getArray( String filename) {
        SharedPreferences sp = getActivity().getSharedPreferences(filename, Activity.MODE_PRIVATE);

        //NOTE: if shared preference is null, the method return empty Hashset and not null
        Set<String> set = sp.getStringSet("list", new HashSet<String>());

        return new ArrayList<String>(set);
    }
}

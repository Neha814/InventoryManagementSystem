package com.example.lightspeeddemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity11 extends Activity
{
                // Create Object of EditText and TextWatcher
                EditText editTextPassword;
                TextView textViewPasswordStrengthIndiactor;
                @Override
                protected void onCreate(Bundle savedInstanceState)
                {
                            super.onCreate(savedInstanceState);
                            setContentView(R.layout.textwatcher);
                           
                            // Get the Reference of EditText
                            editTextPassword=(EditText)findViewById(R.id.editTextPassword);
                            textViewPasswordStrengthIndiactor=(TextView)findViewById(R.id.textViewPasswordStrength);
                   
                            // Attach TextWatcher to EditText
                            editTextPassword.addTextChangedListener(mTextEditorWatcher);
                   
                }

   
                // EditTextWacther  Implementation
 
                private final TextWatcher  mTextEditorWatcher = new TextWatcher() {
                   
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {
                                // When No Password Entered
                               textViewPasswordStrengthIndiactor.setText("Not Entered");
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                       
                    }

                    public void afterTextChanged(Editable s)
                    {
                    	
                    	String abc = s.toString();
                    	if(abc.endsWith("**"))
                    	{
                    		Toast.makeText(MainActivity11.this, "done", Toast.LENGTH_LONG).show();
                    		
                    	}
                    	Log.d("string", abc);
                    	
                    	
                                 if(s.length()==0)
                                        textViewPasswordStrengthIndiactor.setText("Not Entered");
                                 else if(s.length()<6)
                                        textViewPasswordStrengthIndiactor.setText("EASY");
                                 else if(s.length()<10)
                                        textViewPasswordStrengthIndiactor.setText("MEDIUM");
                                 else if(s.length()<15)
                                        textViewPasswordStrengthIndiactor.setText("STRONG");
                                   else
                                        textViewPasswordStrengthIndiactor.setText("STRONGEST");
                       
                               if(s.length()==20)
                                   textViewPasswordStrengthIndiactor.setText("Password Max Length Reached");
                    }
            };

}

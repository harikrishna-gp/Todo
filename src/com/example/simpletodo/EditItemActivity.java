package com.example.simpletodo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {

	private long  itemPosition =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String itemTxt = getIntent().getStringExtra("ItemText");
		itemPosition = getIntent().getLongExtra("ItemPos", 0);
		setContentView(R.layout.activity_edit_item);
		EditText textFld =  (EditText)findViewById(R.id.itemNewTxt);
		if (textFld != null)
			textFld.setText(itemTxt);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	public void onSave(View view) {
		EditText itemText =  (EditText)findViewById(R.id.itemNewTxt);
		Intent data = new Intent();
		data.putExtra("newItemTxt", itemText.getText().toString());
		data.putExtra("ItemPos", itemPosition);
		setResult(RESULT_OK, data);  
		finish();		
	}
}

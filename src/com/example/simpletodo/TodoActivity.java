package com.example.simpletodo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
//import java.io.IOException;
//import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import java.util.ArrayList;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemClickListener;


public class TodoActivity extends Activity {
	ListView lvItems;
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	File 	dataFile=null;
	@SuppressLint("SdCardPath")
	String  dirName ="/sdcard/";
	String  fileName="todo.txt";
	private final int REQUEST_CODE = 1;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
		lvItems = (ListView) findViewById(R.id.lvItems);		
		items = new ArrayList<String>();
		items = readItems();	
		if (items == null)
			items = new ArrayList<String>();

		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		setupListViewListener();
		setupClickListener();
    }
    
    private void setupClickListener() {
    	lvItems.setOnItemClickListener (new OnItemClickListener(){
    		@Override
    		public void onItemClick (AdapterView<?> aView, View item, int pos, long id){
    			String toastTxt;
    			toastTxt = "Item selected is : " + String.valueOf(pos);
    			Log.e(toastTxt, toastTxt);
    			launchEditView(pos);
    			return;
			}
    	});    	
    }
    
    private void setupListViewListener() {
    	lvItems.setOnItemLongClickListener(new OnItemLongClickListener(){
    		@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item,
					int pos, long id) {
				items.remove(pos);
				itemsAdapter.notifyDataSetChanged();
				saveItems(items,false);
				return true;
			}
    	});
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo, menu);
        return true;
    }
    
	public void addTodoItem(View v) {
		EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
		itemsAdapter.add(etNewItem.getText().toString());
		saveItems(items, false);
		etNewItem.setText("");
}    

	private ArrayList<String> readItems() {
		File dataFile = new File(dirName, fileName);
		items = new ArrayList<String>();
		if (dataFile.exists() != true){
			return null;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(dataFile));
			String line;
			while ((line = reader.readLine()) != null) {
				items.add(line);
			 }
		} catch (Exception e) {
			    e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
						e.printStackTrace();
				}
			}
		}
		return items;
	}
	
	private void saveItems(ArrayList<String> items, Boolean truncate) {
		File dataFile = new File(dirName, fileName);
		try {
			if (truncate == true) {
				FileOutputStream stream = new FileOutputStream(dataFile);
			    FileChannel outChan = stream.getChannel();
				if (truncate == true) {
					outChan.truncate(0);
					outChan.close();
				}
				stream.close();
			}
				
			PrintWriter pw = new PrintWriter(new FileOutputStream(dataFile));
			for (String item : items) {
				pw.write(item + "\n");
		    } 
			pw.close();
		}catch (Exception e) {
		    e.printStackTrace();
		} 	
	}

	public void launchEditView(long  pos) {
		  String itemTxt = items.get((int)pos);
		  Log.e("Got to launch edit","Got to launch edit");
		  Intent intnt = new Intent(TodoActivity.this, EditItemActivity.class);
		  intnt.putExtra("ItemText", itemTxt);
		  intnt.putExtra("ItemPos", pos);
		  startActivityForResult(intnt,REQUEST_CODE); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
	     String item = data.getExtras().getString("newItemTxt");
	     int pos    = (int)data.getExtras().getLong("ItemPos");
	     items.set(pos, item);
	     saveItems(items, true);
	     itemsAdapter.notifyDataSetChanged();
	  }
	} 
}
package com.shafernotes.freemindreader;

import java.util.ArrayList;

import org.w3c.dom.Node;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.shafernotes.freemindreader.CustomListViewAdapter;
import com.shafernotes.freemindreader.MapView;
import com.shafernotes.freemindreader.MyApplication;
import com.shafernotes.freemindreader.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MapView extends ActionBarActivity {
    Node currentRoot = null;
    ArrayList<String> nodeValues = new ArrayList<String>();
    ArrayList<Node> nodeInstances = new ArrayList<Node>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);  // sets layout to resource

        MyApplication appState = ((MyApplication)getApplicationContext());
        nodeValues = new ArrayList<String>();
        nodeInstances = new ArrayList<Node>();

        if (getIntent().getStringExtra("nodeID") != null ) {
            currentRoot = appState.getCurrentRoot();
            NodeList childNodes = null;
            childNodes = currentRoot.getChildNodes();

            for (int j = 0; j < childNodes.getLength();j++){
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    if (childNodes.item(j).getNodeName().equals("node"))  {
                        if (childNodes.item(j).getAttributes().getNamedItem("TEXT") != null) {
                            nodeValues.add(childNodes.item(j).getAttributes().getNamedItem("TEXT").getNodeValue());
                            nodeInstances.add(childNodes.item(j));
                        }
                    }
                }
            }
        } else {
            Document document = null;
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
                try {
                    builder = builderFactory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    //TODO: Handle Error
                }

                Intent intent = getIntent();
                Uri uri = intent.getData();
                String scheme = uri.getScheme();
                InputStream is = null;

                if (scheme.equals("file")) {
                    String filePath = getIntent().getData().getPath();
                    FileInputStream in = new FileInputStream(new File(filePath));
                    is = new BufferedInputStream(in);
                } else if (scheme.equals("content")) {
                    is = new BufferedInputStream(getContentResolver().openInputStream(uri));
                }

                try {
                    document = builder.parse(is);
                } catch (SAXException e) {
                    //TODO: Handle Error
                }
                finally {
                    /*
                    if (in != null) {
                        in.close();
                    }
                    */
                }

                if (document != null) {
                    appState.setDocument(document);
                    NodeList nodeList = null;
                    NodeList childNodes = null;

                    nodeList = document.getElementsByTagName("node");
                    currentRoot = nodeList.item(0);
                    childNodes = currentRoot.getChildNodes();

                    for (int j = 0; j < childNodes.getLength();j++){
                        if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if (childNodes.item(j).getNodeName().equals("node"))  {
                                if (childNodes.item(j).getAttributes().getNamedItem("TEXT") != null) {
                                    nodeValues.add(childNodes.item(j).getAttributes().getNamedItem("TEXT").getNodeValue());
                                    nodeInstances.add(childNodes.item(j));
                                }
                            }
                        }
                    }
                }
            }  catch (IOException e) {
                //TODO: Handle Error
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        ListView lv;
        lv = (ListView) findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,R.layout.list_item, nodeInstances);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NodeList childNodes = ((Node)nodeInstances.get(position)).getChildNodes();
                for (int j = 0; j < childNodes.getLength();j++){
                    if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        if (childNodes.item(j).getNodeName().equals("node"))  {
                            MyApplication appState = ((MyApplication)getApplicationContext());
                            appState.setCurrentRoot((Node)nodeInstances.get(position));
                            Intent intent = new Intent(MapView.this, MapView.class);
                            intent.putExtra("nodeID", "child");
                            startActivity(intent);
                            j = childNodes.getLength();
                        }
                    }
                }

            }
        });
	}

	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId()==R.id.listView) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.longclickmenu, menu);
        }
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Node currentItem = (Node)nodeInstances.get(info.position);
        if (currentItem.getAttributes().getNamedItem("LINK") == null) {
            MenuItem myLocationMenuItem = menu.findItem(R.id.browse);
            myLocationMenuItem.setVisible(false);
            myLocationMenuItem.setEnabled(false);
            myLocationMenuItem = menu.findItem(R.id.copyLink);
            myLocationMenuItem.setVisible(false);
            myLocationMenuItem.setEnabled(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition = info.position;
        Node currentItem;
        switch (item.getItemId()) {
            case R.id.copy:
                currentItem = (Node)nodeInstances.get(listPosition);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text", currentItem.getAttributes().getNamedItem("TEXT").getNodeValue());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Text Copied", Toast.LENGTH_LONG).show();
                return true;
            case R.id.copyLink:
                currentItem = (Node)nodeInstances.get(listPosition);
                clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clip = ClipData.newPlainText("Text", currentItem.getAttributes().getNamedItem("LINK").getNodeValue());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Link Copied", Toast.LENGTH_LONG).show();
                return true;
            case R.id.browse:
                currentItem = (Node)nodeInstances.get(listPosition);
                String url = currentItem.getAttributes().getNamedItem("LINK").getNodeValue();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
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

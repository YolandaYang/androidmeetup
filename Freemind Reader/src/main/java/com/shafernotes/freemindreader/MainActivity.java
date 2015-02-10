package com.shafernotes.freemindreader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Document document = null;
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = builderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                //TODO: Handle Error
            }

            InputStream is = null;
            is = getResources().openRawResource(getResources().getIdentifier("raw/help","raw", getPackageName()));

            try {
                document = builder.parse(is);
            } catch (SAXException e) {
                //TODO: Handle Error
            }
            finally {
            }

            MyApplication appState = ((MyApplication)getApplicationContext());
            Node currentRoot = null;

            if (document != null) {
                appState.setDocument(document);
                NodeList nodeList = null;
                nodeList = document.getElementsByTagName("node");
                currentRoot = nodeList.item(0);
                appState.setCurrentRoot(currentRoot);
                Intent intent = new Intent(this, MapView.class);
                intent.putExtra("nodeID", "child");
                startActivity(intent);
            }
        }  catch (IOException e) {
            //TODO: Handle Error
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

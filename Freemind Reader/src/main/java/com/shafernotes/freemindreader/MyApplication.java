package com.shafernotes.freemindreader;

import android.app.Application;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Created by Eric on 3/2/14.
 */
public class MyApplication extends Application {
    Document myDocument = null;
    Node currentRoot = null;

    public Document getDocument(){
        return myDocument;
    }
    public void setDocument(Document s){
        myDocument = s;
    }
    public Node getCurrentRoot(){
        return currentRoot;
    }
    public void setCurrentRoot(Node s){
        currentRoot = s;
    }

}

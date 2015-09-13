package fr.marc.remotebutons;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UnknownFormatConversionException;


public class buttons extends ActionBarActivity {

    // Declare web access
    static Socket socket;
    final static int SERVERPORT = 64696;
    final static String SERVER_IP = "192.168.1.39";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_buttons, container, false);

            // start Thread for client connection
            new Thread(new ClientThread()).start();

            // Declare actions taken on button click
            Button buttonCopy = (Button) rootView.findViewById(R.id.button_copy);
            buttonCopy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("buttonApp", "onclick listener");
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())), true);
                        out.println("[['ctrl','c']]");
                    } catch (Exception e) {e.printStackTrace();}
                }
            });


            return rootView;
        }

        class ClientThread implements Runnable{
            @Override
            public void run(){
                try{
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                    socket = new Socket(serverAddr, SERVERPORT);
                }catch(UnknownHostException e1){ e1.printStackTrace();
                }catch(IOException e1){  e1.printStackTrace(); }

            }
        }
    }




    // Click actions of buttons
    public void do_copy(View v){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("[['ctrl','v']]");
        } catch (Exception e) {e.printStackTrace();}
    }

    public void do_undo(View v){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("[['ctrl','z']]");
        } catch (Exception e) {e.printStackTrace();}
    }

    public void do_redo(View v){
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("[['ctrl','y']]");
        } catch (Exception e) {e.printStackTrace();}
    }


}

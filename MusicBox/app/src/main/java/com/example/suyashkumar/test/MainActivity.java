/*
MainActivity.java
TODO: Description

@author Suyash Kumar
@author Shana Fielding
@author Amy Zhao
 */
package com.example.suyashkumar.test;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import android.app.Activity;

public class MainActivity extends Activity {
    int number = 0; // Number of times clicked

    String songRecording; // String of notes that user has input
    String origSong; // String of notes from original song

    HashMap<Integer,Integer> buttonToSound; // Maps button id to R.raw.N id
    HashMap<Integer,String> buttonToNote; // Maps pressed button id to note letter
    HashMap<String, Integer> noteToButton; // Maps pressed button id to note letter

    ImageButton buttonC;
    ImageButton buttonD;
    ImageButton buttonE;
    ImageButton buttonF;
    ImageButton buttonG;
    ImageButton buttonA;
    ImageButton buttonB;
    ImageButton buttonHighC;
    TextView text;

    // state = 0: free play --> button: start game
    // state = 1: in game --> button: good luck -> score me
    // state = 2: done scoring --> button: reset
    int state;

    //USB STUFF:
    private int currentKey=-1;
    // TAG is used to debug in Android logcat console
    private static final String TAG = "ArduinoAccessory";

    private static final String ACTION_USB_PERMISSION = "com.example.arduinoblinker.action.USB_PERMISSION";

    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;
    private boolean mPermissionRequestPending;


    UsbAccessory mAccessory;
    ParcelFileDescriptor mFileDescriptor;
    FileInputStream mInputStream;
    FileOutputStream mOutputStream;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonC = (ImageButton) findViewById(R.id.imageButton);
        buttonD = (ImageButton) findViewById(R.id.imageButton2);
        buttonE = (ImageButton) findViewById(R.id.imageButton3);
        buttonF = (ImageButton) findViewById(R.id.imageButton4);
        buttonG = (ImageButton) findViewById(R.id.imageButton5);
        buttonA = (ImageButton) findViewById(R.id.imageButton6);
        buttonB = (ImageButton) findViewById(R.id.imageButton7);
        buttonHighC = (ImageButton) findViewById(R.id.imageButton8);
        initMaps();
        text=(TextView) findViewById(R.id.textView);

        text.setText("creating");

        ToggleButton startToggleButton=(ToggleButton) findViewById(R.id.startButtonID);
        startToggleButton.setText("Start Game");
        // Initialize state
        state = 0;
        super.onCreate(savedInstanceState);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        registerReceiver(mUsbReceiver, filter);

        TextView text2 = (TextView) findViewById(R.id.textView2);
        text2.setText("before text");
        if (getLastNonConfigurationInstance() != null) {
            text2.setText("getting accessory");
            mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
            openAccessory(mAccessory);
        }

        text.setText("done creating");
    }

    /*
    initMaps
    This function initializes two hashmaps. buttontoSound maps imageButton IDs to sound IDs.
    buttonToNote maps imageButtonIDs to a string containing the note being played.
     */
    private void initMaps(){
        buttonToSound=new HashMap<Integer,Integer>();
        buttonToSound.put(R.id.imageButton, R.raw.c);
        buttonToSound.put(R.id.imageButton2,R.raw.d);
        buttonToSound.put(R.id.imageButton3,R.raw.e);
        buttonToSound.put(R.id.imageButton4,R.raw.f);
        buttonToSound.put(R.id.imageButton5,R.raw.g);
        buttonToSound.put(R.id.imageButton6, R.raw.a);
        buttonToSound.put(R.id.imageButton7, R.raw.b);
        buttonToSound.put(R.id.imageButton8,R.raw.high_c);

        buttonToNote=new HashMap<Integer, String>();
        buttonToNote.put(R.id.imageButton, "C");
        buttonToNote.put(R.id.imageButton2,"D");
        buttonToNote.put(R.id.imageButton3,"E");
        buttonToNote.put(R.id.imageButton4,"F");
        buttonToNote.put(R.id.imageButton5,"G");
        buttonToNote.put(R.id.imageButton6,"A");
        buttonToNote.put(R.id.imageButton7, "B");
        buttonToNote.put(R.id.imageButton8, "H");

        noteToButton=new HashMap<String, Integer>();
        noteToButton.put("C",R.id.imageButton);
        noteToButton.put("D",R.id.imageButton2);
        noteToButton.put("E",R.id.imageButton3);
        noteToButton.put("F",R.id.imageButton4);
        noteToButton.put("G",R.id.imageButton5);
        noteToButton.put("A",R.id.imageButton6);
        noteToButton.put("B",R.id.imageButton7);
        noteToButton.put("H",R.id.imageButton8);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    /*
    delay
    This function waits a specified amount of time before moving to the next instruction.
    @param time The time in ms to delay
    */
    public void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ie) {
            //Handle exception
        }
    }

    /*
    playSound
    This function plays a piano sound based on which button press called this function.
    @param v The view information from the calling object
     */
    public String playSound(View v) {
        int soundId=buttonToSound.get(v.getId());
        songRecording+=buttonToNote.get(v.getId());
        playSoundById(soundId);
        return songRecording;
    }


    /*
    playSoundById
    This function plays a piano sound based on the soundID (resource id) passed from playSound.
    @param soundId The integer corresponding to the soundID for the note .wav file being played.
    */
    public void playSoundById(int soundId) {

        MediaPlayer mp = MediaPlayer.create(this, soundId); // Create a new MediaPlayer object

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Explicitly release this object when sound finished playing
            }
        });

        mp.start(); // Start playing the selected sound (specified by soundId)

    }

    /*
    playAndHighlight
    This function plays the sound and highlights the key corresponding to given button, followed by
    a specified delay time.
    @param button The ImageButton object to be played.
    @param delay The delay time in ms to follow the sound.
    */
    private void playAndHighlight(final ImageButton button, int delay){
        button.post(new Runnable() {
            public void run() {
                button.setSelected(true);
            }
        });
        playSoundById(buttonToSound.get(button.getId()));
        delay(delay);
        button.post(new Runnable() {
            public void run() {
                button.setSelected(false);
            }
        });
    }

    /*
    maryHadALittleLamb
    This function plays a maryHadALittleLamb, and lights up the corresponding keys.
    It returns a string containing the correct notes.
    @param v The view information from the calling object
     */
    Thread tSong;
    public String maryHadALittleLamb(final View v) {
        // Play song in separate thread
//        tSong=new Thread(new Runnable() {
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
                        msgDisplay.setText("Wait and listen!");
                        msgDisplay.setVisibility(v.VISIBLE);
//                    }
//                });
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonC, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonE, 1000);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonD, 1000);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonG, 500);
                playAndHighlight(buttonG, 1000);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonC, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonE, 500);
                playAndHighlight(buttonD, 500);
                playAndHighlight(buttonC, 500);

                // Update messages and buttons on UI after song finishes
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        msgDisplay = (TextView) findViewById(R.id.msgDisplay);
                        msgDisplay.setText("Your turn!");
                        msgDisplay.setVisibility(v.VISIBLE);
                        Button b = (Button) findViewById(R.id.startButtonID);
                        b.setVisibility(v.VISIBLE);
                        b.setEnabled(true);
                        b.setText("Score Me");
//                    }
//                });

//                stopThread = false;
               //t.start();

//            }
//        });
//        tSong.start();

        return "EDCDEEEDDDEGGEDCDEEEEDDEDC";
    }


    /*
    buttonClick
    This function sets the button text and calls the appropriate functions based on the value of the
    state variable.
    @param v The view information from the calling object.
    */
    @SuppressWarnings("deprecation")
    public void buttonClick(View v) {
        int cur = state;
        Button b = (Button) findViewById(R.id.startButtonID);
        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);

        switch (cur) {
            case 0:
                //b.setText("Good Luck!");
                b.setVisibility(v.INVISIBLE);

                msgDisplay.setText("Wait and listen!");
                msgDisplay.setVisibility(v.VISIBLE);
                b.setEnabled(false);
                state = 1;

                /*TextView text2 = (TextView) findViewById(R.id.textView2);
                text2.setText("before text");
                if (getLastNonConfigurationInstance() != null) {
                    text2.setText("getting accessory");
                    mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
                    openAccessory(mAccessory);
                }*/

                // Start the game, disable button for duration of song
                startGame(v, true);
                break;
            case 1:
                // Score the user
                scoreUser();
                b.setText("Reset");
                state = 2;
                break;
            case 2:
                // Reset all text, user input, and state
                songRecording = "";

                msgDisplay.setVisibility(msgDisplay.INVISIBLE);

                TextView scoreNum = (TextView) findViewById(R.id.scoreNum);
                scoreNum.setVisibility(msgDisplay.INVISIBLE);

                b.setText("Start Game");
                state = 0;
                break;
        }
    }

    /*
    startGame
    This function starts the game after a song selection has been made. It will play the song when
    the 'Start Game' button is clicked.
    @param v The view information from the calling object
     */
    public void startGame(View v, boolean playMary) {

        if (playMary){
            origSong = maryHadALittleLamb(v); // play mary had a little lamb right now
            songRecording = ""; //reset string
        }

        delay(1000);
        // Start Listening
        char lastReadChar = startListening();
        if (lastReadChar == 'X') { // If gets X stop
                String scoring = "Scoring: " + songRecording;
                text.setText(scoring);
                scoreUser();
                return;
        }
        else{
            int button = noteToButton.get(Character.toString(lastReadChar));
            ImageButton b = (ImageButton) findViewById(button);
            playAndHighlight(b, 500);
            songRecording = songRecording + Character.toString(lastReadChar);
            startGame(v, false);
        }

//        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
////        msgDisplay.setText("Your turn!");
////        msgDisplay.setVisibility(v.VISIBLE);
//        Button b = (Button) findViewById(R.id.startButtonID);
//        b.setVisibility(v.VISIBLE);
//        b.setEnabled(true);
//        b.setText("Score Me");
    }

    /*
    scoreUser
    This function ends the game once the user has finished playing their song. It will
    take user input, then compare that to the original notes them to determine a score.
    @param v The view information from the calling object
     */
    public void scoreUser() {
        // Get score
        int score = finalScore(origSong, songRecording);

        byte[] win = new byte[1];
        if (score == 100) {
            win[0] = 1;
        } else {
            win[0] = 0;
        }

        if (mOutputStream!=null) {
            try {
                mOutputStream.write(win);
            } catch (IOException e) {
                Log.e(TAG, "write failed", e);
            }
        }

        String s = Integer.toString(score);
        String out = s + "%";

        // Display score
        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
        msgDisplay.setVisibility(msgDisplay.VISIBLE);
        msgDisplay.setText("You got:");

        TextView scoreNum = (TextView) findViewById(R.id.scoreNum);
        scoreNum.setText(out);
        scoreNum.setVisibility(msgDisplay.VISIBLE);
    }

    /*
    finalScore
    This function returns a score for the user based on % correct (edit distance/orig length)*100.
    @param orig The original string of notes played in the song
    @param userIn The string of notes the user plays
     */
    public int finalScore(String orig, String userIn) {

        int origLen = orig.length();
        int userLen = userIn.length();

        // If no input, score = 0
        if (userLen == 0) {
            return 0;
        }

        // Initialize DP table
        Integer[][] table = new Integer[origLen][userLen];

        for (int i = 0; i < origLen; i++) {
            for (int j = 0; j < userLen; j++) {
                // d = 1 if diff characters; d = 0 if same
                int d = diff(orig.charAt(i), userIn.charAt(j));

                // If first row of the table
                if (i == 0) {
                    // If first cell of the table
                    if (j == 0) {
                        // Then table[0][0] = d
                        table[i][j] = d;
                    } else {
                        // o.w. only option is to d + table[i][j-1] b/c no cells above or upper-left
                        table[i][j] = d + table[i][j - 1];
                    }
                } else if (j == 0) {
                    // If first column of table, then only option is d+table[i-1][j] b/c no cells
                    // to the left or upper-left
                    table[i][j] = d + table[i - 1][j];
                } else {
                    int interMin = Math.min(1 + table[i - 1][j], 1 + table[i][j - 1]);
                    int realMin = Math.min(interMin, d + table[i - 1][j - 1]);
                    table[i][j] = realMin;
                }
            }
        }

        int editDist = table[origLen - 1][userLen - 1];
        float percentWrong = editDist / ((float) origLen) * 100;
        float score = 100 - percentWrong;

        // In case num errors > original length
        int ret = (int) Math.floor(score);
        if (ret < 0){
            ret = 0;
        }

        return ret;
    }

    /*
    diff
    This function checks if two characters are the same
    @param i Character i
    @param j Character j
     */
    public int diff(char i, char j) {
        if (i == j) {
            return 0;
        } else {
            return 1;
        }
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        openAccessory(accessory);
                    } else {
                        Log.d(TAG, "permission denied for accessory "
                                + accessory);
                    }
                    mPermissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                UsbAccessory accessory = (UsbAccessory)intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY); {
                    if (accessory != null && accessory.equals(mAccessory)) {
                        closeAccessory();
                    }
                }
            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public Object onRetainNonConfigurationInstance() {
        if (mAccessory != null) {
            return mAccessory;
        } else {
            return super.onRetainNonConfigurationInstance();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mInputStream != null && mOutputStream != null) {
            return;
        }

        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            if (mUsbManager.hasPermission(accessory)) {
                openAccessory(accessory);
            } else {
                synchronized (mUsbReceiver) {
                    if (!mPermissionRequestPending) {
                        mUsbManager.requestPermission(accessory,mPermissionIntent);
                        mPermissionRequestPending = true;
                    }
                }
            }
        } else {
            Log.d(TAG, "mAccessory is null");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        closeAccessory();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    private void openAccessory(UsbAccessory mAccessory2) {
        mFileDescriptor = mUsbManager.openAccessory(mAccessory2);
        text.setText("in open accessory");
        if (mFileDescriptor != null) {
            mAccessory = mAccessory2;
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);
            Log.d(TAG, "accessory opened");
        } else {
            Log.d(TAG, "accessory open fail");
        }
    }


    private void closeAccessory() {
        try {
            if (mFileDescriptor != null) {
                mFileDescriptor.close();
            }
        } catch (IOException e) {
        } finally {
            mFileDescriptor = null;
            mAccessory = null;
        }
    }

    boolean stopThread=false;



    public char startListening() {
        /*
        Spawns the recording thread that populates
        @param View v the calling view.
        */
        char lastReadChar;
        text.setText("Listening");
        byte[] lastRead = new byte[6];

        while(true) {
            if (mInputStream != null) {
                try {
                    if (mInputStream.read(lastRead, 0, 6) > 0) {
                        lastReadChar = (char) lastRead[0];
                        text.setText("read char");
                        return lastReadChar;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}

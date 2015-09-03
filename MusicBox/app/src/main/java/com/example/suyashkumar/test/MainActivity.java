/*
MainActivity.java
TODO: Description

@author Suyash Kumar
@author Shana Fielding
@author Amy Zhao
 */
package com.example.suyashkumar.test;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    int number = 0; // Number of times clicked

    String songRecording; // String of notes that user has input
    String origSong; // String of notes from original song

    HashMap<Integer,Integer> buttonToSound; // Maps button id to R.raw.N id
    HashMap<Integer,String> buttonToNote; // Maps pressed button id to note letter

    ImageButton buttonC;
    ImageButton buttonD;
    ImageButton buttonE;
    ImageButton buttonF;
    ImageButton buttonG;
    ImageButton buttonA;
    ImageButton buttonB;
    ImageButton buttonHighC;

    // state = 0: free play --> button: start game
    // state = 1: in game --> button: good luck -> score me
    // state = 2: done scoring --> button: reset
    int state;

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

        // Initialize state
        state = 0;
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
        buttonToSound.put(R.id.imageButton6,R.raw.a);
        buttonToSound.put(R.id.imageButton7,R.raw.b);
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
    maryHadALittleLamb
    This function plays a maryHadALittleLamb, and lights up the corresponding keys.
    It returns a string containing the correct notes.
    @param v The view information from the calling object
     */
    public String maryHadALittleLamb(final View v) {
        // Play song in separate thread
        new Thread(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
                        msgDisplay.setText("Wait and listen!");
                        msgDisplay.setVisibility(v.VISIBLE);
                    }
                });
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
                        msgDisplay.setText("Your turn!");
                        msgDisplay.setVisibility(v.VISIBLE);
                        Button b = (Button) findViewById(R.id.startButtonID);
                        b.setEnabled(true);
                        b.setText("Score Me");
                    }
                });

            }
        }).start();

        return "EDCDEEEDDDEGGEDCDEEEEDDEDC";
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
    startGame
    This function starts the game after a song selection has been made. It will play the song and
    then call the recordSong method and determine a score.
    @param v The view information from the calling object
     */
    public void startGame(View v) {
        origSong = maryHadALittleLamb(v); // play mary had a little lamb right now
        songRecording = ""; //reset string
    }

    /*
    recordSong
    This function ends the game once the user has finished playing their song. It will
    then user input, then compare that to the original notes them to determine a score.
    @param v The view information from the calling object
     */
    public void recordSong(View v) {
       // String userIn = songRecording;

        // Get score
        int score = finalScore(origSong, songRecording);

        // Display score
        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
        msgDisplay.setVisibility(v.VISIBLE);
        msgDisplay.setText("You got:");

        String s = Integer.toString(score);
        String out = s + "%";

        TextView scoreNum = (TextView) findViewById(R.id.scoreNum);
        scoreNum.setText(out);
        scoreNum.setVisibility(v.VISIBLE);
    }

    /*
    finalScore
    This function returns a score for the user based on % correct (edit distance/orig length)*100
    @param orig The original string of notes played in the song
    @param userIn The string of notes the user plays
     */
    public int finalScore(String orig, String userIn) {
        int origLen = orig.length();
        int userLen = userIn.length();

        if (userLen == 0) {
            return 0;
        }

        Integer[][] table = new Integer[origLen][userLen];

        for (int i = 0; i < origLen; i++) {
            for (int j = 0; j < userLen; j++) {
                int d = diff(orig.charAt(i), userIn.charAt(j));

                if (i == 0) {
                    if (j == 0) {
                        table[i][j] = d;
                    } else {
                        table[i][j] = d + table[i][j - 1];
                    }
                } else if (j == 0) {
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

    /*
    buttonClick
    This function sets the button text and calls the appropriate functions based on the value of the
    state variable.
    @param v The view information from the calling object.
    */
        public void buttonClick(View v) {
        int cur = state;
        Button b = (Button) findViewById(R.id.startButtonID);

        switch (cur) {
            case 0:
                b.setText("Good Luck!");
                // Start the game
                b.setEnabled(false);
                startGame(v);
                state = 1;
                break;
            case 1:
                recordSong(v);
                state = 2;
                b.setText("Reset");
                break;
            case 2:
                songRecording = "";

                TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
                msgDisplay.setVisibility(v.INVISIBLE);

                TextView scoreNum = (TextView) findViewById(R.id.scoreNum);
                scoreNum.setVisibility(v.INVISIBLE);

                b.setText("Start Game");
                state = 0;
                break;
        }
    }

}

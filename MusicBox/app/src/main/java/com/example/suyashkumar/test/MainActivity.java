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
import android.view.View.OnClickListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    int number = 0; // Number of times clicked
    String songRecording;
    String origSong;
    ImageButton buttonC;
    ImageButton buttonD;

    HashMap<Integer,Integer> buttonToSound; // Maps button id to R.raw.N id
    HashMap<Integer,String> buttonToNote; // Maps pressed button id to note letter

    ImageButton buttonE;
    ImageButton buttonF;
    ImageButton buttonG;
    ImageButton buttonA;
    ImageButton buttonB;
    ImageButton buttonHighC;

    // state = 0: free play --> button: start game
    // state = 1: in game --> button: good luck
    // state = 2: song done --> button: score me
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

        state = 0;

        Button startButton = (Button) findViewById(R.id.startButtonID);
        Button recordButton = (Button) findViewById(R.id.recordSongID);

    }
    /*

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
        buttonToNote.put(R.id.imageButton8, "HIGH_C");

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

        //  new Thread(new Runnable() {
        //    public void run() {
        MediaPlayer mp = MediaPlayer.create(this, soundId); // Create a new MediaPlayer object

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Explicitly release this object when sound finished playing
            }
        });

        mp.start(); // Start playing the selected sound (specified by soundId)
        // }
        //  }).start();


    }

    public void test(View v) {
        // new Thread(new Runnable() {
        //   public void run() {


        //buttonC.setImageResource(R.drawable.one_pressed);
        buttonC.setSelected(true);
        buttonC.refreshDrawableState();
        buttonC.performClick();
        //buttonC.setSelected(true);
        //new pressC(buttonC).execute();
        //buttonC.setActivated(true);

        buttonC.performClick();


        delay(500);
        // }
        // }).start();

    }

    /*
    maryHadALittleLamb
    This function plays a maryHadALittleLamb, and lights up the corresponding keys.
    It returns a string containing the correct notes.
     */
    public String maryHadALittleLamb(View v) {
        new Thread(new Runnable() {
            public void run() {
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

            }
        }).start();

        //make score button visible after song played


        return "EDCDEEEDDDEGGEDCDEEEEDDEDC";
    }

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
        //Button startButton = (Button) findViewById(R.id.startButtonID); // find the button on the view
        origSong = maryHadALittleLamb(v); // play mary had a little lamb right now
        // String orig = playSong(songId); // play whatever song the user chose, implement later if time
        songRecording = ""; //reset string
        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
        msgDisplay.setText("Your turn!");
        msgDisplay.setVisibility(v.VISIBLE);

    }

        /*
    recordSong
    This function ends the game once the user has finished playing their song. It will
    then user input, then compare that to the original notes them to determine a score
    @param v The view information from the calling object
     */

    public void recordSong(View v) {
        Button recordSongButton = (Button) findViewById(R.id.recordSongID); // find the button on the view
        System.out.println(songRecording);  //display recorded string for debug
        String userIn = songRecording;

        int score = finalScore(origSong, userIn);

        TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
        msgDisplay.setVisibility(v.VISIBLE);
        msgDisplay.setText("You got:");

        String s = Integer.toString(score);
        String out = s + "%";

        TextView scoreNum = (TextView) findViewById(R.id.scoreNum);
        scoreNum.setText(out);
        scoreNum.setVisibility(v.VISIBLE);

        //Button reset = (Button) findViewById(R.id.resetButton);
        //reset.setVisibility(v.VISIBLE);
        //reset.setEnabled(true);

        System.out.println(score);  //display score for debug
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

        // initialize 0th row and 0th column to all 0s
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
    onClickListener determines what each button does when clicked
     */
    /*
    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.startButtonID:
                    Button b = (Button) findViewById(R.id.startButtonID);
                    b.setText("Good luck!");
                    startGame(v);
                    break;
                case R.id.recordSongID:
                    recordSong(v);
                    break;
                case R.id.resetButton:
                    // clear score messages, set reset button to invisible, disabled, set score button to invisible, disabled, reset global var
                    TextView msgDisplay = (TextView) findViewById(R.id.msgDisplay);
                    msgDisplay.setVisibility(v.INVISIBLE);

                    TextView scoreNum = (TextView) findViewById(R.id.scoreNum);
                    scoreNum.setVisibility(v.INVISIBLE);

                    Button reset = (Button) findViewById(R.id.resetButton);
                    reset.setVisibility(v.INVISIBLE);
                    reset.setEnabled(false);

                    Button score = (Button) findViewById(R.id.recordSongID);
                    score.setVisibility(v.INVISIBLE);
                    score.setEnabled(false);

                    songRecording = "";
                    break;
            }
        }
    };*/

    public void buttonClick(View v) {
        int cur = state;
        Button b = (Button) findViewById(R.id.startButtonID);

        switch (cur) {
            case 0:
                b.setText("Good Luck!");
                // Start the game
                startGame(v);
                b.setText("Score Me");
                state = 1;
                break;
            case 1:
                //b.setText("Score Me");
                recordSong(v);
                state = 2;
                b.setText("Reset");
                break;
            case 2:
                //b.setText("Reset");
                // Do reset-y stuff here 
                state = 3;
                break;
            case 3:
                b.setText("Start Game");
                state = 0;
                break;
        }
    }

}

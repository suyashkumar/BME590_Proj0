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
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {
    int number=0; // Number of times clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    playSound
    This function plays a piano sound based on which button press called this function.
    @param v The view information from the calling object
     */
    public void playSound(View v) {
        int soundId=-1; // Default value of soundId
        System.out.println(v.getId()); // Print the caller id for debug
        System.out.println(R.id.imageButton2);

        switch(v.getId()){ //Switch over the id to determine who the caller was and set the proper soundId
            case (R.id.imageButton):
                soundId=R.raw.c;
                break;
            case (R.id.imageButton2):
                soundId=R.raw.d;
                break;
            case (R.id.imageButton3):
                soundId=R.raw.e;
                break;
            case (R.id.imageButton4):
                soundId=R.raw.f;
                break;
            case (R.id.imageButton5):
                soundId=R.raw.g;
                break;
            case (R.id.imageButton6):
                soundId=R.raw.a;
                break;
            case (R.id.imageButton7):
                soundId=R.raw.b;
                break;
            default:
                System.exit(0); // If soundId is unchanged, something wrong is calling this function.
                break;
        }

        playSoundById(soundId);
    }

    /*
 playSoundById
 This function plays a piano sound based on the soundID (resource id) passed from playSound.
 @param soundId The integer corresponding to the soundID for the note .wav file being played.
  */

    public void playSoundById(int soundId){
        MediaPlayer mp = MediaPlayer.create(this,soundId); // Create a new MediaPlayer object

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Explicitly release this object when sound finished playing
            }
        });

        mp.start(); // Start playing the selected sound (specified by soundId)
    }

    /*
    startGame
    This function starts the game after a song selection has been made. It will play the song and
    then record user input, then compare them to determine a score
    @param v The view information from the calling object
    @param songId The song that's been chosen
     */

    public void startGame(View v, String songId){
        ToggleButton startButton = (ToggleButton) findViewById(R.id.startButtonID); // find the button on the view

        if(startButton.isChecked()){ //1 if start button pressed, 0 o.w.
            //String orig = maryHadALittleLamb(); // play mary had a little lamb right now
           // String orig = playSong(songId); // play whatever song the user chose, implement later if time
            //String userIn = recordSong();
            //int finalScore = score(orig, userIn);
        }
        else{
            String noSong = "Please select a song";
            TextView text;
            //text = (TextView)findViewById(R.id.text_message);
            //text.setText(noSong);
        }
    }

     /*
    percentScore
    This function returns a score for the user based on % correct. May be altered later to reflect edit distance.
    @param orig The original string of notes played in the song
    @param userIn The string of notes the user plays


    public float percentScore(String orig, String userIn){
       float score = 0;
       float total = orig.length();

       for(int i = 0; i < total; i++){
           if(orig.charAt(i)==userIn.charAt(i)){
               score++;
           }
       }

       float percent = (score/total)*100;

       return percent;
    }
    */

    /*
    finalScore
    This function returns a score for the user based on % correct (edit distance/orig length)*100
    @param orig The original string of notes played in the song
    @param userIn The string of notes the user plays
     */

    public float finalScore(String orig, String userIn){

        // add +1 to help with the 0th row and column which should be initialized to all 0s
        int origLen = orig.length();
        int userLen = userIn.length();
        Integer[][] table = new Integer[origLen][userLen];

        // initialize 0th row and 0th column to all 0s
        for(int i=0; i < origLen; i++){
            for(int j=0; j < userLen; j++){
                int d = diff(orig.charAt(i),userIn.charAt(j));

                if(i==0){
                    if(j==0){
                        table[i][j] = d;
                    }else{
                        table[i][j] = d+table[i][j-1];
                    }
                }else if(j==0){
                    table[i][j] = d+table[i-1][j];
                }else{
                    int interMin = Math.min(1+table[i-1][j],1+table[i][j-1]);
                    int realMin = Math.min(interMin,d+table[i-1][j-1]);
                    table[i][j] = realMin;
                }
            }
        }

        int editDist = table[origLen-1][userLen-1];
        float percentWrong = editDist/((float) origLen)*100;
        float score = 100-percentWrong;

        return score;
    }

    /*
    diff
    This function checks if two characters are the same
    @param i Character i
    @param j Character j
     */
    public int diff(char i, char j){
        if(i==j){
            return 0;
        }else{
            return 1;
        }
    }

}

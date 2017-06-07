package com.shalom.itai.theservantexperience.Activities;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;

import java.util.Scanner;

import static com.shalom.itai.theservantexperience.Utils.Functions.throwRandom;

public class MatchesGameActivity extends AppCompatActivity {
    MatchesGameActivity act;
    boolean turnIsBegan = false;
    int matchesCounterTurn = 0;
    boolean takeFromHeapOne = false;
    int heap1 = 12;
    int heap2 = 12;
    int n1 = 0;
    int randomNum;
    int userNum = 0;
    boolean isFirstCase = false;
//check
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches_game);
        Button fire = (Button) findViewById(R.id.fire);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (matchesCounterTurn != 0) {

                    if(takeFromHeapOne){
                        deleteMatches(R.id.heap1);
                        heap1 -= matchesCounterTurn;
                    }else{
                        deleteMatches(R.id.heap2);
                        heap2 -= matchesCounterTurn;
                    }
                    matchesCounterTurn = 0;
                    turnIsBegan = false;
                    takeFromHeapOne = false;
                    secondStep();
                } else {
                    toastThis("no matches selected!");
                }
            }
        });
        act = this;
        firstStep();
    }

    private void deleteMatches(int id){
        ConstraintLayout ll = (ConstraintLayout) findViewById(id);
        final int childCount = ll.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View img = ll.getChildAt(i);
            if (img instanceof ImageView) {
                ImageView match = (ImageView) img;
                if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(act, R.drawable.match_burned).getConstantState()) {
                    match.setVisibility(View.INVISIBLE);
                }
            }
        }
    }


    public void onImageClick(View view) {
        if (!turnIsBegan) {
            ImageView match = (ImageView) view;
            if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(this, R.drawable.match).getConstantState()) {
                match.setImageResource(R.drawable.match_burned);
                matchesCounterTurn++;

                int parentId = ((View) view.getParent()).getId();
                if (parentId == R.id.heap1) {
                    takeFromHeapOne = true;
                } else {
                    takeFromHeapOne = false;
                }
                turnIsBegan = true;

            } else {
                match.setImageResource(R.drawable.match);
                matchesCounterTurn--;
                if (matchesCounterTurn == 0) {
                    takeFromHeapOne = false;
                    turnIsBegan = false;
                }
            }


        } else {

            int parentId = ((View) view.getParent()).getId();
            ImageView match = (ImageView) view;
            if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(this, R.drawable.match).getConstantState()) {
                if ((parentId == R.id.heap1 && takeFromHeapOne && matchesCounterTurn < 4) || (parentId == R.id.heap2 && !takeFromHeapOne && matchesCounterTurn < 4)) {
                    match.setImageResource(R.drawable.match_burned);
                    matchesCounterTurn++;
                }
            } else {
                match.setImageResource(R.drawable.match);
                matchesCounterTurn--;
                if (matchesCounterTurn == 0) {
                    takeFromHeapOne = false;
                    turnIsBegan = false;
                }
            }
        }
    }

    private void toastThis(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }


    private void secondStep() {
        if (isFirstCase) {
            /*toastThis("Select heap to take " +
                    "matches from:");
            n1 = scan.nextInt();
            toastThis("Select number of matches" +
                    " to take:");
            userNum = scan.nextInt();

            do {
                if ((userNum > 4) || (userNum < 1) || (userNum - heap1 > 0)
                        && (n1 == 1)) {

                    toastThis("Select number of " +
                            "matches to take:");
                    userNum = scan.nextInt();
                }
            }
            while ((userNum > 4) || (userNum < 1) || (userNum - heap1 > 0)
                    && (n1 == 1));
            if (n1 == 1) {
                heap1 = heap1 - userNum;
            }*/
            if (heap1 <= 0) {

                toastThis("Number" +
                        "of matches in the first" +
                        "heap is:" + heap1);
                toastThis("Number of matches" +
                        "in the second" +
                        " heap is:" + heap2);
                toastThis("The computer beats" +
                        " the user");
            }

            if (heap2 <= 0) {

                toastThis("Number of matches in the" +
                        " first heap is:" + heap1);
                toastThis("Number of matches" +
                        "in the second" +
                        "heap is:" + heap2);
                toastThis("The computer" +
                        "beats the user");
            }
        } else {
           /* toastThis("Select heap to " +
                    "take matches from:");
            n1 = scan.nextInt();

            userNum = scan.nextInt();

            do {
                if ((userNum > 4) || (userNum < 1) || (userNum - heap2 > 0)) {

                    toastThis("Select number" +
                            "of matches to take:");
                    randomNum = scan.nextInt();
                }
            }
            while ((userNum > 4) || (userNum < 1) || (userNum - heap2 > 0));
*/
            if (heap1 <= 0) {

                toastThis("Number of matches in" +
                        "the first" +
                        " heap is:" + heap1);
                toastThis("Number of matches in" +
                        "the second" +
                        "heap is:" + heap2);

                toastThis("The computer beats"
                        + "the user");
            }
            /*if (n1 == 1) {
                heap1 = heap1 - userNum;

            }

            if (n1 != 1) {
                heap2 = heap2 - userNum;
            }
*/

            if (heap2 <= 0) {

                toastThis("Number of matches in" +
                        " the first heap is:" + heap1);
                toastThis("Number of matches" +
                        " in the second" +
                        " heap is:" + heap2);
                toastThis("The computer" +
                        " beats the user");
                return;
            }
        }
        if ((heap1 == 1) && (heap2 == 1)) {

            toastThis("Number of matches in the" +
                    " first heap is:1");
            toastThis("Number of matches in the second" +
                    "heap is:1");
            toastThis("Computer took 1 matches from" +
                    " heap number 2");
            toastThis("Number of matches in the" +
                    " first heap is:1");
            toastThis("Number of matches in" +
                    " the second heap is:0");
            toastThis("The user beats the computer");
            return;
        }
        if ((heap1 == 0) || (heap2 == 0)) {
            return;
        } else {
            firstStep();
        }
    }


    private void firstStep() {
        toastThis("Number of matches in the first heap" +
                " is:" + heap1);
        toastThis("Number of matches in the second" +
                " heap is:" + heap2);


        randomNum = throwRandom(4, 1);

        if (heap1 > 1) {
            isFirstCase = true;
            if ((heap1 < 5) && (heap1 > 1) && ((heap1 - randomNum) <= 0)) {
                do {
                    randomNum = throwRandom(4, 1);
                }
                while (heap1 - randomNum < 1);
            }
            heap1 = heap1 - randomNum;
            toastThis("Computer took " + randomNum +
                    " matches from heap number 1");
            toastThis("Number of matches in the " +
                    "first heap is:" + heap1);
            toastThis("Number of matches in the " +
                    "second heap is:" + heap2);
            jonBurn(R.id.heap1,randomNum);

        } else {
            isFirstCase = false;
            if ((heap2 < 5) && (heap2 > 1)
                    && ((heap2 - randomNum) <= 0)) {
                do {
                    randomNum = throwRandom(4, 1);
                }
                while (heap2 - randomNum < 1);
            }

            heap2 = heap2 - randomNum;
            toastThis("Computer took "
                    + randomNum + " matches "
                    + "from heap number 2");
            toastThis("Number of matches " +
                    "in the first heap is:" + heap1);
            toastThis("Number of matches " +
                    "in the second heap is:" + heap2);
            jonBurn(R.id.heap2,randomNum);
        }
        toastThis("Select number of matches" +
                "to take:");
    }


    private void jonBurn(final int id, int numOfBurns){
        ConstraintLayout ll = (ConstraintLayout) findViewById(id);
        final int childCount = ll.getChildCount();
        for (int i = 0; i < childCount && numOfBurns>0; i++) {

            View img = ll.getChildAt(i);
            if (img instanceof ImageView) {
                ImageView match = (ImageView) img;
                if (match.getDrawable().getConstantState() == ContextCompat.getDrawable(act, R.drawable.match).getConstantState()) {
                    match.setImageResource(R.drawable.match_burned);
                    numOfBurns--;
                }
            }
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                deleteMatches(id);
            }
        }, 2000);

    }

    private void startGame() {
        while ((heap1 != 0) && (heap2 != 0)) {

            toastThis("Number of matches in the first heap" +
                    " is:" + heap1);
            toastThis("Number of matches in the second" +
                    " heap is:" + heap2);

            Scanner scan = new Scanner(System.in);

            randomNum = throwRandom(4, 1);

            if (heap1 > 1) {

                if ((heap1 < 5) && (heap1 > 1) && ((heap1 - randomNum) <= 0)) {
                    do {
                        randomNum = throwRandom(4, 1);
                    }
                    while (heap1 - randomNum < 1);
                }
                heap1 = heap1 - randomNum;
                toastThis("Computer took " + randomNum +
                        " matches from heap number 1");
                toastThis("Number of matches in the " +
                        "first heap is:" + heap1);
                toastThis("Number of matches in the " +
                        "second heap is:" + heap2);
                toastThis("Select heap to take " +
                        "matches from:");
                n1 = scan.nextInt();
                toastThis("Select number of matches" +
                        " to take:");
                userNum = scan.nextInt();

                do {
                    if ((userNum > 4) || (userNum < 1) || (userNum - heap1 > 0)
                            && (n1 == 1)) {

                        toastThis("Select number of " +
                                "matches to take:");
                        userNum = scan.nextInt();
                    }
                }
                while ((userNum > 4) || (userNum < 1) || (userNum - heap1 > 0)
                        && (n1 == 1));
                if (n1 == 1) {
                    heap1 = heap1 - userNum;
                }
                if (heap1 <= 0) {

                    toastThis("Number" +
                            "of matches in the first" +
                            "heap is:" + heap1);
                    toastThis("Number of matches" +
                            "in the second" +
                            " heap is:" + heap2);
                    toastThis("The computer beats" +
                            " the user");
                }
                if (n1 != 1) {
                    heap2 = heap2 - userNum;
                }
                if (heap2 <= 0) {

                    toastThis("Number of matches in the" +
                            " first heap is:" + heap1);
                    toastThis("Number of matches" +
                            "in the second" +
                            "heap is:" + heap2);
                    toastThis("The computer" +
                            "beats the user");
                }
            } else {
                if ((heap2 < 5) && (heap2 > 1)
                        && ((heap2 - randomNum) <= 0)) {
                    do {
                        randomNum = throwRandom(4, 1);
                    }
                    while (heap2 - randomNum < 1);
                }

                heap2 = heap2 - randomNum;
                toastThis("Computer took "
                        + randomNum + " matches "
                        + "from heap number 2");
                toastThis("Number of matches " +
                        "in the first heap is:" + heap1);
                toastThis("Number of matches " +
                        "in the second heap is:" + heap2);
                toastThis("Select heap to " +
                        "take matches from:");
                n1 = scan.nextInt();
                toastThis("Select number of matches" +
                        "to take:");
                userNum = scan.nextInt();

                do {
                    if ((userNum > 4) || (userNum < 1) || (userNum - heap2 > 0)) {

                        toastThis("Select number" +
                                "of matches to take:");
                        randomNum = scan.nextInt();
                    }
                }
                while ((userNum > 4) || (userNum < 1) || (userNum - heap2 > 0));

                if (n1 == 1) {
                    heap1 = heap1 - userNum;
                    if (heap1 <= 0) {

                        toastThis("Number of matches in" +
                                "the first" +
                                " heap is:" + heap1);
                        toastThis("Number of matches in" +
                                "the second" +
                                "heap is:" + heap2);

                        toastThis("The computer beats"
                                + "the user");
                    }
                }

                if (n1 != 1) {
                    heap2 = heap2 - userNum;

                }
                if (heap2 <= 0) {

                    toastThis("Number of matches in" +
                            " the first heap is:" + heap1);
                    toastThis("Number of matches" +
                            " in the second" +
                            " heap is:" + heap2);
                    toastThis("The computer" +
                            " beats the user");
                    break;
                }
            }

            if ((heap1 == 1) && (heap2 == 1)) {

                toastThis("Number of matches in the" +
                        " first heap is:1");
                toastThis("Number of matches in the second" +
                        "heap is:1");
                toastThis("Computer took 1 matches from" +
                        " heap number 2");
                toastThis("Number of matches in the" +
                        " first heap is:1");
                toastThis("Number of matches in" +
                        " the second heap is:0");
                toastThis("The user beats the computer");
                break;
            }
        }
    }
}

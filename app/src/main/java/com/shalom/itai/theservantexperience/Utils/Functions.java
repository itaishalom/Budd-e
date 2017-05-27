package com.shalom.itai.theservantexperience.Utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.ChatBot.MyScheduledReceiver;
import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.Services.BuggerService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;


import static com.shalom.itai.theservantexperience.Services.DayActions.SYSTEM_CURRENT_NUM_OF_CHATS_POINTS;
import static com.shalom.itai.theservantexperience.Services.DayActions.SYSTEM_NUM_OF_CHATS_POINTS;
import static com.shalom.itai.theservantexperience.Services.DayActions.SYSTEM_oldDay;
import static com.shalom.itai.theservantexperience.Services.DayActions.allFacts;
import static com.shalom.itai.theservantexperience.Services.DayActions.allInsults;
import static com.shalom.itai.theservantexperience.Services.DayActions.allJokes;
import static com.shalom.itai.theservantexperience.Utils.Constants.*;

/**
 * Created by Itai on 11/04/2017.
 */

public class Functions {

    public static void createJokes(){
        String facts = "If you have 3 quarters, 4 dimes, and 4 pennies, you have $1.19. You also have the largest amount of money in coins without being able to make change for a dollar. ++ ++The numbers '172' can be found on the back of the U.S. $5 dollar bill in the bushes at the base of the Lincoln Memorial. ++ ++President Kennedy was the fastest random speaker in the world with upwards of 350 words per minute. ++ ++In the average lifetime, a person will walk the equivalent of 5 times around the equator. ++ ++Odontophobia is the fear of teeth. ++ ++The 57 on Heinz ketchup bottles represents the number of varieties of pickles the company once had. ++ ++In the early days of the telephone, operators would pick up a call and use the phrase, \"Well, are you there?\". It wasn't until 1895 that someone suggested answering the phone with the phrase \"number please?\" ++ ++The surface area of an average-sized brick is 79 cm squared. ++ ++According to suicide statistics, Monday is the favored day for self-destruction. ++ ++Cats sleep 16 to 18 hours per day. ++ ++The most common name in the world is Mohammed. ++ ++It is believed that Shakespeare was 46 around the time that the King James Version of the Bible was written. In Psalms 46, the 46th word from the first word is shake and the 46th word from the last word is spear. ++ ++Karoke means \"empty orchestra\" in Japanese. ++ ++The Eisenhower interstate system requires that one mile in every five must be straight. These straight sections are usable as airstrips in times of war or other emergencies. ++ ++The first known contraceptive was crocodile dung, used by Egyptians in 2000 B.C. ++ ++Rhode Island is the smallest state with the longest name. The official name, used on all state documents, is \"Rhode Island and Providence Plantations.\" ++ ++When you die your hair still grows for a couple of months. ++ ++There are two credit cards for every person in the United States. ++ ++Isaac Asimov is the only author to have a book in every Dewey-decimal category. ++ ++The newspaper serving Frostbite Falls, Minnesota, the home of Rocky and Bullwinkle, is the Picayune Intellegence. ++ ++It would take 11 Empire State Buildings, stacked one on top of the other, to measure the Gulf of Mexico at its deepest point. ++ ++The first person selected as the Time Magazine Man of the Year - Charles Lindbergh in 1927. ++ ++The most money ever paid for a cow in an auction was $1.3 million. ++ ++It took Leo Tolstoy six years to write \"War & Peace\". ++ ++The Neanderthal's brain was bigger than yours is. ++ ++On the new hundred dollar bill the time on the clock tower of Independence Hall is 4:10. ++ ++Each of the suits on a deck of cards represents the four major pillars of the economy in the middle ages: heart represented the Church, spades represented the military, clubs represented agriculture, and diamonds represented the merchant class. ++ ++The names of the two stone lions in front of the New York Public Library are Patience and Fortitude. They were named by then-mayor Fiorello LaGuardia. ++ ++The Main Library at Indiana University sinks over an inch every year because when it was built, engineers failed to take into account the weight of all the books that would occupy the building. ++ ++The sound of E.T. walking was made by someone squishing her hands in jelly. ++ ++Lucy and Linus (who where brother and sister) had another little brother named Rerun. (He sometimes played left-field on Charlie Brown\'s baseball team, [when he could find it!]). ++ ++The pancreas produces Insulin. ++ ++1 in 5,000 north Atlantic lobsters are born bright blue. ++ ++There are 10 human body parts that are only 3 letters long (eye hip arm leg ear toe jaw rib lip gum). ++ ++A skunk's smell can be detected by a human a mile away. ++ ++The word \"lethologica\" describes the state of not being able to remember the word you want. ++ ++The king of hearts is the only king without a moustache. ++ ++Henry Ford produced the model T only in black because the black paint available at the time was the fastest to dry. ++ ++Mario, of Super Mario Bros. fame, appeared in the 1981 arcade game, Donkey Kong. His original name was Jumpman, but was changed to Mario to honor the Nintendo of America's landlord, Mario Segali. ++ ++The three best-known western names in China: Jesus Christ, Richard Nixon, and Elvis Presley. ++ ++Every year about 98% of the atoms in your body are replaced. ++ ++Elephants are the only mammals that can't jump. ++ ++The international telephone dialing code for Antarctica is 672. ++ ++World Tourist day is observed on September 27. ++ ++Women are 37% more likely to go to a psychiatrist than men are. ++ ++The human heart creates enough pressure to squirt blood 30 feet (9 m). ++ ++Diet Coke was only invented in 1982. ++ ++There are more than 1,700 references to gems and precious stones in the King James translation of the Bible. ++ ++When snakes are born with two heads, they fight each other for food. ++ ++American car horns beep in the tone of F. ++ ++Turning a clock's hands counterclockwise while setting it is not necessarily harmful. It is only damaging when the timepiece contains a chiming mechanism. ++ ++There are twice as many kangaroos in Australia as there are people. The kangaroo population is estimated at about 40 million. ++ ++Police dogs are trained to react to commands in a foreign language; commonly German but more recently Hungarian. ++ ++The Australian $5 to $100 notes are made of plastic. ++ ++St. Stephen is the patron saint of bricklayers. ++ ++The average person makes about 1,140 telephone calls each year. ++ ++Stressed is Desserts spelled backwards. ++ ++If you had enough water to fill one million goldfish bowls, you could fill an entire stadium. ++ ++Mary Stuart became Queen of Scotland when she was only six days old. ++ ++Charlie Brown's father was a barber. ++ ++Flying from London to New York by Concord, due to the time zones crossed, you can arrive 2 hours before you leave. ++ ++Dentists have recommended that a toothbrush be kept at least 6 feet (2 m) away from a toilet to avoid airborne particles resulting from the flush. ++ ++You burn more calories sleeping than you do watching TV. ++ ++A lion's roar can be heard from five miles away. ++ ++The citrus soda 7-UP was created in 1929; \"7\" was selected because the original containers were 7 ounces. \"UP\" indicated the direction of the bubbles. ++ ++Canadian researchers have found that Einstein's brain was 15% wider than normal. ++ ++The average person spends about 2 years on the phone in a lifetime. ++ ++The fist product to have a bar code was Wrigleys gum. ++ ++The largest number of children born to one woman is recorded at 69. From 1725-1765, a Russian peasant woman gave birth to 16 sets of twins, 7 sets of triplets, and 4 sets of quadruplets. ++ ++Beatrix Potter created the first of her legendary \"Peter Rabbit\" children's stories in 1902. ++ ++In ancient Rome, it was considered a sign of leadership to be born with a crooked nose. ++ ++The word \"nerd\" was first coined by Dr. Seuss in \"If I Ran the Zoo.\" ++ ++A 41-gun salute is the traditional salute to a royal birth in Great Britain. ++ ++The bagpipe was originally made from the whole skin of a dead sheep. ++ ++The roar that we hear when we place a seashell next to our ear is not the ocean, but rather the sound of blood surging through the veins in the ear. Any cup-shaped object placed over the ear produces the same effect. ++ ++Revolvers cannot be silenced because of all the noisy gasses which escape the cylinder gap at the rear of the barrel. ++ ++Liberace Museum has a mirror-plated Rolls Royce; jewel-encrusted capes, and the largest rhinestone in the world, weighing 59 pounds and almost a foot in diameter. ++ ++A car that shifts manually gets 2 miles more per gallon of gas than a car with automatic shift. ++ ++Cats can hear ultrasound. ++ ++Dueling is legal in Paraguay as long as both parties are registered blood donors. ++ ++The highest point in Pennsylvania is lower than the lowest point in Colorado. ++ ++The United States has never lost a war in which mules were used. ++ ++Children grow faster in the springtime. ++ ++On average, there are 178 sesame seeds on each McDonalds BigMac bun. ++ ++Paul Revere rode on a horse that belonged to Deacon Larkin. ++ ++The Baby Ruth candy bar was actually named after Grover Cleveland's baby daughter, Ruth. ++ ++Minus 40 degrees Celsius is exactly the same as minus 40 degrees Fahrenheit. ++ ++Clans of long ago that wanted to get rid of unwanted people without killing them used to burn their houses down -- hence the expression \"to get fired\" ++ ++Nobody knows who built the Taj Mahal. The names of the architects, masons, and designers that have come down to us have all proved to be latter-day inventions, and there is no evidence to indicate who the real creators were. ++ ++Every human spent about half an hour as a single cell. ++ ++7.5 million toothpicks can be created from a cord of wood. ++ ++The plastic things on the end of shoelaces are called aglets. ++ ++A 41-gun salute is the traditional salute to a royal birth in Great Britain. ++ ++The earliest recorded case of a man giving up smoking was on April 5, 1679, when Johan Katsu, Sheriff of Turku, Finland, wrote in his diary \"I quit smoking tobacco.\" He died one month later. ++ ++\"Goodbye\" came from \"God bye\" which came from \"God be with you.\" ++ ++February is Black History Month. ++ ++Jane Barbie was the woman who did the voice recordings for the Bell System. ++ ++The first drive-in service station in the United States was opened by Gulf Oil Company - on December 1, 1913, in Pittsburgh, Pennsylvania. ++ ++The elephant is the only animal with 4 knees. ++ ++Kansas state law requires pedestrians crossing the highways at night to wear tail lights.";
        String jokes = "Q: In the Jewish doctrine, when does a fetus become a human? A: When it graduates from med school. Q: Why do Jewish men like to watch porno movies backward? A: They like the part where the hooker gives the money back. Q: What's the definition of a queer Jew? A: Someone that likes girls more than money. Q: What's the difference between a Catholic wife and a Jewish wife? A: A Catholic wife has real orgasms and fake jewelry. Q: What do you call someone from Israel that has to sneeze? A: A Jew Q: Why were gentiles invented? A: Somebody has to pay retail. Q: What do you call a potato that picks on Jews? A: a dicTATER. Q: Why don't people mug Jews on Yom Kippur? A: Dey fast. Q: Why do Jewish men have to be circumcised? A: Because a Jewish women wont touch anything unless it's 20% off Q: What is the difference between a crucifixion and a circumcision? A. In a crucifixion, they throw out the whole Jew. Q: Why do Jews have big noses? A: Because the air is free. Q: Did you hear about the Jewish troll? A: His name was Rumpled Foreskin. Q: Did you hear about the new tires, Firestein? A: They not only stop on a dime, they also pick it up! Q: What is a jews least favorite hotdog topping? A: Sauerkraut Q: How does a Jew celebrate Christmas? A: He installs a parking meter on the roof. Q: What would you call a bloodthirsty Jew on a rampage? A: Genghis Cohen. Q: Would you believe the Flinstones were Jewish? A: Yabba Dabba Jew! Q: What do you call a Jewish knight? A: Sir Cumsiced. Q: What Holiday does a Jewish car celebrate? A: Honk-in-ka Q: What aren't Jews in the Boy Scouts? A: Their parents refuse to send them to a camp. Q: What do you call a Jewish kid in a hat? A: Fedorable. Q: What do you call an Asian Jew? A: Jew Wa Lee (Julie) Q: Define: Genius A: A \"C\" student with a Jewish mother. Q: Who was the most well known Jewish cook? A: Hitler! Jewish people are the most optimistic people in the world. They have some cut off before they even know how big it will get. What's the difference between four Christians and four Jews? Fore-skins! Q: Did you hear about the new facility Kraft Foods is building in Israel? A: It's called \"Cheeses of Nazareth. Q: What is the proper blessing to recite before logging on to the Internet? A: \"Modem anachnu loch... Q: Why don't Jews trust Germans? A: Because the first time they did nazi that coming. Q: If a doctor carries a black leather bag and a plumber carries a box of tools, what does a mohel carry? A: A bris kit. Q: What do you call the steaks ordered by ten Jewish men? A: Fillet minyan. Q: What kind of cheese melts on a piece of matza to make a passover pizza? A: Matzarello Q: what does a Jewish pirate say? A: Ahoy vey! Q: How can you tell if someone is half Catholic and half Jewish? A: When he goes to confession, he takes a lawyer with him. Q: Where does Moshe hide money from his wife Sadie? A: Under the vacuum cleaner. Q: Did you hear about the new jewish tire coming out this summer? A: It not only stops you on a dime but it picks it up too. Q: A Jew walks in to a wall with a boner. What hits first? A: His nose";
        String[] jokesArr = jokes.split("Q:");
        String[] factsArr = facts.split(" ++ ++");
        allInsults = new ArrayList<>();
        allJokes = new ArrayList<>();
        allFacts = new ArrayList<>();
        Collections.addAll(allJokes, jokesArr);
        Collections.addAll(allFacts, factsArr);
        allInsults.add("Your momma is so fat, I took a picture of her last Christmas and it's still printing.");
        allInsults.add("Your momma is so fat when she got on the scale it said, \"I need your weight not your phone number.\"");
        allInsults.add("Your momma's so fat and old when God said, \"Let there be light,\" he asked your mother to move out of the way.");
        allInsults.add("Your momma is so fat that Dora can't even explore her!");
        allInsults.add("Your momma is so fat her bellybutton gets home 15 minutes before she does.");
        allInsults.add("Your momma is so stupid she brought a spoon to the super bowl.");
        allInsults.add("Your mamma is so fat she doesn't need the internet, because she's already world wide.");
        allInsults.add("Your momma is so fat that, when she fell in love, she broke it.");
        allInsults.add("Your momma is so old that she knew Burger King when he was a prince");
        allJokes.add("My friend thinks he is smart. He told me an onion is the only food that makes you cry, so I threw a coconut at his face.");
        allJokes.add("Q: Is Google male or female? \nA: Female, because it doesn't let you finish a sentence before making a suggestion.");
    }


    private static void addCalendarMeeting(Context context) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Calendar cal = Calendar.getInstance();
        values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis() + 60 * 60 * 1000);
        values.put(CalendarContract.Events.TITLE, "Jon's birthday!");
        values.put(CalendarContract.Events.DESCRIPTION, "Happy birthday to me!");
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        // default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.RRULE, "FREQ=YEARLY");
        //for one hour
        values.put(CalendarContract.Events.DURATION, "+P1H");
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        // insert event to calendar
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.calendar");
        context.startActivity(LaunchIntent);
    }

    public static String getPrimaryEmail(Context context) {
        try {
            AccountManager accountManager = AccountManager.get(context);
            if (accountManager == null)
                return "";
            Account[] accounts = accountManager.getAccounts();
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            for (Account account : accounts) {
                // make sure account.name is an email address before adding to the list
                if (emailPattern.matcher(account.name).matches()) {
                    return account.name.split("@")[0];
                }
            }
            return "";
        } catch (SecurityException e) {
            // exception will occur if app doesn't have GET_ACCOUNTS permission
            return "";
        }
    }

    private static boolean isPassOrPinSet(Context context)
    {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); //api 16+
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    public static double getDistanceFromLatLonInKm(double lat1,double lon1,double lat2,double lon2) {
        int R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }


    public static boolean checkScreenAndLock(Context context)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if(!pm.isInteractive() || isPassOrPinSet(context))
            return false;
        return true;
    }


    public static void fadingText(final Activity activity, final int viewID){
        final Animation animationFadeIn = AnimationUtils.loadAnimation(activity, R.anim.fadein);
        TextView welcome_text = (TextView) activity.findViewById(viewID);
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
        boolean isInstalled = settings.getBoolean(IS_INSTALLED, false);
        if(!isInstalled)
            welcome_text.setText("Hello "+ getPrimaryEmail(activity.getApplicationContext())+"\n I am Jon, your new friend");
        else
            welcome_text.setText("Welcome back "+ getPrimaryEmail(activity.getApplicationContext())+"!!");
        welcome_text.startAnimation(animationFadeIn);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                final Animation animationFadeOut = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fadeout);
                TextView  welcome_text = (TextView) activity.findViewById(viewID);
                welcome_text.startAnimation(animationFadeOut);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TextView  welcome_text = (TextView) activity.findViewById(viewID);
                        welcome_text.setText("");
                        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
                        boolean isInstalled = settings.getBoolean(IS_INSTALLED, false);
                        if(!isInstalled) {
                            takeScreenshot(activity,"I was born");
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean(IS_INSTALLED, true);
                            editor.commit();
                            addCalendarMeeting(activity.getApplicationContext());
                            activity.finish();
                        }
                    }
                }, 2000);
            }
        }, 5000);
    }

    public static Bitmap getImageBitmap(String name){
        try{
            File file = new File(name);
            if(file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(name, options);
                return bitmap;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static int throwRandom(int upper, int lower) {
        Random rand = new Random();
        return rand.nextInt(upper) + lower;
    }

    public static void popUpMessage(Context context, String text){
        int x = 1; //Mins
        Intent intent = new Intent(context, MyScheduledReceiver.class);
        intent.putExtra(MESSAGE_BOX_START_ACTIVITY,"MainActivity");
        intent.putExtra("START_TEXT",text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + (x * 1000),
                pendingIntent);

    }

    public static void takeScreenshot(Activity activity, String text) {

            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            saveMemory(bitmap,  text);
    }


    public static boolean createJonFolder(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),"Jon");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return false;
            }
        }
        Directory = mediaStorageDir.getAbsolutePath();
        return true;
    }


    public static void saveMemory(Bitmap bmp, String text){
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String mPathImage =Directory + "/" + now + ".jpg";
        String mPathData =Directory + "/" + now + ".txt";
        FileOutputStream outImage = null;
        FileOutputStream fileData = null;
        File file = new File(mPathData);
        try {
            fileData = new FileOutputStream(file);
            outImage = new FileOutputStream(mPathImage);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outImage); // bmp is your Bitmap instance
            fileData.write(text.getBytes());
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outImage != null) {
                    outImage.close();
                }
                if (fileData != null) {
                    fileData.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean allowToChangeFromChat(){
        java.util.Calendar c = java.util.Calendar.getInstance();
        int day = c.get(java.util.Calendar.DAY_OF_YEAR);
        if(day != SYSTEM_oldDay){
            SYSTEM_oldDay = day;
            SYSTEM_CURRENT_NUM_OF_CHATS_POINTS=1;
            return true;
        }else if(SYSTEM_CURRENT_NUM_OF_CHATS_POINTS<= SYSTEM_NUM_OF_CHATS_POINTS){
            SYSTEM_CURRENT_NUM_OF_CHATS_POINTS++;
            return true;
        }else{
            return false;
        }
    }
}
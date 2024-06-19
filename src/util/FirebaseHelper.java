package util;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseHelper {

	private static FirebaseHelper instance;
    private FirebaseDatabase database;

    private FirebaseHelper() {
        try {
            FileInputStream serviceAccount = new FileInputStream("resources/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                // .setDatabaseUrl("https://JetPackRiderDB.firebaseio.com/")
                .setDatabaseUrl("https://jetpackrider-b12a2-default-rtdb.firebaseio.com/")
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .build();

        
            FirebaseApp.initializeApp(options);
           
            database = FirebaseDatabase.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public void updateScore(String playerName, int score) {
       // DatabaseReference ref = database.getReference("scores").push();
   
        //ref.setValueAsync(new Score(playerName, score));
    	
    	database.getReference("scores").child("1").setValue(new Score(playerName, score), null);
        
        System.out.println("The score is updated");
    }
    
    public void getScore(String playerName, ScoreCallback callback) {
        DatabaseReference ref = database.getReference("scores").child("1");
        
        ref.addValueEventListener((ValueEventListener) new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Score score = dataSnapshot.getValue(Score.class);
                System.out.println("Score is " + score);
                callback.onCallback(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            	System.out.println(databaseError.getMessage());
                // Handle possible errors.
            }

		
        });
    }
}

  


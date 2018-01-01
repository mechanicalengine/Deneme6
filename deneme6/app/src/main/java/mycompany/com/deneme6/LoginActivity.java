package mycompany.com.deneme6;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import mycompany.com.deneme6.adapter.BuildingAdapter;
import mycompany.com.deneme6.model.Building;

public class LoginActivity extends AppCompatActivity implements ItemClickListener, View.OnClickListener {
    //public abstract class LoginActivity extends AppCompatActivity implements ItemClickListener, View.OnClickListener {

    public static final String BUILDING_NAME = "mycompany.com.deneme6.customername";
    public static final String BUILDING_ID = "mycompany.com.deneme6.customerid";

    private Button buttonLogOut;
    //private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private EditText editText;
    private Button buttonAddBuilding;
    private DatabaseReference databaseReferenceBuildings;
    private RecyclerView recyclerViewBuildings;
    private RecyclerView.LayoutManager recyclerViewLayoutManagerBuildings;
    private BuildingAdapter buildingAdapter;
    private List<Building> buildings;
    private Spinner spinnerAddress;


    @Override
    protected void onStart() {
        super.onStart();
        databaseReferenceBuildings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                buildings.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Building customer = postSnapshot.getValue(Building.class);
                    buildings.add(customer);
                }


                recyclerViewBuildings.setHasFixedSize(true);

                // use a linear layout manager
                recyclerViewLayoutManagerBuildings = new LinearLayoutManager(LoginActivity.this);
                recyclerViewBuildings.setLayoutManager(recyclerViewLayoutManagerBuildings);

                buildingAdapter = new BuildingAdapter(LoginActivity.this, buildings);
                buildingAdapter.setClickListener(LoginActivity.this);
                recyclerViewBuildings.setAdapter(buildingAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


   @Override
    public void onClick(View v, int adapterPosition) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);
        buttonAddBuilding = (Button) findViewById(R.id.buttonAddBuilding);
        //progressDialog = new ProgressDialog(this);
        spinnerAddress = (Spinner)findViewById(R.id.spinnerArray);
        recyclerViewBuildings = (RecyclerView)findViewById(R.id.recyclerViewBuilding);

        firebaseAuth = FirebaseAuth.getInstance();
        editText = (EditText)findViewById(R.id.editText2);

        databaseReferenceBuildings = FirebaseDatabase.getInstance().getReference("buildings");

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogOut.setOnClickListener(this);

        buttonAddBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBuilding();
            }
        });
    }

    private void addBuilding() {

        if(editText.getText().length()>0){

            String id = databaseReferenceBuildings.push().getKey();
            Building building = new Building(id,editText.getText().toString(),spinnerAddress.getSelectedItem().toString());
            //Building building = new Building(id,editText.getText().toString());
            databaseReferenceBuildings.child(id).setValue(building);
            editText.setText("");
            Toast.makeText(getApplicationContext(),"Yeni Bina Eklendi",Toast.LENGTH_SHORT).show();

        }else {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Hata");
        builder.setMessage("Lütfen Bina Adını Giriniz!");

        String positiveText = "TAMAM";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

  /*  @Override
    public void onClick(View view, int position) {
        Building building = buildings.get(position);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra(BUILDING_ID, building.getBuildingId());
        intent.putExtra(BUILDING_NAME, building.getBuildingName());
        startActivity(intent);
    }
*/
    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogOut){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
    }




}

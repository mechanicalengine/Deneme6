package mycompany.com.deneme6.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import mycompany.com.deneme6.ItemClickListener;
import mycompany.com.deneme6.R;
import mycompany.com.deneme6.model.Building;

/**
 * Created by ugur on 28-Dec-17.
 */

//public class BuildingAdapter {
//}


public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    List<Building> buildingList;
    Context context;
    private ItemClickListener clickListener;

    public BuildingAdapter(Context context, List<Building> buildingList) {

        this.buildingList = buildingList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView buildingName;
        public TextView buildingAddress;
        public ImageView buildingPhoto;
        public ImageView optionMenu;


        public ViewHolder(View view) {
            super(view);

            buildingName = (TextView)view.findViewById(R.id.buildingName);
            buildingAddress = (TextView)view.findViewById(R.id.buildingAddress);
            buildingPhoto = (ImageView)view.findViewById(R.id.buildingPhoto);
            optionMenu = (ImageView)view.findViewById(R.id.optionMenu);
            view.setTag(view);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }

    @Override
    public BuildingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buildingadapter, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.buildingName.setText(buildingList.get(position).getBuildingName());
        holder.buildingAddress.setText(buildingList.get(position).getBuildingAddress());
        holder.buildingPhoto.setImageResource(R.drawable.user);
        holder.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.optionMenu);
                popup.inflate(R.menu.menu_items);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                //update customer
                                updateBuilding(buildingList.get(position));
                                break;
                            case R.id.delete:
                                //delete customer
                                deleteBuilding(buildingList.get(position).getBuildingId());
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }

    private Building updateBuilding;
    private void updateBuilding(final Building building) {

        updateBuilding = building;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.update_building, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextBuildingName);
        final Spinner spinnerUpdate = (Spinner) dialogView.findViewById(R.id.spinnerUpdate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateCustomer);

        editTextName.setText(updateBuilding.getBuildingName());

        dialogBuilder.setTitle("Bina Düzenleme");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextName.getText().length()>0){

                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference("buildings").child(updateBuilding.getBuildingId());
                    Building building = new Building(updateBuilding.getBuildingId(),editTextName.getText().toString(),spinnerUpdate.getSelectedItem().toString());
                   // Building building = new Building(updateBuilding.getBuildingId(),editTextName.getText().toString());
                    dR.setValue(building);
                    b.dismiss();
                    Toast.makeText(context, "Bina Güncellendi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteBuilding(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("buildings").child(id);
        dR.removeValue();
        DatabaseReference drRooms = FirebaseDatabase.getInstance().getReference("rooms").child(id);
        drRooms.removeValue();
        Toast.makeText(context ,"Müşteri Silindi", Toast.LENGTH_SHORT).show();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }
}

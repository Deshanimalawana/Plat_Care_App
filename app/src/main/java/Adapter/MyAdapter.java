package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantcareapp.DBHelper;
import com.example.plantcareapp.R;
import com.example.plantcareapp.UpdateDetails;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {

    private Context context;
    private ArrayList plant_id,plant_name,plant_desc;
    private ArrayList<byte[]> plant_images;
    int position;

    public MyAdapter(Context context, ArrayList plant_id, ArrayList plant_name, ArrayList plant_desc, ArrayList<byte[]> plant_images) {
        this.context = context;
        this.plant_id = plant_id;
        this.plant_name = plant_name;
        this.plant_desc = plant_desc;
        this.plant_images = plant_images;
    }

    @NonNull
    @Override
    public MyAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.plant_id.setText(String.valueOf(plant_id.get(position)));
        holder.plant_name.setText(String.valueOf(plant_name.get(position)));
        holder.plant_desc.setText(String.valueOf(plant_desc.get(position)));

        byte[] imageBytes = plant_images.get(position);
        if (imageBytes != null && imageBytes.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.plant_images.setImageBitmap(bitmap);
        } else {
            holder.plant_images.setImageResource(R.drawable.default_image);
        }


        this.position = position;

        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateDetails.class);
                intent.putExtra("id",String.valueOf(plant_id.get(position)));
                intent.putExtra("name",String.valueOf(plant_name.get(position)));
                intent.putExtra("desc",String.valueOf(plant_desc.get(position)));
                intent.putExtra("image",String.valueOf(plant_images.get(position)));
                context.startActivity(intent);
            }
        });

        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return plant_id.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        public TextView plant_id, plant_name, plant_desc;
        LinearLayout edit_layout;
        ImageView delete_layout;
        ImageView plant_images;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            plant_id = (TextView) itemView.findViewById(R.id.plant_id);
            plant_name = (TextView) itemView.findViewById(R.id.plant_name);
            plant_desc = (TextView) itemView.findViewById(R.id.plant_desc);
            delete_layout = (ImageView) itemView.findViewById(R.id.delete_layout);
            plant_images = (ImageView) itemView.findViewById(R.id.plant_images);

            edit_layout = (LinearLayout) itemView.findViewById(R.id.edit_Layout);
        }
    }

    public void deleteItem(int position) {
        int id = Integer.parseInt(String.valueOf(plant_id.get(position)));

        DBHelper dbHelper = new DBHelper(context);
        dbHelper.deletePlant(id);
        plant_id.remove(position);
        plant_name.remove(position);
        plant_desc.remove(position);
        plant_images.remove(position);
        notifyItemRemoved(position);
        dbHelper.close();
    }
}

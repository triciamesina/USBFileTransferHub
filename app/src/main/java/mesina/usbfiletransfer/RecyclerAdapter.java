package mesina.usbfiletransfer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Tricia on 3/17/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    // Declare Array Lists
    private ArrayList<selection> arrayList;

    // Reference views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView filenameTextView;
        public TextView destinationTextView;

        //Constructor that accepts both filename and destination in a single row
        public ViewHolder(View itemView) {
            super(itemView);

            filenameTextView = (TextView) itemView.findViewById(R.id.filename);
            destinationTextView = (TextView) itemView.findViewById(R.id.destination);

        }


    }

    // Pass selection array into constructor
    public RecyclerAdapter(ArrayList<selection> selection) {
        arrayList = selection;
    }

    // Inflate the layout and return the holder
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        // Inflate custom layout
        View recyclerView = inflater.inflate(R.layout.files_row_layout, parent, false);

        // Return view holder
        ViewHolder viewHolder = new ViewHolder(recyclerView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {

        // Get data model based on position
        selection selection = arrayList.get(position);

        // Set item views used on data model
        TextView filenameTextView = viewHolder.filenameTextView;
        filenameTextView.setText(selection.getFilename());

        TextView destinationTextView = viewHolder.destinationTextView;
        destinationTextView.setText(selection.getDestination());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

package mesina.usbfiletransfer;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tricia on 3/17/2016.
 */
public class RenameAdapter extends RecyclerView.Adapter<RenameAdapter.mViewHolder> {

    private ArrayList<String> dirFiles;
    AdapterView.OnItemClickListener mItemClickListener;
    private int selectedPos = 0;

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_rename_layout, parent, false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        String filename = dirFiles.get(position);
        holder.filename.setText(filename);

    }

    @Override
    public int getItemCount() {
        return dirFiles.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        public TextView filename;

        public mViewHolder(View view) {

            super(view);
            filename = (TextView) view.findViewById(R.id.file);
        }

    }

    public RenameAdapter(ArrayList<String> dirFiles) {
        this.dirFiles = dirFiles;
    }

}

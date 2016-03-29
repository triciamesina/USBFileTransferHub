package mesina.usbfiletransfer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tricia on 3/17/2016.
 */
public class DirectoryLongClickAdapter extends RecyclerView.Adapter<DirectoryLongClickAdapter.mViewHolder> {

    private ArrayList<String> dirFiles;
    AdapterView.OnItemClickListener mItemClickListener;
    private static int index;
    static Context context;
    ContextMenu.ContextMenuInfo info;
    private static final String TAG = "thebluetooth";
    private static final int TAB_FRAGMENT = 4;

    public static int getPosition() {
        return index;
    }

    public void setPosition(int position) {
        this.index = position;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.files_directory_layout, parent, false);
        return new mViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        final String filename = dirFiles.get(position);
        holder.filename.setText(filename);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dirFiles.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView filename;

        public mViewHolder(final View view) {

            super(view);
            filename = (TextView) view.findViewById(R.id.file);
          //  view.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Choose the operation");
            menu.add(TAB_FRAGMENT, R.id.action_transfer, 0, "Transfer");
            menu.add(TAB_FRAGMENT, R.id.action_delete, 0, "Delete");
            menu.add(TAB_FRAGMENT, R.id.action_rename, 0, "Rename");
        }

        /*
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("position", index);
                    context.startActivity(intent);
                    return false;
                }
        */    }

        @Override
        public void onViewRecycled(mViewHolder holder) {
            holder.itemView.setOnLongClickListener(null);
            super.onViewRecycled(holder);
        }

        public DirectoryLongClickAdapter(ArrayList<String> dirFiles) {
            this.dirFiles = dirFiles;
        }

    }

package com.example.videomarker.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videomarker.Listener.ClickListener;
import com.example.videomarker.R;
import com.example.videomarker.activity.InfoActivity;
import com.example.videomarker.data.entities.Data;
import com.example.videomarker.data.util.ContentLoader;
import com.example.videomarker.data.util.FileUtil;
import com.example.videomarker.holder.Holder;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<Holder> implements ClickListener {

    private List<Data> datas;
    public final Context context;
    private int id;

    public RecyclerAdapter(List<Data> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        Data data = datas.get(position);
        holder.setId(String.valueOf(data.getResId()));
        holder.setName(data.getName());
        holder.setDur(data.getDur());
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemLongClick(v,position);
            }
        });
        holder.btnMore.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onItemLongClick(View v, int position) {
        List<Data> datas = ContentLoader.getContent(context);

        id = datas.get(position).getResId();
        PopupMenu p = new PopupMenu(context, v);
        MenuInflater inflater = p.getMenuInflater();
        Menu menu = p.getMenu();
        inflater.inflate(R.menu.popup_menu, menu);

        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.pPlay:
                        break;
                    case R.id.pInfo:
                        Intent intent = new Intent(context, InfoActivity.class);
                        intent.putExtra("ID", id);
                        context.startActivity(intent);
                        break;
                    case R.id.pAddpl:
                        break;
                    case R.id.pDel:

                        break;
                }
                return false;
            }
        });
        p.show();
    }
}

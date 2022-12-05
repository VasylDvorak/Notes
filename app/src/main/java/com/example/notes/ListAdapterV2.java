package com.example.notes;

import static com.example.notes.MainActivity.notes_text_color;
import static com.example.notes.MainActivity.notes_text_color_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapterV2 extends RecyclerView.Adapter<ListAdapterV2.ViewHolder> {

    CardsSource dateSource;
    private OnItemClickListener itemClickListener;
    private final Fragment fragment;
    private int menuPosition;

    public ListAdapterV2(CardsSource dateSource, Fragment fragment) {
        this.dateSource = dateSource;
        this.fragment = fragment;
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    private void registerContextMenu(View itemView) {
        if (fragment != null) {
            fragment.registerForContextMenu(itemView);
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(dateSource.getCardData(position));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_v2, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return dateSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        // private final TextView description;
        private final AppCompatImageView image;
        private final CheckBox like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            // description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.imageView);
            like = itemView.findViewById(R.id.like);
            registerContextMenu(itemView);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(view, position);
                }
            });
            image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(15, 15);

                    return true;
                }
            });
        }

        public void setData(CardData cardData) {
            title.setText(cardData.getTitle());
            title.setId(cardData.getId());
            if (notes_text_color == 0) notes_text_color = notes_text_color_first;
            title.setTextColor(notes_text_color);
            //    description.setText(cardData.getDescription());
            like.setChecked(cardData.isLike());
            image.setImageResource(cardData.getPicture());

        }
    }

}




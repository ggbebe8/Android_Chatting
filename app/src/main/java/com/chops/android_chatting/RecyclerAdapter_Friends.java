package com.chops.android_chatting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter_Friends extends RecyclerView.Adapter<RecyclerAdapter_Friends.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    public ArrayList<Data_Friends> listData = new ArrayList<>();

    //커스텀 리스너 만들기 (객체마다 클릭 이벤트를 주기 위함)
    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friendlist, parent, false);



        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(Data_Friends data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivFriendProfile;
        private TextView tvFriendEmail;
        private Button btnFriendChat;

        ItemViewHolder(View itemView) {
            super(itemView);

            ivFriendProfile = itemView.findViewById(R.id.ivFriendProfile);
            tvFriendEmail = itemView.findViewById(R.id.tvFriendEmail);
            btnFriendChat = itemView.findViewById(R.id.btnFriendChat);

            btnFriendChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ipos = getAdapterPosition();
                    if (ipos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, ipos);
                        //String strEmail = listData.get(ipos).mstrFriendEmail;
                    }
                }
            });

            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ipos = getAdapterPosition();
                    if (ipos != RecyclerView.NO_POSITION)
                    {
                        mListener.onItemClick(v, ipos);
                        //String strEmail = listData.get(ipos).mstrFriendEmail;
                    }
                }
            });

             */
        }

        void onBind(Data_Friends data) {
            try {
                if(data.mbitFriendProfile == null)
                    ivFriendProfile.setImageResource(R.drawable.img_android);
                else
                    ivFriendProfile.setImageBitmap(data.mbitFriendProfile);
            }
            catch (Exception e) {
                ivFriendProfile.setImageResource(R.drawable.img_android);
            }

            tvFriendEmail.setText(data.mstrFriendEmail);
        }
    }
}


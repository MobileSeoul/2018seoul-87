package com.hippo.jun.weandseoul;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by JUN on 2018-08-03.
 */

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<RecyclerItem> recyclerItems;
    private Context mContext;
    private Intent mIntent;

    //ViewHolder 클래스에서는 사용할 UI Element를 초기화 시켜준다.
    //이 클래스가 RecyclerView 의 행(row)를 표시하는 클래스이다.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView mName, mContent, mPlace, mNumber;

        ViewHolder(View view){
            super(view);
            cardView = view.findViewById(R.id.cardView);
            mName = view.findViewById(R.id.name);
            mContent = view.findViewById(R.id.content);
            mPlace = view.findViewById(R.id.place);
            mNumber = view.findViewById(R.id.number);
        }
    }
    //생성자
    public RecyclerAdapter2 (Context context, ArrayList<RecyclerItem> recyclerItems) {
        mContext = context;
        this.recyclerItems = recyclerItems;
    }
    public RecyclerAdapter2 (Context context, ArrayList<RecyclerItem> recyclerItems, Intent intent) {
        mContext = context;
        this.recyclerItems = recyclerItems;
        this.mIntent = intent;
    }

    //onCreateViewHolder()함수는 RecyclerView의 행을 표시하는데 사용되는 레이아웃 xml을 가져오는 역할을 함.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design2, parent, false);

        return new ViewHolder(v);
    }

    //onBindViewHolder()함수에서 RecyclerView 의 행에 보여질 ImageView 와 TextView 를 설정합니다.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

//-------예시 코드 --------------------------------------------------------------------------------------------
//       holder.imageView.setImageResource(arrayList.get(position).getImg_id()); <----BEFORE
//       Glide.with(context).load(arrayList.get(position).getImg_id()).into(holder.imageView); <---AFTER
//------------------------------------------------------------------------------------------------------------
        final ViewHolder viewHolder = (ViewHolder) holder;

        //텍스트뷰에 출력할 텍스트 설정
        viewHolder.mName.setText(recyclerItems.get(position).mName);
        viewHolder.mContent.setText(recyclerItems.get(position).mContent);
        viewHolder.mNumber.setText(recyclerItems.get(position).mNumber);
        viewHolder.mPlace.setText(recyclerItems.get(position).mPlace);

        //인텐트 정보가 있는경우
        if(mIntent != null){
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIntent.putExtra("name", recyclerItems.get(position).mName);
                    mIntent.putExtra("content", recyclerItems.get(position).mContent);
                    mIntent.putExtra("tel", recyclerItems.get(position).mTel);
                    mIntent.putExtra("date", recyclerItems.get(position).mDate);
                    mIntent.putExtra("place", recyclerItems.get(position).mPlace);
                    mIntent.putExtra("number", recyclerItems.get(position).mNumber);
                    mIntent.putExtra("position", recyclerItems.get(position).mPosition);
                    v.getContext().startActivity(mIntent);
                }
            });
        }
    }

    //getItemCount()함수는 RecyclerView 의 행 갯수를 리턴하면 됩니다.
    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    public void refreshAdapter(){
        notifyDataSetChanged();
    }
}

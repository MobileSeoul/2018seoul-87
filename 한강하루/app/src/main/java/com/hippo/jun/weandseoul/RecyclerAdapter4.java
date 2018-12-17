package com.hippo.jun.weandseoul;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by JUN on 2018-08-03.
 */

public class RecyclerAdapter4 extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private ArrayList<RecyclerItem> recyclerItems;
    private Context mContext;
    private Intent mIntent;

    //ViewHolder 클래스에서는 사용할 UI Element를 초기화 시켜준다.
    //이 클래스가 RecyclerView 의 행(row)를 표시하는 클래스이다.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImg;
        TextView cardTxt;
        CardView cardView;

        ViewHolder(View view){
            super(view);
            cardImg = view.findViewById(R.id.image);
            cardTxt = view.findViewById(R.id.name);
            cardView = view.findViewById(R.id.cardView);
        }
    }
    //생성자
    public RecyclerAdapter4(Context context, ArrayList<RecyclerItem> recyclerItems) {
        this.mContext = context;
        this.recyclerItems = recyclerItems;
    }
    public RecyclerAdapter4(Context context, ArrayList<RecyclerItem> recyclerItems, Intent intent) {
        this.mContext = context;
        this.recyclerItems = recyclerItems;
        this.mIntent = intent;
    }

    //onCreateViewHolder()함수는 RecyclerView의 행을 표시하는데 사용되는 레이아웃 xml을 가져오는 역할을 함.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design4, parent, false);

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

        //Glide 활용하여 서버 이미지 가져오기
//        Glide.with(mContext).load(recyclerItems.get(position).url).fitCenter().into(viewHolder.cardImg);

        //이름, 전화번호, 이용정보, x 좌표, y 좌표를 리사이클러에서 받아온 값으로 지정
        viewHolder.cardTxt.setText(recyclerItems.get(position).bName);

        if(mIntent != null){
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIntent.putExtra("name", recyclerItems.get(position).bName);
                    mIntent.putExtra("fee", recyclerItems.get(position).bFee);
                    mIntent.putExtra("info", recyclerItems.get(position).bInfo);
                    mIntent.putExtra("tel", recyclerItems.get(position).bTel);
                    mIntent.putExtra("bicycle", recyclerItems.get(position).bBicycle);
                    mIntent.putExtra("addr", recyclerItems.get(position).bAddr);
                    mIntent.putExtra("xcode", recyclerItems.get(position).bXcode);
                    mIntent.putExtra("ycode", recyclerItems.get(position).bYcode);

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
}

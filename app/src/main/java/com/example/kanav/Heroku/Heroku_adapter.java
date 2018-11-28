package com.example.kanav.Heroku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class Heroku_adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<user> users;
    int width;
    Heroku_adapter(List<user> users, Context context, int width)
    {
        this.context=context;
      this.users=users;
       this.width=width;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v;
            if(viewType==0) {
                // for setting layout for even rows
                 v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_even_row, parent, false);
                UserViewHolder bvh = new UserViewHolder(v);
                return bvh;
            }
            else {

                // for setting layout for odd rows
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_odd_row, parent, false);
                UserViewHolder1 bvh = new UserViewHolder1(v);
                return bvh;
            }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType==0) {
            UserViewHolder uvh=(UserViewHolder)holder;
            uvh.userName.setText(users.get(position).getUserName());
            Log.d("image", users.get(position).getDp_url());
            Picasso.with(context).load("https"+users.get(position).getDp_url().substring(4)).resize(150, 150).transform(new Transform()).placeholder(R.drawable.iconssmall).into(uvh.mImageView);
            GridLayoutManager llm = new GridLayoutManager(context, 2);
            uvh.pics.setLayoutManager(llm);
            List<String> a = new ArrayList<>();
            a = users.get(position).getPics_urls();
            uvh.pics.setAdapter(new Heroku_photos_adapter(a, context, width));
        }
        else {
            UserViewHolder1 uvh=(UserViewHolder1)holder;
            uvh.userName.setText(users.get(position).getUserName());
            Log.d("image", "https"+users.get(position).getDp_url().substring(4));
            String u="https"+users.get(position).getDp_url().substring(4);
            uvh.pics1.setMinimumWidth(width);
            Picasso.with(context).load(u).resize(150, 150).transform(new Transform()).placeholder(R.drawable.iconssmall).into(uvh.dp);
            Picasso.with(context).load("https"+users.get(position).getPics_urls().get(0).substring(4,23)+width/2+"/120"+users.get(position).getPics_urls().get(0).substring(30)).resize(width, width).transform(new RoundTransformation(15,0)).placeholder(R.drawable.iconssmall).into(uvh.pics1);
            GridLayoutManager llm = new GridLayoutManager(context, 2);
            uvh.pics.setLayoutManager(llm);
            List<String> a = new ArrayList<>();
            List<String> b = new ArrayList<>();
            a = users.get(position).getPics_urls();
            for(int i=1;i<a.size();i++)
                b.add(a.get(i));
            uvh.pics.setAdapter(new Heroku_photos_adapter(b, context, width));
        }
    }




    @Override
    public int getItemCount()
    {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (users.get(position).getPics_urls().size()%2==0) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView mImageView;
        RecyclerView pics;


        UserViewHolder(View itemView) {
            super(itemView);
            userName = (TextView)itemView.findViewById(R.id.userName);
            mImageView =(ImageView)itemView.findViewById(R.id.displayPic);
            pics=(RecyclerView)itemView.findViewById(R.id.Images);

        }
    }
    public static class UserViewHolder1 extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView dp,pics1;
        RecyclerView pics;


        UserViewHolder1(View itemView) {
            super(itemView);
            userName = (TextView)itemView.findViewById(R.id.userName);
            dp=(ImageView)itemView.findViewById(R.id.displayPic);
            pics=(RecyclerView)itemView.findViewById(R.id.Images);
            pics1=(ImageView)itemView.findViewById(R.id.pic1);


        }
    }
    public class Transform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
    public class RoundTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;  // mImageView

        // radius is corner radii in mImageView
        // margin is the board in mImageView
        public RoundTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }

            return output;
        }

        @Override
        public String key() {
            return "round";
        }
    }

    public void addAll(List<user> usersList) {
        for (user users2 : usersList) {
            add(users2);
        }
    }



    private void add(user user1) {
        users.add(user1);
        notifyItemInserted(users.size() - 1);
    }


}

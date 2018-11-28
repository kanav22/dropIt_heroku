package com.example.kanav.Heroku;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Heroku_photos_adapter extends RecyclerView.Adapter<Heroku_photos_adapter.UserViewHolder> {
    Context context;
    List<String> users;
    int width;

    Heroku_photos_adapter(List<String> pics, Context context, int width)
    {
        this.context=context;
        this.users=pics;
        this.width=width;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_heroku, parent, false);
        UserViewHolder bvh = new UserViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.gallery.setMinimumWidth(width/2);
        Picasso.with(context).load("https"+users.get(position).substring(4,23)+width/2+"/120"+users.get(position).substring(30)).resize(width/2,width/2).transform(new RoundedTransformation(15,0)).placeholder(R.drawable.iconssmall).into(holder.gallery);

    }


    @Override
    public int getItemCount()
    {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView gallery;



        UserViewHolder(View itemView) {
            super(itemView);

            gallery=(ImageView)itemView.findViewById(R.id.gallery);


        }
    }
    public class RoundedTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;

        public RoundedTransformation(final int radius, final int margin) {
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
            return "rounded";
        }
    }

}

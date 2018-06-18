package postcard.card.post;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Wall_post_RecyclerAdapter extends RecyclerView.Adapter<Wall_post_RecyclerAdapter.ViewHolder>{

    Context context;

    //Member Variable so can used in bellow methods
    List<Wall_post> wallPosts_list;

    //retriving data from wall post model
    public Wall_post_RecyclerAdapter(List<Wall_post> wallPosts_list){

        //data receved from Home fragment & assign it to wallPosts_list
        this.wallPosts_list = wallPosts_list;
    }

    //Required for our Adapter
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //to infiltrate the wallpost_list_items Layout view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpost_list_items,parent,false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    //Reguired for our Adapter
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //receiving post text
        String post_data = wallPosts_list.get(position).getPost_text();
        //setting the received data from post_dat to setuserPostDescText
        holder.setuserPostDescText(post_data);
        //reciving the post image url
        String post_image_url = wallPosts_list.get(position).getImage_uri();
        holder.setUserPostImage(post_image_url);

    }
    //Required for our Adapter
    //GetItemCount will populate  no of items in the Recycler Adapter
    @Override
    public int getItemCount() {
        //wall post.size we need to mention how many items we want to populate
        return wallPosts_list.size();
    }

    ////Required for our ViewHolder to set the view to the wallpost_list_items.xml layout
    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        TextView userPostDesc;
        ImageView userPostImage;

        ///used to set view holder
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        //received text data
        public void setuserPostDescText(String userDesctext){
            userPostDesc = mView.findViewById(R.id.postDesc);
            userPostDesc.setText(userDesctext);


        }

        public void setUserPostImage(String downloadUrl){
            userPostImage = mView.findViewById(R.id.postImage);
            Glide.with(context).load(downloadUrl).into(userPostImage);

        }


    }



}

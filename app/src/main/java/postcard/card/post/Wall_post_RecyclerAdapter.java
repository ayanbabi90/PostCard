package postcard.card.post;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Wall_post_RecyclerAdapter extends RecyclerView.Adapter<Wall_post_RecyclerAdapter.ViewHolder>{

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

        return new ViewHolder(view);
    }

    //Reguired for our Adapter
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //receiving items
        String post_data = wallPosts_list.get(position).getPost_text();

        //setting the received data from post_dat to setuserPostDescText
        holder.setuserPostDescText(post_data);




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
    }



}

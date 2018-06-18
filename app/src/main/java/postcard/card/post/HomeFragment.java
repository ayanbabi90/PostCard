package postcard.card.post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView wall_post_list_view;
    private List<Wall_post> wallPosts_list;
    FirebaseFirestore firebaseFirestore;
    private Wall_post_RecyclerAdapter wallpostRecyclerAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //to infiltrator home fragment
        View view = inflater.inflate(R.layout.fragment_home, container ,false);
        //
        wallPosts_list = new ArrayList<>();

        wall_post_list_view = view.findViewById(R.id.wall_post_list_view);

        //initialised the adapter and pass the List type data wallPosts_list
        wallpostRecyclerAdapter = new Wall_post_RecyclerAdapter(wallPosts_list);
        //
        wall_post_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //setting the adapter
        wall_post_list_view.setAdapter(wallpostRecyclerAdapter);
        wall_post_list_view.setHasFixedSize(true);

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Snapshot listener help us to retrive the data in real time
        firebaseFirestore.collection("user_post").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                //check for any changes in document
                for (DocumentChange doc: documentSnapshots.getDocumentChanges()){

                    //check if document get added
                    if (doc.getType() == DocumentChange.Type.ADDED){

                        Wall_post wall_post = doc.getDocument().toObject(Wall_post.class);
                        wallPosts_list.add(wall_post);

                        wallpostRecyclerAdapter.notifyDataSetChanged();

                    }

                }


            }
        });


        return view ;
    }


}

package com.example.etp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.ViewCommentHolder> {
    Context mContext;
    ArrayList<String> mCommentsList;

    public RecyclerCommentAdapter(Context mContext, ArrayList<String> mCommentsList) {
        this.mContext = mContext;
        this.mCommentsList = mCommentsList;
    }

    @NonNull
    @Override
    public ViewCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        ViewCommentHolder mViewCommentHolder = new ViewCommentHolder(view);
        return mViewCommentHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewCommentHolder holder, int position) {
        DatabaseReference mCommentRaw = FirebaseDatabase.getInstance().getReference("Comments").child(mCommentsList.get(position));
        mCommentRaw.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.mUserComment.setText(dataSnapshot.child("CommentContent").getValue(String.class));
                DatabaseReference mUserRaw = FirebaseDatabase.getInstance().getReference("Users").child(dataSnapshot.child("CommentUserId").getValue(String.class));
                mUserRaw.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot DS) {
                        holder.mUserName.setText(DS.child("userName").getValue(String.class));

                        if (DS.hasChild("userImage")) {
                            if (DS.child("userImage").getValue(String.class).contains("gs://test-b8daf.appspot.com/UserImage")) {
                                FirebaseStorage mStorage = FirebaseStorage.getInstance();
                                StorageReference mImageReference = mStorage.getReferenceFromUrl(DS.child("userImage").getValue(String.class));
                                mImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.with(mContext).load(uri).fit().into(holder.mUserImage);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                    }
                                });
                            }
                            else {
                                Picasso.with(mContext).load(DS.child("userImage").getValue(String.class)).fit().into(holder.mUserImage);
                            }
                        }
                        else {
                            holder.mUserImage.setImageResource(R.drawable.ic_person);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError DE) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public class ViewCommentHolder extends RecyclerView.ViewHolder{
        ImageView mUserImage;
        TextView mUserName, mUserComment;

        public ViewCommentHolder(View itemView){
            super(itemView);
            mUserImage = itemView.findViewById(R.id.charImg);
            mUserName = itemView.findViewById(R.id.who_comment_this);
            mUserComment = itemView.findViewById(R.id.his_comment);
        }
    }
}

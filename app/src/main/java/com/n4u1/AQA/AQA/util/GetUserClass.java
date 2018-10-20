package com.n4u1.AQA.AQA.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetUserClass {


//    private int getUserClass() {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//        String uId = mUser.getUid();
//        mDatabase.child("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                return user.getUserClass();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return 0;
//    }


    int userClass;
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    String uId = mUser.getUid();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uId);



    public DatabaseReference getmDatabase() {
        return mDatabase;
    }
}

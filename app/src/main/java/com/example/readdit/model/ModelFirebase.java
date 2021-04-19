package com.example.readdit.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.BoringLayout;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.readdit.ReadditApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.readdit.ReadditApplication.REVIEWS_COLLECTION;

public class ModelFirebase {
    public static FirebaseAuth mAuth;

    public ModelFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    // region Image functions
    public void uploadImage(Bitmap imageBmp, String folder, String name, Model.AsyncListener<String> listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child(folder).child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }

    public void deleteImage(String name, Model.AsyncListener<Boolean> listener, String folder) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child(folder).child(name);
        imagesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(false);
            }
        });
    }
    // endregion

    // region User functions
    public void addUser(User user, Model.AsyncListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(ReadditApplication.USERS_COLLECTION)
                .document(String.valueOf(user.getUserID()))
                .set(user.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener != null) {
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
    }

    public void getAllUsers(Long lastUpdated, Model.AsyncListener<List<User>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection(ReadditApplication.USERS_COLLECTION)
                .whereGreaterThanOrEqualTo("lastUpdated", ts)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<User> data = new ArrayList<User>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                User user = new User();
                                data.add(user.fromMap(document.getData()));
                            }
                        }

                        listener.onComplete(data);
                    }
                });
    }

    public static String getCurrentUserID() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        }

        return null;
    }
    // endregion

    // region Reviews functions
    public void addReview(Review review, Model.AsyncListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(REVIEWS_COLLECTION)
                .add(review.toMap())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        if(listener != null){
                            listener.onComplete(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(listener != null){
                            listener.onComplete(false);
                        }
                    }
                });

    }

    public void editReview(Review review, Model.AsyncListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(REVIEWS_COLLECTION)
                .document(String.valueOf(review.getId()))
                .set(review.toMap())
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        if(listener != null){
                            listener.onComplete(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(listener != null){
                            listener.onComplete(false);
                        }
                    }
                });
    }

    public void getAllReviews(Long lastUpdated, Model.AsyncListener<List<Review>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection(REVIEWS_COLLECTION)
                .whereGreaterThanOrEqualTo("lastUpdated", ts)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Review> data = new ArrayList<Review>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Review review = new Review();
                                review.fromMap(document.getData());
                                review.setId(document.getId());
                                data.add(review);
                            }
                        }

                        listener.onComplete(data);
                    }
                });
    }

    public void getReview(String id, Model.AsyncListener<Review> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(REVIEWS_COLLECTION).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Review review = null;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        review = new Review();
                        review.fromMap(task.getResult().getData());
                    }
                }
                listener.onComplete(review);
            }
        });
    }
    // endregion
}

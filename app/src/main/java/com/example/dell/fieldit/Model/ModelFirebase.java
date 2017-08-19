package com.example.dell.fieldit.Model;

/**
 * Created by dell on 12/08/2017.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.Query;
import java.util.LinkedList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class ModelFirebase {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public void getAllFieldsAsynch(double lastUpdateDate, final Model.GetFieldsListener listener) {

        // Get reference for the fields node
        DatabaseReference myRef = database.getReference("fields");

        // Get all the fields whose last update time is equal or greater than the local last update time
        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdateDate);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Create fields list and fields to delete list
                final List<Field> fieldsList = new LinkedList<Field>();
                final List<Field> fieldsToDelete = new LinkedList<Field>();

                // Loop over all fetched fields
                for (DataSnapshot fieldSnapshot : dataSnapshot.getChildren()) {

                    // Create a field object for the current snapshot
                    Field field = fieldSnapshot.getValue(Field.class);

                    // Set the id of the current field
                    field.setId(fieldSnapshot.getKey());

                    // If the field is no deleted - add it to the fields list
                    // Otherwise - add it to the fields to delete list
                    if (!field.getIsDeleted()) {
                        fieldsList.add(field);
                    } else {
                        fieldsToDelete.add(field);
                    }
                }

                // Call listener's onResult method with both lists
                listener.onResult(fieldsList, fieldsToDelete);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Call listener's onCancel method
                listener.onCancel();
            }
        });
    }
    public void addField(Field field,final Model.AddFieldListener listener) {
        DatabaseReference myRef = database.getReference("fields");
        myRef.child(field.getId()).setValue(field.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                // If there isn't an error
                if (databaseError == null) {

                    // Call listener's onResult method
                    listener.onResult();
                }
                // If there is an error
                else {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            }
        });
    }
    public void deleteField(final String id, final Model.DeleteFieldListener listener) {

        // Get reference for the field node
        final DatabaseReference myRef = database.getReference("fields").child(id);

        // Mark the field as deleted
        myRef.child("isDeleted").setValue(true, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                // If there isn't an error
                if (databaseError == null) {

                    // Update the field's last update time
                    myRef.child("lastUpdated").setValue(ServerValue.TIMESTAMP, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            // If there isn't an error
                            if (databaseError == null) {

                                // Call listener's onResult method with the id
                                listener.onResult(id);
                            }
                            // If there is an error
                            else {

                                // Call listener's onCancel method
                                listener.onCancel();
                            }
                        }
                    });
                }
                // If there is an error
                else {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            }
        });
    }

    public void editField(Field field, final Model.EditFieldListener listener) {

        // Get reference for the requierd field's node
        DatabaseReference myRef = database.getReference("fields").child(field.getId());

        // Update the field
        myRef.setValue(field.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                // If there isn't an error
                if (databaseError == null) {

                    // Call listener's onResult method
                    listener.onResult();
                }
                // If there is an error
                else {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            }
        });
    }
    public void addReview(Review review,final Model.AddReviewListener listener) {
        DatabaseReference myRef = database.getReference("reviews");
        myRef.child(review.getId()).setValue(review.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                // If there isn't an error
                if (databaseError == null) {

                    // Call listener's onResult method
                    listener.onResult();
                }
                // If there is an error
                else {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            }
        });
    }
    public FirebaseUser getUser()
    {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
    public String getNewFieldKey() {

        // Push a new child node to the fields node and return it's id
        return database.getReference("fields").push().getKey();
    }

    public String getNewReviewKey() {
        // Push a new child node to the review node and return it's id
        return database.getReference("reviews").push().getKey();
    }
    public void saveImage(Bitmap imageBitmap, String name, final Model.SaveImageListener listener){

        // Get reference to the images node of Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child("images").child(name);

        // Process the bitmap in order to get byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the image
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {

                // Call listener's fail method
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Get the url of the uploaded image
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();

                // Call listener's complete method with the url
                listener.complete(downloadUrl.toString());
            }
        });
    }

    public void getImage(String url, final Model.GetImageListener listener){

        // Get reference to Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(url);

        // Get the image
        final long ONE_MEGABYTE = 1024 * 1024;
        httpsReference.getBytes(3* ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                // Decode the fetched byte array to bitmap
                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Call listener's onSuccess method with the image bitmap
                listener.onSuccess(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {

                // Call listener's onFail method
                listener.onFail();
            }
        });
    }

    public void handleDatabaseChanges() {

        // Register for any change within the fields node
        database.getReference("fields").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                // Create a field object for the current snapshot
                Field field = dataSnapshot.getValue(Field.class);

                // Set the id of the current field
                field.setId(dataSnapshot.getKey());

                // If the field isn't deleted
                if (!field.getIsDeleted()) {

                    // If the field not exists in the local database
                    if (Model.getInstance().getFieldById(field.getId()) == null) {

                        // Get the current user and check if it is different than the user that created the field
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        if (!user.getUid().equals(field.getUserId())) {
//
//                            // Set the icon of the refresh button to "updates"
//                            //MainActivity.changeRefreshButtonIcon(true);
//                        }
                    }

                    // Add the field to the local database
                    FieldSql.addField(Model.getInstance().modelSql.getWritableDB(), field);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {


                // Get the current user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Create a field object for the current snapshot
                Field field = dataSnapshot.getValue(Field.class);

                // Set the id of the current field
                field.setId(dataSnapshot.getKey());

                // If the field isn't deleted
                if (!field.getIsDeleted()) {

                    // Update the field in the local database
                    FieldSql.editField(Model.getInstance().modelSql.getWritableDB(), field);
                } else {
                    // If the field is deleted and the current user is different than the user that created the field
                    //else if (!user.getUid().equals(field.getUserId())) {

                    // Remove field's image from the device
                    //TODO: IMAGE HANDLE
                    //Model.getInstance().removeImageFromDevice(field.getImageName());

                    // Delete the field from the local database
                    FieldSql.deleteField(Model.getInstance().modelSql.getWritableDB(), field.getId());
                    //}
                }

                // If the current user is different than the user that created the field
//                if (!user.getUid().equals(field.getUserId())) {
//
//                    // Set the icon of the refresh button to "updates"
//                   // MainActivity.changeRefreshButtonIcon(true);
//                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // For future features
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // For future features
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // For future features
            }
        });
    }
}


package com.example.dell.fieldit.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;
import android.net.ConnectivityManager;

import com.example.dell.fieldit.FieldUpdateListener;
import com.example.dell.fieldit.MyApplication;
import com.example.dell.fieldit.ReviewUpdateListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Pack200;


public class Model {
    private final static Model instance = new Model();
    ModelFirebase modelFirebase;
    ModelSql modelSql;
    private Model() {

        // Init Firebase model
        modelFirebase = new ModelFirebase();

        // Register for data changes in Firebase in order to implement real-time change support
        modelFirebase.handleDatabaseChanges();
        modelFirebase.handleReviewDatabaseChanges();

        // Init local database model
        modelSql = new ModelSql(MyApplication.getAppContext());
    }

    public static Model getInstance() {
        return instance;
    }

    public static boolean checkNetwork() {
        Context context = MyApplication.getAppContext();
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public interface GetFieldsListener{
        public void onResult(List<Field> fields, List<Field> fieldsToDelete);
        public void onCancel();
    }

    public interface GetReviewsListener{
        public void onResult(List<Review> reviews, List<Review> reviewsToDelete);
        public void onCancel();
    }

    public void getAllUpdatedFields(final GetFieldsListener listener) {

        if (checkNetwork()) {

            // Get the last update time
            final double lastUpdateDate = FieldSql.getLastUpdateDate(modelSql.getReadbleDB());

            // Get all fields records from Firebase that where updated since local last update time
            modelFirebase.getAllUpdatedFields(lastUpdateDate, new GetFieldsListener() {
                @Override
                public void onResult(List<Field> fields, List<Field> fieldsToDelete) {

                    // Check if there is fields that added/updated since local last update time
                    if (fields != null && fields.size() > 0) {

                        // Init the variable that will contain the maximum last update time with the value of the current local last update time
                        double recentUpdate = lastUpdateDate;

                        // Loop over all these fields
                        for (Field field : fields) {

                            // Add the current field to the local database
                            FieldSql.addField(modelSql.getWritableDB(), field);

                            // Get the maximum last update time
                            if (field.getLastUpdated() > recentUpdate) {
                                recentUpdate = field.getLastUpdated();
                            }
                        }

                        // Set the last update time as the maximum we find
                        FieldSql.setLastUpdateDate(modelSql.getWritableDB(), recentUpdate);
                    }

                    // Check if there is fields that deleted since local last update time
                    if (fieldsToDelete != null && fieldsToDelete.size() > 0) {

                        // Loop over all these fields
                        for (Field fieldToDelete : fieldsToDelete) {

                            // Remove field's image from the device
                            removeImageFromDevice(fieldToDelete.getImageName());

                            // Delete the field from the local database
                            FieldSql.deleteField(modelSql.getWritableDB(), fieldToDelete.getId());
                        }
                    }

                    // Return the complete field list (ordered by field name) to the caller
                    List<Field> fieldsList = FieldSql.getAllFields(modelSql.getReadbleDB());
                    listener.onResult(fieldsList, null);
                }

                @Override
                public void onCancel() {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            });
        }
        else
        {
             //Return the complete field list (ordered by field name) to the caller
            List<Field> fieldsList = FieldSql.getAllFields(modelSql.getReadbleDB());
            listener.onResult(fieldsList, null);
        }
    }

    public void getAllUpdatedReviews(final String fieldId, final GetReviewsListener listener) {

        if (checkNetwork()) {

            // Get the last update time
            final double lastUpdateDate = ReviewSql.getLastUpdateDate(modelSql.getReadbleDB());

            // Get all fields records from Firebase that where updated since local last update time
            modelFirebase.getAllUpdatedReviews(lastUpdateDate, new GetReviewsListener() {
                @Override
                public void onResult(List<Review> reviews, List<Review> reviewsToDelete) {

                    // Check if there is reviews that added/updated since local last update time
                    if (reviews != null && reviews.size() > 0) {

                        // Init the variable that will contain the maximum last update time with the value of the current local last update time
                        double recentUpdate = lastUpdateDate;

                        // Loop over all these fields
                        for (Review review : reviews) {

                            // Add the current field to the local database
                            ReviewSql.addReview(modelSql.getWritableDB(), review);

                            // Get the maximum last update time
                            if (review.getLastUpdated() > recentUpdate) {
                                recentUpdate = review.getLastUpdated();
                            }
                        }

                        // Set the last update time as the maximum we find
                        ReviewSql.setLastUpdateDate(modelSql.getWritableDB(), recentUpdate);
                    }

                    // Check if there is fields that deleted since local last update time
                    if (reviewsToDelete != null && reviewsToDelete.size() > 0) {

                        // Loop over all these fields
                        for (Review reviewToDelete : reviewsToDelete) {

                            // Delete the field from the local database
                            FieldSql.deleteField(modelSql.getWritableDB(), reviewToDelete.getId());
                        }
                    }

                    // Return the complete Review list (ordered by field name) to the caller
                    List<Review> reviewsList = ReviewSql.getFieldReviews(modelSql.getReadbleDB(),fieldId);
                    listener.onResult(reviewsList, null);
                }

                @Override
                public void onCancel() {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            });
        }
        else
        {
            //Return the complete field list (ordered by field name) to the caller
            List<Review> reviewsList = ReviewSql.getFieldReviews(modelSql.getReadbleDB(),fieldId);
            listener.onResult(reviewsList, null);
        }
    }

    public List<Field> refreshFieldsList() {

        // Return all fields (ordered by field name) from the local database
        return FieldSql.getAllFields(modelSql.getReadbleDB());
    }

    public Field getFieldById(String id){

        // Return the field by the required id if it exists
        // Otherwise - return null
        return FieldSql.getFieldById(modelSql.getReadbleDB(), id);
    }

    public Review getReviewById(String id){

        // Return the review by the required id if it exists
        // Otherwise - return null
        return ReviewSql.getReviewById(modelSql.getReadbleDB(), id);
    }

    public interface AddFieldListener{
        public void onResult();
        public void onCancel();
    }

    public interface AddReviewListener{
        public void onResult();
        public void onCancel();
    }

    public void addField(final Field field, final Bitmap imageBitmap, final AddFieldListener listener){

        // Get new key from Firebase
        field.setId(modelFirebase.getNewFieldKey());

        // If we need to save image
        if (imageBitmap != null) {

            // Set the image name
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String imName = "image_" + field.getId() + "_" + timeStamp + ".jpg";
            Log.d("IMGMANE", imName);

            // Save image to Firebase and local storage
            saveImage(imageBitmap, imName, new Model.SaveImageListener() {
                @Override
                public void complete(String url) {
                    Log.d("SAVE_IMAGE", "complete: ");
                    // Set the field's image name as the url from Firebase
                    field.setImageName(url);

                    // Add field to both Firebase and local database
                    saveField(field, listener);
                }

                @Override
                public void fail() {
                    Log.d("SAVE_IMAGE", "fail: ");
                    // Add field to both Firebase and local database
                    saveField(field, listener);
                }
            });
        }
        // If we don't need to save image
        else {

            // Add field to both Firebase and local database
            saveField(field, listener);
        }
    }

    private void saveField(final Field field, final AddFieldListener listener) {

        // Add field to Firebase
        modelFirebase.addField(field, new AddFieldListener() {
            @Override
            public void onResult() {

                // Add the field to the local database
                FieldSql.addField(Model.getInstance().modelSql.getWritableDB(), field);

                // Call listener's onResult method
                listener.onResult();
            }

            @Override
            public void onCancel() {

                // Call listener's onCancel method
                listener.onCancel();
            }
        });
    }

    public interface DeleteFieldListener{
        public void onResult(String id);
        public void onCancel();
    }

    public void setFieldUpdateListener(FieldUpdateListener lsn){
        modelFirebase.fieldUpdateListener = lsn;
    }

    public void setReviewUpdateListener(ReviewUpdateListener lsn) {
        modelFirebase.reviewUpdateListener = lsn;
    }

    public void deleteField(String id, final DeleteFieldListener listener){

        // Delete field from Firebase
        modelFirebase.deleteField(id, new DeleteFieldListener() {
            @Override
            public void onResult(String id) {

                // Remove field's image from the device
                Field deletedField = FieldSql.getFieldById(modelSql.getReadbleDB(), id);
                if(deletedField != null) {
                    removeImageFromDevice(deletedField.getImageName());
                }
                // Delete the field from the local database
                FieldSql.deleteField(modelSql.getReadbleDB(), id);

                // Call listener's onResult method with the id
                listener.onResult(id);
            }

            @Override
            public void onCancel() {

                // Call listener's onCancel method
                listener.onCancel();
            }
        });
    }

    public interface EditFieldListener{
        public void onResult();
        public void onCancel();
    }

    public void editField(final Field field, final Bitmap imageBitmap, final EditFieldListener listener) {

        // If we need to save image
        if (imageBitmap != null) {

            // Set the image name
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String imName = "image_" + field.getId() + "_" + timeStamp + ".jpg";

            // Save image to Firebase and local storage
            saveImage(imageBitmap, imName, new Model.SaveImageListener() {
                @Override
                public void complete(String url) {

                    // Set the field's image name as the url from Firebase
                    field.setImageName(url);

                    // Edit field on Firebase
                    modelFirebase.editField(field, new EditFieldListener() {
                        @Override
                        public void onResult() {

                            // Update the current field on the local database
                            FieldSql.editField(modelSql.getReadbleDB(), field);

                            // Call listener's onResult method
                            listener.onResult();
                        }

                        @Override
                        public void onCancel() {

                            // Call listener's onCancel method
                            listener.onCancel();
                        }
                    });
                }

                @Override
                public void fail() {

                    // Update the current field on the local database
                    FieldSql.editField(modelSql.getReadbleDB(), field);
                }
            });
        }
        // If we don't need to save image
        else {

            // Edit field on Firebase
            modelFirebase.editField(field, new EditFieldListener() {
                @Override
                public void onResult() {

                    // Update the current field on the local database
                    FieldSql.editField(modelSql.getReadbleDB(), field);

                    // Call listener's onResult method
                    listener.onResult();
                }

                @Override
                public void onCancel() {

                    // Call listener's onCancel method
                    listener.onCancel();
                }
            });
        }
    }

    public void saveReview(String field_id,String text,float rating,final AddReviewListener listener) {
        String user_id = modelFirebase.getUser().getUid();
        String user_email = modelFirebase.getUser().getEmail();
        String id = modelFirebase.getNewReviewKey();
        final Review review = new Review(id,text,rating,field_id,user_id,user_email);

        // Add field to Firebase
        modelFirebase.addReview(review, new AddReviewListener() {
            @Override
            public void onResult() {

                // Add the field to the local database
                ReviewSql.addReview(Model.getInstance().modelSql.getWritableDB(), review);

                // Call listener's onResult method
                listener.onResult();
            }

            @Override
            public void onCancel() {

                // Call listener's onCancel method
                listener.onCancel();
            }
        });
    }

    private String getLocalImageFileName(String url) {

        // Get image name by url
        String name = URLUtil.guessFileName(url, null, null);
        return name;
    }

    public interface SaveImageListener{
        void complete(String url);
        void fail();
    }

    public void saveImage(final Bitmap imageBitmap, String name, final SaveImageListener listener) {

        // Save the image on Firebase
        modelFirebase.saveImage(imageBitmap, name, new SaveImageListener() {
            @Override
            public void complete(String url) {

                // Get image name by url
                String localName = getLocalImageFileName(url);
                Log.d("ImageDirectoy", localName);

                // Synchronously save image locally
                saveImageToFile(imageBitmap, localName);

                // Call listener's complete method with the url
                listener.complete(url);
            }
            @Override
            public void fail() {

                // Call listener's fail method
                listener.fail();
            }
        });
    }

    private void addPicureToGallery(File imageFile){

        // Add the picture to the gallery
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
    }

    private void refreshGallery(){

        // Refresh the gallery in order to remove deleted images from there
        MediaScannerConnection.scanFile(MyApplication.getAppContext(),
                new String[] { Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() },
                null, null);
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        try {

            // Get the storage directory on the device
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            Log.d("PATH", dir.getPath());
            // If the directory not exists - create it
            if (!dir.exists()) {
                dir.mkdir();
            }

            // Create new image file with the relevant name
            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();

            // Save the image to the file
            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            // Add the picture to the gallery
            addPicureToGallery(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeImageFromDevice(String imageFileName) {

        // Get image name by url
        String localFileName = getLocalImageFileName(imageFileName);

        // Get the storage directory on the device
        File dir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        // Create file object with the relevant image path
        File fdelete = new File(dir + "/" + localFileName);

        // If the image exists
        if (fdelete.exists()) {

            // If the image deleted successfully
            if (fdelete.delete()) {

                // Refresh the gallery in order to remove deleted images from there
               refreshGallery();
            }
        }
    }

    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }

    public void loadImage(final String url, final GetImageListener listener) {

        // First try to find the image on the device
        final String localFileName = getLocalImageFileName(url);
        Bitmap image = loadImageFromFile(localFileName);

        // If image not found - try downloading it from Firebase
        if (image == null) {

            // Get image from Firebase
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onSuccess(Bitmap image) {

                    // Save the image locally
                    saveImageToFile(image, localFileName);

                    // Return the image using the listener
                    listener.onSuccess(image);
                }

                @Override
                public void onFail() {

                    // Call listener's onFail method
                    listener.onFail();
                }
            });
        }
        // If image found locally
        else {

            // Return the image using the listener
            listener.onSuccess(image);
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {

            // Get the storage directory on the device
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            // Create new image file with the relevant name
            File imageFile = new File(dir, imageFileName);

            // Load the image from the file and decode it to bitmap
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the image bitmap
        return bitmap;
    }
}

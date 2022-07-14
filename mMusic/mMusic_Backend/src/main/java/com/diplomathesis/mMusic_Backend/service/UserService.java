package com.diplomathesis.mMusic_Backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.diplomathesis.mMusic_Backend.model.FullUser;
import com.diplomathesis.mMusic_Backend.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    // Access to Firebase Firestore provided
    Firestore firestoreDatabase = FirestoreClient.getFirestore();

    // get a document
    public User getUserByUserDocumentId(String userDocumentId) throws InterruptedException, ExecutionException {
        DocumentReference userDocumentReference = firestoreDatabase.collection("Users").document(userDocumentId);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = userDocumentReference.get();
        DocumentSnapshot userDocument = future.get();
        User user = new User();
        if (userDocument.exists()) {
            // convert document to User POJO
            user = userDocument.toObject(User.class);
            return user;
        }
        return null;
    }

    // get multiple documents from a collection
    public List<User> getUsers() throws InterruptedException, ExecutionException {
        List<User> users = new ArrayList<>();
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = firestoreDatabase.collection("Users").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            users.add(document.toObject(User.class));
        }
        return users;
    }

    // add a document
    public FullUser addNewUser(FullUser user) throws InterruptedException, ExecutionException {
        ApiFuture<WriteResult> future = firestoreDatabase.collection("Users").document(user.getUserDocumentId()).set(user.getUser());
        WriteResult userAdded = future.get();
        if (userAdded != null) {
            // convert document to FullUser POJO
            return user;
        }
        return null;
    }

    // update a document
    public FullUser updateUserDetails(String email, String birthday, String firstname, String lastname, String phone, String stageName) throws InterruptedException, ExecutionException {
        DocumentReference docRef = firestoreDatabase.collection("Users").document(email);
        ApiFuture<WriteResult> future = docRef.update("birthday", birthday, "firstname", firstname, "lastname", lastname, "phone", phone, "stageName", stageName);
        WriteResult userUpdated = future.get();

        //get document from db to form it
        ApiFuture<DocumentSnapshot> future2 = docRef.get();
        DocumentSnapshot document = future2.get();
        if (userUpdated != null && document.exists()) {
            FullUser user = new FullUser();
            user.setUserDocumentId(email);
            user.setArtist(document.toObject(User.class).getArtist());
            user.setBirthday(birthday);
            user.setFirstname(firstname);
            user.setLastname(lastname);
            user.setPhone(phone);
            user.setPlaylist(document.toObject(User.class).getPlaylist());
            user.setStageName(stageName);
            user.setUploads(document.toObject(User.class).getUploads());
            return user;
        }
        return null;
    }

    // delete a document
    public String deleteUserByUserDocumentId(String userDocumentId) throws InterruptedException, ExecutionException {
        firestoreDatabase.collection("Users").document(userDocumentId).delete();
        return "User with id " + userDocumentId + " was successfully deleted!";
    }

}
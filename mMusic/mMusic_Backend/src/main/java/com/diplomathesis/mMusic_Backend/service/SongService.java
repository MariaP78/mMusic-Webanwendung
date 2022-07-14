package com.diplomathesis.mMusic_Backend.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import javax.xml.bind.DatatypeConverter;
import com.diplomathesis.mMusic_Backend.model.ApiResponseSong;
import com.diplomathesis.mMusic_Backend.model.FullSong;
import com.diplomathesis.mMusic_Backend.model.Song;
import com.diplomathesis.mMusic_Backend.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.util.AbstractMap;
@Service
public class SongService {
    // global variable to save song's IPFS hash
    public static String mySongIPFSHash = "";

    // global variable to save song's SHA-256 hash
    public static String mySongSHA256Hash = "";

    // global variable to save song's blockchain transaction id
    public static String myBlockchainTransactionId = "";

    // IPFS Connection - by host and port
    static IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");

    // Access to Firebase Firestore provided
    static Firestore firestoreDatabase = FirestoreClient.getFirestore();

    // get a document by id
    public FullSong getSongByIPFSHashCode(String hashCode) throws InterruptedException, ExecutionException {
        DocumentReference songDocumentReference = firestoreDatabase.collection("Songs").document(hashCode);
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = songDocumentReference.get();
        DocumentSnapshot songDocument = future.get();
        FullSong song = new FullSong(hashCode, new Song());
        if (songDocument.exists()) {
            // convert document to FullSong POJO
            song = songDocument.toObject(FullSong.class);
            return song;
        }
        return null;
    }

    // get multiple documents from a collection
    public List<Song> getSongs() throws InterruptedException, ExecutionException {
        List<Song> songs = new ArrayList<>();
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = firestoreDatabase.collection("Songs").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            songs.add(document.toObject(Song.class));
        }
        return songs;
    }

    //get FullSongs
    public List<FullSong> getFullSongs() throws InterruptedException, ExecutionException {
        List<FullSong> songs = new ArrayList<>();
        // asynchronously retrieve multiple documents
        ApiFuture<QuerySnapshot> future = firestoreDatabase.collection("Songs").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            FullSong song = new FullSong();
            song.setHashCode(document.getId());
            song.setTitle(document.toObject(Song.class).getTitle());
            song.setAuthor(document.toObject(Song.class).getAuthor());
            song.setStageName(document.toObject(Song.class).getStageName());
            song.setAuthorsEmail(document.toObject(Song.class).getAuthorsEmail());
            song.setAlbum(document.toObject(Song.class).getAlbum());
            song.setGenre(document.toObject(Song.class).getGenre());
            song.setReleaseYear(document.toObject(Song.class).getReleaseYear());
            song.setSha256Hash(document.toObject(Song.class).getSha256Hash());
            song.setBlockchainTransactionId(document.toObject(Song.class).getBlockchainTransactionId());
            songs.add(song);
        }
        return songs;
    }

    // function to upload song to IPFS and hash it to SHA-256
    public static ApiResponseSong uploadToIPFSAndHashToSHA256(MultipartFile multiPartFile)
            throws NoSuchAlgorithmException {
        try {
            NamedStreamable.InputStreamWrapper isw = new NamedStreamable.InputStreamWrapper(
                    multiPartFile.getInputStream());
            MerkleNode response = ipfs.add(isw).get(0);
            // System.out.println("This is my file with Hash (base 58): " +
            // response.hash.toBase58());
            System.out.println("IPFS Hash: " + response.hash.toBase58());
            // cu asta caut song in ipfs

            // hash to SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(multiPartFile.getBytes());

            mySongSHA256Hash = DatatypeConverter.printHexBinary(hash);
            System.out.println("SHA-256 Hash: " + mySongSHA256Hash); // in data din transaction blockchain

            // save hash in order to add Song to DB
            mySongIPFSHash = response.hash.toBase58();

            return new ApiResponseSong(0,
                    "Song successfully added with hash: " + mySongIPFSHash + " and SHA-256 hash: " + mySongSHA256Hash);
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    // UPLOAD SONG ON BLOCKCHAIN
    // add a song - upload to ipfs, hash it to SHA-256, create blockchain
    // transaction, add song details to Firestore
    public FullSong addSong(FullSong song, MultipartFile file)
            throws InterruptedException, ExecutionException, TransactionException, Exception {
        // upload file to ipfs and hash it to SHA-256
        uploadToIPFSAndHashToSHA256(file);
        if(firestoreDatabase.collection("Songs").document(mySongIPFSHash).get().get().exists()){
            //nu e bine , cantecul exista deja in db
            throw new Exception("Song already exists in database");
        }
        // set SHA-256 hash for song to be added to db
        song.setSha256Hash(mySongSHA256Hash);

        // -------------------------------------------- ETHEREUM TRANSACTION --------------------------------------------
        try (InputStream input = new FileInputStream(
                "C://IG_3_UNI//LICENTA//APLICATIE//mMusic_Backend//src//main//resources//mMusic_secrets.properties")) {
            // read properties
            Properties props = new Properties();
            props.load(input);

            System.out.println("Connecting to Ethereum Ropsten Testnet...");

            // ethereum transaction
            Web3j web3 = Web3j.build(new HttpService(props.getProperty("ROPSTEN_ETHEREUM_ENDPOINT")));
            System.out.println("Successfully connected to Ethereum!!");

            // define nonce and gas limit
            BigInteger nonce = web3.ethGetTransactionCount(props.getProperty("ROPSTEN_ETHEREUM_ACOUNT_1"),
                    DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasLimit = BigInteger.valueOf(300000);
            // define gas price in wei
            BigInteger gasPrice = Convert.toWei("100", Convert.Unit.GWEI).toBigInteger();
            // define value
            BigInteger value = Convert.toWei("0.00001", Convert.Unit.ETHER).toBigInteger();
            // define data
            String data = mySongSHA256Hash;
            System.out.println("Transaction Data(SHA 256 Song): " + data);

            // create transaction
            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                    props.getProperty("ROPSTEN_ETHEREUM_ACOUNT_2"), value, data);
            // sign the transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 3,
                    Credentials.create(props.getProperty("ROPSTEN_ETHEREUM_ACCOUNT_1_PRIVATE_KEY")));

            // send raw transaction to the network
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();

            // in case an error shows up
            if (ethSendTransaction.getError() != null) {
                System.out.println("Error: " + ethSendTransaction.getError().getMessage());
            }

            // get transaction hash
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("Transaction Hash: " + transactionHash);
            myBlockchainTransactionId = transactionHash;
            // set transaction id for song to be added to db
            song.setBlockchainTransactionId(myBlockchainTransactionId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ApiFuture<WriteResult> future = firestoreDatabase.collection("Songs").document(mySongIPFSHash)
                .set(song.getSong());
        WriteResult songAdded = future.get();
        if (songAdded != null) {
            // test SHA-256 hash for Blockchain Transaction
            System.out.println("SHA-256 Hash for data in Blockchain Transaction: " + mySongSHA256Hash);

            //add the song's ipfs hash to the user's list of uploaded songs
            DocumentReference userRef = firestoreDatabase.collection("Users").document(song.getAuthorsEmail());
            ApiFuture<WriteResult> arrayUnion = userRef.update("uploads", FieldValue.arrayUnion(mySongIPFSHash));
            System.out.println("Song added to user's list of uploaded songs - time: " + arrayUnion.get());
            // convert document to FullSong POJO
            return song;
        }
        return null;
    }

    // VERIFY SONG ON BLOCKCHAIN
    //  get song ifps url and status in a JSON format to play it in front end
    public Map<String, String> getSongUrlFromIpfsToPlayIt(String ipfsSongHash) throws NoSuchAlgorithmException, InterruptedException, ExecutionException{
        String ipfsSongGateway = "";
        Map<String, String> songJsonObject = new HashMap<>();
        try {
            // GET CONTENT FROM IPFS
            Multihash multihash = Multihash.fromBase58(ipfsSongHash);
            byte[] contentFromIPFS = ipfs.cat(multihash);

            // HASH content to 256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] contentFromIPFShashedToSHA256 = md.digest(contentFromIPFS);
            String hashedContentGotFromIPFS = DatatypeConverter.printHexBinary(contentFromIPFShashedToSHA256); //asta o sa il compar cu data tranzactiei din blockchain
            System.out.println("SHA-256 Hash of content from IPFS: " + hashedContentGotFromIPFS);
            //System.out.println("Song content: " + new String(content));

            // CREATE GATEWAY for playing song
            // access ipfs content using public gateway
            ipfsSongGateway = "http://127.0.0.1:8081/ipfs/" + ipfsSongHash; //"https://ipfs.io/ipfs/" + ipfsSongHash;
            System.out.println("Url Song with IPFS hash "+ ipfsSongHash + ": " + ipfsSongGateway);

            // get song transaction id from database
            DocumentReference songRef = firestoreDatabase.collection("Songs").document(ipfsSongHash);
            ApiFuture<DocumentSnapshot> future = songRef.get();
            DocumentSnapshot document = future.get();
            if(document.exists()){
                // get transaction id from firestore
                String transactionId = document.getString("blockchainTransactionId");

                // get transaction input data from blockchain
                try (InputStream input = new FileInputStream("C://IG_3_UNI//LICENTA//APLICATIE//mMusic_Backend//src//main//resources//mMusic_secrets.properties")) {
                    Properties props = new Properties();
                    props.load(input);

                    // connect to ethereum
                    Web3j web3 = Web3j.build(new HttpService(props.getProperty("ROPSTEN_ETHEREUM_ENDPOINT")));
                    
                    // get transaction data from blockchain
                    String transactionInputData = "";
                    Optional<Transaction> transaction = web3.ethGetTransactionByHash(transactionId).send().getTransaction();
                    if (transaction.isPresent()) {
                        transactionInputData = transaction.get().getInput();
                        
                        // verify song originality
                        
                        if(hashedContentGotFromIPFS.equals(transactionInputData.substring(2).toUpperCase())){
                            // System.out.println("Song is original and was uploaded by the author!");  ------ HAPPY PATH

                            //create json object for happy path
                            songJsonObject.put("songUrl", ipfsSongGateway);
                            songJsonObject.put("songStatus", "0");
                            System.out.println("Song JSON happy path: " + songJsonObject.get("songUrl") + " " + songJsonObject.get("songStatus"));

                        }
                        else{
                            // System.out.println("Song is not original and was hacked!"); ------ SAD PATH

                            //create json object for sad path
                            songJsonObject.put("songUrl", ipfsSongGateway);
                            songJsonObject.put("songStatus", "1");
                            System.out.println("Song JSON sad path: " + songJsonObject.get("songUrl") + " " + songJsonObject.get("songStatus"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("No such document!");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node",ex);
        }

        return songJsonObject;
    }

    // update only certain fields in the song document
    public FullSong updateSongDetails(String ipfsHash, String title, String stageName, String genre) throws InterruptedException, ExecutionException {
        DocumentReference docRef = firestoreDatabase.collection("Songs").document(ipfsHash);
        ApiFuture<WriteResult> future = docRef.update("title", title, "stageName", stageName, "genre", genre);
        WriteResult songUpdated = future.get();

        //get document from db to form it
        ApiFuture<DocumentSnapshot> future2 = docRef.get();
        DocumentSnapshot document = future2.get();
        if (songUpdated != null && document.exists()) {
            FullSong song = new FullSong();
            song.setHashCode(ipfsHash);
            song.setTitle(title);
            song.setAuthor(document.toObject(Song.class).getAuthor());
            song.setStageName(stageName);
            song.setAuthorsEmail(document.toObject(Song.class).getAuthorsEmail());
            song.setAlbum(document.toObject(Song.class).getAlbum());
            song.setGenre(genre);
            song.setReleaseYear(document.toObject(Song.class).getReleaseYear());
            song.setSha256Hash(document.toObject(Song.class).getSha256Hash());
            song.setBlockchainTransactionId(document.toObject(Song.class).getBlockchainTransactionId());
            return song;
        }
        return null;
    }

    //add song to playlist
    public ApiResponseSong addSongToUserPlaylist(String userEmail, String songHash) throws InterruptedException, ExecutionException{
        //add the song's ipfs hash to the user's list of uploaded songs
        DocumentReference userRef = firestoreDatabase.collection("Users").document(userEmail);
        ApiFuture<WriteResult> arrayUnion = userRef.update("playlist", FieldValue.arrayUnion(songHash));
        System.out.println("Song added to user's list playlist - time: " + arrayUnion.get());
        ApiResponseSong response = new ApiResponseSong(0, "Song added to user's list playlist");
        return response;
    }

    // delete song from playlist
    public ApiResponseSong deleteSongFromUserPlaylist(String userEmail, String songHash) throws InterruptedException, ExecutionException{
        //add the song's ipfs hash to the user's list of uploaded songs
        DocumentReference userRef = firestoreDatabase.collection("Users").document(userEmail);
        ApiFuture<WriteResult> arrayRemove = userRef.update("playlist", FieldValue.arrayRemove(songHash));
        System.out.println("Song successfully deleted from user's playlist - time: " + arrayRemove.get());
        ApiResponseSong response = new ApiResponseSong(0, "Song successfully deleted from user's playlist");
        return response;
    }

    // get all songs from playlist

    public List<FullSong> getAllSongsFromUserPlaylist(String userEmail) throws InterruptedException, ExecutionException{
        List<FullSong> songs = new ArrayList<>();
        DocumentReference userRef = firestoreDatabase.collection("Users").document(userEmail);
        ApiFuture<DocumentSnapshot> future = userRef.get();
        DocumentSnapshot document = future.get();
        if(document.exists()){
            List<String> playlist = document.toObject(User.class).getPlaylist();
            if(playlist != null){
                for(String songHash : playlist){
                    DocumentReference songRef = firestoreDatabase.collection("Songs").document(songHash);
                    ApiFuture<DocumentSnapshot> future2 = songRef.get();
                    DocumentSnapshot document2 = future2.get();
                    if(document2.exists()){
                        FullSong song = new FullSong();
                        song.setHashCode(songHash);
                        song.setTitle(document2.toObject(Song.class).getTitle());
                        song.setAuthor(document2.toObject(Song.class).getAuthor());
                        song.setStageName(document2.toObject(Song.class).getStageName());
                        song.setAuthorsEmail(document2.toObject(Song.class).getAuthorsEmail());
                        song.setAlbum(document2.toObject(Song.class).getAlbum());
                        song.setGenre(document2.toObject(Song.class).getGenre());
                        song.setReleaseYear(document2.toObject(Song.class).getReleaseYear());
                        song.setSha256Hash(document2.toObject(Song.class).getSha256Hash());
                        song.setBlockchainTransactionId(document2.toObject(Song.class).getBlockchainTransactionId());
                        songs.add(song);
                    }
                }
            }
        }
        return songs;
    }


    // get all songs from uploads

    public List<FullSong> getAllSongsFromUserUploads(String userEmail) throws InterruptedException, ExecutionException{
        List<FullSong> songs = new ArrayList<>();
        DocumentReference userRef = firestoreDatabase.collection("Users").document(userEmail);
        ApiFuture<DocumentSnapshot> future = userRef.get();
        DocumentSnapshot document = future.get();
        if(document.exists()){
            List<String> uploads = document.toObject(User.class).getUploads();
            if(uploads != null){
                for(String songHash : uploads){
                    DocumentReference songRef = firestoreDatabase.collection("Songs").document(songHash);
                    ApiFuture<DocumentSnapshot> future2 = songRef.get();
                    DocumentSnapshot document2 = future2.get();
                    if(document2.exists()){
                        FullSong song = new FullSong();
                        song.setHashCode(songHash);
                        song.setTitle(document2.toObject(Song.class).getTitle());
                        song.setAuthor(document2.toObject(Song.class).getAuthor());
                        song.setStageName(document2.toObject(Song.class).getStageName());
                        song.setAuthorsEmail(document2.toObject(Song.class).getAuthorsEmail());
                        song.setAlbum(document2.toObject(Song.class).getAlbum());
                        song.setGenre(document2.toObject(Song.class).getGenre());
                        song.setReleaseYear(document2.toObject(Song.class).getReleaseYear());
                        song.setSha256Hash(document2.toObject(Song.class).getSha256Hash());
                        song.setBlockchainTransactionId(document2.toObject(Song.class).getBlockchainTransactionId());
                        songs.add(song);
                    }
                }
            }
        }
        return songs;
    }
    
    // get recommended songs from playlist

    public List<FullSong> getRecommendationsPlaylist(String userEmail) throws InterruptedException, ExecutionException{
        List<FullSong> allSongs = getFullSongs();
		List<FullSong> recommended = new ArrayList<>();
        
		List<FullSong> likedSongs = getAllSongsFromUserPlaylist(userEmail);
        System.out.println(likedSongs.size());
		double userScore = 0.0;
		for (Song song : likedSongs) {
			double score = song.calculateScore();
			userScore += score;
		}
		userScore /= likedSongs.size();
		List<AbstractMap.SimpleEntry<FullSong, Double>> deviations = new ArrayList<>();
		for (FullSong song : allSongs) {
			Double score = song.calculateScore();
			Double diff = Math.abs(score - userScore);
			deviations.add(new AbstractMap.SimpleEntry<FullSong, Double>(song, diff));
		}
        
		deviations.sort(new Comparator<AbstractMap.SimpleEntry<FullSong, Double>>() {
			@Override
			public int compare(AbstractMap.SimpleEntry<FullSong, Double> o1, AbstractMap.SimpleEntry<FullSong, Double> o2) {
				if ((o1.getValue() < o2.getValue()))
					return -1;
				return 1;
			}
		});
        System.out.println(userScore);
        // print deviations
        for (int i = 0; i < deviations.size(); i++) {
            System.out.println(deviations.get(i).getKey().getGenre() + " " + deviations.get(i).getValue());
        }
		for (AbstractMap.SimpleEntry<FullSong, Double> pair : deviations) {
			recommended.add(pair.getKey());
		}
        List<FullSong> temporaryRecommendedList=recommended.subList(0, 8);
        Collections.shuffle(temporaryRecommendedList);
		return temporaryRecommendedList;        
    }

    // play song function for play song endpoint
    public static void readFromIPFS() throws NoSuchAlgorithmException, InterruptedException, ExecutionException {

        // link song to play from ipfs: https://ipfs.io/ipfs/<your-ipfs-hash>

        // read from database and get song from ipfs
        ApiFuture<QuerySnapshot> future = firestoreDatabase.collection("Songs").get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("Song ID: " + document.getId());
            try {
                // GET CONTENT FROM IPFS
                String ipfsHash = document.getId(); // Hash of a file
                Multihash multihash = Multihash.fromBase58(ipfsHash);
                byte[] contentFromIPFS = ipfs.cat(multihash);

                // HASH content to 256
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] contentFromIPFShashedToSHA256 = md.digest(contentFromIPFS);
                String hashedContentGotFromIPFS = DatatypeConverter.printHexBinary(contentFromIPFShashedToSHA256);
                System.out.println("SHA-256 Hash of content from IPFS: " + hashedContentGotFromIPFS);
                //System.out.println("Song content: " + new String(content));

                // CREATE GATEWAY for playing song
                // access ipfs content using public gateway
                // String ipfsSongGateway = "https://ipfs.io/ipfs/" + ipfsHash;
                String ipfsSongGateway = "http://127.0.0.1:8081/ipfs/" + ipfsHash;
                System.out.println("Url Song with IPFS hash "+ ipfsHash + ": " + ipfsSongGateway);
                
            } catch (IOException ex) {
                throw new RuntimeException("Error whilst communicating with the IPFS node",ex);
            }
        }
    }

    // transaction Function
    public static void transactionBlockchain() throws InterruptedException, TransactionException, Exception {
        System.out.println("Connecting to Ethereum");

        try (InputStream input = new FileInputStream(
                "C://IG_3_UNI//LICENTA//APLICATIE//mMusic_Backend//src//main//resources//mMusic_secrets.properties")) {
            Properties props = new Properties();
            props.load(input);

            // ethereum transaction
            Web3j web3 = Web3j.build(new HttpService(props.getProperty("ROPSTEN_ETHEREUM_ENDPOINT")));
            System.out.println("Successfully connected to Ethereum!!");
            // define nonce and gas limit
            BigInteger nonce = web3.ethGetTransactionCount(props.getProperty("ROPSTEN_ETHEREUM_ACOUNT_1"),
                    DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger gasLimit = BigInteger.valueOf(300000);
            // define gas price in wei
            BigInteger gasPrice = Convert.toWei("100", Convert.Unit.GWEI).toBigInteger();
            // define value
            BigInteger value = Convert.toWei("0.00001", Convert.Unit.ETHER).toBigInteger();
            // define data
            // String data = mySongSHA256Hash;

            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit,
                    props.getProperty("ROPSTEN_ETHEREUM_ACOUNT_2"), value, "data");
            // sign the transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 3,
                    Credentials.create(props.getProperty("ROPSTEN_ETHEREUM_ACCOUNT_1_PRIVATE_KEY")));
            // send raw transaction to the network
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).send();
            if (ethSendTransaction.getError() != null) {
                System.out.println("Error: " + ethSendTransaction.getError().getMessage());
            }
            String transactionHash = ethSendTransaction.getTransactionHash();
            System.out.println("Transaction Hash: " + transactionHash);

            // TRAN VECHE
            // Web3j web3 = Web3j.build(new
            // HttpService(props.getProperty("ROPSTEN_ETHEREUM_ENDPOINT")));
            // System.out.println("Successfully connected to Ethereum!!");
            // Credentials credentials = Credentials
            // .create(props.getProperty("ROPSTEN_ETHEREUM_ACCOUNT_2_PRIVATE_KEY"));
            // TransactionReceipt transactionReceipt = Transfer.sendFunds(
            // web3, credentials, props.getProperty("ROPSTEN_ETHEREUM_ACOUNT_1"),
            // BigDecimal.valueOf(1.0), Convert.Unit.ETHER).send();

            // System.out.println("Hash: "+ transactionReceipt.getTransactionHash());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
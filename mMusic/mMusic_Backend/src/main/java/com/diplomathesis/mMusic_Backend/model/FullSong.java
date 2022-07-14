package com.diplomathesis.mMusic_Backend.model;

public class FullSong extends Song {
    private String hashCode;

    /**
     * Constructors
     * @param song
     * @param ipfsHash
     */
    public FullSong(String ipfsHash, Song song) {

    }

    public FullSong(String hashCode, String title, String author, String stageName, String authorsEmail, String album, String genre, Integer releaseYear, String sha256Hash, String blockchainTransactionId) {
        super(title, author, stageName, authorsEmail, album, genre, releaseYear, sha256Hash, blockchainTransactionId);
        this.hashCode = hashCode;
    }

    public FullSong() {
    }

    /**
     * @return String return the hashCode
     */
    public String getHashCode() {
        return hashCode;
    }

    /**
     * @param hashCode the hashCode to set
     */
    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return "FullSong{" +
                "title='" + super.getTitle() + '\'' +
                ", author='" + super.getAuthor() + '\'' +
                ", stageName='" + super.getStageName() + '\'' +
                ", authorsEmail='" + super.getAuthorsEmail() + '\'' +
                ", album='" + super.getAlbum() + '\'' +
                ", genre='" + super.getGenre() + '\'' +
                ", releaseYear='" + super.getReleaseYear() + '\'' +
                ", sha256Hash='" + super.getSha256Hash() + '\'' +
                ", blockchainTransactionId='" + super.getBlockchainTransactionId() + '\'' +
                ", hashCode='" + hashCode + '\'' +
                '}';
    }

    // get only the song details, without its hash
    public Song getSong() {
        return new Song(super.getTitle(), super.getAuthor(), super.getStageName(), super.getAuthorsEmail(), super.getAlbum(), super.getGenre(), super.getReleaseYear(), super.getSha256Hash(), super.getBlockchainTransactionId());
    }
}
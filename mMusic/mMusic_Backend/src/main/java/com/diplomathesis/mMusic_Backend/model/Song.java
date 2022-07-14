package com.diplomathesis.mMusic_Backend.model;

public class Song {
    private String title;
    private String author;
    private String stageName;
    private String authorsEmail;
    private String album;
    private String genre;
    private Integer releaseYear;
    private String sha256Hash;
    private String blockchainTransactionId;

    /**
     * Constructors
     */
    public Song() {

    }

    public Song(String title, String author, String stageName, String authorsEmail, String album, String genre, Integer releaseYear,
            String sha256Hash, String blockchainTransactionId) {
        this.title = title;
        this.author = author;
        this.stageName = stageName;
        this.authorsEmail = authorsEmail;
        this.album = album;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.sha256Hash = sha256Hash;
        this.blockchainTransactionId = blockchainTransactionId;
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

        /**
     * @return String return the stageName
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * @param stageName the stageName to set
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * @return String return the authorsEmail
     */
    public String getAuthorsEmail() {
        return authorsEmail;
    }

    /**
     * @param authorsEmail the authorsEmail to set
     */
    public void setAuthorsEmail(String authorsEmail) {
        this.authorsEmail = authorsEmail;
    }

    /**
     * @return String return the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * @return String return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return Integer return the releaseYear
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }

    /**
     * @param releaseYear the releaseYear to set
     */
    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * @return String return the sha256Hash
     */
    public String getSha256Hash() {
        return sha256Hash;
    }

    /**
     * @param sha256Hash the sha256Hash to set
     */
    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    /**
     * @return String return the blockchainTransactionId
     */
    public String getBlockchainTransactionId() {
        return blockchainTransactionId;
    }

    /**
     * @param blockchainTransactionId the blockchainTransactionId to set
     */
    public void setBlockchainTransactionId(String blockchainTransactionId) {
        this.blockchainTransactionId = blockchainTransactionId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", stageName='" + stageName + '\'' +
                ", authorsEmail='" + authorsEmail + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", sha256Hash='" + sha256Hash + '\'' +
                ", blockchainTransactionId='" + blockchainTransactionId + '\'' +
                '}';
    }

    // get score for recommendation algorithm - content based filtering
    public Double calculateScore() {
        // create a map
        // formula: (danceability+tempo+feeling+lyrics) / 4
        // Map<String, Double> genres = new HashMap<String, Double>(); 
        // genres.put("rock", (0.5 + 0.7 + 0.7 + 0.5) / 4);
        // genres.put("pop", (0.3 + 0.2 + 1 + 0.7) / 4);
        // genres.put("folk-pop", (0.2 + 0.2 + 0.8 + 0.9) / 4);
        // genres.put("jazz", (0.9 + 0.4 + 0.7 + 0.5) / 4);
        // genres.put("opera", (0.0 + 0.6 + 0.2 + 0.5) / 4);
        // genres.put("hip hop", (0.4 + 0.5 + 0.1 + 0.7) / 4);
        // genres.put("rap", (0.6 + 0.8 + 0.8 + 1) / 4);
        // genres.put("classical", (0.1 + 0.1 + 0.9 + 0.5) / 4);
        // genres.put("electronic", (0.9 + 1 + 0.0 + 0.05) / 4);
        // genres.put("dance", (1 + 0.9 + 0.0 + 0.2) / 4);
        // genres.put("latino", (0.8 + 0.7 + 0.4 + 0.4) / 4);
        // genres.put("heavy-metal", (0.6 + 0.7 + 0.2 + 0.6) / 4);
        return Genre.getScoreOf(this.genre);
        // return genres.get(this.genre);
    }

    public enum Genre {
        rock(0.5, 0.7, 0.7, 0.5),
        pop(0.3, 0.2, 1.0, 0.7),
        folk_pop(0.2, 0.2, 0.8, 0.9),
        jazz(0.9, 0.4, 0.7, 0.5),
        opera(0.0, 0.6, 0.2, 0.5),
        hip_hop(0.4, 0.5, 0.1, 0.7),
        rap(0.6, 0.8, 0.8, 1.0),
        classical(0.1, 0.1, 0.9, 0.5),
        electronic(0.9, 1.0, 0.0, 0.05),
        dance(1.0, 0.9, 0.0, 0.2),
        latino(0.8, 0.7, 0.4, 0.4),
        heavy_metal(0.6, 0.7, 0.2, 0.6);
    
        private Double danceability;
        private Double tempo;
        private Double feeling;
        private Double lyrics;
    
        Genre(Double danceability, Double tempo, Double feeling, Double lyrics) {
            this.danceability = danceability;
            this.tempo = tempo;
            this.feeling = feeling;
            this.lyrics = lyrics;
        }
    
        public Double getScore() {
            return (this.danceability + this.tempo + this.feeling + this.lyrics)/4;
        }
    
        public static Double getScoreOf(String genre) {
            for (Genre g : Genre.values()) {
                if (g.name().equalsIgnoreCase(genre)) {
                    return g.getScore();
                }
            }
            return -1.0;
        }
    }
}
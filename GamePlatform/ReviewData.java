import java.util.Date;

public class ReviewData {
    private int id;
    private String username;
    private String gameName;
    private int rating;
    private String review;
    private Date reviewDate;
    
    public ReviewData(int id, String username, String gameName, int rating, String review, Date reviewDate) {
        this.id = id;
        this.username = username;
        this.gameName = gameName;
        this.rating = rating;
        this.review = review;
        this.reviewDate = reviewDate;
    }
    
    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getGameName() { return gameName; }
    public int getRating() { return rating; }
    public String getReview() { return review; }
    public Date getReviewDate() { return reviewDate; }
} 
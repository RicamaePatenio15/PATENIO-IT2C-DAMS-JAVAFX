package models;

public class ProfileImage {
    private int imageId;
    private String imageName;
    private int userId;
    private String imagePath;
    private byte[] imageFile;

    // Constructor
    public ProfileImage(int imageId, String imageName, int userId,
        String imagePath, byte[] imageFile) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.userId = userId;
        this.imagePath = imagePath;
        this.imageFile = imageFile;
    }

    // Getters and setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }
}
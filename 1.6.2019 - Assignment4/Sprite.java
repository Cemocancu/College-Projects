import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Sprite{
	
	
	//Vairable declarations begin
	private Image image;
    private double positionX;
    private double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    //Variable declarations end
    
    
    //Getters and setter begin
    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }
    public double getPositionX() {
    	return this.positionX;
    }
    public double getPositionY() {
    	return this.positionY;
    }
    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }
    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }
	public void setVelocity(double x,double y) {
		this.velocityX = x;
		this.velocityY = y;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {

		this.height = height;
	}
	//Getters and setters end
    
	
	//Methods begin
	public void update(double time)// v(t)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    } 
	public void render(GraphicsContext gc) {
		gc.drawImage(image,positionX,positionY);
	}
    public Rectangle2D getBoundary()// returns boundary
    {	
        return new Rectangle2D(positionX,positionY,width,height);
    }
    public boolean intersects(Sprite s)// returns collison
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
	public void addVelocity(double x, double y)// adds to velocity
    {
        velocityX += x;
        velocityY += y;
    }
    //Methods end
}

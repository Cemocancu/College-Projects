import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Assignment4 extends Application{
	
	
	//Static variables begin
	static Sprite userCar = new Sprite();
	static HashSet<String> input = new HashSet<String>();
	static Scene theScene;
	static Stage theStage;
	static GraphicsContext gc;
	static long lastNanoTime =  System.nanoTime();
	static double deAcc = 0.5;
	static double velocity = 0;
	static double acc = 1;
	static double maxSpeed = 5;
	static int scoreValue = 0;
	static int levelValue = 1;
	static int levelCounter = 0;
	static ArrayList<Rectangle> roadLinesList = new ArrayList<Rectangle>();
	static ArrayList<Circle> bushesList = new ArrayList<Circle>();
	static ArrayList<Sprite> rivalCars0 = new ArrayList<Sprite>();
	static ArrayList<Sprite> rivalCars1 = new ArrayList<Sprite>();
	static ArrayList<Rectangle> rivalCarsRecs = new ArrayList<Rectangle>();
	//static variables end
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	public void start(Stage theStage) {
		startGame(theStage);
	}
	
	
	public void startGame(Stage theStage) {	
		//Background begin
		Rectangle road = new Rectangle(156.25,0,312.5,750);
		road.setFill(Color.GREY);
	    Rectangle grass0 = new Rectangle(0,0,156.25,750);
	    grass0.setFill(Color.GREEN);
	    Rectangle grass1 = new Rectangle(468.75,0,156.25,750);
	    grass1.setFill(Color.GREEN);
	    //Background end
		
		
	    //Initialize begin
		theStage.setTitle("HUBBM-Racer");
	    Group root = new Group(road,grass0,grass1);
	    theScene = new Scene(root,625,750);
		theStage.setScene(theScene);
		Canvas canvas = new Canvas(625,700);
		root.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		Font theFont = Font.font("Times New Roman",FontWeight.BOLD,22);
	    gc.setFont(theFont);
	    userCar.setImage("file:userCar.jpg");// userCar generated
        userCar.setPosition(335, 625);
        Rectangle userCarRec = new Rectangle(335, 625, userCar.getWidth(),userCar.getHeight());
        userCarRec.setFill(Color.RED);
        root.getChildren().add(userCarRec);
	    for(int i=0;i<15;i++) { // roadLines generated
		    Rectangle roadLines = new Rectangle(312.5,50 *i,15,25);
		    roadLinesList.add(roadLines);
		    root.getChildren().add(roadLines);
	    }
	    for(int i=0;i<7;i++) { // bushes generated
	    	if(i<3) {
		    	Circle bushes = new Circle(78.125,150 + (200 * i),20, Color.DARKGREEN);
		    	bushesList.add(bushes);
		    	root.getChildren().add(bushes);
	    	}else if(i == 6){
	    		Circle bushes = new Circle(547,650,20, Color.DARKGREEN);
		    	bushesList.add(bushes);
		    	root.getChildren().add(bushes);
	    	}else {
	    		Circle bushes = new Circle(547,50 + (200 * (i % 3)),20, Color.DARKGREEN);
		    	bushesList.add(bushes);
		    	root.getChildren().add(bushes);
	    	}
	    }
	    for(int i=0; i<10;i++) { // rivalCars generated
	    	Sprite rivalCar = new Sprite();
	    	rivalCar.setImage("file:rivalCar.jpg");
	    	double px =40 * (int)(Math.random() * 6) + 200;
	    	double py =100 * (int)(Math.random() * 6);
	    	rivalCar.setPosition(px, py);
	    	rivalCars0.add(rivalCar);
	    }
	    for(int i=0;i<rivalCars0.size();i++) {// Makes sure rivalCars doesn't overlap
	    	Sprite tempI = rivalCars0.get(i);
	    	int counter = 0;
	    	for(int j=i;j<rivalCars0.size();j++) {
	    		Sprite tempJ = rivalCars0.get(j);
		    		if(!(Math.abs(tempI.getPositionX() - tempJ.getPositionX()) <= tempI.getWidth())) {
		    			if(!(Math.abs(tempI.getPositionY() - tempJ.getPositionY()) >= tempI.getHeight())) {
			    			counter++;
		    			}
		    		}
	    	}
	    	if(counter == 0) {
	    		rivalCars1.add(tempI);
		        Rectangle rivalCarRec = new Rectangle(tempI.getPositionX(), tempI.getPositionY(),tempI.getWidth(),tempI.getHeight());
	    	    rivalCarRec.setFill(Color.YELLOW);
	    	    rivalCarsRecs.add(rivalCarRec);
	    	    root.getChildren().add(rivalCarRec);
	    	}
	    }
	    //Initialize end
	    
	    
	    //Getting input begin
		theScene.setOnKeyPressed(
	            new EventHandler<KeyEvent>()
	            {
	                public void handle(KeyEvent e)
	                {
	                    String code = e.getCode().toString();
	                    if ( !input.contains(code) )
	                    	input.add( code );
	                }
	            });

	        theScene.setOnKeyReleased(
	            new EventHandler<KeyEvent>()
	            {
	                public void handle(KeyEvent e)
	                {
	                    String code = e.getCode().toString();
	                    input.remove( code );
	                }
	            });
	    //Getting input end
	        
	    
	    //Main loop begin
	    new AnimationTimer()
	    {
	    	public void handle(long currentNanoTime)
	        {	
	    		//Variables begin
	        	double elapsedTime = (currentNanoTime - lastNanoTime) / 80000000.0;
	        	double elapsedTime0 = (currentNanoTime - lastNanoTime) / 300000000.0;
                lastNanoTime = currentNanoTime;
	            userCar.setVelocity(0,0);
	            maxSpeed = 10 + levelValue * 2;
	            //Variables end
	            
	          //Collision detection begin
	            Iterator<Sprite> rivalCarsIter = rivalCars1.iterator();
            	Iterator<Rectangle> rivalCarsRecsIter = rivalCarsRecs.iterator();
                while ( rivalCarsIter.hasNext()){
                	Rectangle rec = rivalCarsRecsIter.next();
                    Sprite rivalCar = rivalCarsIter.next();
                    if(userCar.intersects(rivalCar)){
                    	rec.setFill(Color.BLACK);
                    	userCarRec.setFill(Color.BLACK);
                    	velocity = 0;
                    }else if(userCarRec.getY() < rec.getY()) {
                    	if(!(rec.getFill().equals(Color.GREEN))) {
                    		scoreValue += levelValue * 10;
                    		rec.setFill(Color.GREEN);
                    		levelCounter++;
                    		if(levelCounter == levelValue) {
                    			levelValue++;
                    			levelCounter = 0;
                    		}
                    	}
                    }
                }
                //Collision detection end
                
                
                //Pressing left begin
	            if (input.contains("LEFT")) {
	            	if(!(userCar.getBoundary().intersects(0,0,156.25,750))) {
	            		if(!(userCarRec.getFill().equals(Color.BLACK))){
		            		userCar.addVelocity(-25 ,0);
	            		}
	            	}
	            }
	            //Pressing left end
	            
	            
	            //Pressing right begin
	            if (input.contains("RIGHT")) {//Pressing right
	            	if(!(userCar.getBoundary().intersects(468.75,0,156.25,750))) {
	            		if(!(userCarRec.getFill().equals(Color.BLACK))){
		            		userCar.addVelocity(25 ,0);
	            		}
	            	}
	            }
	            //Pressing right end
	            
	            
	            //Speeding up begin
	            if(input.contains("UP")) {
	            	if(!(userCarRec.getFill().equals(Color.BLACK))){
	            		if(velocity < maxSpeed){
		            		velocity += acc;
		            	}
	            	}
	            	for(Rectangle r: roadLinesList) {
	            		if(r.getY() > 730) {
	            			r.setY(0);
	            		}else {
		            		r.setY(r.getY() + velocity);
	            		}
	            	}
	            	for(Circle c: bushesList) {
	            		if(c.getCenterY() > 750) {
	            			c.setCenterY(100);
	            		}else {
	            			c.setCenterY(c.getCenterY() + velocity );
	            		}
	            	for(int i = 0; i < rivalCars1.size();i++) {
	            		if(rivalCars1.get(i).getPositionY() > 750) {
	            			double px =40 * (int)(Math.random() * 6) + 200;
	            			rivalCars1.get(i).setPosition(px,0);
	            			rivalCarsRecs.get(i).setX(px);
	            			rivalCarsRecs.get(i).setY(0);
	            			rivalCarsRecs.get(i).setFill(Color.YELLOW);
	            		}else {
	            			rivalCars1.get(i).setPosition(rivalCars1.get(i).getPositionX(),rivalCars1.get(i).getPositionY() + velocity * elapsedTime0);
	            			rivalCarsRecs.get(i).setX(rivalCars1.get(i).getPositionX());
	            			rivalCarsRecs.get(i).setY(rivalCars1.get(i).getPositionY() + velocity * elapsedTime0);
	            		}
	            	}
	            	}
	            }
	            //Speeding up end
	            
	            
	            // Slowing down begin
	            else {
	            	if(velocity > 0) {
		            	velocity -= deAcc;
	            	}
	            	for(Rectangle r: roadLinesList) {
	            		if(r.getY() > 730) {
	            			r.setY(0);
	            		}else {
				            r.setY(r.getY() + velocity);
	            		}
	            	}
	            	for(Circle c: bushesList) {
	            		if(c.getCenterY() > 750) {
	            			c.setCenterY(100);
	            		}else {
		            		c.setCenterY(c.getCenterY() + velocity);
	            		}
	            	}
	            	for(int i = 0; i < rivalCars1.size();i++) {
	            		if(rivalCars1.get(i).getPositionY() > 750) {
	            			double px =40 * (int)(Math.random() * 6) + 200;
	            			rivalCars1.get(i).setPosition(px,0);
	            			rivalCarsRecs.get(i).setX(px);
	            			rivalCarsRecs.get(i).setY(0);
	            			rivalCarsRecs.get(i).setFill(Color.YELLOW);
	            		}else {
	            			rivalCars1.get(i).setPosition(rivalCars1.get(i).getPositionX(),rivalCars1.get(i).getPositionY() + velocity / 4);
	            			rivalCarsRecs.get(i).setX(rivalCars1.get(i).getPositionX());
	            			rivalCarsRecs.get(i).setY(rivalCars1.get(i).getPositionY() + velocity/ 4);
	            		}
	            	}
	            	
	            }
	            // Slowing down end
	            
	            
	            //Restart game begin
	            if(userCarRec.getFill().equals(Color.BLACK)){
	            	if(input.contains("ENTER")) {
	            		theStage.close();
		            	restart(theStage);
		            }
	            }
	            //Restart game end
	            
	            
                //Rendering to screen begin
                userCar.update(elapsedTime);
                userCarRec.setX(userCar.getPositionX());
                userCarRec.setY(userCar.getPositionY());
                gc.clearRect(0, 0, 625,750);
                userCar.render(gc);
                for(int i = 0; i < rivalCars1.size();i++) {
                    rivalCars1.get(i).render(gc);
                }
                userCarRec.toFront();
        	    gc.fillText("Score: " + scoreValue,10,25);
        	    gc.fillText("Level: " + levelValue,10,52.5);
        	    Font theFont0 = Font.font("Times New Roman",FontWeight.BOLD,50);
        	    //Rendering to screen end
        	    
        	    
        	    //Game end text begin
        	    if(userCarRec.getFill().equals(Color.BLACK)) {
        	    	Text text = new Text();
        	    	Text text0 = new Text();
        	    	text.setFont(theFont0);
        	    	text.setFill(Color.DARKRED);
        	    	text.setX(150);
        	    	text.setY(250);
        	    	text.setText("GAME OVER!\nYour Score: "+scoreValue);
        	    	text0.setFont(theFont0);
        	    	text0.setFill(Color.DARKRED);
        	    	text0.setX(50);
        	    	text0.setY(250);
        	    	text0.setText("\n\nPress ENTER to restart!");
        	    	root.getChildren().addAll(text, text0);
        	    }
        	    //Game end text end    
	        }
	    }.start();
	    theStage.show();
	    //Main loop end 
	}
	
	
	public void restart(Stage theStage) {//restarts the game
		cleanup();
		startGame(theStage);
	}
	
	
	public void cleanup() {//resets everything
		userCar = new Sprite();
		input = new HashSet<String>();
		lastNanoTime =  System.nanoTime();
		deAcc = 0.5;
		velocity = 0;
		acc = 1;
		maxSpeed = 5;
		scoreValue = 0;
		levelValue = 1;
		levelCounter = 0;
		roadLinesList.clear();
		bushesList.clear();
		input.clear();
		rivalCars0.clear();
		rivalCars1.clear();
		rivalCarsRecs.clear();
		gc.clearRect(0, 0, 625, 750);
	}
}
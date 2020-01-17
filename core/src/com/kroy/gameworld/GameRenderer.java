package com.kroy.gameworld;

import com.kroy.helpers.InputHandler;

import com.badlogic.gdx.Gdx; import com.badlogic.gdx.graphics.GL20; import com.badlogic.gdx.graphics.OrthographicCamera; import com.badlogic.gdx.graphics.Texture; import com.badlogic.gdx.graphics.g2d.SpriteBatch; import com.badlogic.gdx.graphics.g2d.TextureRegion; import com.badlogic.gdx.graphics.glutils.ShapeRenderer; import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.kroy.gameobjects.Firetruck; import com.kroy.helpers.AssetLoader; import com.badlogic.gdx.graphics.g2d.Animation; import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameRenderer {

private GameWorld myWorld;
private OrthographicCamera cam;
private ShapeRenderer shapeRenderer;
private InputHandler inputHandler;
private SpriteBatch batcher;

private Firetruck truck;
private Animation truckAnimation;
private TextureRegion truckStraight, truckStraight1;

private Texture backgroundTexture, minsterTexture;
private Sprite background;


public GameRenderer(GameWorld world) {
    myWorld = world;
    InputHandler inputHandler = new InputHandler(myWorld);
    
    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();
    
    
    cam = new OrthographicCamera(1000, 1000*(h/w));
    cam.setToOrtho(true, w, h);
    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0); //set start pos
    
    batcher = new SpriteBatch();
    batcher.setProjectionMatrix(cam.combined);
    
    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(cam.combined);
    
    initGameObjects();
    initAssets();
    
}

private void initGameObjects() {
    truck = myWorld.getFiretruck();
}

private void initAssets() {
    truckAnimation = AssetLoader.truckAnimation;
    truckStraight = AssetLoader.truck;
    truckStraight1 = AssetLoader.truck1;
    
    minsterTexture = AssetLoader.minster;
    
    backgroundTexture = AssetLoader.map;
    background = new Sprite(backgroundTexture);
    background.flip(false,  true);
    background.setScale(0.945f);
}

public void render(float runTime) {

    // Fill the entire screen with black, to prevent potential flickering.
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    batcher.setProjectionMatrix(cam.combined);
    
    if (myWorld.isReady()) {
    	shapeRenderer.begin(ShapeType.Filled);

        // Draw Background colour
        shapeRenderer.setColor(55 / 255.0f, 80 / 255.0f, 100 / 255.0f, 1);
        shapeRenderer.rect(0, 0, 500, Gdx.graphics.getHeight());

        shapeRenderer.end();
    } else {
    	batcher.begin();
    	background.draw(batcher);
    	batcher.end();
    }
    
    // Begin SpriteBatch
    batcher.begin();
    //batcher.enableBlending();
    
    if (myWorld.isReady()) {
        startMenu();
    } else if (myWorld.isGameOver()) {
    	gameOver();
    } else if (myWorld.isRunning()) {
    	gameRunning(runTime);
    	moveCamera();
    }
    
    // End SpriteBatch
    batcher.end();
    
    

}

public void startMenu() {
	AssetLoader.shadow.draw(batcher, "Click to Start", 50, 100);
    AssetLoader.font.draw(batcher, "Click to Start", 50, 100);
}

public void gameOver() {
	AssetLoader.shadow.draw(batcher, "Game Over", 25, 56);
    AssetLoader.font.draw(batcher, "Game Over", 24, 55);	
}

public void gameRunning(float runTime) {
	batcher.draw((TextureRegion) truckAnimation.getKeyFrame(runTime),  truck.getX(),
    		truck.getY()+20, 36f,
    		52.5f, truck.getWidth(), truck.getHeight(),
    		0.3f, 0.3f, truck.getRotation());
	batcher.draw(minsterTexture, 1665, 90);
}

public void moveCamera() {
	System.out.println(cam.position);
	Vector2 truckVelocity = truck.getVelocity();
	cam.translate(truckVelocity.x, truckVelocity.y);
	System.out.println(cam.position.x);
	System.out.println();
	cam.update();
}
}
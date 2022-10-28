package com.dohyun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.Stack;

public class Dude {
    private Location loc;
    private WumpusWorld myWorld;
    private Texture texture;
    private boolean HasGold = false;
    private int totalSteps = 0;
    private boolean killedWumpus = false;
    private Stack<Location> stack = new Stack<>();
    private boolean visited[][];

    public Dude(Location loc, WumpusWorld myWorld){
        this.loc = loc;
        this.myWorld = myWorld;
        stack.push(loc);
        texture = new Texture("guy.png");
        myWorld.makeVisible(loc);
        visited = new boolean[10][10];
        visited[9][0]=true;
    }

    public boolean KilledWumpus(){
        return killedWumpus;
    }
    public void moveRight(){
        if(loc.getCol()+1 < myWorld.getNumCols()) {
            loc.setCol(loc.getCol() + 1);
            myWorld.makeVisible(loc);
            totalSteps ++;
        }
    }
    public void moveLeft() {
        if(loc.getCol()-1 >= 0) {
            loc.setCol(loc.getCol() - 1);
            myWorld.makeVisible(loc);
            totalSteps ++;
        }
    }

    public void moveDown() {
        if(loc.getRow()+1 < myWorld.getNumRows()) {
            loc.setRow(loc.getRow() + 1);
            myWorld.makeVisible(loc);
            totalSteps ++;
        }
    }

    public void moveUp() {
        if(loc.getRow()-1 >= 0) {
            loc.setRow(loc.getRow() - 1);
            myWorld.makeVisible(loc);
            totalSteps ++;
        }
    }
    public void draw(SpriteBatch spriteBatch){
        Point myPoint = myWorld.convertRowColToCoords(loc);
        spriteBatch.draw(texture,(int)myPoint.getX(),(int)myPoint.getY());
    }
    public Location getLoc(){
        return loc;
    }
    public void reset(Location loc){
        this.loc = loc;
        myWorld.makeVisible(loc);
    }public void setTotalSteps(){
        totalSteps = 0;
    }
    public int getTotalSteps(){
        return totalSteps;
    }
    public boolean hasGold() {
        return HasGold;
    }

    public void setHasGold(boolean hasGold) {
        HasGold = hasGold;
    }
    public void randomAISolution(){
        int choice = (int)(1+Math.random() * 4);
        if(choice==1){
            moveDown();
        }else if(choice==2){
            moveLeft();
        }else if(choice==3){
            moveRight();
        }else {
            moveUp();
        }
        if(loc != stack.lastElement()){
            stack.push(loc);
            if(visited[loc.getRow()][loc.getCol()]){
                stack.pop();
                loc = stack.lastElement();
                totalSteps--;
            }
        }
    }

    //this method makes ONE step
    public void step(){
        randomAISolution();
        visited[loc.getRow()][loc.getCol()] = true;
        System.out.println(stack.lastElement().toString());
        if(myWorld.getTileId(loc) == WumpusWorld.GROUND){
            stack.push(loc);
            System.out.println("hit ground");
        }else if(myWorld.getTileId(loc) == WumpusWorld.WEB||myWorld.getTileId(loc) == WumpusWorld.WIND||myWorld.getTileId(loc) == WumpusWorld.STINK){
            stack.pop();
            loc = stack.lastElement();
            System.out.println("hit bad");
        }
    }
}

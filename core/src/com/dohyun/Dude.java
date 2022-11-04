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
    private boolean noway;


    public Dude(Location loc, WumpusWorld myWorld){
        this.loc = loc;
        this.myWorld = myWorld;
        stack.push(new Location(loc.getRow(),loc.getCol()));
        texture = new Texture("guy.png");
        myWorld.makeVisible(loc);
        visited = new boolean[10][10];
        visited[9][0]=true;
        System.out.println("Stack: " + stack);
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
        visited = new boolean[10][10];
        visited[9][0]=true;
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
        System.out.println("------------");
        System.out.println("Stack: " + stack);
        System.out.println(stack.size());
        stack.push(new Location(loc.getRow(),loc.getCol()));
        System.out.println("loc: " + loc.toString() + "stack: " + stack.lastElement().toString());
        if(myWorld.getTileId(loc) == WumpusWorld.WUMPUS||myWorld.getTileId(loc) == WumpusWorld.PIT||myWorld.getTileId(loc) == WumpusWorld.SPIDER){//fix it
            stack.pop();
            loc = stack.lastElement();
        }
        else if(visited[loc.getRow()][loc.getCol()]&& stack.get(0) != loc){
            stack.pop();
            if(choice==1){
                moveUp();
            }else if(choice==2){
                moveRight();
            }else if(choice==3){
                moveLeft();
            }else {
                moveDown();
            }
            totalSteps--;
            }
        System.out.println("------------");


    }
    public void foundGlitter(){

    }
    public boolean is_noway(){
        noway = true;//
        for(Location x : myWorld.getNeighbors(loc)){
            if(!visited[x.getRow()][x.getCol()]) {
                noway = false;
                return false;
            }
        }
        if(noway && !loc.equals(stack.get(0))){
            stack.pop();
            loc = stack.lastElement();
        }
        return true;
    }
    //this method makes ONE step
    public void step(){
        if(is_noway());// if there is no way to go out, get back one.
        else{

            if(myWorld.getTileId(loc) == WumpusWorld.GOLD){

            }
            else if(myWorld.getTileId(loc) == WumpusWorld.GLITTER){

            }
            else if(myWorld.getTileId(loc) == WumpusWorld.WEB||myWorld.getTileId(loc) == WumpusWorld.WIND||myWorld.getTileId(loc) == WumpusWorld.STINK){
                stack.pop();
                loc = stack.lastElement();
                randomAISolution();


            }else {
                randomAISolution();
                System.out.println(stack.lastElement().toString());
                if (myWorld.getTileId(loc) == WumpusWorld.GROUND && !visited[loc.getRow()][loc.getCol()]) {
                    stack.push(new Location(loc.getRow(), loc.getCol()));
                }
            }
            visited[loc.getRow()][loc.getCol()] = true;
        }
    }
}

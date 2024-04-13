package edu.eci.arsw.model;

public class Head {

     private int row;
     private int col;

     public Head(int row, int col){
         this.row = row;
         this.col = col;
     }

    public Head(){

    }

    public void moveUp(){
         this.row =- 1;
    }

    public void moveDown(){
        this.row =+ 1;
    }

    public void moveLeft(){
        this.col =- 1;
    }

    public void moveRight(){
        this.row =+ 1;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}

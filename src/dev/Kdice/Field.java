package dev.Kdice;

public class Field {
    private int cubes, ownerId;

    public Field() { cubes =0; ownerId = 99;}
    public Field(int cubes, int ownerId) {
        this.cubes = cubes;
        this.ownerId = ownerId;
    }


    public int getCubes() { return cubes; }
    public int getOwnerId() { return ownerId; }

    public void setCubes(int cubes) { this.cubes = cubes; }
    public void  setOwnerId(int ownerId) { this.ownerId = ownerId; }
}

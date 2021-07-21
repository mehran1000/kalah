package ir.mastcheshmi.kalah.domain;

public class Pit {
    private int id;
    /**
     *  Each player has a larger pit, his Kalah or house.
     */
    private boolean isKalah;

    private int stoneCount;


    public Pit(int id, boolean isKalah, int stoneCount) {
        this.id = id;
        this.isKalah = isKalah;
        this.stoneCount = stoneCount;
    }

    public int getId() {
        return id;
    }

    public boolean isKalah() {
        return isKalah;
    }

    public void setKalah(boolean kalah) {
        isKalah = kalah;
    }

    public int getStoneCount() {
        return stoneCount;
    }

    public void setStoneCount(int stoneCount) {
        if( stoneCount < 0 ){
            throw new IllegalArgumentException("The stone count cannot be negative");
        }
        this.stoneCount = stoneCount;
    }

   public void decrementStone(){
        setStoneCount(this.stoneCount - 1);
   }

    public void incrementStone(){
        this.stoneCount++;
    }

    public void addStones( int count ){
        this.stoneCount += count;
    }

    public boolean isEmpty(){
        return stoneCount == 0;
    }

}

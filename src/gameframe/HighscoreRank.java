/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gameframe;

/**
 * HighscoreRank
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class HighscoreRank {
    private int score;
    private int rank;
    private boolean madeItToTheList;

    public HighscoreRank() {
    }

    public HighscoreRank(int score, int rank, boolean madeItToTheList) {
        this.score = score;
        this.rank = rank;
        this.madeItToTheList = madeItToTheList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isMadeItToTheList() {
        return madeItToTheList;
    }

    public void setMadeItToTheList(boolean madeItToTheList) {
        this.madeItToTheList = madeItToTheList;
    }

}

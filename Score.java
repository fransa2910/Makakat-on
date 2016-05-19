package com.makakaton.dm.ac.tec.makakaton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GOTOUCH on 4/25/2016.
 */
public class Score implements Comparable<Score> {

    public Score(int num){
        scoreNum=num;
    }

    private int scoreNum;

    public int getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(int scoreNum) {

        this.scoreNum = scoreNum;
    }

    public int compareTo(Score sc){
        //return 0 if equal
        //1 if passed greater than this
        //-1 if this greater than passed
        return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
    }

    public String getScoreText()
    {
        return ""+scoreNum;
    }


}

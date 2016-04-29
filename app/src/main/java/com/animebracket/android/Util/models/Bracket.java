package com.animebracket.android.Util.models;

import java.io.Serializable;

/**
 * Created by Noah Pederson on 1/24/2015.
 */
public class Bracket implements Serializable {

    private int id;
    private String name;
    private String perma;
    private long start;
    private int state;
    private String pic;
    private int winnerCharacterId;
    private CharacterInfo winner;
    private String rules;
    private int source;
    private int advanceHour;
    private String nameLabel;
    private String sourceLabel;

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }

    public String getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    private CharacterInfo[] randomCharacterInfos = new CharacterInfo[0];

    public CharacterInfo[] getRandomCharacterInfos() {
        return randomCharacterInfos;
    }

    public void setRandomCharacterInfos(CharacterInfo[] randomCharacterInfos) {
        this.randomCharacterInfos = randomCharacterInfos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerma() {
        return perma;
    }

    public void setPerma(String perma) {
        this.perma = perma;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getWinnerCharacterId() {
        return winnerCharacterId;
    }

    public void setWinnerCharacterId(int winnerCharacterId) {
        this.winnerCharacterId = winnerCharacterId;
    }

    public CharacterInfo getWinner() {
        return winner;
    }

    public void setWinner(CharacterInfo winner) {
        this.winner = winner;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getAdvanceHour() {
        return advanceHour;
    }

    public void setAdvanceHour(int advanceHour) {
        this.advanceHour = advanceHour;
    }
}

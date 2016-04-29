package com.animebracket.android.Util.models;

import java.io.Serializable;

/**
 * Created by noah on 10/30/2015.
 */
public class Round implements Serializable {
    //Bracket/round info
    private int id;
    private int bracketId;
    private int tier;
    private int order;
    private int group;

    //Character 1 info
    private int character1Id;
    private CharacterInfo character1;
    private int character1Votes;

    //Character 2 info
    private int character2Id;
    private CharacterInfo character2;
    private int getCharacter2Votes;

    private boolean voted;
    private int votedCharacterId;
    private boolean isFinal; //"final" is a reserved keyword in java, so I can't use it as a name
    //I will probably have to alias this using a custom TypeAdapter, which will suck


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBracketId() {
        return bracketId;
    }

    public void setBracketId(int bracketId) {
        this.bracketId = bracketId;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getCharacter1Id() {
        return character1Id;
    }

    public void setCharacter1Id(int character1Id) {
        this.character1Id = character1Id;
    }

    public CharacterInfo getCharacter1() {
        return character1;
    }

    public void setCharacter1(CharacterInfo character1) {
        this.character1 = character1;
    }

    public int getCharacter1Votes() {
        return character1Votes;
    }

    public void setCharacter1Votes(int character1Votes) {
        this.character1Votes = character1Votes;
    }

    public int getCharacter2Id() {
        return character2Id;
    }

    public void setCharacter2Id(int character2Id) {
        this.character2Id = character2Id;
    }

    public CharacterInfo getCharacter2() {
        return character2;
    }

    public void setCharacter2(CharacterInfo character2) {
        this.character2 = character2;
    }

    public int getGetCharacter2Votes() {
        return getCharacter2Votes;
    }

    public void setGetCharacter2Votes(int getCharacter2Votes) {
        this.getCharacter2Votes = getCharacter2Votes;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public int getVotedCharacterId() {
        return votedCharacterId;
    }

    public void setVotedCharacterId(int votedCharacterId) {
        this.votedCharacterId = votedCharacterId;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

}

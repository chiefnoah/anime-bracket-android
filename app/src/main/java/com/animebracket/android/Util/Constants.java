package com.animebracket.android.Util;

/**
 * Created by Noah Pederson on 1/22/2015.
 */
public class Constants {

    //URLs
    public final static String BASE_URL = "http://animebracket.com";

    public final static String BRACKETS_LIST_URL = BASE_URL + "/api/brackets/";
    public final static String CHARACTER_IMAGES_URL = "http://cdn.awwni.me/bracket/"; //to be immediately followed by the image name
    public final static String TOURNAMENT_IMAGES_URL = BASE_URL + "/images/"; //Server directory that holds images for the brackets
    public final static String CHARACTERS_LIST_URL = BASE_URL + "/api/characters/?bracketId="; //immediately followed by the ID of the bracket you want to get the list of characters for
    public final static String BRACKET_ACTIVE_ROUND_URL = BASE_URL + "/api/rounds/?bracketId="; //followed by bracket ID
    public final static String BRACKET_ALL_ROUNDS_URL = BASE_URL + "/api/bracket/?bracketId="; //followed by bracket ID
    public final static String REDDIT_OAUTH_URL = BASE_URL + "/api/login/";
    public final static String REDDIT_LOGGED_IN_USER_DETAILS_URL = BASE_URL + "/api/user/";
    public final static String POST_VOTE_URL = BASE_URL + "/submit/?action=vote";

    //Bracket State values
    public final static int BRACKET_STATE_NOMINATIONS = 1;
    public final static int BRACKET_STATE_ELIMINATIONS = 2;
    public final static int BRACKET_STATE_VOTING = 3;
    public final static int BRACKET_STATE_WILDCARD = 4;
    public final static int BRACKET_STATE_FINAL = 5;
    public final static int BRACKET_STATE_HIDDEN = 6;

    //Group Letter
    public final static char GROUP[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    //Log/debug Constants
    public final static String COOKIE = "COOKIE";
}

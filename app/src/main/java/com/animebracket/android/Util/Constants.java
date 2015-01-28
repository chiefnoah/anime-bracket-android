package com.animebracket.android.Util;

/**
 * Created by Noah Pederson on 1/22/2015.
 */
public class Constants {

    //URLs
    public final static String BASE_URL = "http://animebracket.com";

    public final static String BRACKETS_LIST_URL = "/api/brackets/";
    public final static String CHARACTER_IMAGES_URL = "http://cdn.awwni.me/bracket/"; //to be immediately followed by the image name
    public final static String TOURNAMENT_IMAGES_URL = "/images/"; //Server directory that holds images for the brackets
    public final static String CHARACTERS_LIST_URL = "/api/characters/"; //immediately followed by the ID of the bracket you want to get the list of characters for
    public final static String BRACKET_ACTIVE_ROUND_URL = "/api/rounds/"; //followed by bracket ID
    public final static String BRACKET_ALL_ROUNDS_URL = "/api/bracket/"; //followed by bracket ID
    public final static String REDDIT_OAUTH_URL = "/api/login/";
    public final static String USER_DETAILS_URL = "/api/user/";
    public final static String POST_VOTE_URL = "/submit/";
    public final static String TYPEAHEAD_URL = "/typeahead/";

    //Reddit
    public final static String REDDIT_URL = "http://reddit.com";

    //Bracket State values
    public final static int BRACKET_STATE_NOMINATIONS = 1;
    public final static int BRACKET_STATE_ELIMINATIONS = 2;
    public final static int BRACKET_STATE_VOTING = 3;
    public final static int BRACKET_STATE_WILDCARD = 4;
    public final static int BRACKET_STATE_FINAL = 5;
    public final static int BRACKET_STATE_HIDDEN = 6;

    public final static String[] BRACKET_INFO_STATE = {"error", "Now accepting nominations", "Eliminations round", "Voting in session", "Wildcard round", "Finals", "Hidden"};
    public final static String[] BRACKET_ACTION_STATE = {"error", "Nominate", "Vote", "Vote", "Vote", "Results", "Hidden"};

    //Group Letter
    public final static char GROUP[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    public static class FLAGS {
        public final static String COOKIE = "COOKIE";
        public final static String JSON = "JSON";
        public final static String LOGGEDIN = "LOGGED IN";
        public final static String GLOBAL_PREFERENCES = "GLOBAL PREFERENCES";
        public final static String BRACKET_ARG = "BRACKET ARG";
        public final static String USER_INFO_BUNDLE = "USER INFO";
        public final static String RUNNING_BRACKETS_BUNDLE = "RUNNING BRACKETS";
    }
    //Log/debug Constants

}

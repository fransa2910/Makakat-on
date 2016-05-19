package com.makakaton.dm.ac.tec.makakaton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.internal.game.Acls;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;

public class MultiplayerVR extends CardboardActivity implements RoomStatusUpdateListener,
        RoomUpdateListener, GoogleApiClient.ConnectionCallbacks, RealTimeMessageReceivedListener,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
        OnInvitationReceivedListener, CardboardView.StereoRenderer {

    private String txtOptInterface ="";
    // MULTI
    private boolean mPlaying = false;
    private final static int MIN_PLAYERS = 2;
    private GoogleApiClient mGoogleApiClient;
    private String mRoomId;
    private int isEveryoneReady = 0;
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;
    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;
    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    private ArrayList<Participant> mParticipants = null;
    final static String TAG = "Multi";
    private byte[] msgBuff = new byte[100];
    String mMyId = null;
    private boolean mSignInClicked = false;
    int mCurScreen = -1;
    String mIncomingInvitationId = null;
    boolean mMultiplayer = false;

    TextView txtUsername;
    ImageView imgUser;

    // Auto Sign in
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = true;
    //MULTI


    // ------------------------------------------------- VR------------------------------------///

    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "HighscoreFile";

    private String extraLvl3;

    private boolean gameStarted = false;

    private TextView txtTimerMsg;
    private TextView txtScoreL;


    private Integer gameMode;
    private Integer gameDifficulty;

    private CountDownTimer timer;

    private Random random;
    private MathOperation mathOperation;

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    private static final float CAMERA_Z = 0.01f;
    private static final float TIME_DELTA = 0.3f;

    private static final float YAW_LIMIT = 0.12f;
    private static final float PITCH_LIMIT = 0.12f;

    private static final int COORDS_PER_VERTEX = 3;

    // We keep the light always position just above the user.
    private static final float[] LIGHT_POS_IN_WORLD_SPACE = new float[]{0.0f, 2.0f, 0.0f, 1.0f};

    private static final float MIN_MODEL_DISTANCE = 3.0f;
    private static final float MAX_MODEL_DISTANCE = 7.0f;

    private static final String SOUND_FILE = "cube_sound.wav";

    private final float[] lightPosInEyeSpace = new float[4];

    private double[] options;
    private FloatBuffer floorVertices;
    private FloatBuffer floorColors;
    private FloatBuffer floorNormals;

    private FloatBuffer cubeVertices;
    private FloatBuffer cubeColors;
    private FloatBuffer cubeFoundColors;
    private FloatBuffer cubeNormals;

    private FloatBuffer rParentVertices;
    private FloatBuffer rParentColors;
    private FloatBuffer rParentFoundColors;
    private FloatBuffer rParentNormals;


    private FloatBuffer lParentVertices;
    private FloatBuffer lParentColors;
    private FloatBuffer lParentFoundColors;
    private FloatBuffer lParentNormals;

    private FloatBuffer dotVertices;
    private FloatBuffer dotColors;
    private FloatBuffer dotFoundColors;
    private FloatBuffer dotNormals;

    private FloatBuffer plusVertices;
    private FloatBuffer plusColors;
    private FloatBuffer plusFoundColors;
    private FloatBuffer plusNormals;

    private FloatBuffer minusVertices;
    private FloatBuffer minusColors;
    private FloatBuffer minusFoundColors;
    private FloatBuffer minusNormals;

    private FloatBuffer timesVertices;
    private FloatBuffer timesColors;
    private FloatBuffer timesFoundColors;
    private FloatBuffer timesNormals;

    private FloatBuffer divisionVertices;
    private FloatBuffer divisionColors;
    private FloatBuffer divisionFoundColors;
    private FloatBuffer divisionNormals;

    private FloatBuffer oneVertices;
    private FloatBuffer oneColors;
    private FloatBuffer oneFoundColors;
    private FloatBuffer oneNormals;

    private FloatBuffer twoVertices;
    private FloatBuffer twoColors;
    private FloatBuffer twoFoundColors;
    private FloatBuffer twoNormals;

    private FloatBuffer threeVertices;
    private FloatBuffer threeColors;
    private FloatBuffer threeFoundColors;
    private FloatBuffer threeNormals;

    private FloatBuffer fourVertices;
    private FloatBuffer fourColors;
    private FloatBuffer fourFoundColors;
    private FloatBuffer fourNormals;

    private FloatBuffer fiveVertices;
    private FloatBuffer fiveColors;
    private FloatBuffer fiveFoundColors;
    private FloatBuffer fiveNormals;

    private FloatBuffer sixVertices;
    private FloatBuffer sixColors;
    private FloatBuffer sixFoundColors;
    private FloatBuffer sixNormals;

    private FloatBuffer sevenVertices;
    private FloatBuffer sevenColors;
    private FloatBuffer sevenFoundColors;
    private FloatBuffer sevenNormals;

    private FloatBuffer eightVertices;
    private FloatBuffer eightColors;
    private FloatBuffer eightFoundColors;
    private FloatBuffer eightNormals;

    private FloatBuffer nineVertices;
    private FloatBuffer nineColors;
    private FloatBuffer nineFoundColors;
    private FloatBuffer nineNormals;

    private FloatBuffer zeroVertices;
    private FloatBuffer zeroColors;
    private FloatBuffer zeroFoundColors;
    private FloatBuffer zeroNormals;


    private int cubeProgram;
    private int program;
    private int rParentProgram;
    private int lParentProgram;
    private int dotProgram;
    private int plusProgram;
    private int minusProgram;
    private int timesProgram;
    private int divisionProgram;
    private int oneProgram;
    private int twoProgram;
    private int threeProgram;
    private int fourProgram;
    private int fiveProgram;
    private int sixProgram;
    private int sevenProgram;
    private int eightProgram;
    private int nineProgram;
    private int zeroProgram;
    private int floorProgram;

    private int cubePositionParam;
    private int cubeNormalParam;
    private int cubeColorParam;
    private int cubeModelParam;
    private int cubeModelViewParam;
    private int cubeModelViewProjectionParam;
    private int cubeLightPosParam;

    private int positionParam;
    private int normalParam;
    private int colorParam;
    private int modelParam;
    private int modelViewParam;
    private int modelViewProjectionParam;
    private int lightPosParam;

    private int rParentPositionParam;
    private int rParentNormalParam;
    private int rParentColorParam;
    private int rParentModelParam;
    private int rParentModelViewParam;
    private int rParentModelViewProjectionParam;
    private int rParentLightPosParam;

    private int lParentPositionParam;
    private int lParentNormalParam;
    private int lParentColorParam;
    private int lParentModelParam;
    private int lParentModelViewParam;
    private int lParentModelViewProjectionParam;
    private int lParentLightPosParam;

    private int dotPositionParam;
    private int dotNormalParam;
    private int dotColorParam;
    private int dotModelParam;
    private int dotModelViewParam;
    private int dotModelViewProjectionParam;
    private int dotLightPosParam;

    private int plusPositionParam;
    private int plusNormalParam;
    private int plusColorParam;
    private int plusModelParam;
    private int plusModelViewParam;
    private int plusModelViewProjectionParam;
    private int plusLightPosParam;

    private int minusPositionParam;
    private int minusNormalParam;
    private int minusColorParam;
    private int minusModelParam;
    private int minusModelViewParam;
    private int minusModelViewProjectionParam;
    private int minusLightPosParam;

    private int timesPositionParam;
    private int timesNormalParam;
    private int timesColorParam;
    private int timesModelParam;
    private int timesModelViewParam;
    private int timesModelViewProjectionParam;
    private int timesLightPosParam;

    private int divisionPositionParam;
    private int divisionNormalParam;
    private int divisionColorParam;
    private int divisionModelParam;
    private int divisionModelViewParam;
    private int divisionModelViewProjectionParam;
    private int divisionLightPosParam;

    private int onePositionParam;
    private int oneNormalParam;
    private int oneColorParam;
    private int oneModelParam;
    private int oneModelViewParam;
    private int oneModelViewProjectionParam;
    private int oneLightPosParam;

    private int twoPositionParam;
    private int twoNormalParam;
    private int twoColorParam;
    private int twoModelParam;
    private int twoModelViewParam;
    private int twoModelViewProjectionParam;
    private int twoLightPosParam;


    private int threePositionParam;
    private int threeNormalParam;
    private int threeColorParam;
    private int threeModelParam;
    private int threeModelViewParam;
    private int threeModelViewProjectionParam;
    private int threeLightPosParam;

    private int fourPositionParam;
    private int fourNormalParam;
    private int fourColorParam;
    private int fourModelParam;
    private int fourModelViewParam;
    private int fourModelViewProjectionParam;
    private int fourLightPosParam;


    private int fivePositionParam;
    private int fiveNormalParam;
    private int fiveColorParam;
    private int fiveModelParam;
    private int fiveModelViewParam;
    private int fiveModelViewProjectionParam;
    private int fiveLightPosParam;

    private int sixPositionParam;
    private int sixNormalParam;
    private int sixColorParam;
    private int sixModelParam;
    private int sixModelViewParam;
    private int sixModelViewProjectionParam;
    private int sixLightPosParam;

    private int sevenPositionParam;
    private int sevenNormalParam;
    private int sevenColorParam;
    private int sevenModelParam;
    private int sevenModelViewParam;
    private int sevenModelViewProjectionParam;
    private int sevenLightPosParam;

    private int eightPositionParam;
    private int eightNormalParam;
    private int eightColorParam;
    private int eightModelParam;
    private int eightModelViewParam;
    private int eightModelViewProjectionParam;
    private int eightLightPosParam;

    private int ninePositionParam;
    private int nineNormalParam;
    private int nineColorParam;
    private int nineModelParam;
    private int nineModelViewParam;
    private int nineModelViewProjectionParam;
    private int nineLightPosParam;

    private int zeroPositionParam;
    private int zeroNormalParam;
    private int zeroColorParam;
    private int zeroModelParam;
    private int zeroModelViewParam;
    private int zeroModelViewProjectionParam;
    private int zeroLightPosParam;

    private int floorPositionParam;
    private int floorNormalParam;
    private int floorColorParam;
    private int floorModelParam;
    private int floorModelViewParam;
    private int floorModelViewProjectionParam;
    private int floorLightPosParam;

    private float[] modelCube;
    private ArrayList<float[]> modelsOperation;
    private ArrayList<float[]> models1;
    private ArrayList<float[]> models2;
    private ArrayList<float[]> models3;
    private ArrayList<float[]> models4;


    private float[] modelOperation;
    private float[] modelOne;
    private float[] modelTwo;
    private float[] modelThree;
    private float[] modelFour;


    private float[] camera;
    private float[] view;
    private float[] headView;
    private float[] modelViewProjection;
    private float[] modelViewOperation;
    private float[] modelViewOpt1;
    private float[] modelViewOpt2;
    private float[] modelViewOpt3;
    private float[] modelViewFloor;
    private float[] modelFloor;

    private float[] modelPositionOperation;
    private float[] modelPositionOne;
    private float[] modelPositionTwo;
    private float[] modelPositionThree;
    private float[] modelPositionFour;
    private float[] modelPositionN;
    private float[] modelPositionS;
    private float[] modelPositionE;
    private float[] modelPositionO;

    private float[] headRotation;

    private int score;
    private int lives;
    private int miliseconds = 60000;
    private float objectDistanceOne = MAX_MODEL_DISTANCE / 2.0f;
    private float objectDistanceTwo = MAX_MODEL_DISTANCE / 2.0f;
    private float floorDepth = 10f;

    private Vibrator vibrator;
    private CardboardOverlayView overlayView;

    private CardboardAudioEngine cardboardAudioEngine;
    private volatile int soundId = CardboardAudioEngine.INVALID_ID;

    private double result;
    private String level;
    private String operation;
    private TextView txtOpt;

    private double[] optionsCliente;
    String result1;
    String result2;

    MediaPlayer backgroundMusic;


    // ------------------------------------------------- VR------------------------------------///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_vr);

        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(this);
        setCardboardView(cardboardView);

        Games.GamesOptions gamesOptions = Games.GamesOptions.builder().setShowConnectingPopup(true,
                Gravity.TOP).build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API, gamesOptions).addScope(Games.SCOPE_GAMES)
                .setViewForPopups(findViewById(android.R.id.content))
                .addApi(AppIndex.API).build();

//        // set up a click listener for everything we care about
        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }
        optionsCliente = new double[4];
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        imgUser = (ImageView) findViewById(R.id.imgUser);


        // VR

        mathOperation = new MathOperation();
        gameStarted = false;


        random = new Random();
        options = new double[4];
        //modelCube = new float[16];
        modelsOperation = new ArrayList<float[]>();

        models1 = new ArrayList<float[]>();
        models2 = new ArrayList<float[]>();
        models3 = new ArrayList<float[]>();
        models4 = new ArrayList<float[]>();


        modelOne = new float[16];

        modelTwo = new float[16];

        modelOperation = new float[16];

        modelThree = new float[16];

        modelFour = new float[16];

        camera = new float[16];
        view = new float[16];
        modelViewProjection = new float[16];
        modelViewOperation = new float[16];

        modelViewOpt1 = new float[16];

        modelViewOpt2 = new float[16];
        modelViewOpt3 = new float[16];

        modelViewFloor = new float[16];
        modelFloor = new float[16];
        // Model first appears directly in front of user.
        modelPositionOperation = new float[]{0.0f, 50.0f, 0.0f};
        modelPositionOne = new float[]{-50.0f, 50.0f, 0.0f};/*-MAX_MODEL_DISTANCE / 2.0f*/
        modelPositionTwo = new float[]{0.0f, 0.0f, -50.0f};/*-MAX_MODEL_DISTANCE / 2.0f*/
        modelPositionThree = new float[]{0.0f, 0.0f, 50.0f};/*-MAX_MODEL_DISTANCE / 2.0f*/
        modelPositionFour = new float[]{50.0f, 0.0f, 0.0f};


        headRotation = new float[4];
        headView = new float[16];
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        overlayView = (CardboardOverlayView) findViewById(R.id.overlayMulti);


        // Initialize 3D audio engine.
        cardboardAudioEngine =
                new CardboardAudioEngine(getAssets(), CardboardAudioEngine.RenderingQuality.HIGH);

        score = 0;
        lives = 3;

        txtTimerMsg = (TextView) findViewById(R.id.txtTimerMsg);
        txtScoreL = (TextView) findViewById(R.id.txtScoreL);

        txtScoreL.setText(String.valueOf(score));


        txtOpt = (TextView) findViewById(R.id.txtOperationMulti);

        gameMode = getIntent().getIntExtra("gameMode", R.string.rush);
        gameDifficulty = getIntent().getIntExtra("gameDifficulty", R.string.level1);

        switch (gameDifficulty) {
            case R.string.level1:
                level = "LEVEL 1";
                break;
            case R.string.level2:
                level = "LEVEL 2";
                break;
            case R.string.level3:
                level = "LEVEL 3";
                break;
        }


        stopService(new Intent(this, Music.class));
        backgroundMusic = MediaPlayer.create(this, R.raw.game_song);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        gamePrefs = getSharedPreferences(GAME_PREFS, 0);
    }


    @Override
    protected void onStop() {

        Log.d(TAG, "**** got onStop");
        // if we're in a room, leave it.
        leaveRoom();

        // stop trying to keep the screen on
        stopKeepingScreenOn();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.btSignIn);
        } else {
            switchToScreen(R.id.screen_wait);
        }
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStart() {

        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.w(TAG,
                    "GameHelper: client was already connected on onStart()");
        } else {
            Log.d(TAG, "Connecting client.");
            mGoogleApiClient.connect();
        }
        super.onStart();

    }

    void switchToScreen(int screenId) {
        // make the requested screen visible; hide all others.
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
        }
        mCurScreen = screenId;

        // should we show the invitation popup?
        boolean showInvPopup;
        if (mIncomingInvitationId == null) {
            // no invitation, so no popup
            showInvPopup = false;
        } else if (mMultiplayer) {
            // if in multiplayer, only show invitation on main screen
            showInvPopup = (mCurScreen == R.id.btSetup);
        } else {
            // single-player: show on main screen and gameplay screen
            showInvPopup = (mCurScreen == R.id.btSetup || mCurScreen == R.id.btSetup);
        }
        findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
    }

    void switchToMainScreen() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.btSetup);
        } else {
            switchToScreen(R.id.btSignIn);
        }
    }

    public boolean partyEnough(Room room) {
        int connectedPlayers = 0;
        for (Participant participant : room.getParticipants()) {
            if (participant.isConnectedToRoom()) ++connectedPlayers;
        }
        return connectedPlayers >= MIN_PLAYERS;
    }


    @Override
    public void onPeerLeft(Room room, List<String> list) {
        if (!mPlaying && cancelTheGame(room)) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        //get participants and my ID:
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));

        // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
        if (mRoomId == null)
            mRoomId = room.getRoomId();

        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
        showGameError();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        if (mPlaying) {

        } else if (partyEnough(room)) {

        }
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        if (mPlaying) {

        } else if (cancelTheGame(room)) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, null, mRoomId);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }


    // Handle the result of the "Select players UI" we launched when the user clicked the
    // "Invite friends" button. We react by creating a room with those players.
    private void handleSelectPlayersResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** select players UI cancelled, " + response);
            switchToMainScreen();
            return;
        }

        Log.d(TAG, "Select players UI succeeded.");

        // get the invitee list
        final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        Log.d(TAG, "Invitee count: " + invitees.size());

        // get the automatch criteria
        Bundle autoMatchCriteria = null;
        int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
                    minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
        }

        // create the room
        Log.d(TAG, "Creating room...");
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.addPlayersToInvite(invitees);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        if (autoMatchCriteria != null) {
            rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        }
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
        Log.d(TAG, "Room created, waiting for it to be ready...");
    }

    // Handle the result of the invitation inbox UI, where the player can pick an invitation
    // to accept. We react by accepting the selected invitation, if any.
    private void handleInvitationInboxResult(int response, Intent data) {
        if (response != Activity.RESULT_OK) {
            Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
            switchToMainScreen();
            return;
        }

        Log.d(TAG, "Invitation inbox UI succeeded.");
        Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

        // accept invitation
        acceptInviteToRoom(inv.getInvitationId());
    }


    void startGame(boolean multiplayer) {
        mMultiplayer = multiplayer;
        switchToScreen(R.id.mtGame);
    }

    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            switchToScreen(R.id.screen_wait);
        } else {
            switchToMainScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_SELECT_PLAYERS:
                // we got the result from the "select players" UI -- ready to create the room
                handleSelectPlayersResult(responseCode, intent);
                break;
            case RC_INVITATION_INBOX:
                // we got the result from the "select invitation" UI (invitation inbox). We're
                // ready to accept the selected invitation:
                handleInvitationInboxResult(responseCode, intent);
                break;
            case RC_WAITING_ROOM:
                // we got the result from the "waiting room" UI.
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    startGame(true);

                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {
                    // Dialog was cancelled (user pressed back key, for instance). In our game,
                    // this means leaving the room too. In more elaborate games, this could mean
                    // something else (like minimizing the waiting room UI).
                    leaveRoom();
                }
                break;
            case RC_SIGN_IN:
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this, requestCode, responseCode, R.string.signin_other_error);
                }
                break;
        }
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }

        // save room ID so we can leave cleanly before the game starts.
        mRoomId = room.getRoomId();

        // show the waiting room UI
        showWaitingRoom(room);
    }

    @Override
    public void onJoinedRoom(int i, Room room) {


        Log.d(TAG, "onRoomCreated(" + i + ", " + room + ")");
        if (i != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + i);
            showGameError();
            return;
        }

        // save room ID so we can leave cleanly before the game starts.
        mRoomId = room.getRoomId();

        // show the waiting room UI
        showWaitingRoom(room);
    }

    void showWaitingRoom(Room room) {
        // minimum number of players required for our game
        // For simplicity, we require everyone to join the game before we start it
        // (this is signaled by Integer.MAX_VALUE).
        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);

        // show waiting room UI
        startActivityForResult(i, RC_WAITING_ROOM);
    }


    private RoomConfig.Builder makeBasicRoomConfigBuilder() {
        return RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        Log.d(TAG, "onConnected() called. Sign in successful!");

        Log.d(TAG, "Sign-in succeeded.");

        Player currentPlayer = Games.Players.getCurrentPlayer(mGoogleApiClient);
        ImageManager imageManager = ImageManager.create(this);
        Uri imgUri = currentPlayer.getIconImageUri();
        String userName = currentPlayer.getDisplayName();

        // register listener so we are notified if we receive an invitation to play
        // while we are in the game
        Games.Invitations.registerInvitationListener(mGoogleApiClient, this);

        if (connectionHint != null) {
            Log.d(TAG, "onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint
                    .getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                // retrieve and cache the invitation ID
                Log.d(TAG, "onConnected: connection hint has a room invite!");
                acceptInviteToRoom(inv.getInvitationId());
                return;
            }
        }


        txtUsername.setText(userName);
        imageManager.loadImage(imgUser, imgUri);

        switchToMainScreen();
    }

    public void sendMessageScore(int score){
        String scoreTag ="score," + score ;
        for (Participant participant : mParticipants) {
            if (!participant.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, scoreTag.getBytes(),
                        mRoomId, participant.getParticipantId());

            }
        }

    }

    public void sendMessageOnReady() {
        String ready = "ready,"+mMyId;
        Log.e("sendmessasgeonreay?","yes!");
        for (Participant participant : mParticipants) {
            if (!participant.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, ready.getBytes(),
                        mRoomId, participant.getParticipantId());

            }
        }
    }

    public void sendMessageOperation() {
        operation = mathOperation.mathLevel(level);
        extraLvl3 = "";
        if (gameDifficulty == R.string.level3) {
            if (mathOperation.getTypeFunction().equals("∫")) {
                extraLvl3 = ", " + mathOperation.getDerORInt0() + "→ " + mathOperation.getDerORInt1();
            } else {
                extraLvl3 = ", x→" + mathOperation.getDerORInt1();
            }

        }


        mathOperation.increaseRounds();


        int whichOpt = random.nextInt(4);

        if (gameDifficulty == R.string.level1 || gameDifficulty == R.string.level2) {
            options[whichOpt] = Math.round(mathOperation.eval(operation));
        } else if (gameDifficulty == R.string.level3) {
            options[whichOpt] = Math.round(mathOperation.evalIntegralAndDerivative(operation));
        }

        boolean keepResetting = true;
        while (keepResetting){
            //overlayView.show3DToast("result is " + String.valueOf(options[whichOpt]));
            for (int i = 0; i < options.length; i++) {
                if (i != whichOpt) {
                    options[i] = WorldLayoutData.makeItWrong(options[whichOpt]);
                }

            }
            int equal = 0;
            for (int dup = 0; dup < options.length; dup++) {

                for (int j = dup + 1; j < options.length; j++) {
                    if (dup==2 && options[dup]!=options[j] &&equal==0) {
                        keepResetting=false;
                    }else if (options[dup]==options[j]){
                        equal = 1;
                    }
                }
            }
        }



        //hideObject(operation, 0);


        /*hideObject(String.valueOf(options[0]), 1);

        hideObject(String.valueOf(options[1]), 2);
        hideObject(String.valueOf(options[2]), 3);
        hideObject(String.valueOf(options[3]), 4);*/
        //clearModels();
        String operationTag = "operation," + operation +extraLvl3;
        String options0Tag = "options0," + options[0];
        String options1Tag = "options1," + options[1];
        String options2Tag = "options2," + options[2];
        String options3Tag = "options3," + options[3];
        for (Participant participant : mParticipants) {
            if (!participant.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, operationTag.getBytes(),
                        mRoomId, participant.getParticipantId());
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, options0Tag.getBytes(),
                        mRoomId, participant.getParticipantId());
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, options1Tag.getBytes(),
                        mRoomId, participant.getParticipantId());
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, options2Tag.getBytes(),
                        mRoomId, participant.getParticipantId());
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, options3Tag.getBytes(),
                        mRoomId, participant.getParticipantId());

            }
        }
    }


    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        byte[] b = realTimeMessage.getMessageData();
        String isEveryoneReadyStr;
        String b1 = new String(b);
        String result = b1.split(",")[0];
        String result1 = b1.split(",")[1];

        Log.e("result",b1.split(",")[0]);
        switch (result) {

            case "ready":
                for (Participant participant : mParticipants) {
                    if (!participant.getParticipantId().equals(mMyId)) {
                        isEveryoneReady++;
                        isEveryoneReadyStr = String.valueOf(isEveryoneReady);
                        sendMessageIsEveryoneReady("isEveryoneReady," + isEveryoneReadyStr);


                    }
                }
                break;
            case "isEveryoneReady":
                isEveryoneReady = Integer.valueOf(result1);
                break;
            case "operation":

                txtOptInterface = result1;
                txtOpt.setText(txtOptInterface);
                Log.e("result1",result1);
                break;
            case "options0":
                clearModels();
                options[0] = Double.valueOf(result1);
                hideObject(String.valueOf(options[0]), 1);


                break;
            case "options1":
                options[1] = Double.valueOf(result1);
                hideObject(String.valueOf(options[1]), 2);

                break;
            case "options2":
                options[2] = Double.valueOf(result1);
                hideObject(String.valueOf(options[2]), 3);

                break;
            case "options3":
                options[3] = Double.valueOf(result1);
                hideObject(String.valueOf(options[3]), 4);

                break;

            case "score":
                scoreUpdate(Integer.valueOf(result1));

                break;
            default:
                Log.e("ohhhhhhhhhhhh my gosh","ooooooh");
                break;
        }


        Log.i(TAG, b.toString());
    }

    public void sendMessageIsEveryoneReady(String isEveryoneReady) {
        for (Participant participant : mParticipants) {
            if (!participant.getParticipantId().equals(mMyId)) {
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, isEveryoneReady.getBytes(),
                        mRoomId, participant.getParticipantId());

            }
        }
    }

    // UI

    final static int[] CLICKABLES = {
            R.id.btnInvAmigos, R.id.button_sign_in, R.id.btnInvi
    };

    final static int[] SCREENS = {
            R.id.btSetup, R.id.btSignIn, R.id.screen_wait, R.id.mtGame
    };

    // Accept the given invitation.
    void acceptInviteToRoom(String invId) {
        // accept the invitation
        Log.d(TAG, "Accepting invitation: " + invId);
        RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
        roomConfigBuilder.setInvitationIdToAccept(invId)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button_sign_in:
                if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
                    Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                }
                Log.d(TAG, "Sign-in button clicked");
                mSignInClicked = true;
                mGoogleApiClient.connect();
                break;
            case R.id.btnInvAmigos:
                // show list of invitable players
                intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
                switchToScreen(R.id.screen_wait);
                startActivityForResult(intent, RC_SELECT_PLAYERS);
                break;

            case R.id.btnInvi:
                intent = Games.Invitations.getInvitationInboxIntent(mGoogleApiClient);
                switchToScreen(R.id.screen_wait);
                startActivityForResult(intent, RC_INVITATION_INBOX);
                break;

            case R.id.button_accept_popup_invitation:
                acceptInviteToRoom(mIncomingInvitationId);
                mIncomingInvitationId = null;
                break;

        }
    }

    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mInSignInFlow) {
            mInSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }

        switchToScreen(R.id.btSignIn);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLeftRoom(int i, String s) {
        Log.d(TAG, "onLeftRoom, code " + i);
        switchToMainScreen();
    }


    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.game_problem));
        switchToMainScreen();
    }

    @Override
    public void onRoomConnected(int i, Room room) {

    }

    @Override
    public void onP2PConnected(String s) {

    }

    @Override
    public void onP2PDisconnected(String s) {

    }

    public boolean cancelTheGame(Room room) {
        return false;
    }


    @Override
    public void onRoomConnecting(Room room) {

    }


    @Override
    public void onRoomAutoMatching(Room room) {

    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {

    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {

    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {

    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        mIncomingInvitationId = invitation.getInvitationId();
        ((TextView) findViewById(R.id.incoming_invitation_text)).setText(
                invitation.getInviter().getDisplayName() + " " +
                        getString(R.string.is_inviting_you));
        switchToScreen(mCurScreen); // This will show the invitation popup
    }

    @Override
    public void onInvitationRemoved(String s) {
        if (mIncomingInvitationId.equals(s) && mIncomingInvitationId != null) {
            mIncomingInvitationId = null;
            switchToScreen(mCurScreen); // This will hide the invitation popup
        }
    }


    // ------------------------------------------------- VR------------------------------------///

    private int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }

    @Override
    public void onPause() {
        cardboardAudioEngine.pause();
        super.onPause();
        backgroundMusic.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        cardboardAudioEngine.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundMusic.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        startService(new Intent(this, Music.class));

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        backgroundMusic = MediaPlayer.create(this, R.raw.game_song);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

    }


    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Build the Model part of the ModelView matrix.

        //Matrix.rotateM(modelThree, 0, TIME_DELTA, 0.5f, 0.5f, 1.0f);

        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        headTransform.getHeadView(headView, 0);

        // Update the 3d audio engine with the most recent head rotation.
        headTransform.getQuaternion(headRotation, 0);
        cardboardAudioEngine.setHeadRotation(
                headRotation[0], headRotation[1], headRotation[2], headRotation[3]);

        checkGLError("onReadyToDraw");
    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        checkGLError("colorParam");

        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);

        // Set the position of the light
        Matrix.multiplyMV(lightPosInEyeSpace, 0, view, 0, LIGHT_POS_IN_WORLD_SPACE, 0);

        // Build the ModelView and ModelViewProjection matrices
        // for calculating cube position and light.
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
/*    Matrix.multiplyMM(modelView, 0, view, 0, modelCube, 0);
    Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);*/
        //drawCube();


   /* Matrix.multiplyMM(modelViewOpt1, 0, view, 0, modelOne, 0);
    Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);*/

      /*MAIN ALGORITHM*/



      /*MAIN ALGORITHM*/


        //drawOne();
        //drawNumbers(operation, perspective, modelsOperation, "UP");


        drawNumbers(String.valueOf(options[0]), perspective, models1, "O");
        drawNumbers(String.valueOf(options[1]), perspective, models2, "N");
        drawNumbers(String.valueOf(options[2]), perspective, models3, "S");
        drawNumbers(String.valueOf(options[3]), perspective, models4, "E");


        // drawNumbers("1256", perspective, modelViewOpt2, models2);
        //drawNumbers("777", perspective, modelViewOpt3, models3);

      /*Matrix.multiplyMM(modelViewOpt2, 0, view, 0, modelTwo, 0);
      Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt2, 0);
      drawTwo();
*/

        // Set modelView for the floor, so we draw floor in the correct location
        Matrix.multiplyMM(modelViewFloor, 0, view, 0, modelFloor, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewFloor, 0);
        drawFloor();
    }

    public void drawNumbers(String number, float[] perspective, ArrayList<float[]> models, String coordenada) {
        int numberLength = number.length();

        int i = 0;
        while (i < numberLength) {

            char letter = number.charAt(i);
            if (letter == '(') {
                /*LEFTPARENT*/


                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(lParentLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(lParentModelParam, 1, false, models.get(i), 0);


                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(lParentModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                lParentVertices.clear();
                lParentVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.LEFTPAR_COORDS, coordenada));
                lParentVertices.position(0);
                GLES20.glVertexAttribPointer(
                        lParentPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, lParentVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(lParentModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(lParentNormalParam, 3, GLES20.GL_FLOAT, false, 0, lParentNormals);
                GLES20.glVertexAttribPointer(lParentColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? lParentFoundColors : lParentColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 18);

            } else if (letter == ')') {
                /*RIGHTPARENT*/


                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(rParentLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting


                GLES20.glUniformMatrix4fv(rParentModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(rParentModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                rParentVertices.clear();
                rParentVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.RIGHTPAR_COORDS, coordenada));
                rParentVertices.position(0);
                GLES20.glVertexAttribPointer(
                        rParentPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, rParentVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(rParentModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(rParentNormalParam, 3, GLES20.GL_FLOAT, false, 0, rParentNormals);
                GLES20.glVertexAttribPointer(rParentColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? rParentFoundColors : rParentColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 18);

            } else if (letter == '.') {
                /*DOT*/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(dotLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting


                GLES20.glUniformMatrix4fv(dotModelParam, 1, false, models.get(i), 0);
                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(dotModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                dotVertices.clear();
                dotVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.DOT_COORDS, coordenada));
                dotVertices.position(0);
                GLES20.glVertexAttribPointer(
                        dotPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, dotVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(dotModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(dotNormalParam, 3, GLES20.GL_FLOAT, false, 0, dotNormals);
                GLES20.glVertexAttribPointer(dotColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? dotFoundColors : dotColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

            } else if (letter == '+') {

        /*PLUS*/


                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(plusLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(plusModelParam, 1, false, models.get(i), 0);


                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(plusModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                plusVertices.clear();
                plusVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.PLUS_COORDS, coordenada));
                plusVertices.position(0);
                GLES20.glVertexAttribPointer(
                        plusPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, plusVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(plusModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(plusNormalParam, 3, GLES20.GL_FLOAT, false, 0, plusNormals);

                GLES20.glVertexAttribPointer(plusColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? plusFoundColors : plusColors);


                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 30);
            } else if (letter == '-') {
                 /*MINUS*/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(minusLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(minusModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(minusModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                minusVertices.clear();
                minusVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.MINUS_COORDS, coordenada));
                minusVertices.position(0);
                GLES20.glVertexAttribPointer(
                        minusPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, minusVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(minusModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(minusNormalParam, 3, GLES20.GL_FLOAT, false, 0, minusNormals);

                GLES20.glVertexAttribPointer(minusColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? minusFoundColors : minusColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 18);


            } else if (letter == '*') {
               /*TIMES*/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(timesLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(timesModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(timesModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                timesVertices.clear();
                timesVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.TIMES_COORDS, coordenada));
                timesVertices.position(0);
                GLES20.glVertexAttribPointer(
                        timesPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, timesVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(timesModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(timesColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? timesFoundColors : timesColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12);

            } else if (letter == '/') {
               /*DIVISION*/
                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(divisionLightPosParam, 1, lightPosInEyeSpace, 0);


                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(divisionModelParam, 1, false, models.get(i), 0);
                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(divisionModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                divisionVertices.clear();
                divisionVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.DIVISION_COLORS, coordenada));
                divisionVertices.position(0);
                GLES20.glVertexAttribPointer(
                        divisionPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, divisionVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(divisionModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(divisionNormalParam, 3, GLES20.GL_FLOAT, false, 0, divisionNormals);

                GLES20.glVertexAttribPointer(divisionColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? divisionFoundColors : divisionColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 18);

            } else if (letter == '1') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(oneLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(oneModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(oneModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                oneVertices.clear();
                oneVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.ONE_COORDS, coordenada));
                oneVertices.position(0);
                GLES20.glVertexAttribPointer(
                        onePositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, oneVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(oneModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(oneNormalParam, 3, GLES20.GL_FLOAT, false, 0, oneNormals);
                GLES20.glVertexAttribPointer(oneColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? oneFoundColors : oneColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 30);

            } else if (letter == '2') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(twoLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting


                GLES20.glUniformMatrix4fv(twoModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(twoModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                twoVertices.clear();
                twoVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.TWO_COORDS, coordenada));
                twoVertices.position(0);
                GLES20.glVertexAttribPointer(
                        twoPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, twoVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(twoModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(twoNormalParam, 3, GLES20.GL_FLOAT, false, 0, twoNormals);
                GLES20.glVertexAttribPointer(twoColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? twoFoundColors : twoColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 66);
            } else if (letter == '3') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(threeLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting


                GLES20.glUniformMatrix4fv(threeModelParam, 1, false, models.get(i), 0);


                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(threeModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                threeVertices.clear();
                threeVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.THREE_COORDS, coordenada));
                threeVertices.position(0);
                GLES20.glVertexAttribPointer(
                        threePositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, threeVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(threeModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(threeNormalParam, 3, GLES20.GL_FLOAT, false, 0, threeNormals);
                GLES20.glVertexAttribPointer(threeColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? threeFoundColors : threeColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 60);
            } else if (letter == '4') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(fourLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(fourModelParam, 1, false, models.get(i), 0);
                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(fourModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                fourVertices.clear();
                fourVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.FOUR_COORDS, coordenada));
                fourVertices.position(0);
                GLES20.glVertexAttribPointer(
                        fourPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, fourVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(fourModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(fourNormalParam, 3, GLES20.GL_FLOAT, false, 0, fourNormals);
                GLES20.glVertexAttribPointer(fourColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? fourFoundColors : fourColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 54);
            } else if (letter == '5') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(fiveLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(fiveModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(fiveModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                fiveVertices.clear();
                fiveVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.FIVE_COORDS, coordenada));
                fiveVertices.position(0);
                GLES20.glVertexAttribPointer(
                        fivePositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, fiveVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(fiveModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(fiveNormalParam, 3, GLES20.GL_FLOAT, false, 0, fiveNormals);
                GLES20.glVertexAttribPointer(fiveColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? fiveFoundColors : fiveColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 66);
            } else if (letter == '6') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);

                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(sixLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(sixModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(sixModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                sixVertices.clear();
                sixVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.SIX_COORDS, coordenada));
                sixVertices.position(0);
                GLES20.glVertexAttribPointer(
                        sixPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, sixVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(sixModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(sixNormalParam, 3, GLES20.GL_FLOAT, false, 0, sixNormals);
                GLES20.glVertexAttribPointer(sixColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? sixFoundColors : sixColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 72);
            } else if (letter == '7') {

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(sevenLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(sevenModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(sevenModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                sevenVertices.clear();
                sevenVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.SEVEN_COORDS, coordenada));
                sevenVertices.position(0);
                GLES20.glVertexAttribPointer(
                        sevenPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, sevenVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(sevenModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(sevenNormalParam, 3, GLES20.GL_FLOAT, false, 0, sevenNormals);
                GLES20.glVertexAttribPointer(sevenColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? sevenFoundColors : sevenColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 48);
            } else if (letter == '8') {
                /********EIGHT**********/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(eightLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(eightModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(eightModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                eightVertices.clear();
                eightVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.EIGHT_COORDS, coordenada));
                eightVertices.position(0);
                GLES20.glVertexAttribPointer(
                        eightPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, eightVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(eightModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(eightNormalParam, 3, GLES20.GL_FLOAT, false, 0, eightNormals);
                GLES20.glVertexAttribPointer(eightColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? eightFoundColors : eightColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 78);

            } else if (letter == '9') {
                /********NINE*********/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(nineLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting


                GLES20.glUniformMatrix4fv(nineModelParam, 1, false, models.get(i), 0);
                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(nineModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                nineVertices.clear();
                nineVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.NINE_COORDS, coordenada));
                nineVertices.position(0);
                GLES20.glVertexAttribPointer(
                        ninePositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, nineVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(nineModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(nineNormalParam, 3, GLES20.GL_FLOAT, false, 0, nineNormals);
                GLES20.glVertexAttribPointer(nineColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? nineFoundColors : nineColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 72);

            } else if (letter == '0') {
/********ZERO*******/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(zeroLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(zeroModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(zeroModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                zeroVertices.clear();
                zeroVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.ZERO_COORDS, coordenada));
                zeroVertices.position(0);
                GLES20.glVertexAttribPointer(
                        zeroPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, zeroVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(zeroModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(zeroNormalParam, 3, GLES20.GL_FLOAT, false, 0, zeroNormals);
                GLES20.glVertexAttribPointer(zeroColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? zeroFoundColors : zeroColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 72);
            } else {
                /*TIMES*/

                Matrix.multiplyMM(modelViewOpt1, 0, view, 0, models.get(i), 0);
                Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelViewOpt1, 0);

                GLES20.glUseProgram(program);

                GLES20.glUniform3fv(timesLightPosParam, 1, lightPosInEyeSpace, 0);

                // Set the Model in the shader, used to calculate lighting

                GLES20.glUniformMatrix4fv(timesModelParam, 1, false, models.get(i), 0);

                // Set the ModelView in the shader, used to calculate lighting
                GLES20.glUniformMatrix4fv(timesModelViewParam, 1, false, modelViewOpt1, 0);

                // Set the position of the cubeSquare
                timesVertices.clear();
                timesVertices.put(WorldLayoutData.rotARTE(WorldLayoutData.TIMES_COORDS, coordenada));
                timesVertices.position(0);
                GLES20.glVertexAttribPointer(
                        timesPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, timesVertices);

                // Set the ModelViewProjection matrix in the shader.
                GLES20.glUniformMatrix4fv(timesModelViewProjectionParam, 1, false, modelViewProjection, 0);

                // Set the normal positions of the cube, again for shading
                GLES20.glVertexAttribPointer(timesNormalParam, 3, GLES20.GL_FLOAT, false, 0, timesNormals);
                GLES20.glVertexAttribPointer(timesColorParam, 4, GLES20.GL_FLOAT, false, 0,
                        isLookingAtObject1(models) ? timesFoundColors : timesColors);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12);
            }
            i++;

        }

    }

    public void drawFloor() {
        GLES20.glUseProgram(floorProgram);

        // Set ModelView, MVP, position, normals, and color.
        GLES20.glUniform3fv(floorLightPosParam, 1, lightPosInEyeSpace, 0);
        GLES20.glUniformMatrix4fv(floorModelParam, 1, false, modelFloor, 0);
        GLES20.glUniformMatrix4fv(floorModelViewParam, 1, false, modelViewFloor, 0);
        GLES20.glUniformMatrix4fv(floorModelViewProjectionParam, 1, false, modelViewProjection, 0);
        GLES20.glVertexAttribPointer(
                floorPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, floorVertices);
        GLES20.glVertexAttribPointer(floorNormalParam, 3, GLES20.GL_FLOAT, false, 0, floorNormals);
        GLES20.glVertexAttribPointer(floorColorParam, 4, GLES20.GL_FLOAT, false, 0, floorColors);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12);

        checkGLError("drawing floor");
    }

    public void scoreUpdate (int score){
        this.score+=score;
        txtScoreL.setText(String.valueOf(this.score));
    }

    @Override
    public void onCardboardTrigger() {
        Log.e("en comparacion afuera", String.valueOf(isEveryoneReady));
        if (isLookingAtObject1(models1) || isLookingAtObject1(models2) || isLookingAtObject1(models3) || isLookingAtObject1(models4)) {
            if (isEveryoneReady == mParticipants.size()) {
                Log.e("listo","listo!");
                if (gameStarted){
                    if (isLookingAtObject1(models1)) {
                        if (mathOperation.eval(operation)==options[0]){
                            overlayView.show3DToast("correct op1");
                            scoreUpdate(100);
                            //sendMessageScore(100);

                        }else{
                            overlayView.show3DToast("incorrect op1, correct answer: "+ mathOperation.eval(operation));
                            scoreUpdate(-50);
                            //sendMessageScore(-50);
                        }


                    } else if (isLookingAtObject1(models2)) {
                        if (mathOperation.eval(operation)==options[0]){
                            overlayView.show3DToast("correct op2");
                            scoreUpdate(100);
                           // sendMessageScore(100);
                        }else{
                            overlayView.show3DToast("incorrect op2, correct answer: "+ mathOperation.eval(operation));
                            scoreUpdate(-50);
                            //sendMessageScore(-50);
                        }

                    } else if (isLookingAtObject1(models3)) {

                        if (mathOperation.eval(operation)==options[0]){
                            overlayView.show3DToast("correct op3");
                            scoreUpdate(100);
                           // sendMessageScore(100);
                        }else{
                            overlayView.show3DToast("incorrect op3, correct answer: "+ mathOperation.eval(operation));
                            scoreUpdate(-50);
                          //  sendMessageScore(-50);
                        }

                    } else if (isLookingAtObject1(models4)) {
                        if (mathOperation.eval(operation)==options[0]){
                            overlayView.show3DToast("correct op4");
                            scoreUpdate(100);
                           // sendMessageScore(100);
                        }else{
                            overlayView.show3DToast("incorrect op4, correct answer: "+ mathOperation.eval(operation));
                            scoreUpdate(-50);
                           // sendMessageScore(-50);
                        }

                    }
                }

                if (mathOperation.getRounds() == mathOperation.getMaxRounds()) {
                    mathOperation.resetLimitExp();
                    Log.e("entre?", "entre!");
                    mathOperation.resetRounds();
                    mathOperation.chooseRandomlyMaxRounds();
                    if (level.equals(mathOperation.getLevel())) {
                        mathOperation.setSubLevel(2);
                    } else {
                        mathOperation.setSubLevel(mathOperation.getSubLevel() + 1);
                    }
                }




                sendMessageOperation();
                clearModels();
                txtOpt.setText(operation+extraLvl3);
                Log.e("operation operation",operation);
                hideObject(String.valueOf(options[0]), 1);
                hideObject(String.valueOf(options[1]), 2);
                hideObject(String.valueOf(options[2]), 3);
                hideObject(String.valueOf(options[3]), 4);




                vibrator.vibrate(50);
            } else {
                Log.e("entre a la comparacion", String.valueOf(isEveryoneReady));

                if (!gameStarted && (isLookingAtObject1(models1) || isLookingAtObject1(models2) || isLookingAtObject1(models3) || isLookingAtObject1(models4))) {
                    Log.e("entre!","entre!");
                    sendMessageOnReady();
                    if (!gameStarted) {
                        gameStarted = true;
                    }
                }
                vibrator.vibrate(50);



                Log.i(TAG, "onCardboardTrigger");

            }
        }


    }


    private void clearModels() {
        modelsOperation.clear();

        models1.clear();

        models2.clear();

        models3.clear();

        models4.clear();

    }

    private void hideObject(String number, int whichModel) {
        float[] rotationMatrix = new float[16];
        float[] posVec = new float[4];
        float[] rotationMatrixY = new float[16];
        float[] posVecY = new float[4];
        float angleXZ = 0;
        float angleYZ = 0;
        float newX;
        float newY;
        float space = 9.0f;
        // First rotate in XZ plane, between 90 and 270 deg away, and scale so that we vary
        // the object's distance from the user.
        if (whichModel != 0) {
            Matrix.setRotateM(rotationMatrix, 0, angleXZ, 0f, 1f, 0f)/*angleXZ*/;
            float oldObjectDistanceOne = objectDistanceOne;
            Log.e("oldobject distance one", String.valueOf(oldObjectDistanceOne));
            double rand = Math.random();
            Log.e("random ", String.valueOf(rand));
            objectDistanceOne =
                    (float) 1 * (MAX_MODEL_DISTANCE - MIN_MODEL_DISTANCE) + MIN_MODEL_DISTANCE;/*(float) Math.random() * (MAX_MODEL_DISTANCE - MIN_MODEL_DISTANCE) + MIN_MODEL_DISTANCE*/
            float objectScalingFactor = 1;/*objectDistanceOne / oldObjectDistanceOne*/
            Matrix.scaleM(rotationMatrix, 0, objectScalingFactor, objectScalingFactor, objectScalingFactor);
            if (whichModel == 1) {
                Matrix.multiplyMV(posVec, 0, rotationMatrix, 0, modelOne, 12);
                float angleY = (float) Math.random() * -120.0f + 80.0f; // Angle in Y plane, between -40 and 40.
                angleY = (float) Math.toRadians(angleY);
                newY = (float) Math.tan(angleY) * objectDistanceOne;

                modelPositionOne[1] = newY;

                Log.e("position 1 0", String.valueOf(modelPositionOne[0]));
                Log.e("position 1 1", String.valueOf(modelPositionOne[1]));
                Log.e("position 1 2", String.valueOf(modelPositionOne[2]));
                updateModelPosition(number, modelPositionOne, 1, "O");
            } else if (whichModel == 2) {


                Matrix.multiplyMV(posVec, 0, rotationMatrix, 0, modelTwo, 12);
                float angleY = (float) Math.random() * -120.0f + 80.0f;  /*(float) 80 - 40*/// Angle in Y plane, between -40 and 40.
                angleY = (float) Math.toRadians(angleY);
                newY = (float) Math.tan(angleY) * objectDistanceOne;

                modelPositionTwo[1] = newY;

                Log.e("position 2 0", String.valueOf(modelPositionTwo[0]));
                Log.e("position 2 1", String.valueOf(modelPositionTwo[1]));
                Log.e("position 2 2", String.valueOf(modelPositionTwo[2]));
                updateModelPosition(number, modelPositionTwo, 2, "N");


            } else if (whichModel == 3) {
                Matrix.multiplyMV(posVec, 0, rotationMatrix, 0, modelThree, 12);
                float angleY = (float) Math.random() * -120.0f + 80.0f;   // Angle in Y plane, between -40 and 40.
                angleY = (float) Math.toRadians(angleY);
                newY = (float) Math.tan(angleY) * objectDistanceOne;
                //Toast.makeText(MainActivityVR.this, "I got this 2", Toast.LENGTH_SHORT).show();

                modelPositionThree[1] = newY;

                Log.e("position 3 0", String.valueOf(modelPositionThree[0]));
                Log.e("position 3 1", String.valueOf(modelPositionThree[1]));
                Log.e("position 3 2", String.valueOf(modelPositionThree[2]));
           /* Toast.makeText(MainActivityVR.this,"x"+String.valueOf(posVec[0]), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivityVR.this,"y"+String.valueOf(newY), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivityVR.this,"z"+String.valueOf(posVec[2]), Toast.LENGTH_SHORT).show();*/
                updateModelPosition(number, modelPositionThree, 3, "S");
            } else {
                Matrix.multiplyMV(posVec, 0, rotationMatrix, 0, modelFour, 12);
                float angleY = (float) Math.random() * -120.0f + 80.0f;   // Angle in Y plane, between -40 and 40.
                angleY = (float) Math.toRadians(angleY);
                newY = (float) Math.tan(angleY) * objectDistanceOne;
                //Toast.makeText(MainActivityVR.this, "I got this 2", Toast.LENGTH_SHORT).show();

                modelPositionFour[1] = newY;

                Log.e("position 4 0", String.valueOf(modelPositionFour[0]));
                Log.e("position 4 1", String.valueOf(modelPositionFour[1]));
                Log.e("position 4 2", String.valueOf(modelPositionFour[2]));
           /* Toast.makeText(MainActivityVR.this,"x"+String.valueOf(posVec[0]), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivityVR.this,"y"+String.valueOf(newY), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivityVR.this,"z"+String.valueOf(posVec[2]), Toast.LENGTH_SHORT).show();*/
                updateModelPosition(number, modelPositionFour, 4, "E");
            }
        } else {
            Matrix.setRotateM(rotationMatrix, 0, 0, 1f, 0f, 0f)/*angleXZ*/;
            float oldObjectDistanceOne = objectDistanceOne;
            Log.e("oldobject distance one", String.valueOf(oldObjectDistanceOne));
            double rand = Math.random();
            Log.e("random ", String.valueOf(rand));
            objectDistanceOne =
                    (float) 1 * (MAX_MODEL_DISTANCE - MIN_MODEL_DISTANCE) + MIN_MODEL_DISTANCE;/*(float) Math.random() * (MAX_MODEL_DISTANCE - MIN_MODEL_DISTANCE) + MIN_MODEL_DISTANCE*/
            float objectScalingFactor = 1;/*objectDistanceOne / oldObjectDistanceOne*/
            Matrix.scaleM(rotationMatrix, 0, objectScalingFactor, objectScalingFactor, objectScalingFactor);
            Matrix.multiplyMV(posVec, 0, rotationMatrix, 0, modelOperation, 12);
            float angleX = (float) Math.random() * -120.0f + 80.0f;   // Angle in Y plane, between -40 and 40.
            angleX = (float) Math.toRadians(angleX);
            newX = (float) Math.tan(angleX) * objectDistanceOne;
            //Toast.makeText(MainActivityVR.this, "I got this 2", Toast.LENGTH_SHORT).show();

            Log.e("position 4 0", String.valueOf(modelPositionOperation[0]));
            Log.e("position 4 1", String.valueOf(modelPositionOperation[1]));
            Log.e("position 4 2", String.valueOf(modelPositionOperation[2]));
           /* Toast.makeText(MainActivityVR.this,"x"+String.valueOf(posVec[0]), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivityVR.this,"y"+String.valueOf(newY), Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivityVR.this,"z"+String.valueOf(posVec[2]), Toast.LENGTH_SHORT).show();*/
            //updateModelPosition(number, modelPositionOperation, 0, "UP");
        }

    }

    private boolean isLookingAtObject1(ArrayList<float[]> models) { /*ONE*/
        float[] initVec = {0, 0, 0, 1.0f};
        float[] objPositionVec = new float[4];

        // Convert object space to camera space. Use the headView from onNewFrame.
        Matrix.multiplyMM(modelViewOpt1, 0, headView, 0, models.get((models.size() / 2)), 0);


        Matrix.multiplyMV(objPositionVec, 0, modelViewOpt1, 0, initVec, 0);

        float pitch = (float) Math.atan2(objPositionVec[1], -objPositionVec[2]);
        float yaw = (float) Math.atan2(objPositionVec[0], -objPositionVec[2]);

        return Math.abs(pitch) < PITCH_LIMIT && Math.abs(yaw) < YAW_LIMIT;
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }


    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well.



/*RPARENT*/

        ByteBuffer bbVerticesRParent = ByteBuffer.allocateDirect(WorldLayoutData.RIGHTPAR_COORDS.length * 4);
        bbVerticesRParent.order(ByteOrder.nativeOrder());
        rParentVertices = bbVerticesRParent.asFloatBuffer();
        rParentVertices.put(WorldLayoutData.RIGHTPAR_COORDS);
        rParentVertices.position(0);

        ByteBuffer bbColorsRParent = ByteBuffer.allocateDirect(WorldLayoutData.RIGHTPAR_COLORS.length * 4);
        bbColorsRParent.order(ByteOrder.nativeOrder());
        rParentColors = bbColorsRParent.asFloatBuffer();
        rParentColors.put(WorldLayoutData.RIGHTPAR_COLORS);
        rParentColors.position(0);

        ByteBuffer bbFoundColorsRParent =
                ByteBuffer.allocateDirect(WorldLayoutData.RIGHTPAR_FOUND_COLORS.length * 4);
        bbFoundColorsRParent.order(ByteOrder.nativeOrder());
        rParentFoundColors = bbFoundColorsRParent.asFloatBuffer();
        rParentFoundColors.put(WorldLayoutData.RIGHTPAR_FOUND_COLORS);
        rParentFoundColors.position(0);

        ByteBuffer bbNormalsRParent = ByteBuffer.allocateDirect(WorldLayoutData.RIGHTPAR_NORMALS.length * 4);
        bbNormalsRParent.order(ByteOrder.nativeOrder());
        rParentNormals = bbNormalsRParent.asFloatBuffer();
        rParentNormals.put(WorldLayoutData.RIGHTPAR_NORMALS);
        rParentNormals.position(0);
/*LPARENT*/

        ByteBuffer bbVerticesLParent = ByteBuffer.allocateDirect(WorldLayoutData.LEFTPAR_COORDS.length * 4);
        bbVerticesLParent.order(ByteOrder.nativeOrder());
        lParentVertices = bbVerticesLParent.asFloatBuffer();
        lParentVertices.put(WorldLayoutData.LEFTPAR_COORDS);
        lParentVertices.position(0);

        ByteBuffer bbColorsLParent = ByteBuffer.allocateDirect(WorldLayoutData.LEFTPAR_COLORS.length * 4);
        bbColorsLParent.order(ByteOrder.nativeOrder());
        lParentColors = bbColorsLParent.asFloatBuffer();
        lParentColors.put(WorldLayoutData.LEFTPAR_COLORS);
        lParentColors.position(0);

        ByteBuffer bbFoundColorsLParent =
                ByteBuffer.allocateDirect(WorldLayoutData.LEFTPAR_FOUND_COLORS.length * 4);
        bbFoundColorsLParent.order(ByteOrder.nativeOrder());
        lParentFoundColors = bbFoundColorsLParent.asFloatBuffer();
        lParentFoundColors.put(WorldLayoutData.LEFTPAR_FOUND_COLORS);
        lParentFoundColors.position(0);

        ByteBuffer bbNormalsLParent = ByteBuffer.allocateDirect(WorldLayoutData.LEFTPAR_NORMALS.length * 4);
        bbNormalsLParent.order(ByteOrder.nativeOrder());
        lParentNormals = bbNormalsLParent.asFloatBuffer();
        lParentNormals.put(WorldLayoutData.LEFTPAR_NORMALS);
        lParentNormals.position(0);

        /*DOT*/

        ByteBuffer bbVerticesDot = ByteBuffer.allocateDirect(WorldLayoutData.DOT_COORDS.length * 4);
        bbVerticesDot.order(ByteOrder.nativeOrder());
        dotVertices = bbVerticesDot.asFloatBuffer();
        dotVertices.put(WorldLayoutData.DOT_COORDS);
        dotVertices.position(0);

        ByteBuffer bbColorsDot = ByteBuffer.allocateDirect(WorldLayoutData.DOT_COLORS.length * 4);
        bbColorsDot.order(ByteOrder.nativeOrder());
        dotColors = bbColorsDot.asFloatBuffer();
        dotColors.put(WorldLayoutData.DOT_COLORS);
        dotColors.position(0);

        ByteBuffer bbFoundColorsDot =
                ByteBuffer.allocateDirect(WorldLayoutData.DOT_FOUND_COLORS.length * 4);
        bbFoundColorsDot.order(ByteOrder.nativeOrder());
        dotFoundColors = bbFoundColorsDot.asFloatBuffer();
        dotFoundColors.put(WorldLayoutData.DOT_FOUND_COLORS);
        dotFoundColors.position(0);

        ByteBuffer bbNormalsDot = ByteBuffer.allocateDirect(WorldLayoutData.DOT_NORMALS.length * 4);
        bbNormalsDot.order(ByteOrder.nativeOrder());
        dotNormals = bbNormalsDot.asFloatBuffer();
        dotNormals.put(WorldLayoutData.DOT_NORMALS);
        dotNormals.position(0);

         /*PLUS*/
        ByteBuffer bbVerticesPlus = ByteBuffer.allocateDirect(WorldLayoutData.PLUS_COORDS.length * 4);
        bbVerticesPlus.order(ByteOrder.nativeOrder());
        plusVertices = bbVerticesPlus.asFloatBuffer();
        plusVertices.put(WorldLayoutData.PLUS_COORDS);
        plusVertices.position(0);

        ByteBuffer bbColorsPlus = ByteBuffer.allocateDirect(WorldLayoutData.PLUS_COLORS.length * 4);
        bbColorsPlus.order(ByteOrder.nativeOrder());
        plusColors = bbColorsPlus.asFloatBuffer();
        plusColors.put(WorldLayoutData.PLUS_COLORS);
        plusColors.position(0);

        ByteBuffer bbFoundColorsPlus =
                ByteBuffer.allocateDirect(WorldLayoutData.PLUS_FOUND_COLORS.length * 4);
        bbFoundColorsPlus.order(ByteOrder.nativeOrder());
        plusFoundColors = bbFoundColorsPlus.asFloatBuffer();
        plusFoundColors.put(WorldLayoutData.PLUS_FOUND_COLORS);
        plusFoundColors.position(0);

        ByteBuffer bbNormalsPlus = ByteBuffer.allocateDirect(WorldLayoutData.PLUS_NORMALS.length * 4);
        bbNormalsPlus.order(ByteOrder.nativeOrder());
        plusNormals = bbNormalsPlus.asFloatBuffer();
        plusNormals.put(WorldLayoutData.PLUS_NORMALS);
        plusNormals.position(0);

        /*MINUS*/

        ByteBuffer bbVerticesMinus = ByteBuffer.allocateDirect(WorldLayoutData.MINUS_COORDS.length * 4);
        bbVerticesMinus.order(ByteOrder.nativeOrder());
        minusVertices = bbVerticesMinus.asFloatBuffer();
        minusVertices.put(WorldLayoutData.MINUS_COORDS);
        minusVertices.position(0);

        ByteBuffer bbColorsMinus = ByteBuffer.allocateDirect(WorldLayoutData.MINUS_COLORS.length * 4);
        bbColorsMinus.order(ByteOrder.nativeOrder());
        minusColors = bbColorsMinus.asFloatBuffer();
        minusColors.put(WorldLayoutData.MINUS_COLORS);
        minusColors.position(0);

        ByteBuffer bbFoundColorsMinus =
                ByteBuffer.allocateDirect(WorldLayoutData.MINUS_FOUND_COLORS.length * 4);
        bbFoundColorsMinus.order(ByteOrder.nativeOrder());
        minusFoundColors = bbFoundColorsMinus.asFloatBuffer();
        minusFoundColors.put(WorldLayoutData.MINUS_FOUND_COLORS);
        minusFoundColors.position(0);

        ByteBuffer bbNormalsMinus = ByteBuffer.allocateDirect(WorldLayoutData.MINUS_NORMALS.length * 4);
        bbNormalsMinus.order(ByteOrder.nativeOrder());
        minusNormals = bbNormalsMinus.asFloatBuffer();
        minusNormals.put(WorldLayoutData.MINUS_NORMALS);
        minusNormals.position(0);


        /*TIMES*/

        ByteBuffer bbVerticesTimes = ByteBuffer.allocateDirect(WorldLayoutData.TIMES_COORDS.length * 4);
        bbVerticesTimes.order(ByteOrder.nativeOrder());
        timesVertices = bbVerticesTimes.asFloatBuffer();
        timesVertices.put(WorldLayoutData.TIMES_COORDS);
        timesVertices.position(0);

        ByteBuffer bbColorsTimes = ByteBuffer.allocateDirect(WorldLayoutData.TIMES_COLORS.length * 4);
        bbColorsTimes.order(ByteOrder.nativeOrder());
        timesColors = bbColorsTimes.asFloatBuffer();
        timesColors.put(WorldLayoutData.TIMES_COLORS);
        timesColors.position(0);

        ByteBuffer bbFoundColorsTimes =
                ByteBuffer.allocateDirect(WorldLayoutData.TIMES_FOUND_COLORS.length * 4);
        bbFoundColorsTimes.order(ByteOrder.nativeOrder());
        timesFoundColors = bbFoundColorsTimes.asFloatBuffer();
        timesFoundColors.put(WorldLayoutData.TIMES_FOUND_COLORS);
        timesFoundColors.position(0);

        ByteBuffer bbNormalsTimes = ByteBuffer.allocateDirect(WorldLayoutData.TIMES_NORMALS.length * 4);
        bbNormalsTimes.order(ByteOrder.nativeOrder());
        timesNormals = bbNormalsTimes.asFloatBuffer();
        timesNormals.put(WorldLayoutData.TIMES_NORMALS);
        timesNormals.position(0);

        /*DIVISION*/
        ByteBuffer bbVerticesDivision = ByteBuffer.allocateDirect(WorldLayoutData.DIVISION_COORDS.length * 4);
        bbVerticesDivision.order(ByteOrder.nativeOrder());
        divisionVertices = bbVerticesDivision.asFloatBuffer();
        divisionVertices.put(WorldLayoutData.DIVISION_COORDS);
        divisionVertices.position(0);

        ByteBuffer bbColorsDivision = ByteBuffer.allocateDirect(WorldLayoutData.DIVISION_COLORS.length * 4);
        bbColorsDivision.order(ByteOrder.nativeOrder());
        divisionColors = bbColorsDivision.asFloatBuffer();
        divisionColors.put(WorldLayoutData.DIVISION_COLORS);
        divisionColors.position(0);

        ByteBuffer bbFoundColorsDivision =
                ByteBuffer.allocateDirect(WorldLayoutData.DIVISION_FOUND_COLORS.length * 4);
        bbFoundColorsDivision.order(ByteOrder.nativeOrder());
        divisionFoundColors = bbFoundColorsDivision.asFloatBuffer();
        divisionFoundColors.put(WorldLayoutData.DIVISION_FOUND_COLORS);
        divisionFoundColors.position(0);

        ByteBuffer bbNormalsDivision = ByteBuffer.allocateDirect(WorldLayoutData.DIVISION_NORMALS.length * 4);
        bbNormalsDivision.order(ByteOrder.nativeOrder());
        divisionNormals = bbNormalsDivision.asFloatBuffer();
        divisionNormals.put(WorldLayoutData.DIVISION_NORMALS);
        divisionNormals.position(0);

    /*ONE*/

        ByteBuffer bbVerticesUno = ByteBuffer.allocateDirect(WorldLayoutData.ONE_COORDS.length * 4);
        bbVerticesUno.order(ByteOrder.nativeOrder());
        oneVertices = bbVerticesUno.asFloatBuffer();
        oneVertices.put(WorldLayoutData.ONE_COORDS);
        oneVertices.position(0);

        ByteBuffer bbColorsUno = ByteBuffer.allocateDirect(WorldLayoutData.ONE_COLORS.length * 4);
        bbColorsUno.order(ByteOrder.nativeOrder());
        oneColors = bbColorsUno.asFloatBuffer();
        oneColors.put(WorldLayoutData.ONE_COLORS);
        oneColors.position(0);

        ByteBuffer bbFoundColorsUno =
                ByteBuffer.allocateDirect(WorldLayoutData.ONE_FOUND_COLORS.length * 4);
        bbFoundColorsUno.order(ByteOrder.nativeOrder());
        oneFoundColors = bbFoundColorsUno.asFloatBuffer();
        oneFoundColors.put(WorldLayoutData.ONE_FOUND_COLORS);
        oneFoundColors.position(0);

        ByteBuffer bbNormalsUno = ByteBuffer.allocateDirect(WorldLayoutData.ONE_NORMALS.length * 4);
        bbNormalsUno.order(ByteOrder.nativeOrder());
        oneNormals = bbNormalsUno.asFloatBuffer();
        oneNormals.put(WorldLayoutData.ONE_NORMALS);
        oneNormals.position(0);


    /*TWO*/
        ByteBuffer bbVerticesTwo = ByteBuffer.allocateDirect(WorldLayoutData.TWO_COORDS.length * 4);
        bbVerticesTwo.order(ByteOrder.nativeOrder());
        twoVertices = bbVerticesTwo.asFloatBuffer();
        twoVertices.put(WorldLayoutData.TWO_COORDS);
        twoVertices.position(0);

        ByteBuffer bbColorsTwo = ByteBuffer.allocateDirect(WorldLayoutData.TWO_COLORS.length * 4);
        bbColorsTwo.order(ByteOrder.nativeOrder());
        twoColors = bbColorsTwo.asFloatBuffer();
        twoColors.put(WorldLayoutData.TWO_COLORS);
        twoColors.position(0);

        ByteBuffer bbFoundColorsTwo =
                ByteBuffer.allocateDirect(WorldLayoutData.TWO_FOUND_COLORS.length * 4);
        bbFoundColorsTwo.order(ByteOrder.nativeOrder());
        twoFoundColors = bbFoundColorsTwo.asFloatBuffer();
        twoFoundColors.put(WorldLayoutData.TWO_FOUND_COLORS);
        twoFoundColors.position(0);

        ByteBuffer bbNormalsTwo = ByteBuffer.allocateDirect(WorldLayoutData.TWO_NORMALS.length * 4);
        bbNormalsTwo.order(ByteOrder.nativeOrder());
        twoNormals = bbNormalsTwo.asFloatBuffer();
        twoNormals.put(WorldLayoutData.TWO_NORMALS);
        twoNormals.position(0);


       /*THREE*/
        ByteBuffer bbVerticesThree = ByteBuffer.allocateDirect(WorldLayoutData.THREE_COORDS.length * 4);
        bbVerticesThree.order(ByteOrder.nativeOrder());
        threeVertices = bbVerticesThree.asFloatBuffer();
        threeVertices.put(WorldLayoutData.THREE_COORDS);
        threeVertices.position(0);

        ByteBuffer bbColorsThree = ByteBuffer.allocateDirect(WorldLayoutData.THREE_COLORS.length * 4);
        bbColorsThree.order(ByteOrder.nativeOrder());
        threeColors = bbColorsThree.asFloatBuffer();
        threeColors.put(WorldLayoutData.THREE_COLORS);
        threeColors.position(0);

        ByteBuffer bbFoundColorsThree =
                ByteBuffer.allocateDirect(WorldLayoutData.THREE_FOUND_COLORS.length * 4);
        bbFoundColorsThree.order(ByteOrder.nativeOrder());
        threeFoundColors = bbFoundColorsThree.asFloatBuffer();
        threeFoundColors.put(WorldLayoutData.THREE_FOUND_COLORS);
        threeFoundColors.position(0);

        ByteBuffer bbNormalsThree = ByteBuffer.allocateDirect(WorldLayoutData.THREE_NORMALS.length * 4);
        bbNormalsThree.order(ByteOrder.nativeOrder());
        threeNormals = bbNormalsThree.asFloatBuffer();
        threeNormals.put(WorldLayoutData.THREE_NORMALS);
        threeNormals.position(0);
      /*four*/

        ByteBuffer bbVerticesFour = ByteBuffer.allocateDirect(WorldLayoutData.FOUR_COORDS.length * 4);
        bbVerticesFour.order(ByteOrder.nativeOrder());
        fourVertices = bbVerticesFour.asFloatBuffer();
        fourVertices.put(WorldLayoutData.FOUR_COORDS);
        fourVertices.position(0);

        ByteBuffer bbColorsFour = ByteBuffer.allocateDirect(WorldLayoutData.FOUR_COLORS.length * 4);
        bbColorsFour.order(ByteOrder.nativeOrder());
        fourColors = bbColorsFour.asFloatBuffer();
        fourColors.put(WorldLayoutData.FOUR_COLORS);
        fourColors.position(0);

        ByteBuffer bbFoundColorsFour =
                ByteBuffer.allocateDirect(WorldLayoutData.FOUR_FOUND_COLORS.length * 4);
        bbFoundColorsFour.order(ByteOrder.nativeOrder());
        fourFoundColors = bbFoundColorsFour.asFloatBuffer();
        fourFoundColors.put(WorldLayoutData.FOUR_FOUND_COLORS);
        fourFoundColors.position(0);

        ByteBuffer bbNormalsFour = ByteBuffer.allocateDirect(WorldLayoutData.FOUR_NORMALS.length * 4);
        bbNormalsFour.order(ByteOrder.nativeOrder());
        fourNormals = bbNormalsFour.asFloatBuffer();
        fourNormals.put(WorldLayoutData.FOUR_NORMALS);
        fourNormals.position(0);
    /*five*/

        ByteBuffer bbVerticesFive = ByteBuffer.allocateDirect(WorldLayoutData.FIVE_COORDS.length * 4);
        bbVerticesFive.order(ByteOrder.nativeOrder());
        fiveVertices = bbVerticesFive.asFloatBuffer();
        fiveVertices.put(WorldLayoutData.FIVE_COORDS);
        fiveVertices.position(0);

        ByteBuffer bbColorsFive = ByteBuffer.allocateDirect(WorldLayoutData.FIVE_COLORS.length * 4);
        bbColorsFive.order(ByteOrder.nativeOrder());
        fiveColors = bbColorsFive.asFloatBuffer();
        fiveColors.put(WorldLayoutData.FIVE_COLORS);
        fiveColors.position(0);

        ByteBuffer bbFoundColorsFive =
                ByteBuffer.allocateDirect(WorldLayoutData.FIVE_FOUND_COLORS.length * 4);
        bbFoundColorsFive.order(ByteOrder.nativeOrder());
        fiveFoundColors = bbFoundColorsFive.asFloatBuffer();
        fiveFoundColors.put(WorldLayoutData.FIVE_FOUND_COLORS);
        fiveFoundColors.position(0);

        ByteBuffer bbNormalsFive = ByteBuffer.allocateDirect(WorldLayoutData.FIVE_NORMALS.length * 4);
        bbNormalsFive.order(ByteOrder.nativeOrder());
        fiveNormals = bbNormalsFive.asFloatBuffer();
        fiveNormals.put(WorldLayoutData.FIVE_NORMALS);
        fiveNormals.position(0);


     /*six*/

        ByteBuffer bbVerticesSix = ByteBuffer.allocateDirect(WorldLayoutData.SIX_COORDS.length * 4);
        bbVerticesSix.order(ByteOrder.nativeOrder());
        sixVertices = bbVerticesSix.asFloatBuffer();
        sixVertices.put(WorldLayoutData.SIX_COORDS);
        sixVertices.position(0);

        ByteBuffer bbColorsSix = ByteBuffer.allocateDirect(WorldLayoutData.SIX_COLORS.length * 4);
        bbColorsSix.order(ByteOrder.nativeOrder());
        sixColors = bbColorsSix.asFloatBuffer();
        sixColors.put(WorldLayoutData.SIX_COLORS);
        sixColors.position(0);

        ByteBuffer bbFoundColorsSix =
                ByteBuffer.allocateDirect(WorldLayoutData.SIX_FOUND_COLORS.length * 4);
        bbFoundColorsSix.order(ByteOrder.nativeOrder());
        sixFoundColors = bbFoundColorsSix.asFloatBuffer();
        sixFoundColors.put(WorldLayoutData.SIX_FOUND_COLORS);
        sixFoundColors.position(0);

        ByteBuffer bbNormalsSix = ByteBuffer.allocateDirect(WorldLayoutData.SIX_NORMALS.length * 4);
        bbNormalsSix.order(ByteOrder.nativeOrder());
        sixNormals = bbNormalsSix.asFloatBuffer();
        sixNormals.put(WorldLayoutData.SIX_NORMALS);
        sixNormals.position(0);

    /*SEVEN*/

        ByteBuffer bbVerticesSeven = ByteBuffer.allocateDirect(WorldLayoutData.SEVEN_COORDS.length * 4);
        bbVerticesSeven.order(ByteOrder.nativeOrder());
        sevenVertices = bbVerticesSeven.asFloatBuffer();
        sevenVertices.put(WorldLayoutData.SEVEN_COORDS);
        sevenVertices.position(0);

        ByteBuffer bbColorsSeven = ByteBuffer.allocateDirect(WorldLayoutData.SEVEN_COLORS.length * 4);
        bbColorsSeven.order(ByteOrder.nativeOrder());
        sevenColors = bbColorsSeven.asFloatBuffer();
        sevenColors.put(WorldLayoutData.SEVEN_COLORS);
        sevenColors.position(0);

        ByteBuffer bbFoundColorsSeven =
                ByteBuffer.allocateDirect(WorldLayoutData.SEVEN_FOUND_COLORS.length * 4);
        bbFoundColorsSeven.order(ByteOrder.nativeOrder());
        sevenFoundColors = bbFoundColorsSeven.asFloatBuffer();
        sevenFoundColors.put(WorldLayoutData.SEVEN_FOUND_COLORS);
        sevenFoundColors.position(0);

        ByteBuffer bbNormalsSeven = ByteBuffer.allocateDirect(WorldLayoutData.SEVEN_NORMALS.length * 4);
        bbNormalsSeven.order(ByteOrder.nativeOrder());
        sevenNormals = bbNormalsSeven.asFloatBuffer();
        sevenNormals.put(WorldLayoutData.SEVEN_NORMALS);
        sevenNormals.position(0);

        /*EIGHT*/
        ByteBuffer bbVerticesEight = ByteBuffer.allocateDirect(WorldLayoutData.EIGHT_COORDS.length * 4);
        bbVerticesEight.order(ByteOrder.nativeOrder());
        eightVertices = bbVerticesEight.asFloatBuffer();
        eightVertices.put(WorldLayoutData.EIGHT_COORDS);
        eightVertices.position(0);

        ByteBuffer bbColorsEight = ByteBuffer.allocateDirect(WorldLayoutData.EIGHT_COLORS.length * 4);
        bbColorsEight.order(ByteOrder.nativeOrder());
        eightColors = bbColorsEight.asFloatBuffer();
        eightColors.put(WorldLayoutData.EIGHT_COLORS);
        eightColors.position(0);

        ByteBuffer bbFoundColorsEight =
                ByteBuffer.allocateDirect(WorldLayoutData.EIGHT_FOUND_COLORS.length * 4);
        bbFoundColorsEight.order(ByteOrder.nativeOrder());
        eightFoundColors = bbFoundColorsEight.asFloatBuffer();
        eightFoundColors.put(WorldLayoutData.EIGHT_FOUND_COLORS);
        eightFoundColors.position(0);

        ByteBuffer bbNormalsEight = ByteBuffer.allocateDirect(WorldLayoutData.EIGHT_NORMALS.length * 4);
        bbNormalsEight.order(ByteOrder.nativeOrder());
        eightNormals = bbNormalsEight.asFloatBuffer();
        eightNormals.put(WorldLayoutData.EIGHT_NORMALS);
        eightNormals.position(0);

        /*****NINE*******/
        ByteBuffer bbVerticesNine = ByteBuffer.allocateDirect(WorldLayoutData.NINE_COORDS.length * 4);
        bbVerticesNine.order(ByteOrder.nativeOrder());
        nineVertices = bbVerticesNine.asFloatBuffer();
        nineVertices.put(WorldLayoutData.NINE_COORDS);
        nineVertices.position(0);

        ByteBuffer bbColorsNine = ByteBuffer.allocateDirect(WorldLayoutData.NINE_COLORS.length * 4);
        bbColorsNine.order(ByteOrder.nativeOrder());
        nineColors = bbColorsNine.asFloatBuffer();
        nineColors.put(WorldLayoutData.NINE_COLORS);
        nineColors.position(0);

        ByteBuffer bbFoundColorsNine =
                ByteBuffer.allocateDirect(WorldLayoutData.NINE_FOUND_COLORS.length * 4);
        bbFoundColorsNine.order(ByteOrder.nativeOrder());
        nineFoundColors = bbFoundColorsNine.asFloatBuffer();
        nineFoundColors.put(WorldLayoutData.NINE_FOUND_COLORS);
        nineFoundColors.position(0);

        ByteBuffer bbNormalsNine = ByteBuffer.allocateDirect(WorldLayoutData.NINE_NORMALS.length * 4);
        bbNormalsNine.order(ByteOrder.nativeOrder());
        nineNormals = bbNormalsNine.asFloatBuffer();
        nineNormals.put(WorldLayoutData.NINE_NORMALS);
        nineNormals.position(0);

        /*******ZERO********/
        ByteBuffer bbVerticesZero = ByteBuffer.allocateDirect(WorldLayoutData.ZERO_COORDS.length * 4);
        bbVerticesZero.order(ByteOrder.nativeOrder());
        zeroVertices = bbVerticesZero.asFloatBuffer();
        zeroVertices.put(WorldLayoutData.ZERO_COORDS);
        zeroVertices.position(0);

        ByteBuffer bbColorsZero = ByteBuffer.allocateDirect(WorldLayoutData.ZERO_COLORS.length * 4);
        bbColorsZero.order(ByteOrder.nativeOrder());
        zeroColors = bbColorsZero.asFloatBuffer();
        zeroColors.put(WorldLayoutData.ZERO_COLORS);
        zeroColors.position(0);

        ByteBuffer bbFoundColorsZero =
                ByteBuffer.allocateDirect(WorldLayoutData.ZERO_FOUND_COLORS.length * 4);
        bbFoundColorsZero.order(ByteOrder.nativeOrder());
        zeroFoundColors = bbFoundColorsZero.asFloatBuffer();
        zeroFoundColors.put(WorldLayoutData.ZERO_FOUND_COLORS);
        zeroFoundColors.position(0);

        ByteBuffer bbNormalsZero = ByteBuffer.allocateDirect(WorldLayoutData.ZERO_NORMALS.length * 4);
        bbNormalsZero.order(ByteOrder.nativeOrder());
        zeroNormals = bbNormalsZero.asFloatBuffer();
        zeroNormals.put(WorldLayoutData.ZERO_NORMALS);
        zeroNormals.position(0);

        // make a floor


        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        floorVertices = bbFloorVertices.asFloatBuffer();
        floorVertices.put(WorldLayoutData.FLOOR_COORDS);
        floorVertices.position(0);

        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_NORMALS.length * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        floorNormals = bbFloorNormals.asFloatBuffer();
        floorNormals.put(WorldLayoutData.FLOOR_NORMALS);
        floorNormals.position(0);

        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COLORS.length * 4);
        bbFloorColors.order(ByteOrder.nativeOrder());
        floorColors = bbFloorColors.asFloatBuffer();
        floorColors.put(WorldLayoutData.FLOOR_COLORS);
        floorColors.position(0);

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.light_vertex);
        int gridShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.grid_fragment);
        int passthroughShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.passthrough_fragment);


    /**/

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, passthroughShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);

        checkGLError("Cube program");

        positionParam = GLES20.glGetAttribLocation(program, "a_Position");
        normalParam = GLES20.glGetAttribLocation(program, "a_Normal");
        colorParam = GLES20.glGetAttribLocation(program, "a_Color");

        modelParam = GLES20.glGetUniformLocation(program, "u_Model");
        modelViewParam = GLES20.glGetUniformLocation(program, "u_MVMatrix");
        modelViewProjectionParam = GLES20.glGetUniformLocation(program, "u_MVP");
        lightPosParam = GLES20.glGetUniformLocation(program, "u_LightPos");

          /*PLUS*/
        plusProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(plusProgram, vertexShader);
        GLES20.glAttachShader(plusProgram, passthroughShader);
        GLES20.glLinkProgram(plusProgram);
        GLES20.glUseProgram(plusProgram);


        plusPositionParam = GLES20.glGetAttribLocation(plusProgram, "a_Position");
        plusNormalParam = GLES20.glGetAttribLocation(plusProgram, "a_Normal");
        plusColorParam = GLES20.glGetAttribLocation(plusProgram, "a_Color");

        plusModelParam = GLES20.glGetUniformLocation(plusProgram, "u_Model");
        plusModelViewParam = GLES20.glGetUniformLocation(plusProgram, "u_MVMatrix");
        plusModelViewProjectionParam = GLES20.glGetUniformLocation(plusProgram, "u_MVP");
        plusLightPosParam = GLES20.glGetUniformLocation(plusProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(plusPositionParam);
        GLES20.glEnableVertexAttribArray(plusNormalParam);
        GLES20.glEnableVertexAttribArray(plusColorParam);

     /*RPARENT*/
        rParentProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(rParentProgram, vertexShader);
        GLES20.glAttachShader(rParentProgram, passthroughShader);
        GLES20.glLinkProgram(rParentProgram);
        GLES20.glUseProgram(rParentProgram);


        rParentPositionParam = GLES20.glGetAttribLocation(rParentProgram, "a_Position");
        rParentNormalParam = GLES20.glGetAttribLocation(rParentProgram, "a_Normal");
        rParentColorParam = GLES20.glGetAttribLocation(rParentProgram, "a_Color");

        rParentModelParam = GLES20.glGetUniformLocation(rParentProgram, "u_Model");
        rParentModelViewParam = GLES20.glGetUniformLocation(rParentProgram, "u_MVMatrix");
        rParentModelViewProjectionParam = GLES20.glGetUniformLocation(rParentProgram, "u_MVP");
        rParentLightPosParam = GLES20.glGetUniformLocation(rParentProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(rParentPositionParam);
        GLES20.glEnableVertexAttribArray(rParentNormalParam);
        GLES20.glEnableVertexAttribArray(rParentColorParam);

        /*LPARENT*/
        lParentProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(lParentProgram, vertexShader);
        GLES20.glAttachShader(lParentProgram, passthroughShader);
        GLES20.glLinkProgram(lParentProgram);
        GLES20.glUseProgram(lParentProgram);


        lParentPositionParam = GLES20.glGetAttribLocation(lParentProgram, "a_Position");
        lParentNormalParam = GLES20.glGetAttribLocation(lParentProgram, "a_Normal");
        lParentColorParam = GLES20.glGetAttribLocation(lParentProgram, "a_Color");

        lParentModelParam = GLES20.glGetUniformLocation(lParentProgram, "u_Model");
        lParentModelViewParam = GLES20.glGetUniformLocation(lParentProgram, "u_MVMatrix");
        lParentModelViewProjectionParam = GLES20.glGetUniformLocation(lParentProgram, "u_MVP");
        lParentLightPosParam = GLES20.glGetUniformLocation(lParentProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(lParentPositionParam);
        GLES20.glEnableVertexAttribArray(lParentNormalParam);
        GLES20.glEnableVertexAttribArray(lParentColorParam);


        /*DOT*/
        dotProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(dotProgram, vertexShader);
        GLES20.glAttachShader(dotProgram, passthroughShader);
        GLES20.glLinkProgram(dotProgram);
        GLES20.glUseProgram(dotProgram);


        dotPositionParam = GLES20.glGetAttribLocation(dotProgram, "a_Position");
        dotNormalParam = GLES20.glGetAttribLocation(dotProgram, "a_Normal");
        dotColorParam = GLES20.glGetAttribLocation(dotProgram, "a_Color");

        dotModelParam = GLES20.glGetUniformLocation(dotProgram, "u_Model");
        dotModelViewParam = GLES20.glGetUniformLocation(dotProgram, "u_MVMatrix");
        dotModelViewProjectionParam = GLES20.glGetUniformLocation(dotProgram, "u_MVP");
        dotLightPosParam = GLES20.glGetUniformLocation(dotProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(dotPositionParam);
        GLES20.glEnableVertexAttribArray(dotNormalParam);
        GLES20.glEnableVertexAttribArray(dotColorParam);


        /*MINUS*/
        minusProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(minusProgram, vertexShader);
        GLES20.glAttachShader(minusProgram, passthroughShader);
        GLES20.glLinkProgram(minusProgram);
        GLES20.glUseProgram(minusProgram);


        minusPositionParam = GLES20.glGetAttribLocation(minusProgram, "a_Position");
        minusNormalParam = GLES20.glGetAttribLocation(minusProgram, "a_Normal");
        minusColorParam = GLES20.glGetAttribLocation(minusProgram, "a_Color");

        minusModelParam = GLES20.glGetUniformLocation(minusProgram, "u_Model");
        minusModelViewParam = GLES20.glGetUniformLocation(minusProgram, "u_MVMatrix");
        minusModelViewProjectionParam = GLES20.glGetUniformLocation(minusProgram, "u_MVP");
        minusLightPosParam = GLES20.glGetUniformLocation(minusProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(minusPositionParam);
        GLES20.glEnableVertexAttribArray(minusNormalParam);
        GLES20.glEnableVertexAttribArray(minusColorParam);



        /*TIMES*/
        timesProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(timesProgram, vertexShader);
        GLES20.glAttachShader(timesProgram, passthroughShader);
        GLES20.glLinkProgram(timesProgram);
        GLES20.glUseProgram(timesProgram);


        timesPositionParam = GLES20.glGetAttribLocation(timesProgram, "a_Position");
        timesNormalParam = GLES20.glGetAttribLocation(timesProgram, "a_Normal");
        timesColorParam = GLES20.glGetAttribLocation(timesProgram, "a_Color");

        timesModelParam = GLES20.glGetUniformLocation(timesProgram, "u_Model");
        timesModelViewParam = GLES20.glGetUniformLocation(timesProgram, "u_MVMatrix");
        timesModelViewProjectionParam = GLES20.glGetUniformLocation(timesProgram, "u_MVP");
        timesLightPosParam = GLES20.glGetUniformLocation(timesProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(timesPositionParam);
        GLES20.glEnableVertexAttribArray(timesNormalParam);
        GLES20.glEnableVertexAttribArray(timesColorParam);



        /*DIVISION*/
        divisionProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(divisionProgram, vertexShader);
        GLES20.glAttachShader(divisionProgram, passthroughShader);
        GLES20.glLinkProgram(divisionProgram);
        GLES20.glUseProgram(divisionProgram);


        divisionPositionParam = GLES20.glGetAttribLocation(divisionProgram, "a_Position");
        divisionNormalParam = GLES20.glGetAttribLocation(divisionProgram, "a_Normal");
        divisionColorParam = GLES20.glGetAttribLocation(divisionProgram, "a_Color");

        divisionModelParam = GLES20.glGetUniformLocation(divisionProgram, "u_Model");
        divisionModelViewParam = GLES20.glGetUniformLocation(divisionProgram, "u_MVMatrix");
        divisionModelViewProjectionParam = GLES20.glGetUniformLocation(divisionProgram, "u_MVP");
        divisionLightPosParam = GLES20.glGetUniformLocation(divisionProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(divisionPositionParam);
        GLES20.glEnableVertexAttribArray(divisionNormalParam);
        GLES20.glEnableVertexAttribArray(divisionColorParam);




/*one*/


        oneProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(oneProgram, vertexShader);
        GLES20.glAttachShader(oneProgram, passthroughShader);
        GLES20.glLinkProgram(oneProgram);
        GLES20.glUseProgram(oneProgram);

        checkGLError("Cube program");

        onePositionParam = GLES20.glGetAttribLocation(oneProgram, "a_Position");
        oneNormalParam = GLES20.glGetAttribLocation(oneProgram, "a_Normal");
        oneColorParam = GLES20.glGetAttribLocation(oneProgram, "a_Color");

        oneModelParam = GLES20.glGetUniformLocation(oneProgram, "u_Model");
        oneModelViewParam = GLES20.glGetUniformLocation(oneProgram, "u_MVMatrix");
        oneModelViewProjectionParam = GLES20.glGetUniformLocation(oneProgram, "u_MVP");
        oneLightPosParam = GLES20.glGetUniformLocation(oneProgram, "u_LightPos");
/*two*/
        twoProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(twoProgram, vertexShader);
        GLES20.glAttachShader(twoProgram, passthroughShader);
        GLES20.glLinkProgram(twoProgram);
        GLES20.glUseProgram(twoProgram);

        checkGLError("Cube program");

        twoPositionParam = GLES20.glGetAttribLocation(twoProgram, "a_Position");
        twoNormalParam = GLES20.glGetAttribLocation(twoProgram, "a_Normal");
        twoColorParam = GLES20.glGetAttribLocation(twoProgram, "a_Color");

        twoModelParam = GLES20.glGetUniformLocation(twoProgram, "u_Model");
        twoModelViewParam = GLES20.glGetUniformLocation(twoProgram, "u_MVMatrix");
        twoModelViewProjectionParam = GLES20.glGetUniformLocation(twoProgram, "u_MVP");
        twoLightPosParam = GLES20.glGetUniformLocation(twoProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(twoPositionParam);
        GLES20.glEnableVertexAttribArray(twoNormalParam);
        GLES20.glEnableVertexAttribArray(twoColorParam);
/*three*/
        threeProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(threeProgram, vertexShader);
        GLES20.glAttachShader(threeProgram, passthroughShader);
        GLES20.glLinkProgram(threeProgram);
        GLES20.glUseProgram(threeProgram);


        threePositionParam = GLES20.glGetAttribLocation(threeProgram, "a_Position");
        threeNormalParam = GLES20.glGetAttribLocation(threeProgram, "a_Normal");
        threeColorParam = GLES20.glGetAttribLocation(threeProgram, "a_Color");

        threeModelParam = GLES20.glGetUniformLocation(threeProgram, "u_Model");
        threeModelViewParam = GLES20.glGetUniformLocation(threeProgram, "u_MVMatrix");
        threeModelViewProjectionParam = GLES20.glGetUniformLocation(threeProgram, "u_MVP");
        threeLightPosParam = GLES20.glGetUniformLocation(threeProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(threePositionParam);
        GLES20.glEnableVertexAttribArray(threeNormalParam);
        GLES20.glEnableVertexAttribArray(threeColorParam);

/*four*/
        fourProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(fourProgram, vertexShader);
        GLES20.glAttachShader(fourProgram, passthroughShader);
        GLES20.glLinkProgram(fourProgram);
        GLES20.glUseProgram(fourProgram);


        fourPositionParam = GLES20.glGetAttribLocation(fourProgram, "a_Position");
        fourNormalParam = GLES20.glGetAttribLocation(fourProgram, "a_Normal");
        fourColorParam = GLES20.glGetAttribLocation(fourProgram, "a_Color");

        fourModelParam = GLES20.glGetUniformLocation(fourProgram, "u_Model");
        fourModelViewParam = GLES20.glGetUniformLocation(fourProgram, "u_MVMatrix");
        fourModelViewProjectionParam = GLES20.glGetUniformLocation(fourProgram, "u_MVP");
        fourLightPosParam = GLES20.glGetUniformLocation(fourProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(fourPositionParam);
        GLES20.glEnableVertexAttribArray(fourNormalParam);
        GLES20.glEnableVertexAttribArray(fourColorParam);

    /*five*/
        fiveProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(fiveProgram, vertexShader);
        GLES20.glAttachShader(fiveProgram, passthroughShader);
        GLES20.glLinkProgram(fiveProgram);
        GLES20.glUseProgram(fiveProgram);


        fivePositionParam = GLES20.glGetAttribLocation(fiveProgram, "a_Position");
        fiveNormalParam = GLES20.glGetAttribLocation(fiveProgram, "a_Normal");
        fiveColorParam = GLES20.glGetAttribLocation(fiveProgram, "a_Color");

        fiveModelParam = GLES20.glGetUniformLocation(fiveProgram, "u_Model");
        fiveModelViewParam = GLES20.glGetUniformLocation(fiveProgram, "u_MVMatrix");
        fiveModelViewProjectionParam = GLES20.glGetUniformLocation(fiveProgram, "u_MVP");
        fiveLightPosParam = GLES20.glGetUniformLocation(fiveProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(fivePositionParam);
        GLES20.glEnableVertexAttribArray(fiveNormalParam);
        GLES20.glEnableVertexAttribArray(fiveColorParam);

    /*six*/
        sixProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(sixProgram, vertexShader);
        GLES20.glAttachShader(sixProgram, passthroughShader);
        GLES20.glLinkProgram(sixProgram);
        GLES20.glUseProgram(sixProgram);


        sixPositionParam = GLES20.glGetAttribLocation(sixProgram, "a_Position");
        sixNormalParam = GLES20.glGetAttribLocation(sixProgram, "a_Normal");
        sixColorParam = GLES20.glGetAttribLocation(sixProgram, "a_Color");

        sixModelParam = GLES20.glGetUniformLocation(sixProgram, "u_Model");
        sixModelViewParam = GLES20.glGetUniformLocation(sixProgram, "u_MVMatrix");
        sixModelViewProjectionParam = GLES20.glGetUniformLocation(sixProgram, "u_MVP");
        sixLightPosParam = GLES20.glGetUniformLocation(sixProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(sixPositionParam);
        GLES20.glEnableVertexAttribArray(sixNormalParam);
        GLES20.glEnableVertexAttribArray(sixColorParam);

    /*SEVEN*/
        sevenProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(sevenProgram, vertexShader);
        GLES20.glAttachShader(sevenProgram, passthroughShader);
        GLES20.glLinkProgram(sevenProgram);
        GLES20.glUseProgram(sevenProgram);


        sevenPositionParam = GLES20.glGetAttribLocation(sevenProgram, "a_Position");
        sevenNormalParam = GLES20.glGetAttribLocation(sevenProgram, "a_Normal");
        sevenColorParam = GLES20.glGetAttribLocation(sevenProgram, "a_Color");

        sevenModelParam = GLES20.glGetUniformLocation(sevenProgram, "u_Model");
        sevenModelViewParam = GLES20.glGetUniformLocation(sevenProgram, "u_MVMatrix");
        sevenModelViewProjectionParam = GLES20.glGetUniformLocation(sevenProgram, "u_MVP");
        sevenLightPosParam = GLES20.glGetUniformLocation(sevenProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(sevenPositionParam);
        GLES20.glEnableVertexAttribArray(sevenNormalParam);
        GLES20.glEnableVertexAttribArray(sevenColorParam);

        /*******EIGHT********/
        eightProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(eightProgram, vertexShader);
        GLES20.glAttachShader(eightProgram, passthroughShader);
        GLES20.glLinkProgram(eightProgram);
        GLES20.glUseProgram(eightProgram);


        eightPositionParam = GLES20.glGetAttribLocation(eightProgram, "a_Position");
        eightNormalParam = GLES20.glGetAttribLocation(eightProgram, "a_Normal");
        eightColorParam = GLES20.glGetAttribLocation(eightProgram, "a_Color");

        eightModelParam = GLES20.glGetUniformLocation(eightProgram, "u_Model");
        eightModelViewParam = GLES20.glGetUniformLocation(eightProgram, "u_MVMatrix");
        eightModelViewProjectionParam = GLES20.glGetUniformLocation(eightProgram, "u_MVP");
        eightLightPosParam = GLES20.glGetUniformLocation(eightProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(eightPositionParam);
        GLES20.glEnableVertexAttribArray(eightNormalParam);
        GLES20.glEnableVertexAttribArray(eightColorParam);

        /*********NINE********/
        nineProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(nineProgram, vertexShader);
        GLES20.glAttachShader(nineProgram, passthroughShader);
        GLES20.glLinkProgram(nineProgram);
        GLES20.glUseProgram(nineProgram);


        ninePositionParam = GLES20.glGetAttribLocation(nineProgram, "a_Position");
        nineNormalParam = GLES20.glGetAttribLocation(nineProgram, "a_Normal");
        nineColorParam = GLES20.glGetAttribLocation(nineProgram, "a_Color");

        nineModelParam = GLES20.glGetUniformLocation(nineProgram, "u_Model");
        nineModelViewParam = GLES20.glGetUniformLocation(nineProgram, "u_MVMatrix");
        nineModelViewProjectionParam = GLES20.glGetUniformLocation(nineProgram, "u_MVP");
        nineLightPosParam = GLES20.glGetUniformLocation(nineProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(ninePositionParam);
        GLES20.glEnableVertexAttribArray(nineNormalParam);
        GLES20.glEnableVertexAttribArray(nineColorParam);

        /********ZERO*********/
        zeroProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(zeroProgram, vertexShader);
        GLES20.glAttachShader(zeroProgram, passthroughShader);
        GLES20.glLinkProgram(zeroProgram);
        GLES20.glUseProgram(zeroProgram);


        zeroPositionParam = GLES20.glGetAttribLocation(zeroProgram, "a_Position");
        zeroNormalParam = GLES20.glGetAttribLocation(zeroProgram, "a_Normal");
        zeroColorParam = GLES20.glGetAttribLocation(zeroProgram, "a_Color");

        zeroModelParam = GLES20.glGetUniformLocation(zeroProgram, "u_Model");
        zeroModelViewParam = GLES20.glGetUniformLocation(zeroProgram, "u_MVMatrix");
        zeroModelViewProjectionParam = GLES20.glGetUniformLocation(zeroProgram, "u_MVP");
        zeroLightPosParam = GLES20.glGetUniformLocation(zeroProgram, "u_LightPos");

        GLES20.glEnableVertexAttribArray(zeroPositionParam);
        GLES20.glEnableVertexAttribArray(zeroNormalParam);
        GLES20.glEnableVertexAttribArray(zeroColorParam);
/*floor*/
        floorProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(floorProgram, vertexShader);
        GLES20.glAttachShader(floorProgram, gridShader);
        GLES20.glLinkProgram(floorProgram);
        GLES20.glUseProgram(floorProgram);

        checkGLError("Floor program");

        floorModelParam = GLES20.glGetUniformLocation(floorProgram, "u_Model");
        floorModelViewParam = GLES20.glGetUniformLocation(floorProgram, "u_MVMatrix");
        floorModelViewProjectionParam = GLES20.glGetUniformLocation(floorProgram, "u_MVP");
        floorLightPosParam = GLES20.glGetUniformLocation(floorProgram, "u_LightPos");

        floorPositionParam = GLES20.glGetAttribLocation(floorProgram, "a_Position");
        floorNormalParam = GLES20.glGetAttribLocation(floorProgram, "a_Normal");
        floorColorParam = GLES20.glGetAttribLocation(floorProgram, "a_Color");

        GLES20.glEnableVertexAttribArray(floorPositionParam);
        GLES20.glEnableVertexAttribArray(floorNormalParam);
        GLES20.glEnableVertexAttribArray(floorColorParam);

        checkGLError("Floor program params");

        Matrix.setIdentityM(modelFloor, 0);
        Matrix.translateM(modelFloor, 0, 0, -floorDepth, 0); // Floor appears below user.

        // Avoid any delays during start-up due to decoding of sound files.
        new Thread(
                new Runnable() {
                    public void run() {
                        // Start spatial audio playback of SOUND_FILE at the model postion. The returned
                        //soundId handle is stored and allows for repositioning the sound object whenever
                        // the cube position changes.
                        cardboardAudioEngine.preloadSoundFile(SOUND_FILE);
                        soundId = cardboardAudioEngine.createSoundObject(SOUND_FILE);
                        cardboardAudioEngine.setSoundObjectPosition(
                                soundId, modelPositionOne[0], modelPositionOne[1], modelPositionOne[2]);
                        cardboardAudioEngine.playSound(soundId, true /* looped playback */);

                    }
                })
                .start();


        mathOperation.chooseRandomlyMaxRounds();
        operation = mathOperation.mathLevel(level);


        mathOperation.increaseRounds();


        Log.e("operation", operation);


        for (int i = 0; i < options.length; i++) {
            options[i] = 0;
        }


        updateModelPosition(operation, modelPositionOperation, 0, "UP");


        updateModelPosition(String.valueOf(options[0]), modelPositionOne, 1, "O");
        updateModelPosition(String.valueOf(options[1]), modelPositionTwo, 2, "N");
        updateModelPosition(String.valueOf(options[2]), modelPositionThree, 3, "S");
        updateModelPosition(String.valueOf(options[3]), modelPositionFour, 4, "E");
        checkGLError("onSurfaceCreated");


    }

    private void updateModelPosition(String number, float[] positions, int whichModel, String coordenada) {

        float space = 0;

        float x = positions[0];

        float y = positions[1];

        float z = positions[2];
        int numberLength = number.length();
        int i = 0;
        if (whichModel == 0) {
            modelsOperation.add(new float[16]);
        } else if (whichModel == 1) {
            models1.add(new float[16]);
        } else if (whichModel == 2) {
            models2.add(new float[16]);
        } else if (whichModel == 3) {
            models3.add(new float[16]);
        } else {
            models4.add(new float[16]);
        }


        while (i < numberLength) {
            if (coordenada.equals("N")) {
                Matrix.setIdentityM(models2.get(i), 0);
                Matrix.translateM(models2.get(i), 0, x + space, y, z);
            } else if (coordenada.equals("S")) {
                Matrix.setIdentityM(models3.get(i), 0);
                Matrix.translateM(models3.get(i), 0, x - space, y, z);
            } else if (coordenada.equals("E")) {
                Matrix.setIdentityM(models4.get(i), 0);
                Matrix.translateM(models4.get(i), 0, x, y, z + space);
            } else if (coordenada.equals("O")) {
                Matrix.setIdentityM(models1.get(i), 0);
                Matrix.translateM(models1.get(i), 0, x, y, z - space);
            } else {
                Matrix.setIdentityM(modelsOperation.get(i), 0);
                Matrix.translateM(modelsOperation.get(i), 0, x + space, y, z);
            }


            // Update the sound location to match it with the new cube position.
            if (soundId != CardboardAudioEngine.INVALID_ID) {

                cardboardAudioEngine.setSoundObjectPosition(
                        soundId, x, y, z);
            }

            if (number.charAt(i) == '(') {
                space = space + 7.0f;
            } else if (number.charAt(i) == ')') {
                space = space + 7.0f;
            } else if (number.charAt(i) == '+') {
                space = space + 7.0f;
            } else if (number.charAt(i) == '-') {
                space = space + 7.0f;
            } else if (number.charAt(i) == '*') {

                space = space + 7.0f;
            } else if (number.charAt(i) == '/') {

                space = space + 7.0f;
            } else if (number.charAt(i) == '1') {

                space = space + 7.0f;
            } else if (number.charAt(i) == '2') {

                space = space + 7.0f;

            } else if (number.charAt(i) == '3') {

                space = space + 7.0f;
            } else if (number.charAt(i) == '4') {
                space = space + 7.0f;

            } else if (number.charAt(i) == '5') {
                space = space + 7.0f;

            } else if (number.charAt(i) == '6') {

                space = space + 7.0f;

            } else if (number.charAt(i) == '7') {
                space = space + 7.0f;

            } else if (number.charAt(i) == '8') {
                space = space + 7.0f;

            } else if (number.charAt(i) == '9') {

                space = space + 7.0f;

            } else if (number.charAt(i) == '0') {
                space = space + 7.0f;
            } else {

                space = space + 3.0f;

            }
            i++;
            if (whichModel == 0) {
                modelsOperation.add(new float[16]);
            } else if (whichModel == 1) {
                models1.add(new float[16]);
            } else if (whichModel == 2) {
                models2.add(new float[16]);
            } else if (whichModel == 3) {
                models3.add(new float[16]);
            } else {
                models4.add(new float[16]);
            }

        }


    }

    private String readRawTextFile(int resId) {
        InputStream inputStream = getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }
}

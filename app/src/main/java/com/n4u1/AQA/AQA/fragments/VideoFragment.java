package com.n4u1.AQA.AQA.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.n4u1.AQA.AQA.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class VideoFragment extends Fragment {

    private int GALLEY_CODE = 1000;


    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    private String videoPath;
    private int contentCount;

    SimpleExoPlayer player0, player1, player2, player3, player4,
                    player5, player6, player7, player8, player9;


    String[] fileString = {"", "", "", "", "", "", "", "", "", ""};

    ImageView imageView_userAddContent_1, imageView_userAddContent_2, imageView_userAddContent_3,
            imageView_userAddContent_4, imageView_userAddContent_5, imageView_userAddContent_6,
            imageView_userAddContent_7, imageView_userAddContent_8, imageView_userAddContent_9,
            imageView_userAddContent_10;
    LinearLayout linearLayout_userAddContent_1, linearLayout_userAddContent_2, linearLayout_userAddContent_3,
            linearLayout_userAddContent_4, linearLayout_userAddContent_5, linearLayout_userAddContent_6,
            linearLayout_userAddContent_7, linearLayout_userAddContent_8, linearLayout_userAddContent_9,
            linearLayout_userAddContent_10;
    TextView textView_userAddContent_1, textView_userAddContent_2, textView_userAddContent_3,
            textView_userAddContent_4, textView_userAddContent_5, textView_userAddContent_6,
            textView_userAddContent_7, textView_userAddContent_8, textView_userAddContent_9,
            textView_userAddContent_10;


    PlayerView videoView_userAddContent_1, videoView_userAddContent_2, videoView_userAddContent_3,
            videoView_userAddContent_4, videoView_userAddContent_5, videoView_userAddContent_6,
            videoView_userAddContent_7, videoView_userAddContent_8, videoView_userAddContent_9,
            videoView_userAddContent_10;


    private OnFragmentInteractionListener mListener;

    public VideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);


        linearLayout_userAddContent_1 = view.findViewById(R.id.linearLayout_userAddContent_1);
        linearLayout_userAddContent_2 = view.findViewById(R.id.linearLayout_userAddContent_2);
        linearLayout_userAddContent_3 = view.findViewById(R.id.linearLayout_userAddContent_3);
        linearLayout_userAddContent_4 = view.findViewById(R.id.linearLayout_userAddContent_4);
        linearLayout_userAddContent_5 = view.findViewById(R.id.linearLayout_userAddContent_5);
        linearLayout_userAddContent_6 = view.findViewById(R.id.linearLayout_userAddContent_6);
        linearLayout_userAddContent_7 = view.findViewById(R.id.linearLayout_userAddContent_7);
        linearLayout_userAddContent_8 = view.findViewById(R.id.linearLayout_userAddContent_8);
        linearLayout_userAddContent_9 = view.findViewById(R.id.linearLayout_userAddContent_9);
        linearLayout_userAddContent_10 = view.findViewById(R.id.linearLayout_userAddContent_10);
        textView_userAddContent_1 = view.findViewById(R.id.textView_userAddContent_1);
        textView_userAddContent_2 = view.findViewById(R.id.textView_userAddContent_2);
        textView_userAddContent_3 = view.findViewById(R.id.textView_userAddContent_3);
        textView_userAddContent_4 = view.findViewById(R.id.textView_userAddContent_4);
        textView_userAddContent_5 = view.findViewById(R.id.textView_userAddContent_5);
        textView_userAddContent_6 = view.findViewById(R.id.textView_userAddContent_6);
        textView_userAddContent_7 = view.findViewById(R.id.textView_userAddContent_7);
        textView_userAddContent_8 = view.findViewById(R.id.textView_userAddContent_8);
        textView_userAddContent_9 = view.findViewById(R.id.textView_userAddContent_9);
        textView_userAddContent_10 = view.findViewById(R.id.textView_userAddContent_10);
        imageView_userAddContent_1 = view.findViewById(R.id.imageView_userAddContent_1);
        imageView_userAddContent_2 = view.findViewById(R.id.imageView_userAddContent_2);
        imageView_userAddContent_3 = view.findViewById(R.id.imageView_userAddContent_3);
        imageView_userAddContent_4 = view.findViewById(R.id.imageView_userAddContent_4);
        imageView_userAddContent_5 = view.findViewById(R.id.imageView_userAddContent_5);
        imageView_userAddContent_6 = view.findViewById(R.id.imageView_userAddContent_6);
        imageView_userAddContent_7 = view.findViewById(R.id.imageView_userAddContent_7);
        imageView_userAddContent_8 = view.findViewById(R.id.imageView_userAddContent_8);
        imageView_userAddContent_9 = view.findViewById(R.id.imageView_userAddContent_9);
        imageView_userAddContent_10 = view.findViewById(R.id.imageView_userAddContent_10);
        videoView_userAddContent_1 = view.findViewById(R.id.videoView_userAddContent_1);
        videoView_userAddContent_2 = view.findViewById(R.id.videoView_userAddContent_2);
        videoView_userAddContent_3 = view.findViewById(R.id.videoView_userAddContent_3);
        videoView_userAddContent_4 = view.findViewById(R.id.videoView_userAddContent_4);
        videoView_userAddContent_5 = view.findViewById(R.id.videoView_userAddContent_5);
        videoView_userAddContent_6 = view.findViewById(R.id.videoView_userAddContent_6);
        videoView_userAddContent_7 = view.findViewById(R.id.videoView_userAddContent_7);
        videoView_userAddContent_8 = view.findViewById(R.id.videoView_userAddContent_8);
        videoView_userAddContent_9 = view.findViewById(R.id.videoView_userAddContent_9);
        videoView_userAddContent_10 = view.findViewById(R.id.videoView_userAddContent_10);


//
//        ImageView imageView_addVideo = view.findViewById(R.id.imageView_addVideo);
//        imageView_addVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("video/*");
//                startActivityForResult(intent, GALLEY_CODE);
//            }
//        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String[] strings) {
        if (mListener != null) {
            mListener.onFragmentInteraction(strings);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int checkCount = imageViewCheck();
        if (requestCode == GALLEY_CODE) {
            if (data == null) {
                return;
            } else {
                if (checkCount == 0) {
                    linearLayout_userAddContent_1.setVisibility(View.VISIBLE);
                    textView_userAddContent_1.setVisibility(View.VISIBLE);
                    videoView_userAddContent_1.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player0 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player0.prepare(videoSource);


                    videoView_userAddContent_1.setPlayer(player0);
                    fileString[0] = videoPath;
                }

                if (checkCount == 1) {
                    linearLayout_userAddContent_2.setVisibility(View.VISIBLE);
                    textView_userAddContent_2.setVisibility(View.VISIBLE);
                    videoView_userAddContent_2.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player1 =ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player1.prepare(videoSource);
                    videoView_userAddContent_2.setPlayer(player1);

                    fileString[1] = videoPath;
                }
                if (checkCount == 2) {
                    linearLayout_userAddContent_3.setVisibility(View.VISIBLE);
                    textView_userAddContent_3.setVisibility(View.VISIBLE);
                    videoView_userAddContent_3.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());

                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player2 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player2.prepare(videoSource);
                    videoView_userAddContent_3.setPlayer(player2);
                    fileString[2] = videoPath;
                }
                if (checkCount == 3) {
                    linearLayout_userAddContent_4.setVisibility(View.VISIBLE);
                    textView_userAddContent_4.setVisibility(View.VISIBLE);
                    videoView_userAddContent_4.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player3 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player3.prepare(videoSource);
                    videoView_userAddContent_4.setPlayer(player3);
                    fileString[3] = videoPath;
                }
                if (checkCount == 4) {
//                    Toast.makeText()               4개까지 등록되도록.......
                    linearLayout_userAddContent_5.setVisibility(View.VISIBLE);
                    textView_userAddContent_5.setVisibility(View.VISIBLE);
                    videoView_userAddContent_5.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player4 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player4.prepare(videoSource);
                    videoView_userAddContent_5.setPlayer(player4);
                    fileString[4] = videoPath;

                }
                if (checkCount == 5) {
                    linearLayout_userAddContent_6.setVisibility(View.VISIBLE);
                    textView_userAddContent_6.setVisibility(View.VISIBLE);
                    videoView_userAddContent_6.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player5 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player5.prepare(videoSource);
                    videoView_userAddContent_6.setPlayer(player5);
                    fileString[5] = videoPath;
                }
                if (checkCount == 6) {
                    linearLayout_userAddContent_7.setVisibility(View.VISIBLE);
                    textView_userAddContent_7.setVisibility(View.VISIBLE);
                    videoView_userAddContent_7.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player6 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player6.prepare(videoSource);
                    videoView_userAddContent_7.setPlayer(player6);
                    fileString[6] = videoPath;
                }
                if (checkCount == 7) {
                    linearLayout_userAddContent_8.setVisibility(View.VISIBLE);
                    textView_userAddContent_8.setVisibility(View.VISIBLE);
                    videoView_userAddContent_8.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());
                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player7 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player7.prepare(videoSource);
                    videoView_userAddContent_8.setPlayer(player7);
                    fileString[7] = videoPath;

                }
                if (checkCount == 8) {
                    linearLayout_userAddContent_9.setVisibility(View.VISIBLE);
                    textView_userAddContent_9.setVisibility(View.VISIBLE);
                    videoView_userAddContent_9.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());

                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player8 =
                            ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player8.prepare(videoSource);
                    videoView_userAddContent_9.setPlayer(player8);
                    fileString[8] = videoPath;
                }
                if (checkCount == 9) {
                    linearLayout_userAddContent_10.setVisibility(View.VISIBLE);
                    textView_userAddContent_10.setVisibility(View.VISIBLE);
                    videoView_userAddContent_10.setVisibility(View.VISIBLE);
                    videoPath = getPath(data.getData());

                    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                    TrackSelection.Factory videoTrackSelectionFactory =
                            new AdaptiveTrackSelection.Factory(null);
                    DefaultTrackSelector trackSelector =
                            new DefaultTrackSelector(videoTrackSelectionFactory);
                    player9 = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
                    // Measures bandwidth during playback. Can be null if not required.
                    // Produces DataSource instances through which media data is loaded.
                    DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                            Util.getUserAgent(getContext(), "AQA"), bandwidthMeter);
                    // This is the MediaSource representing the media to be played.
                    MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                            .createMediaSource(Uri.parse(videoPath));
                    // Prepare the player with the source.
                    player9.prepare(videoSource);
                    videoView_userAddContent_10.setPlayer(player9);
                    fileString[9] = videoPath;

                }
            }
        } else {
            return;
        }
        mListener.onFragmentInteraction(fileString);
        contentCount = checkCount;
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String[] strings);
    }


    private int imageViewCheck() {
        int count = 0;
        if (videoView_userAddContent_1.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_2.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_3.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_4.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_5.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_6.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_7.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_8.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_9.getVisibility() == View.VISIBLE) {
            count++;
        }
        if (videoView_userAddContent_10.getVisibility() == View.VISIBLE) {
            count++;
        }
        return count;
    }

    //디바이스 경로 가져오기
    private String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(getContext(), uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    @Override
    public void onResume() {
//        Log.d("lkjFragment onResume", "onResume");
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Log.d("lkjFragment onViewCrea", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
//        Log.d("lkjFragment onStop", "onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
//        Log.d("lkjFragment onPause", "onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (player0 != null) {
            player0.stop();
            player0.release();
            player0 = null;
        }

        if (player2 != null) {
            player2.stop();
            player2.release();
            player2 = null;
        }

        if (player1 != null) {
            player1.stop();
            player1.release();
            player1 = null;
        }

        if (player3 != null) {
            player3.stop();
            player3.release();
            player3 = null;
        }

        if (player4 != null) {
            player4.stop();
            player4.release();
            player4 = null;
        }

        if (player5 != null) {
            player5.stop();
            player5.release();
            player5 = null;
        }

        if (player6 != null) {
            player6.stop();
            player6.release();
            player6 = null;
        }

        if (player7 != null) {
            player7.stop();
            player7.release();
            player7 = null;
        }

        if (player8 != null) {
            player8.stop();
            player8.release();
            player8 = null;
        }

        if (player9 != null) {
            player9.stop();
            player9.release();
            player9 = null;
        }

//        Log.d("lkjFragment onDestroy", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        Log.d("lkjFragment onViewState", "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
//        Log.d("lkjFragment onInflate", "onInflate");
        super.onInflate(context, attrs, savedInstanceState);
    }
    
    
    
    

    //
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("lkjFragment4", String.valueOf(contentCount));
//        int curId = item.getItemId();
//        switch (curId) {
//            case R.id.menu_next:
//                upload(videoPath);
//                Intent intent = new Intent(getContext(), FileChoiceActivity.class);
//                startActivity(intent);
//                break;
//
//        }
//        onBackPressed();
//        return super.onOptionsItemSelected(item);
}



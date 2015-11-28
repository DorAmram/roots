package com.example.user.biographics_tests;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.ExtractEditText;
import android.net.ParseException;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenVideo;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.api.VideoChooserListener;
import com.kbeanie.imagechooser.api.VideoChooserManager;
import com.kbeanie.imagechooser.exceptions.ChooserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MediaChooserActivity extends AppCompatActivity implements ImageChooserListener,
        VideoChooserListener {

    private String pass;
    public final static int PICK_PHOTO_CODE = 1046;
    private String idOrNewOrCampaignQ;
    public static int RECORD_REQUEST = 0;
    private ImageChooserManager imageChooserManager;
    private VideoChooserManager videoChooserManager;
    private static int FolderId = 1;
    private String compressedFilePath;
    private String scndTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Log.i("eliLog", "begin log for current session");
        //get the extra idOrNewOrCampaignQ str if came from addContent or from the campaign.
        Intent i = getIntent();
        idOrNewOrCampaignQ = i.getStringExtra("idOrNewOrCampaignQ");
        int intIdOrNew = i.getIntExtra("idOrNewOrCampaignQ", -1);
        // if there was an id passed
        if (intIdOrNew != -1) {
            //reformat the int to string for furter use.
            idOrNewOrCampaignQ = String.format("" + intIdOrNew);
        }
        // if there was nothing passed in the idOrNewOrCampaignQ, it is probably a new story.
        else if (idOrNewOrCampaignQ == null) {
            idOrNewOrCampaignQ = "new";
        }
        //else: a question from the campaign passed!!TODO
        else {

        }

    }
//----------------------------Audio--------------------------------------
    public void MoveToAudio(View view) {
        //Runs the recorder
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, RECORD_REQUEST);
    }
    //----------------------------camera + gallery--------------------------------------
    public void MoveToCamera(View view) throws Exception {
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE,
                "/Roots/"+FolderId+"/photos", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.choose();
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) throws Exception {
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE,
                "/Roots/"+FolderId+"/photos", true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.choose();
    }


    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (image == null) {
                    Intent mainActivity = new Intent(MediaChooserActivity.this, MediaChooserActivity.class);
                    mainActivity.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
                    startActivity(mainActivity);
                    Toast.makeText(MediaChooserActivity.this, "imageChooser Photo Failed", Toast.LENGTH_SHORT).show();
                    Log.e("eliLog", "From MediaChooserActivity.java. Photo capture Failed");
                    return;
                }
                String imagePath = image.getFilePathOriginal();
                // Create an image file name by time and date.
                String tumb = image.getFileThumbnailSmall();//.getFileThumbnail();
                //TODO Chek if necessary
                ImageCompressionTask.OnImageCompressionListener listener =
                        new ImageCompressionTask.OnImageCompressionListener() {
                            @Override
                            public void onImageCompressionStart() {
                            }

                            @Override
                            public void onImageCompressionSuccess(final String result) {
                                //Saves the compressed photo path.
                                compressedFilePath = result;
                                Toast.makeText(MediaChooserActivity.this, "Compression Done", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onImageCompressionFailed() {
                            }
                        };

               // new ImageCompressionTask(listener, getApplicationContext()).execute(imagePath);
                //String tumb = compressedFilePath;
//                if (idOrNewOrCampaignQ.equals("new")) {
//                    Intent storyDetails = new Intent(getApplicationContext(), StoryDetails.class);
//                    storyDetails.putExtra("path", imagePath);
//                    storyDetails.putExtra("tPath", tumb);
//                    storyDetails.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
//                    storyDetails.putExtra("type", "Photo");
//                    startActivity(storyDetails);
//                    return;
//                }
                // if this media file has to be added to a story that already exists
                    try {
                        int id = Integer.parseInt(idOrNewOrCampaignQ);
                        DBHandler db = new DBHandler(getApplicationContext());
                        db.initUnqId(getApplicationContext());
                        db.insertMediaToExistStory(id, "photo", imagePath, tumb);
                        db.close();
                        Intent timeLine = new Intent(getApplicationContext(), DorActivity.class);
                        timeLine.putExtra("type", "Photo");
                        timeLine.putExtra("tPath", tumb);
                        timeLine.putExtra("uid", id);
                        startActivity(timeLine);
                        return;
                    } catch (NumberFormatException e) {
                        //if caught then it is not new not number, it is a question from campaign
                        Intent storyDetails = new Intent(getApplicationContext(), StoryDetails.class);
                        storyDetails.putExtra("path", imagePath);
                        storyDetails.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
                        storyDetails.putExtra("type", "Photo");
                        storyDetails.putExtra("tPath", tumb);
                        startActivity(storyDetails);
                        return;
                    }
                }
        });
    }
//---------------------------------video-----------------------------
    public void MoveToVideo(View view) throws Exception{
        videoChooserManager = new VideoChooserManager(this, ChooserType.REQUEST_CAPTURE_VIDEO,
                "/Roots/"+FolderId+"/videos", true);
        videoChooserManager.setVideoChooserListener(this);
        videoChooserManager.choose();
    }

    @Override
    public void onVideoChosen(final ChosenVideo video) {
        if (video == null) {
            Intent mainActivity = new Intent(MediaChooserActivity.this, MediaChooserActivity.class);
            mainActivity.putExtra("title", idOrNewOrCampaignQ);
            startActivity(mainActivity);
            Toast.makeText(MediaChooserActivity.this, "Video Capture Failed", Toast.LENGTH_SHORT).show();
            Log.e("eliLog", "From MediaChooserActivity.java. Videoc capture Failed");
            return;
        }
        //send to StoryDetails and go to timeline
        String videoPath = video.getVideoFilePath();
        String tumb = video.getThumbnailSmallPath();

        if(idOrNewOrCampaignQ.equals("new")) {
            Intent storyDetails = new Intent(getApplicationContext(), StoryDetails.class);
            storyDetails.putExtra("path", videoPath);
            storyDetails.putExtra("tPath", tumb);
            storyDetails.putExtra("type", "Video");
            startActivity(storyDetails);
            return;
        }
        // if this media file has to be added to a story that already exists
        else if(!idOrNewOrCampaignQ.equals("new"))
        {
            try {
                int id = Integer.parseInt(idOrNewOrCampaignQ);
                DBHandler db = new DBHandler(getApplicationContext());
                db.initUnqId(this);
                db.insertMediaToExistStory(id, "video", videoPath, tumb);
                db.close();
                Intent timeLine = new Intent(getApplicationContext(), DorActivity.class);
                timeLine.putExtra("uid", id);
                timeLine.putExtra("type", "Video");//TODO 1
                startActivity(timeLine);
                return;
            }
            catch(NumberFormatException e)
            {
                //if caught then it is not new not number, it is a question from campaign
                Intent storyDetails = new Intent(getApplicationContext(), StoryDetails.class);
                storyDetails.putExtra("path", videoPath);
                storyDetails.putExtra("tPath", tumb);
                storyDetails.putExtra("type", "Video");//TODO 1
                storyDetails.putExtra("title", idOrNewOrCampaignQ);
                startActivity(storyDetails);
                return;
            }
        }
    }

    //-------------------------timeline---------------------------------------
    public void MoveToTimiline(View view) {
        Intent i = new Intent(MediaChooserActivity.this, DorActivity.class);
        i.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
        startActivity(i);
    }

    //-----------------------------------general--------------------------

    @Override
    public void onActivityResult(final int requestCode,final int resultCode,final Intent data) {
        //Its a photo. New one or from gallery.
        if (resultCode == RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE ||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE )){
            MaterialDialog dialog = new MaterialDialog.Builder(MediaChooserActivity.this)
                    .title("Photo")
                    .backgroundColorRes(R.color.md_divider_white)
                    .titleColorRes(R.color.black_overlay)
                    .icon(null)//TODO
                    .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                    .input(null, null, false, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                            scndTitle = materialDialog.getInputEditText()
                                    .getText()
                                    .toString();
                        }
                    })
                    //.negativeText(R.string.dialog_cancel)
                    .positiveText("DONE")
                    .positiveColorRes(R.color.material_blue_grey_90)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            imageChooserManager.submit(requestCode, data);
                        }
                    })
                    .cancelable(false)
                    .show();
            FolderId++;
        }
        else if (resultCode == RESULT_OK &&
                requestCode == ChooserType.REQUEST_CAPTURE_VIDEO) {
            videoChooserManager.submit(requestCode, data);
            FolderId++;
        }
        else if (requestCode == RECORD_REQUEST) {
            if(data == null)
            {
                Intent MediaChooserActivity=new Intent(MediaChooserActivity.this, MediaChooserActivity.class);
                MediaChooserActivity.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
                startActivity(MediaChooserActivity);
                return;
            }
            else {
                Uri audioFileUri = data.getData();
                String path = audioFileUri.toString();
                FolderId++;
                if (idOrNewOrCampaignQ.equals("new")) {
                    Intent storyDetails = new Intent(getApplicationContext(), StoryDetails.class);
                    storyDetails.putExtra("path", path);
                    storyDetails.putExtra("type", "Audio");
                    startActivity(storyDetails);
                    return;
                } else if (!idOrNewOrCampaignQ.equals("new")) {
                    // if this media file has to be added to a story that already exists
                    try {
                        int id = Integer.parseInt(idOrNewOrCampaignQ);
                        DBHandler db = new DBHandler(getApplicationContext());
                        db.initUnqId(this);
                        db.insertMediaToExistStory(id, "audio", path, null);
                        db.close();
                        Intent timeLine = new Intent(getApplicationContext(), DorActivity.class);
                        timeLine = new Intent(this, DorActivity.class);
                        timeLine.putExtra("type", "Audio");
                        timeLine.putExtra("uid", id);
                        startActivity(timeLine);
                        return;
                    } catch (NumberFormatException e) {
                        //if caught then it is not new not number, it is a question from campaign
                        Intent storyDetails = new Intent(getApplicationContext(), StoryDetails.class);
                        storyDetails = new Intent(this, StoryDetails.class);
                        storyDetails.putExtra("path", path);
                        storyDetails.putExtra("title", idOrNewOrCampaignQ);
                        storyDetails.putExtra("type", "Audio");
                        startActivity(storyDetails);
                        return;
                    }
                }
            }
        }
        else {
            Intent MediaChooserActivity = new Intent(MediaChooserActivity.this, MediaChooserActivity.class);
            MediaChooserActivity.putExtra("idOrNewOrCampaignQ", idOrNewOrCampaignQ);
            startActivity(MediaChooserActivity);
            return;
        }

        return;
    }


    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MediaChooserActivity.this, MediaChooserActivity.class);
                i.putExtra("title", idOrNewOrCampaignQ);
                Toast.makeText(MediaChooserActivity.this, "MediaChooserActivity Failed", Toast.LENGTH_SHORT).show();
                startActivity(i);
                return;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


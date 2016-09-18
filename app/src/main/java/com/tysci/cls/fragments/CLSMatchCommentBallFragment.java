package com.tysci.cls.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.PathUtil;
import com.tysci.cls.R;
import com.tysci.cls.activitys.VideoRecordActivity;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.modles.UserInfoEntity;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.utils.UserInfoUtils;
import com.tysci.cls.views.widgets.LoadingView;
import com.tysci.imuilibrary.adapter.ChatMessageAdapter;
import com.tysci.imuilibrary.models.ChatEmojicon;
import com.tysci.imuilibrary.utils.CommonUtils;
import com.tysci.imuilibrary.views.chatviews.ChatExtendMenu;
import com.tysci.imuilibrary.views.chatviews.ChatInputMenu;
import com.tysci.imuilibrary.views.chatviews.ChatVoiceRecorderView;
import com.tysci.imuilibrary.views.chatviews.chatrows.BaseChatRow;

import java.io.File;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchCommentBallFragment extends BaseFragment implements ChatInputMenu.ChatInputMenuListener,ChatExtendMenu.ChatExtendMenuItemClickListener,
        EMMessageListener,SwipeRefreshLayout.OnRefreshListener {
    protected static final int REQUEST_CODE_CAMERA = 0x0001;
    protected static final int REQUEST_CODE_LOCAL = 0x0002;
    protected static final int REQUEST_CODE_RECORD_VIDEO=0x0003;

    @Bind(R.id.load_view)
    protected LoadingView loadingView=null;
    @Bind(R.id.chat_input_menu)
    protected ChatInputMenu chatInputMenu;
    @Bind(R.id.swipe_refresh_message)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.lv_message)
    protected ListView listView;
    @Bind(R.id.voice_recorder_view)
    protected ChatVoiceRecorderView voiceRecorderView;

    private CLSMatchEntity matchEntity;
    private int[] itemStrings={R.string.attach_take_pic,R.string.attach_picture,R.string.video_record};
    private int[] itemRes={R.drawable.icon_chat_takepic_selector, R.drawable.icon_chat_image_selector,R.drawable.icon_chat_video_record_selector};
    protected int pagesize = 20;

    private String chatRoomId="223459138636612036";
    private EMChatRoomChangeListener chatRoomChangeListener;
    protected EMConversation conversation;
    private ChatMessageAdapter adapter=null;
    private boolean isMessageListInited=false;
    protected File cameraFile;
    protected boolean haveMoreData = true;
    protected InputMethodManager inputManager;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_comment_ball;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        swipeRefresh.setOnRefreshListener(this);
        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Bundle data=getArguments();
        if(data!=null){
           matchEntity=data.getParcelable("match_info");
            if(matchEntity!=null){
                chatRoomId=matchEntity.getChatRoomId();
                //matchEntity.setChatRoomStatus(1);
            }
        }
        chatInputMenu.init(null);
        chatInputMenu.setChatInputMenuListener(this);

        registerExtendMenuItem();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(UserInfoUtils.checkLogin(baseActivity)) {
            if(matchEntity!=null) {
                if(matchEntity.getChatRoomStatus()==1) {
                    loadingView.showLoading("加入聊天室中...");
                    joinChatRoom();
                }else{
                    loadingView.setLoadResultInfo("抱歉，该聊天室暂未开放");
                }
            }else{
                loadingView.setLoadResultInfo("比赛数据异常");
            }
        }else{
            loadingView.setLoadResultInfo("您尚未登录，登录后才可查看", "点击登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoUtils.startLogin(baseActivity);
                }
            });
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onSendMessage(String content) {
        sendTextMessage(content);
    }

    @Override
    public void onBigExpressionClicked(ChatEmojicon emojicon) {
        sendBigExpressionMessage(emojicon);
    }

    @Override
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
        return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new ChatVoiceRecorderView.ChatVoiceRecorderCallback() {
            @Override
            public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                sendVoiceMessage(voiceFilePath,voiceTimeLength);
            }
        });
    }

    protected void registerExtendMenuItem(){
        for(int i = 0; i < itemStrings.length; i++){
            chatInputMenu.registerExtendMenuItem(itemStrings[i], itemRes[i], i+1, this);
        }
    }

    @Override
    public void onClick(int itemId, View view) {
        switch (itemId) {
            case 1:
                selectPicFromCamera();
                break;
            case 2:
                selectPicFromLocal();
                break;
            case 3:
                recordVideo();
                break;
        }

    }

    private void joinChatRoom(){
        KLog.e("加入聊天室...");
        boolean loginFlag=UserInfoUtils.getUserHuanxinLoginFlag(baseActivity);
        if(loginFlag){
            addChatRoom();
        }else{
            String account=UserInfoUtils.getUserAccount(baseActivity);
            loginHuanXinServer(account,account);
        }

    }

    private void addChatRoom(){
        EMClient.getInstance().chatroomManager().joinChatRoom(chatRoomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(final EMChatRoom value) {
                KLog.e("加入聊天室成功:"+value.getId());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity().isFinishing() || !chatRoomId.equals(value.getId()))
                            return;
                        // EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        loadingView.hideLoad();
                        addChatRoomChangeListenr();
                        onConversationInit();
                        initChatMessageList();
                    }
                });
            }

            @Override
            public void onError(final int error, final String errorMsg) {
                // TODO Auto-generated method stub
                KLog.e("加入聊天室失败:"+errorMsg);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingView.setLoadResultInfo("加入聊天室失败:" + errorMsg, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadingView.showLoading("加入聊天室中...");
                                joinChatRoom();
                            }
                        });
                    }
                });
            }
        });
    }

    private void loginHuanXinServer(String uid,String password){
        KLog.e("userId:"+uid);
        KLog.e("password:"+password);
        EMClient.getInstance().login(uid, password, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                UserInfoUtils.setUserHuanxinLoginFlag(baseActivity, true);
                Log.d("main", "登录聊天服务器成功！");
                // UserInfoUtils.requestUserInfo(LoginActivity.this, Tag, userPhone, true, loadingProgressDialog);
                addChatRoom();
            }

            @Override
            public void onProgress(int progress, String status) {
                KLog.e("status:" + status);
            }

            @Override
            public void onError(int code, final String message) {
                baseActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //ToastUtil.show(LoginActivity.this, "登录失败");
                        KLog.e("message:" + message);
                        Log.d("main", "登录聊天服务器失败！");
                        //UserInfoUtils.clearUserInfo(LoginActivity.this);
                        //dimssProgressDialog();
                    }
                });
            }
        });
    }

    /**
     * 初始化聊天
     */
    protected void onConversationInit(){
        conversation = EMClient.getInstance().chatManager().getConversation(chatRoomId, EMConversation.EMConversationType.ChatRoom, true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
    }

    /**
     * 添加聊天室监听事件
     */
    protected void addChatRoomChangeListenr() {
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(chatRoomId)) {
                    KLog.e(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    //getActivity().finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                KLog.e("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                KLog.e("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {

            }
        };
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }

    /**
     * 初始化聊天消息界面
     */
    private void initChatMessageList(){
        adapter=new ChatMessageAdapter(baseActivity,chatRoomId,listView);
        listView.setAdapter(adapter);
        adapter.refreshSelectLast();
        adapter.setChatMessageItemClickListener(new BaseChatRow.ChatMessageItemClickListener() {
            @Override
            public void onResendClick(EMMessage message) {

            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {

            }

            @Override
            public void onUserAvatarClick(String username) {

            }

            @Override
            public void onUserAvatarLongClick(String username) {

            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                chatInputMenu.hideExtendMenuContainer();
                return false;
            }
        });
        isMessageListInited = true;
    }


    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }
            // if the message is for current conversation
            if (username.equals(chatRoomId)) {
                adapter.refreshSelectLast();
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {
        if(isMessageListInited) {
            adapter.refresh();
        }
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {
        if(isMessageListInited) {
            adapter.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
        if(isMessageListInited) {
            adapter.refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isMessageListInited) {
            adapter.refresh();
        }
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatroomManager().leaveChatRoom(chatRoomId);

        if(chatRoomChangeListener != null){
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
        }
    }

    private void sendTextMessage(String content){
        //send message
        EMMessage message = EMMessage.createTxtSendMessage(content, chatRoomId);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, chatRoomId);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, chatRoomId);
        sendMessage(message);
    }

    protected void sendBigExpressionMessage(ChatEmojicon emojicon){
        EMMessage message = EMMessage.createTxtSendMessage("[" + emojicon.getName() + "]", chatRoomId);
        if(emojicon.getIdentityCode() != null){
            message.setAttribute("em_expression_id", emojicon.getIdentityCode());
        }
        message.setAttribute("em_is_big_expression", true);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, chatRoomId);
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message){
        if (message == null) {
            return;
        }
        message.setChatType(EMMessage.ChatType.ChatRoom);
        KLog.e("user_name:"+UserInfoUtils.getUserInfo(baseActivity).getProfile().getNickname());
        message.setAttribute("user_name", UserInfoUtils.getUserInfo(baseActivity).getProfile().getNickname());
        message.setAttribute("user_header",UserInfoUtils.getUserInfo(baseActivity).getProfile().getPortrait());
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if(isMessageListInited) {
            adapter.refreshSelectLast();
        }
    }

    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }


    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!CommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(),"您的手机没有SD卡", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    protected void recordVideo(){
        Intent intent=new Intent(baseActivity, VideoRecordActivity.class);
        startActivityForResult(intent,REQUEST_CODE_RECORD_VIDEO);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                KLog.e("获取拍照图片...");
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            }else if(requestCode==REQUEST_CODE_RECORD_VIDEO){
                if(data!=null){
                    String videoPath=data.getStringExtra("video_path");
                    String videoCoverPath=data.getStringExtra("video_cover");
                    long videoTimes=data.getLongExtra("video_times",0);
                    sendVideoMessage(videoPath,videoCoverPath, (int) (videoTimes/1000));
                }
            }
        }
    }

    @Override
    protected void userLogin(UserInfoEntity userInfoEntity) {
        super.userLogin(userInfoEntity);
        if(matchEntity!=null) {
            if(matchEntity.getChatRoomStatus()==1) {
                loadingView.showLoading("加入聊天室中...");
                joinChatRoom();
            }else{
                loadingView.setLoadResultInfo("抱歉，该聊天室暂未开放");
            }
        }else{
            loadingView.setLoadResultInfo("比赛数据异常");
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (listView.getFirstVisiblePosition() == 0  && haveMoreData) {
                    List<EMMessage> messages;
                    try {
                        messages = conversation.loadMoreMsgFromDB(((EMMessage)adapter.getItem(0)).getMsgId(),
                                    pagesize);
                    } catch (Exception e1) {
                        swipeRefresh.setRefreshing(false);
                        return;
                    }
                    if (messages.size() > 0) {
                        adapter.refreshSeekTo(messages.size() - 1);
                        if (messages.size() != pagesize) {
                            haveMoreData = false;
                        }
                    } else {
                        haveMoreData = false;
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                            Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
            }
        }, 600);
    }
}

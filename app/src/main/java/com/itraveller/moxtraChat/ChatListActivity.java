package com.itraveller.moxtraChat;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.itraveller.R;
import com.moxtra.binder.sdk.InviteToChatCallback;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException;
import com.moxtra.sdk.MXGroupChatSession;
import com.moxtra.sdk.MXGroupChatSessionCallback;
import com.moxtra.sdk.MXSDKException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by blade on 5/13/15.
 */
public class ChatListActivity extends Activity implements View.OnClickListener, MXChatManager.OnCreateChatListener, MXChatManager.OnOpenChatListener {

    private static final String TAG = "ChatList";


    private RecyclerView chatListRecyclerView=null;
    private ChatListAdapter adapter=null;
    private RecyclerView.LayoutManager layoutManager=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_chat_list);


        chatListRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_list);
        chatListRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        chatListRecyclerView.setLayoutManager(layoutManager);


        MXChatManager.getInstance().setOnMeetEndListener(new MXChatManager.OnEndMeetListener() {
            @Override
            public void onMeetEnded(String meetId) {
                adapter.refreshData();
            }
        });

        MXChatManager.getInstance().setGroupChatSessionCallback(new MXGroupChatSessionCallback() {
            @Override
            public void onGroupChatSessionCreated(MXGroupChatSession session) {
                adapter.refreshData();
            }

            @Override
            public void onGroupChatSessionUpdated(MXGroupChatSession session) {
                adapter.refreshData();
            }

            @Override
            public void onGroupChatSessionDeleted(MXGroupChatSession session) {
                adapter.refreshData();
            }
        });

        MXChatManager.getInstance().setChatInviteCallback(new InviteToChatCallback() {
            @Override
            public void onInviteToChat(String binderID, Bundle extras) {
                Log.d(TAG, "Invite to binder: " + binderID);
            }
        });


        adapter = new ChatListAdapter();
        chatListRecyclerView.setAdapter(adapter);


    }



    @Override
    protected void onResume() {
        super.onResume();
        adapter.refreshData();
    }

    @Override
    public void onClick(View v) {

    }

    private String getTopic() {
        return "Persistent Wall";
    }



    @Override
    public void onCreateChatSuccess(String binderId) {
        Log.i(TAG, "Create Chat Success. The binderId = " + binderId);
    }

    @Override
    public void onCreateChatFailed(int i, String s) {
        Log.e(TAG, "Failed to create chat with code: " + i + ", msg: " + s);
        Toast.makeText(this, "Failed to create chat: " + s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOpenChatSuccess() {
        Log.i(TAG, "Open chat success.");
    }

    @Override
    public void onOpenChatFailed(int i, String s) {
        Log.e(TAG, "Failed to open chat with code: " + i + ", msg: " + s);
        Toast.makeText(this, "Failed to open chat: " + s, Toast.LENGTH_LONG).show();
    }

    public class ChatListAdapter extends RecyclerView.Adapter {

        List<MXGroupChatSession> sessions=null;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView ivCover;
            TextView tvTopic, tvLastMessage, tvBadge;
            Button btnDelete, btnMeet;
            MXGroupChatSession session;
            View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
                tvTopic = (TextView) itemView.findViewById(R.id.tv_topic);
                tvLastMessage = (TextView) itemView.findViewById(R.id.tv_last_message);
                tvBadge = (TextView) itemView.findViewById(R.id.tv_badge);
                btnDelete = (Button) itemView.findViewById(R.id.btn_delete);
                btnMeet = (Button) itemView.findViewById(R.id.btn_meet);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (session.isAChat()) {
                            try {
                                MXChatManager.getInstance().openChat(session.getSessionID(), ChatListActivity.this);
                            } catch (MXException.AccountManagerIsNotValid accountManagerIsNotValid) {
                                Log.e(TAG, "Error when open chat", accountManagerIsNotValid);
                            }
                        } else if (session.isAMeet()) {
                            joinMeet();
                        }
                    }
                });
                btnDelete.setOnClickListener(this);
                btnMeet.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_meet) {
                    if (session.isAMeet()) {
                        joinMeet();
                    } else {
                        MXChatManager.getInstance().getChatMembers(session.getSessionID(), new MXChatManager.OnGetChatMembersListener() {
                            @Override
                            public void onGetChatMembersDone(ArrayList<String> arrayList) {
                                try {
                                    MXChatManager.getInstance().startMeet("demo" + "'s meet", null,
                                            arrayList, new MXChatManager.OnStartMeetListener() {
                                                @Override
                                                public void onStartMeetDone(String meetId, String meetUrl) {
                                                    Log.d(TAG, "Meet started: " + meetId);
                                                }

                                                @Override
                                                public void onStartMeetFailed(int i, String s) {
                                                    Log.e(TAG, "onStartMeetFailed: " + s);
                                                }
                                            });
                                } catch (MXSDKException.Unauthorized unauthorized) {
                                    Log.e(TAG, "Error when start meet", unauthorized);
                                } catch (MXSDKException.MeetIsInProgress meetIsInProgress) {
                                    Log.e(TAG, "Error when start meet", meetIsInProgress);
                                }
                            }

                            @Override
                            public void onGetChatMembersFailed(int i, String s) {
                                Log.e(TAG, "onGetMembersFailed: " + s);
                            }
                        });
                    }
                } else if (v.getId() == R.id.btn_delete) {
                    new MaterialDialog.Builder(ChatListActivity.this)
                            .title(R.string.delete_confirm_title)
                            .content(R.string.delete_confirm)
                            .positiveText(android.R.string.yes)
                            .positiveColorRes(R.color.red)
                            .negativeColorRes(R.color.black)
                            .negativeText(android.R.string.no)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    MXChatManager.getInstance().deleteChat(session.getSessionID());
                                    refreshData();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                }
                            })
                            .show();
                }
            }

            private void joinMeet() {
                if (!MXChatManager.getInstance().isAMeetingInProgress()) {
                    try {
                        MXChatManager.getInstance().joinMeet(session.getMeetID(), "dummy",
                                new MXChatManager.OnJoinMeetListener() {
                                    @Override
                                    public void onJoinMeetDone(String meetId, String meetUrl) {
                                        Log.d(TAG, "Joined meet: " + meetId);
                                    }

                                    @Override
                                    public void onJoinMeetFailed() {
                                        Log.e(TAG, "Unable to join meet.");
                                    }
                                });
                    } catch (MXSDKException.MeetIsInProgress meetIsInProgress) {
                        Log.e(TAG, "Error when join meet", meetIsInProgress);
                    }
                }
            }

        }

        public ChatListAdapter() {
            super();
        }

        private void sortData() {
            Collections.sort(sessions, new Comparator<MXGroupChatSession>() {
                @Override
                public int compare(MXGroupChatSession lhs, MXGroupChatSession rhs) {
                    if (lhs.isAMeet()) return -1;
                    if (rhs.isAMeet()) return 1;
                    if (lhs.getLastFeedTimeStamp() > rhs.getLastFeedTimeStamp()) return -1;
                    return 0;
                }
            });
        }

        public void refreshData() {
            sessions = MXChatManager.getInstance().getGroupChatSessions();
            sortData();
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_timeline, parent, false);
            RecyclerView.ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ViewHolder theHolder = (ViewHolder) holder;
            MXGroupChatSession session = sessions.get(position);
            if (session.getCoverImagePath() != null) {
                theHolder.ivCover.setImageURI(Uri.fromFile(new File(session.getCoverImagePath())));
            }
            theHolder.tvTopic.setText(session.getTopic());
            theHolder.tvLastMessage.setText(session.getLastFeedContent());
            theHolder.session = session;
            if (session.isOwner()) {
                theHolder.btnDelete.setText(R.string.Delete);
            } else {
                theHolder.btnDelete.setText(R.string.Leave);
            }
            if (session.isAMeet()) {
                theHolder.tvTopic.setText(session.getMeetID());
                ((CardView) theHolder.itemView).setCardBackgroundColor(getResources().getColor(R.color.yellow_100));
                theHolder.btnDelete.setVisibility(View.GONE);
                if (MXChatManager.getInstance().isAMeetingInProgress()) {
                    theHolder.btnMeet.setVisibility(View.GONE);
                } else {
                    theHolder.btnMeet.setVisibility(View.VISIBLE);
                    theHolder.btnMeet.setText(R.string.Join);
                }
            } else {
                theHolder.btnDelete.setVisibility(View.VISIBLE);
                theHolder.btnMeet.setVisibility(View.VISIBLE);
                theHolder.btnMeet.setText(R.string.Meet);
                ((CardView) theHolder.itemView).setCardBackgroundColor(getResources().getColor(R.color.white));
            }
            if (session.getUnreadFeedCount() > 0) {
                theHolder.tvBadge.setText(String.valueOf(session.getUnreadFeedCount()));
                theHolder.tvBadge.setVisibility(View.VISIBLE);
            } else {
                theHolder.tvBadge.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return sessions.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

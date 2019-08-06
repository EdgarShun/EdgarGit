package com.example.administrator.study;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class AppMethod {
    //定义上下文对象
    public static ReactContext myContext;

    private AIUIAgent mAIUIAgent = null;

    //交互状态
    private int mAIUIState = AIUIConstant.STATE_IDLE;

    private static String TAG = "AppMethod";


    //录音当中...
    public void recording(){

        if( !checkAIUIAgent() ){
            return;
        }
    }

    //检查AIUIAgent时候存在
    private boolean checkAIUIAgent() {

        if (null == mAIUIAgent) {
            //创建AIUIAgent
            mAIUIAgent = AIUIAgent.createAgent(myContext, getAIUIParams(), mAIUIListener);
        }
        if( null == mAIUIAgent ){
            final String strErrorTip = "创建 AIUI Agent 失败！";
            Log.i(TAG,strErrorTip );
//            this.mNlpText.setText( strErrorTip );
        }

        return null != mAIUIAgent;
    }


    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = myContext.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];
            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }


    //AIUI事件监听器
    private AIUIListener mAIUIListener = new AIUIListener() {
        @Override
        public void onEvent(AIUIEvent event) {

            switch (event.eventType){

                case AIUIConstant.EVENT_WAKEUP:
                    break;
                case AIUIConstant.EVENT_RESULT:

                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("param");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")){
                            String cnt_id = content.getString("cnt_id");
                            String cntStr = new String(event.data.getByteArray(cnt_id), "utf-8");
                            JSONObject cntJson = new JSONObject(cntStr);
                            String sub = params.optString("sub");
                            JSONObject result = cntJson.optJSONObject("intent");

                            if ("nlp".equals(sub) && result.length() > 2) {
                                // 解析得到语义结果
                                String str = "";
                                //在线语义结果
                                if (result.optInt("rc") == 0) {
                                    JSONObject answer = result.optJSONObject("answer");
                                    if (answer != null) {
                                        str = answer.optString("text");
                                    }
                                } else {
                                    str = "rc4，无法识别";
                                }
                                if (!TextUtils.isEmpty(str)) {
                                    /*mNlpText.append("\n");
                                    mNlpText.append(str);*/
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    mNlpText.append("\n");
                    break;

                case AIUIConstant.EVENT_ERROR:
                    break;

                case AIUIConstant.EVENT_VAD:
                    break;

                case AIUIConstant.EVENT_START_RECORD:
                    break;

                case AIUIConstant.EVENT_STOP_RECORD:
                    break;

                    //状态事件
                case AIUIConstant.EVENT_STATE:
                    mAIUIState = event.arg1;

                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
//                        showTip("STATE_IDLE");
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
//                        showTip("STATE_READY");
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
//                        showTip("STATE_WORKING");
                    }
                    break;

                    default:
                        break;
            }

        }
    };

    //开始录音
    private void startVoiceNlp(){
        Log.i( TAG, "start voice nlp" );
//        mNlpText.setText("");

        // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收语音输入
        // 默认为oneshot 模式，即一次唤醒后就进入休眠，如果语音唤醒后，需要进行文本语义，请将改段逻辑copy至startTextNlp()开头处
        if( AIUIConstant.STATE_WORKING != 	this.mAIUIState ){
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }

        // 打开AIUI内部录音机，开始录音
        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage( AIUIConstant.CMD_START_RECORD, 0, 0, params, null );
        mAIUIAgent.sendMessage(writeMsg);
    }

}

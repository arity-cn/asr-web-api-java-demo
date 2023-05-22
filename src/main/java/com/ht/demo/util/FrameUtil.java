package com.ht.demo.util;

import com.alibaba.fastjson2.JSON;
import com.ht.demo.pojo.WebsocketClientFrame;

public class FrameUtil {

    /**
     * 构建开始报文
     *
     * @return 开始报文
     */
    public static String buildStartFrame() {
        WebsocketClientFrame frame = new WebsocketClientFrame();
        frame.setSignal("start");
        WebsocketClientFrame.Business business = new WebsocketClientFrame.Business();
        business.setVadEos(5000);
        WebsocketClientFrame.AudioData data = new WebsocketClientFrame.AudioData();
        data.setAudioFormatInfo("WAV");
        data.setSampleRate("SAMPLE_RATE_16K");
        frame.setBusiness(business);
        frame.setData(data);
        return JSON.toJSONString(frame);
    }

    /**
     * 构建结束报文
     *
     * @return 结束报文
     */
    public static String buildEndFrame() {
        WebsocketClientFrame frame = new WebsocketClientFrame();
        frame.setSignal("end");
        return JSON.toJSONString(frame);
    }
}

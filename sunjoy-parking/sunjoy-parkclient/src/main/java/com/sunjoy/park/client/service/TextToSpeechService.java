package com.sunjoy.park.client.service;


import com.sunjoy.common.core.utils.StringUtils;
import com.sunjoy.common.redis.service.LocalCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/25
 */
@Slf4j
@Service
public class TextToSpeechService {

    private final LocalCache<String, byte[]> localVoiceCache = new LocalCache<>();
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ThreadPoolTaskExecutor clientExecutor;

    /**
     * 返数据
     *
     * @param text
     * @return
     */
    public byte[] synthesizeSpeech(String text) {
        // OpenTTS API URL
        String url = "http://192.168.2.11:5500/api/tts";
        StringBuilder urlBuilder = new StringBuilder(url);
        String speechRate = "0.1";
        urlBuilder.append("?text=").append(text).append("&voice=").append("zh");
        //控制语速
        urlBuilder.append("&speech_rate=").append(speechRate);
        log.info("请求地址：{}", urlBuilder.toString());
        return restTemplate.getForObject(urlBuilder.toString(), byte[].class);

    }

    /**
     * 本地发音
     *
     * @param text
     */
    public void localSpeech(String text) {
        Long start = System.currentTimeMillis();
        clientExecutor.submit(() -> {
            try {
                CountDownLatch latch = new CountDownLatch(1); // 创建计数器
                // 获取音频输入流

                String key = StringUtils.hashString(text);
                byte[] byteArray = localVoiceCache.get(key);
                if (null == byteArray) {
                    byteArray = synthesizeSpeech(text);
                    localVoiceCache.put(key, byteArray);
                }
                // 播放音频
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(byteArray));
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
                // 转换流为Clip对象
                AudioFormat format = audioInputStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);

                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(audioInputStream);
                clip.start();

                // 监控音频播放状态
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        latch.countDown(); // 音频播放完成，计数器减1
                    }
                });
                log.info("语音播放完毕，耗时:{}毫秒", System.currentTimeMillis() - start);
                // 等待音频播放完成
                latch.await(); // 阻塞，直到计数器为0
                clip.close();
                bufferedInputStream.close();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
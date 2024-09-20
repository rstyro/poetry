package top.rstyro.poetry.tts;

import lombok.Data;

@Data
public class ParamDto {
    private String ssml;
    /**
     * 1. audio-16khz-128kbitrate-mono-mp3
     * •采样率: 16 kHz
     * •比特率: 128 kbps
     * •声道: 单声道
     * •格式: MP3
     * 2. audio-16khz-64kbitrate-mono-mp3
     * •采样率: 16 kHz
     * •比特率: 64 kbps
     * •声道: 单声道
     * •格式: MP3
     *
     * 3. audio-16khz-32kbitrate-mono-mp3
     * •采样率: 16 kHz
     * •比特率: 32 kbps
     * •声道: 单声道
     * •格式: MP3
     * 4. audio-24khz-48kbitrate-mono-mp3
     * •采样率: 24 kHz
     * •比特率: 48 kbps
     * •声道: 单声道
     * •格式: MP3
     * 5. audio-24khz-96kbitrate-mono-mp3
     * •采样率: 24 kHz
     * •比特率: 96 kbps
     * •声道: 单声道
     * •格式: MP3
     * 6. audio-24khz-160kbitrate-mono-mp3
     * •采样率: 24 kHz
     * •比特率: 160 kbps
     * •声道: 单声道
     * •格式: MP3
     * 7. audio-48khz-96kbitrate-mono-mp3
     * •采样率: 48 kHz
     * •比特率: 96 kbps
     * •声道: 单声道
     * •格式: MP3
     * 8. audio-48khz-192kbitrate-mono-mp3
     * •采样率: 48 kHz
     * •比特率: 192 kbps
     * •声道: 单声道
     * •格式: MP3
     * 9. riff-16khz-16bit-mono-pcm
     * •采样率: 16 kHz
     * •比特率: 16 bit
     * •声道: 单声道
     * •格式: WAV (PCM)
     * 10. riff-24khz-16bit-mono-pcm
     * •采样率: 24 kHz
     * •比特率: 16 bit
     * •声道: 单声道
     * •格式: WAV (PCM)
     * 11. riff-48khz-16bit-mono-pcm
     * •采样率: 48 kHz
     * •比特率: 16 bit
     * •声道: 单声道
     * •格式: WAV (PCM)
     * 12. raw-16khz-16bit-mono-pcm
     * •采样率: 16 kHz
     * •比特率: 16 bit
     * •声道: 单声道
     * •格式: RAW (PCM)
     * 13. raw-24khz-16bit-mono-pcm
     * •采样率: 24 kHz
     * •比特率: 16 bit
     * •声道: 单声道
     * •格式: RAW (PCM)
     * 14. raw-48khz-16bit-mono-pcm
     * •采样率: 48 kHz
     * •比特率: 16 bit
     * •声道: 单声道
     * •格式: RAW (PCM)
     */
    private String ttsAudioFormat="audio-16khz-32kbitrate-mono-mp3";
    private Integer offsetInPlainText=0;
    private ParamPropDto properties;
}

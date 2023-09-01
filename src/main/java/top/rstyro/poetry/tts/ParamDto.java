package top.rstyro.poetry.tts;

import lombok.Data;

@Data
public class ParamDto {
    private String ssml;
    private String ttsAudioFormat="audio-24khz-160kbitrate-mono-mp3";
    private Integer offsetInPlainText=0;
    private ParamPropDto properties;
}

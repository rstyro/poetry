package top.rstyro.poetry.tts;

/**
 * token来源：https://github.com/rany2/edge-tts/blob/master/src/edge_tts/constants.py
 * 支持的声音列表：https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list?trustedclienttoken=6A5AA1D4EAFF4E9FB37E23D68491D6F4
 *
 */
public enum TtsStyle {
    DEFAULT("Default","默认"),
    ASSISTANT("assistant","助理"),
    CHAT("chat","聊天"),
    CUSTOMER_SERVICE("customerservice","客服"),
    NEWSCAST("newscast","新闻"),
    AFFECTIONATE("affectionate","深情"),
    ANGRY("angry","生气"),
    CALM("calm","冷静"),
    CHEERFUL("cheerful","快乐"),
    DISGRUNTLED("disgruntled","不满"),
    FEARFUL("fearful","害怕"),
    GENTLE("gentle","温柔"),
    LYRICAL("lyrical","抒情"),
    SAD("sad","伤心"),
    SERIOUS("serious","严肃"),
    POETRY_READING("poetry-reading","诗歌朗读"),
    NARRATION_PROFESSIONAL("narration-professional","专业客观"),
    NEWSCAST_CASUAL("newscast-casual","通用随意语气发布一般新闻"),
    EMBARRASSED("embarrassed","尴尬"),
    DEPRESSED("depressed","抑郁"),
    ENVIOUS("envious","嫉妒"),
    NARRATION_RELAXED("narration-relaxed","舒缓悦耳"),
    ADVERTISEMENT_UPBEAT("Advertisement_upbeat","兴奋精力充沛的推广产品或服务"),
    SPORTS_COMMENTARY("Sports_commentary","体育解说"),
    SPORTS_COMMENTARY_EXCITED("Sports_commentary_excited","快速且充满活力的播报体育赛事"),
    DOCUMENTARY_NARRATION("documentary-narration","记录片叙事"),
    EXCITED("excited","兴奋"),
    FRIENDLY("friendly","友好"),
    TERRIFIED("terrified","害怕"),
    SHOUTING("shouting","喊叫"),
    UNFRIENDLY("unfriendly","不友好"),
    WHISPERING("whispering","窃窃私语"),
    HOPEFUL("hopeful","充满希望"),
    ;
    private String stype;
    private String desc;

    TtsStyle(String stype,String desc){
        this.stype=stype;
        this.desc=desc;
    }

    public String getStype() {
        return stype;
    }

    public String getDesc() {
        return desc;
    }
}

package jp.ac.titech.itpro.pbl.burning_pro;

import org.json.JSONObject;

/** JSONを管理するクラスの基底クラス。*/
public class JSONData {
    /** オリジナルのJSON */
    protected final JSONObject json;

    /**
     * @param json 管理するJSON
     */
    public JSONData(JSONObject json) {
        this.json = json;
    }
}

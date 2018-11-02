package jp.ac.titech.itpro.pbl.burning_pro;

import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/** JSONを管理するクラスの基底クラス。*/
public class JSONData {
    /** オリジナルのJSON */
    protected final JSONObject json;

    // parseDate
    private static SimpleDateFormat sdf =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US);

    /**
     * @param json 管理するJSON
     */
    public JSONData(JSONObject json) {
        this.json = json;
    }

    /**
     * 日時のパースのためのユーティリティ関数
     * @param dateString パースする文字列
     * */
    protected static Date parseDate(String dateString) throws ParseException {
        // strip milli sec
        dateString = dateString.replaceFirst("\\.\\d*", "");
        return sdf.parse(dateString);
    }
}

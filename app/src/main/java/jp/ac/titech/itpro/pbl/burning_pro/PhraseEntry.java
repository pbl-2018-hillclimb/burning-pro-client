package jp.ac.titech.itpro.pbl.burning_pro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * 不謹慎発言のデータを管理するクラス。
 */
@SuppressWarnings("serial")
public class PhraseEntry extends JSONData implements Serializable{
    /**　発言そのものに関する情報 */
    public final Phrase phrase;

    /** 発言者に関する情報 */
    public final Person person;

    /** システムワイドなデータ */
    public final SysMeta sysMeta;

    /** ユーザーローカルなデータ */
    public final UserMeta userMeta;

    /**
     * @param json 不謹慎発言のJSON
     * @throws JSONException 与えられたJSONの形式が正しくなかった場合
     */
    public PhraseEntry(JSONObject json) throws JSONException {
        super(json);
        phrase = new Phrase(json.getJSONObject("phrase"));
        person = new Person(json.getJSONObject("person"));
        sysMeta = new SysMeta(json.getJSONObject("sys_meta"));
        userMeta = new UserMeta(json.getJSONObject("user_meta"));
    }

    /** 発言情報を管理するクラス。 */
    public class Phrase extends JSONData implements Serializable {
        /** 発言のタイトル */
        public final String title;

        /** 発言の本体 */
        public final String phrase;

        /** 内部ID */
        public final Long internalID;

        /** 元となった投稿のURL */
        public final Optional<URL> url;

        /** 投稿が削除されているか */
        public final boolean deleted;

        /** 投稿日 */
        public final Optional<Date> datetime;

        /** 登録日 */
        public final Date created;

        /**
         * @param json 発言情報のJSON
         * @throws JSONException JSONの形式が正しくなかった場合
         */
        public Phrase(JSONObject json) throws JSONException {
            super(json);
            try {
                phrase = json.getString("phrase");
                title = json.getString("title");
                internalID = json.getLong("internal_id");
                deleted = json.getBoolean("deleted");

                // cannot throw Exception inside Optional.map
                String urlString = json.optString("url");
                if (urlString.length() > 0)
                    url = Optional.of(new URL(urlString));
                else
                    url = Optional.empty();

                String datetimeString = json.optString("datetime");
                if(datetimeString.length() > 0)
                    datetime = Optional.of(JSONData.parseDate(datetimeString));
                else
                    datetime = Optional.empty();

                created = JSONData.parseDate(json.getString("created"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new JSONException("invalid JSON format : Entry.Phrase");
            }
        }
    }

    /** 発言者情報を管理するクラス。 */
    public class Person extends JSONData implements Serializable {
        /** 内部ID */
        public final Long internalID;

        /** 投稿者の実名 */
        public final Optional<String> realName;

        /** 投稿者の表示名 */
        public final Optional<String> displayName;

        /** 投稿者のwebページ */
        public final ArrayList<URL> url;

        /** 投稿者のtwitterアカウント（'@'は含まれない） */
        public final Optional<String> twitter;

        /**
         * @param json 発言情報のJSON
         * @throws JSONException 与えられたJSONの形式が正しくなかった場合
         */
        public Person(JSONObject json) throws JSONException {
            super(json);
            try {
                internalID = json.getLong("internal_id");

                String realNameString = json.optString("real_name");
                if(realNameString.length() > 0)
                    realName = Optional.of(realNameString);
                else
                    realName = Optional.empty();

                String displayNameString = json.optString("display_name");
                if(displayNameString.length() > 0)
                    displayName = Optional.of(displayNameString);
                else
                    displayName = Optional.empty();

                String twitterString = json.optString("twitter");
                if(twitterString.length() > 0)
                    twitter = Optional.of(displayNameString);
                else
                    twitter = Optional.empty();

                JSONArray urlArray = json.getJSONArray("url");
                url = new ArrayList<>();
                for (int i = 0; i < urlArray.length(); ++i) {
                    String urlString = urlArray.getString(i);
                    url.add(new URL(urlString));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JSONException("invalid JSON format : Entry.Person");
            }
        }
    }

    /** システムワイドなメタデータを管理するクラス。 */
    public class SysMeta extends JSONData implements Serializable {
        /** システム全体で利用された回数 */
        public final Long useCount;

        /** システム全体での被お気に入り数 */
        public final Long favCount;

        /** システム共通のタグ */
        public final ArrayList<String> tags;

        /**
         * @param json メタデータのJSON
         * @throws JSONException 与えられたJSONの形式が正しくなかった場合
         */
        public SysMeta(JSONObject json) throws JSONException {
            super(json);
            try {
                useCount = json.getLong("use_count");
                favCount = json.getLong("fav_count");

                JSONArray tagArray = json.getJSONArray("tags");
                tags = new ArrayList<>();
                for (int i = 0; i < tagArray.length(); ++i) {
                    tags.add(tagArray.getString(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JSONException("invalid JSON format : Entry.SysMeta");
            }
        }
    }

    /** ユーザーローカルなメタデータを管理するクラス。 */
    public class UserMeta extends JSONData  implements Serializable {
        /** お気に入りに登録しているか */
        public final boolean favorite;

        /** ユーザーが利用した回数 */
        public final Long useCount;

        /** 登録されているマイリスト */
        public final ArrayList<String> mylists;

        /**
         * @param json メタデータのJSON
         * @throws JSONException 与えられたJSONの形式が正しくなかった場合
         */
        public UserMeta(JSONObject json) throws JSONException {
            super(json);
            try {
                favorite = json.getBoolean("favorite");
                useCount = json.getLong("use_count");

                JSONArray mylistArray = json.getJSONArray("mylists");
                mylists = new ArrayList<>();
                for (int i = 0; i < mylistArray.length(); ++i) {
                    mylists.add(mylistArray.getString(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new JSONException("invalid JSON format : Entry.UserMeta");
            }
        }
    }
}

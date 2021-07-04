package fr.lernejo.navy_battle.entities;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiEntity {
    private final String id;
    private final String url;
    private final String message;

    public ApiEntity(String id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("url", url);
        obj.put("message", message);
        return obj;
    }

    public static ApiEntity fromJSON(JSONObject object) throws JSONException {
        return new ApiEntity(
            object.getString("id"),
            object.getString("url"),
            object.getString("message")
        );
    }

    public ApiEntity withURL(String url) {
        return new ApiEntity(
            this.id,
            url,
            this.message
        );
    }
}

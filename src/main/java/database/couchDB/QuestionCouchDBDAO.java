package database.couchDB;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Question;

public class QuestionCouchDBDAO extends AbstractCouchDBDAO {
    private Gson gson;

    public QuestionCouchDBDAO(CouchDBAccess couchDBAccess) {
        super(couchDBAccess);
        gson = new Gson();
    }

    public String storeOne(Question question) {
        String jsonString = gson.toJson(question);
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return saveDocument(jsonObject);}
}

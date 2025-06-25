package database.couchDB;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionCouchDBDAO extends AbstractCouchDBDAO {
    private Gson gson;

    public QuestionCouchDBDAO(CouchDBAccess couchDBAccess) {
        super(couchDBAccess);
        gson = new Gson();
    }

    public List<Question> getQuestions(String question) {
        // Haal alle documenten op, in de vorm van JsonObjecten;
        // zet om naar Vraag, mogelijk om op deel van vraag te zoeken
        Question result;
        List<Question> questions = new ArrayList<>();
        for (JsonObject jsonObject : getAllDocuments()) {
            result = gson.fromJson(jsonObject, Question.class);
            if (result.getQuestionText().contains(question)) {
                questions.add(result);
            }
        }
        return questions;
    }

        public String storeOne(Question question) {
        String jsonString = gson.toJson(question);
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        return saveDocument(jsonObject);}
}

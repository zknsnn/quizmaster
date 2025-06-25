package database.mysql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.CouchDBAccess;
import model.QuizResult;

import java.time.LocalDateTime;

public class QuizResultCouchDBDAO extends AbstractCouchDBDAO {

    private Gson gson;



    public QuizResultCouchDBDAO(CouchDBAccess couchDBAccess) {
        super(couchDBAccess);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gson = gsonBuilder.setPrettyPrinting().create();
    }

    public String saveSingleQuizResult(QuizResult quizResult) {
        // QuizResult object omzetten naar JsonObject, zodat het opgeslagen kan worden mbv save()
        String jsonString = gson.toJson(quizResult);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        return saveDocument(jsonObject);
    }

    public QuizResult getQuizResultByDocId(String doc_Id) {
        // Als je het id van een couchDB document weet, kun je daarmee een Quiz ophalen
        return gson.fromJson(getDocumentById(doc_Id), QuizResult.class);
    }

    public QuizResult getQuizResult(String quizName, String userName) {
        // Haal alle documenten op, in de vorm van JsonObjecten; zet om naar Quiz en test op quizName
        QuizResult resultaat;
        for (JsonObject jsonObject : getAllDocuments()) {
            resultaat = gson.fromJson(jsonObject, QuizResult.class);
            if (resultaat.getQuiz().getQuizName().equals(quizName) && (resultaat.getUser()
                    .getUserName().equalsIgnoreCase(userName))) {
                return resultaat;
            }
        }
        return null;
    }

    public void deleteQuizResult(QuizResult quizResult) {
        // Op basis van _id en _rev kun je een document in CouchDB verwijderen
        String[] idAndRev = getIdAndRevOfQuizResult(quizResult);
        deleteDocument(idAndRev[0], idAndRev[1]);
    }

    public String[] getIdAndRevOfQuizResult (QuizResult quizResult) {
        // Vind het _id en _rev van document behorend bij een QuizResult met quizName userName combinatie.
        String[] idAndRev = new String[2];
        for (JsonObject jsonObject : getAllDocuments()) {
            if (jsonObject.has("quizName") && jsonObject.get("quizName").getAsString().equals(quizResult.getQuiz().getQuizName())
            && jsonObject.has("userName") && jsonObject.get("userName").getAsString().equals(quizResult.getUser().getUserName()))
            {
                idAndRev[0] = jsonObject.get("_id").getAsString();
                idAndRev[1] = jsonObject.get("_rev").getAsString();
            }
        }
        return idAndRev;
    }

    public String updateQuizResult (QuizResult quizResult) {
        // Haal _id en _rev van document op behorend bij quiz
        // Zet quizResultaat om in JsonObject
        String[] idAndRev = getIdAndRevOfQuizResult(quizResult);
        String jsonString = gson.toJson(quizResult);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        // Voeg _id en _rev toe aan JsonObject, nodig voor de update van een document.
        jsonObject.addProperty("_id" , idAndRev[0]);
        jsonObject.addProperty("_rev" , idAndRev[1]);
        return updateDocument(jsonObject);
    }
}

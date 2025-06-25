package database.couchDB;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;

// DAO voor CouchDB opslag van User-objecten
// Let op: zit gewoon in de mysql-map. Niet moeilijk over doen.
public class UserCouchDbDAO extends AbstractCouchDBDAO {

    private Gson gson;

    // Constructor / CouchDB-access meegeven en Gson klaarzetten.
    public UserCouchDbDAO(CouchDBAccess couchDBAccess) {
        super(couchDBAccess);
        gson = new Gson();
    }

    // CREATE → nieuwe user opslaan in CouchDB
    public String saveSingleUser(User user) {
        // Zet User om naar JSON-string
        String jsonString = gson.toJson(user);
        System.out.println("JSON dat naar CouchDB gestuurd wordt:");
        System.out.println(jsonString);  // Debug: toon volledige JSON

        // Zet JSON-string om naar JsonObject
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // Probeer op te slaan in CouchDB
        String result = saveDocument(jsonObject);
        System.out.println("Resultaat van saveDocument:");
        System.out.println(result);  // Debug: wat komt er terug van CouchDB

        return result;
    }


    // READ → ophalen op document-ID (_id)
    public User getUserByDocId(String doc_Id) {
        return gson.fromJson(getDocumentById(doc_Id), User.class);
    }

    // READ → zoeken op gebruikersnaam (userName)
    public User getUserByUsername(String username) {
        for (JsonObject jsonObject : getAllDocuments()) {
            User result = gson.fromJson(jsonObject, User.class);
            if (result.getUserName().equals(username)) {
                return result;
            }
        }
        return null; // Find nothing, get nothing
    }

    // READ-helper → haalt _id en _rev op van een specifieke user
    // Nodig voor update en delete acties.
    public String[] getIdAndRevOfUser(User user) {
        String[] idAndRev = new String[2];
        for (JsonObject jsonObject : getAllDocuments()) {
            if (jsonObject.get("userName").getAsString().equals(user.getUserName())) {
                idAndRev[0] = jsonObject.get("_id").getAsString();
                idAndRev[1] = jsonObject.get("_rev").getAsString();
            }
        }
        return idAndRev;
    }

    // DELETE → gebruiker verwijderen uit CouchDB
    public void deleteUser(User user) {
        String[] idAndRev = getIdAndRevOfUser(user);
        deleteDocument(idAndRev[0], idAndRev[1]);
    }

    // UPDATE → bestaande user overschrijven (met id en rev)
    public String updateUser(User user) {
        String[] idAndRev = getIdAndRevOfUser(user);
        String jsonString = gson.toJson(user);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        jsonObject.addProperty("_id", idAndRev[0]);
        jsonObject.addProperty("_rev", idAndRev[1]);
        return updateDocument(jsonObject);
    }
}

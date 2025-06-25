package database.mysql;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.CouchDBAccess;
import model.Course;
import model.Group;
import model.Quiz;

public class GroupCouchDBDAO extends AbstractCouchDBDAO{

    private Gson gson;

    public GroupCouchDBDAO(CouchDBAccess couchDBAccess) {
        super(couchDBAccess);
        gson = new Gson();
    }

    public String saveSingleGroup(Group group) {
        // Group object omzetten naar JsonObject, zodat het opgeslagen kan worden mbv save()
        String jsonString = gson.toJson(group);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        return saveDocument(jsonObject);
    }

    public Group getGroupByDocId(String doc_Id) {
        // Als je het id van een couchDB document weet, kun je daarmee een Group ophalen
        return gson.fromJson(getDocumentById(doc_Id), Group.class);
    }

    public Group getGroup(String groupName, String courseName){
        // Haal alle documenten op, in de vorm van JsonObjecten; zet om naar Group en test op groupName
        Group resultaat;
        for(JsonObject jsonObject:getAllDocuments()){
            resultaat = gson.fromJson(jsonObject, Group.class);
            if(resultaat.getGroupName().equals(groupName) && resultaat.getCourse().getCourseName().equals(courseName)){
                return resultaat;
            }
        }
        return null;
    }

    public String[] getIdAndRevOfGroup (Group group) {
        // Vind het _id en _rev van document behorend bij een Verbruiker met postcode-huisnummer combinatie
        String[] idAndRev = new String[2];
        for (JsonObject jsonObject : getAllDocuments()) {

            boolean hasGroup = jsonObject.has("groupName") && jsonObject.has("courseName");

            boolean groupMatches = jsonObject.get("groupName").getAsString().equals(group.getGroupName());
            boolean courseMatches = jsonObject.get("courseName").getAsString().equals(group.getCourse().getCourseName());

            if (hasGroup && groupMatches && courseMatches)
            {
                idAndRev[0] = jsonObject.get("_id").getAsString();
                idAndRev[1] = jsonObject.get("_rev").getAsString();
            }
        }
        return idAndRev;
    }

    public void deleteGroup(Group group){
        // Op basis van _id en _rev kun je een document in CouchDB verwijderen
        String[] idAndRev = getIdAndRevOfGroup(group);
        deleteDocument(idAndRev[0],idAndRev[1]);
    }

    public String updateGroup(Group group) {
        // Haal _id en _rev van document op behorend bij group
        // Zet group om in JsonObject
        String[] idAndRev = getIdAndRevOfGroup(group);
        String jsonString = gson.toJson(group);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        // Voeg _id en _rev toe aan JsonObject, nodig voor de update van een document.
        jsonObject.addProperty("_id", idAndRev[0]);
        jsonObject.addProperty("_rev", idAndRev[1]);
        return updateDocument(jsonObject);
    }


}

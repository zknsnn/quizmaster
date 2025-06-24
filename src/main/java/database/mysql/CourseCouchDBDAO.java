package database.couchdb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.CouchDBAccess;
import database.mysql.AbstractCouchDBDAO;
import model.Course;

public class CourseCouchDBDAO extends AbstractCouchDBDAO {

    private Gson gson;

    public CourseCouchDBDAO(CouchDBAccess couchDBAccess){
        super(couchDBAccess);
        gson = new Gson();
    }

    public String saveSingleCourse(Course course) {
        // Course object omzetten naar JsonObject, zodat het opgeslagen kan worden mbv save()
        String jsonString = gson.toJson(course);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        return saveDocument(jsonObject);
    }

    public Course getCourseByDocId(String doc_Id) {
        // Als je het id van een couchDB document weet, kun je daarmee een Course ophalen
        return gson.fromJson(getDocumentById(doc_Id), Course.class);
    }

    public Course getCourse(String courseName){
        // Haal alle documenten op, in de vorm van JsonObjecten; zet om naar Course en test op courseName
        Course resultaat;
        for(JsonObject jsonObject:getAllDocuments()){
            resultaat = gson.fromJson(jsonObject, Course.class);
            if(resultaat.getCourseName().equals(courseName)){
                return resultaat;
            }
        }
        return null;
    }

    public String[] getIdAndRevOfCourse(Course course){
        // Vind het _id en _rev van document behorend bij een Course met id-name combinatie
        String[] idAndRev = new String[2];
        for (JsonObject jsonObject : getAllDocuments()){
            if (jsonObject.get("courseName").getAsString().equals(course.getCourseName())){
                idAndRev[0] = jsonObject.get("_id").getAsString();
                idAndRev[1] = jsonObject.get("_rev").getAsString();
            }
        }
        return idAndRev;
    }

    public void deleteCourse(Course course){
        // Op basis van _id en _rev kun je een document in CouchDB verwijderen
        String[] idAndRev = getIdAndRevOfCourse(course);
        deleteDocument(idAndRev[0],idAndRev[1]);
    }

    public String updateCourse(Course course) {
        // Haal _id en _rev van document op behorend bij course
        // Zet course om in JsonObject
        String[] idAndRev = getIdAndRevOfCourse(course);
        String jsonString = gson.toJson(course);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();

        // Voeg _id en _rev toe aan JsonObject, nodig voor de update van een document.
        jsonObject.addProperty("_id", idAndRev[0]);
        jsonObject.addProperty("_rev", idAndRev[1]);
        return updateDocument(jsonObject);
    }





}


package database.couchDB;

import com.google.gson.JsonObject;
import org.lightcouch.Response;

import java.util.List;

public abstract class AbstractCouchDBDAO {

    private CouchDBAccess couchDBAccess;

    public AbstractCouchDBDAO(CouchDBAccess couchDBAccess) {
        this.couchDBAccess = couchDBAccess;
    }

    public String saveDocument(JsonObject document) {
        Response response = couchDBAccess.getClient().save(document);
        return response.getId();
    }

    public JsonObject getDocumentById(String idDocument) {
        return couchDBAccess.getClient().find(JsonObject.class, idDocument);
    }

    public List<JsonObject> getAllDocuments() {
        return couchDBAccess.getClient().view("_all_docs").includeDocs(true).query(JsonObject.class);
    }

    public String updateDocument(JsonObject document) {
        Response response = couchDBAccess.getClient().update(document);
        return response.getId();
    }

    public void deleteDocument(String idDocument, String revDocument) {
        couchDBAccess.getClient().remove(idDocument, revDocument);
    }
}

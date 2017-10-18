package Search.googleutil.drive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import Search.global.record.Log;
import Search.global.record.Settings;

/**
 * Used to manage files on google drive on a basic level, only uploading and downloading atm
 * @author Allen
 *
 */
public class DriveManager {
	/** Application name. */
    private static final String APPLICATION_NAME =
        "SearchBot";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(".credentials");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at .credentials
     */
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE);
    
    private static Drive service;
    
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            DriveManager.class.getResourceAsStream("/Search/Library/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public static void setup(){
    	try {
			service = getDriveService();
			//Download from Drive the files for initial load as they aren't included as a part of the build files
			DriveManager.download(new DriveFile(Settings.dataSource,DataEnum.SearchData.id));
			DriveManager.download(new DriveFile(Log.LogSource,DataEnum.LogSource.id));
		} catch (IOException e) {
			Log.logShortError(e, 6);
		}
    }
    /**
     * Updates the file in the drive
     * @param file
     */
    public static void update(DriveFile file){
    	File fileMetaData=new File();
    	fileMetaData.setName(file.getName());
    	java.io.File filePath = new java.io.File(file.getFilePath());
    	FileContent mediaContent = new FileContent("text/plain", filePath);
        try {
			service.files().update(file.getId(), fileMetaData, mediaContent)
			        .setFields("id")
			        .execute();
		} catch (IOException e) {
			Log.logShortError(e, 5);
		}
        Log.log("DRIVE", "updated "+file.getName());
    }
    public static void download(DriveFile file){
    	try {
    	String id=file.getId();
    	OutputStream outputStream = new FileOutputStream(file.getFilePath());
			service.files().get(id)
			        .executeMediaAndDownloadTo(outputStream);
			outputStream.close();
//			InputStream in=service.files().get(id).executeAsInputStream();//alternative download method
//			//Files.copy(in, new java.io.File(file.getFilePath()).toPath(),StandardCopyOption.REPLACE_EXISTING );
//			in.close();
		} catch (IOException e) {
			Log.logShortError(e, 5);
		}
    	Log.log("DRIVE", "downloaded "+file.getName());
    }
}

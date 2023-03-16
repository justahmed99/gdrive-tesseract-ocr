//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.*;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.GenericUrl;
//import com.google.api.client.http.HttpResponse;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.gax.core.FixedCredentialsProvider;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
////import com.google.protobuf.ByteString;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.vision.v1.*;
//import com.google.protobuf.ByteString;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class GoogleDriveCloudVisionOCR {
//
//    private static final String APPLICATION_NAME = "Google Drive Cloud Vision OCR";
//
//    /**
//     * Global instance of the JSON factory.
//     */
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    /**
//     * Directory to store authorization tokens for this application.
//     */
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private static final List<String> SCOPES =
//            Arrays.asList(DriveScopes.DRIVE, DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
//
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        // Load client secrets.
//        InputStream in = GoogleDriveCloudVisionOCR.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
////                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//        //returns an authorized Credential object.
//        return credential;
//    }
//
//
//    private static final String VISION_CREDENTIALS_FILE_PATH = "/vision_credentials.json";
//
//    private static GoogleCredentials getVisionCredentials() throws IOException {
//        InputStream inputStream = GoogleDriveCloudVisionOCR.class.getResourceAsStream("/vision_credentials.json");
//        if (inputStream == null) {
//            throw new FileNotFoundException("Resource not found: vision_credentials.json");
//        }
//        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream)
//                .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-vision"));
//        return credentials;
////        return GoogleCredentials.fromStream(
////                        GoogleDriveCloudVisionOCR.class.getResourceAsStream(VISION_CREDENTIALS_FILE_PATH))
////                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-vision"));
//    }
//
//    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//
//        // Step 2: Retrieve the image file from Google Drive
//        String fileId = "10JM8qmXhdmjUtVWtlAxSsIMFE8iTnoHP";
//        Drive drive = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//        File file = drive.files().get(fileId).execute();
//        String fileName = file.getName();
//        String fileUrl = "https://drive.google.com/uc?id=" + file.getId() + "&export=download";
//        HttpResponse response = drive.getRequestFactory().buildGetRequest(new GenericUrl(fileUrl)).execute();
//        InputStream inputStream = response.getContent();
//
//        // Step 3: Perform OCR on the image using the Google Cloud Vision API
//        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
//                .setCredentialsProvider(FixedCredentialsProvider.create(getVisionCredentials()))
//                .build();
//        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
//            byte[] fileBytes = inputStream.readAllBytes();
//            ByteString imgBytes = ByteString.copyFrom(fileBytes);
//            Image image = Image.newBuilder().setContent(imgBytes).build();
//            Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
//            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
//            List<AnnotateImageRequest> requests = new ArrayList<>();
//            requests.add(request);
//            AnnotateImageResponse responses = client.batchAnnotateImages(requests).getResponses(0);
//            String extractedText = responses.getFullTextAnnotation().getText();
//            System.out.println("Extracted text: " + extractedText);
//        }
//    }
//}
//

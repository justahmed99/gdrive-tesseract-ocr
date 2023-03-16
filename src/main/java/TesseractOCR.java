//import java.io.*;
//import java.security.GeneralSecurityException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//
//import net.sourceforge.tess4j.ITesseract;
//import net.sourceforge.tess4j.Tesseract;
//import net.sourceforge.tess4j.TesseractException;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//
//import javax.imageio.ImageIO;
//
//public class TesseractOCR {
//
//    /**
//     * Application name.
//     */
//    private static final String APPLICATION_NAME = "Google Drive OCR Example";
//    /**
//     * Global instance of the JSON factory.
//     */
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private static final List<String> SCOPES =
//            Collections.singletonList(DriveScopes.DRIVE);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
//
//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        // Load client secrets.
//        InputStream in = GoogleDriveUpload.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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
//    public static void main(String[] args) throws IOException, GeneralSecurityException, TesseractException {
//
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Drive service = new Drive.Builder(
//                new NetHttpTransport(), JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//
//        // Step 2: Load the image file from Google Drive
//        String fileId = "18FJOr8TQX4lNHJQP63qxb76h5dcrhlq8";
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        service.files().get(fileId)
//                .executeMediaAndDownloadTo(outputStream);
//
//        byte[] imageBytes = outputStream.toByteArray();
//
//        // Step 3: Perform OCR on the image using Tesseract
//        ITesseract tesseract = new Tesseract();
//        tesseract.setDatapath("E:\\Program Files\\Tesseract-OCR\\tessdata");
//        tesseract.setLanguage("chi_sim");
//
//        String extractedText = tesseract.doOCR(ImageIO.read(new ByteArrayInputStream(imageBytes)));
//
//        String chinesePattern = "[\\u4E00-\\u9FA5]+"; // regex pattern to match Chinese characters
//        String alphabetPattern = "[a-zA-Z]+"; // regex pattern to match alphabets
//        Pattern pattern = Pattern.compile("(" + chinesePattern + ")|(" + alphabetPattern + ")");
//        Matcher matcher = pattern.matcher(extractedText);
//        List<String> chinese = new ArrayList<>();
//        List<String> alphabet = new ArrayList<>();
//        while (matcher.find()) {
//            if (matcher.group(1) != null) {
//                chinese.add(matcher.group(1));
//            } else if (matcher.group(2) != null) {
//                alphabet.add(matcher.group(2));
//            }
//        }
//
//        System.out.println("Chinese: " + chinese);
//        System.out.println("Alphabet: " + alphabet);
//
//        XWPFDocument chineseDoc = new XWPFDocument();
//        XWPFDocument englishDoc = new XWPFDocument();
//        writeWordsToDoc(chineseDoc, chinese);
//        writeWordsToDoc(englishDoc, alphabet);
//        FileOutputStream chineseFos = new FileOutputStream("chinese_output.docx");
//        FileOutputStream englishFos = new FileOutputStream("english_output.docx");
//        chineseDoc.write(chineseFos);
//        englishDoc.write(englishFos);
//        chineseFos.close();
//        englishFos.close();
//        chineseDoc.close();
//        englishDoc.close();
//
//        // Step 4: Output the extracted text
//        System.out.println(extractedText);
//
//
//    }
//
//    private static void writeWordsToDoc(XWPFDocument doc, List<String> words) {
//        XWPFParagraph para = doc.createParagraph();
//        for (String word : words) {
//            XWPFRun run = para.createRun();
//            run.setText(word + " ");
//            if (word.contains("o") || word.contains("O")) {
//                run.setColor("0000FF");
//            }
//        }
//    }
//
//}
package operations;

import com.google.api.services.drive.Drive;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import utils.GoogleDriveCredentials;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OCROperations {

    private final Drive service;
    public OCROperations() throws GeneralSecurityException, IOException {
        GoogleDriveCredentials credentials = new GoogleDriveCredentials();
        this.service = credentials.buildService();
    }

    public void ocrAndDowloadDocxOperation(String fileId) throws IOException, TesseractException {
        // Step 1: Load the image file from Google Drive
//        String fileId = "18FJOr8TQX4lNHJQP63qxb76h5dcrhlq8";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(fileId)
                .executeMediaAndDownloadTo(outputStream);

        byte[] imageBytes = outputStream.toByteArray();

        // Step 2: Perform OCR on the image using Tesseract
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("E:\\Program Files\\Tesseract-OCR\\tessdata");
        tesseract.setLanguage("chi_sim");

        String extractedText = tesseract.doOCR(ImageIO.read(new ByteArrayInputStream(imageBytes)));

        String chinesePattern = "[\\u4E00-\\u9FA5]+"; // regex pattern to match Chinese characters
        String alphabetPattern = "[a-zA-Z]+"; // regex pattern to match alphabets
        Pattern pattern = Pattern.compile("(" + chinesePattern + ")|(" + alphabetPattern + ")");
        Matcher matcher = pattern.matcher(extractedText);
        List<String> chinese = new ArrayList<>();
        List<String> alphabet = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                chinese.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                alphabet.add(matcher.group(2));
            }
        }

        System.out.println("Chinese: " + chinese);
        System.out.println("Alphabet: " + alphabet);

        // Step 3: Output the extracted text to docx files
        XWPFDocument chineseDoc = new XWPFDocument();
        XWPFDocument alphabetDoc = new XWPFDocument();
        writeWordsToDoc(chineseDoc, chinese);
        writeWordsToDoc(alphabetDoc, alphabet);
        FileOutputStream chineseFos = new FileOutputStream("chinese_output.docx");
        FileOutputStream alphabetFos = new FileOutputStream("alphabet_output.docx");
        chineseDoc.write(chineseFos);
        alphabetDoc.write(alphabetFos);
        chineseFos.close();
        alphabetFos.close();
        chineseDoc.close();
        alphabetDoc.close();
    }

    private void writeWordsToDoc(XWPFDocument doc, List<String> words) {
        XWPFParagraph para = doc.createParagraph();
        for (String word : words) {
            XWPFRun run = para.createRun();
            run.setText(word + " ");
            if (word.contains("o") || word.contains("O")) {
                run.setColor("0000FF");
            }
        }
    }
}

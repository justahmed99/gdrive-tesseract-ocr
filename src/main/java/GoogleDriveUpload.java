import net.sourceforge.tess4j.TesseractException;
import operations.OCROperations;
import operations.UploadFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleDriveUpload {
    public static void main(String[] args) throws IOException, GeneralSecurityException, TesseractException {

        if (args.length < 1) {
            System.out.println("Invalid arguments!");
        } else {
            if (args[0].equals("upload")) {
                UploadFile uploadFile = new UploadFile();
                if (args.length < 2) {
                    System.out.println("Image ID argument is needed!");
                    System.out.println("Command example : java -jar upload-1.0.jar upload [image_path]");
                    return;
                }
                uploadFile.uploadFileToGoogleDrive(args[1]);
            } else if (args[0].equals("extract")) {
                if (args.length < 2) {
                    System.out.println("Image ID argument is needed!");
                    System.out.println("Command example : java -jar upload-1.0.jar extract [image_id]");
                    return;
                }
                OCROperations ocrOperations = new OCROperations();
                ocrOperations.ocrAndDowloadDocxOperation(args[1]);
            }
        }
    }
}
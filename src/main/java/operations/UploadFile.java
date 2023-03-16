package operations;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.apache.commons.io.FilenameUtils;
import utils.GoogleDriveCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class UploadFile {
    private final Drive service;
    public UploadFile() throws GeneralSecurityException, IOException {
        GoogleDriveCredentials credentials = new GoogleDriveCredentials();
        this.service = credentials.buildService();
    }

    public void uploadFileToGoogleDrive(String[] filePathsString) throws IOException {
        List <String> fileIds = new ArrayList<>();
        for (String filePathString : filePathsString) {
            String extension = FilenameUtils.getExtension(filePathString);
            if (!extension.equals("jpg") && !extension.equals("png") && !extension.equals("jpeg")) {
                System.out.println("File extension not supported! Only support .png and .jpg");
                return;
            }

            File fileMetadata = new File();
            fileMetadata.setName("photo." + extension);

            // File's content.
            java.io.File filePath = new java.io.File(filePathString);
            // Specify media type and file-path for file.
            FileContent mediaContent = new FileContent(extension.equals("png") ? "image/png" : "image/jpg", filePath);

            try {
                File file = service.files().create(fileMetadata, mediaContent)
                        .setFields("id, webContentLink")
                        .execute();

                Permission permission = new Permission()
                        .setType("anyone")
                        .setRole("reader")
                        .setAllowFileDiscovery(false);
                service.permissions().create(file.getId(), permission).execute();
                fileIds.add(file.getId());
//                System.out.println("File ID: " + file.getId());
//                System.out.println("Web content link: " + file.getWebContentLink());

                File updatedFile = service.files().get(file.getId()).setFields("id, webContentLink").execute();

            } catch (GoogleJsonResponseException e) {
                System.err.println("Unable to upload file: " + e.getDetails());
                throw e;
            }
        }
        System.out.println("File IDs : " + String.join(" ", fileIds));
    }
}

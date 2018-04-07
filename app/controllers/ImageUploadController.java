package controllers;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import forms.FormData;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.data.Form;
import play.data.FormFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ImageUploadController extends Controller {

    private final FormFactory formFactory;

    private ImageAnnotatorClient vision;

    @Inject
    public ImageUploadController(FormFactory formFactory){
        this.formFactory = formFactory;
        try{
            FileInputStream credentialsStream = new FileInputStream("google_credentials/arquisoft-201810-28ca71000d63.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
            FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
            ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(credentialsProvider)
                    .build();
            this.vision = ImageAnnotatorClient.create(settings);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result UploadImage() {
        Form<FormData> form = formFactory.form(FormData.class);
        return ok(views.html.image_upload.render(form));
    }

    public Result AnalizeImage() throws IOException {
        MultipartFormData<File> body = request().body().asMultipartFormData();
        FilePart<File> filePart = body.getFile("image");
        File file = filePart.getFile();
        List<EntityAnnotation> results = connectGoogleVision(file);

        return ok(views.html.google_api_results.render(results));
    }

    private List<EntityAnnotation> connectGoogleVision(File file) throws IOException{
        // Source https://github.com/GoogleCloudPlatform/java-docs-samples/blob/master/vision/cloud-client/src/main/java/com/example/vision/QuickstartSample.java
        // Docs https://cloud.google.com/vision/docs/libraries#client-libraries-install-java

        // Reads the image file into memory
        byte[] data = Files.readAllBytes(file.toPath());
        ByteString imgBytes = ByteString.copyFrom(data);

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);

        // Performs label detection on the image file
        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        for (AnnotateImageResponse res : responses) {
            if (!res.hasError()) {
                return res.getLabelAnnotationsList();
            }
        }
        return Collections.emptyList();
    }
}

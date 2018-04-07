package controllers;

import forms.FormData;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.data.Form;
import play.data.FormFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ImageUploadController extends Controller {

    private final FormFactory formFactory;

    @Inject
    public ImageUploadController(FormFactory formFactory){
        this.formFactory = formFactory;
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
        String filename = filePart.getFilename();
        File file = filePart.getFile();

        return ok(views.html.google_api_results.render(filename));
    }
}

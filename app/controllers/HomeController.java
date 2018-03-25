package controllers;

import classes.AttachmentWrapper;
import play.mvc.*;
import services.MailService;
import services.PdfGeneratorService;

import javax.inject.Inject;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    @Inject
    private MailService mailService;

    @Inject
    private PdfGeneratorService pdfGeneratorService;

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    public Result test() {
        String[] recipients = {"jc.tangarife1927@gmail.com"};

        byte[] pdfBytes = pdfGeneratorService.generatePdf(views.html.pdfs.test_pdf.render());

        AttachmentWrapper[] attachments = {new AttachmentWrapper("quote.pdf", pdfBytes, "application/pdf")};

        mailService.sendEmail("Cotización Construmarket", recipients, views.html.emails.test_email.render().body(), attachments);
        return ok(views.html.result.render());
    }

}

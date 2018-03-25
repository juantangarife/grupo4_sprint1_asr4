package services;
import javax.inject.Inject;
import it.innove.play.pdf.PdfGenerator;
import play.twirl.api.Html;

public class PdfGeneratorService {
    @Inject
    private PdfGenerator pdfGenerator;

    public byte[] generatePdf(Html template){
        return pdfGenerator.toBytes(template, null);
    }
}

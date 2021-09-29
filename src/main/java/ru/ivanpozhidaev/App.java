package ru.ivanpozhidaev;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;

/**
 * PDFPortfolio can make a portfolio in PDF file, that will contain a wide range of file types
 * created in different applications.
 * For example, it can include any MS Office documents and images.
 *
 */
public class App 
{
    public static final String DEST = "target/Result.pdf";

    public static final String DATA = "src/Recipes.txt";
    public static final String HELLO = "src/Livret.pdf";
    public static final String IMG = "src/Map.jpeg";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new App().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("PDF Portfolio"));

        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.TILE);
        pdfDoc.getCatalog().setCollection(collection);

        addFileAttachment(pdfDoc, DATA, "Recipes.txt");
        addFileAttachment(pdfDoc, HELLO, "Levrit.pdf");
        addFileAttachment(pdfDoc, IMG, "Map.jpeg");

        doc.close();
    }

    // This method adds file attachment to the pdf document
    private void addFileAttachment(PdfDocument document, String attachmentPath, String fileName) throws IOException {
        String embeddedFileName = fileName;
        String embeddedFileDescription = fileName;
        String fileAttachmentKey = fileName;

        // the 5th argument is the mime-type of the embedded file;
        // the 6th argument is the AFRelationship key value.
        //PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(document, attachmentPath, embeddedFileDescription, embeddedFileName, null, null);
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(document, attachmentPath, embeddedFileDescription,
                embeddedFileName, null, null, true);
        document.addFileAttachment(fileAttachmentKey, fileSpec);
    }
}

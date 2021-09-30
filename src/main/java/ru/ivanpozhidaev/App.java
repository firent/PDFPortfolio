package ru.ivanpozhidaev;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PDFPortfolio can make a portfolio in PDF file, that will contain a wide range of file types
 * created in different applications.
 * For example, it can include any MS Office documents and images.
 *
 */
public class App 
{
    public static final String DEST = "target/Result.pdf";
    public static final String DIR = "src/";

    public static final ArrayList<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter filenames from SRC directory for portfolio:");

        while(true){
            String filename = scanner.next();
            if (filename.equalsIgnoreCase("exit"))
                break;
            list.add(DIR + filename);
        }

        new App().manipulatePdf();
    }

    protected void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(App.DEST));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("PDF Portfolio"));

        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.TILE);
        pdfDoc.getCatalog().setCollection(collection);

        for (String s:list){
            addFileAttachment(pdfDoc, s, s);
        }

        doc.close();
    }

    // This method adds file attachment to the pdf document
    private void addFileAttachment(PdfDocument document, String attachmentPath, String fileName) throws IOException {
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(document, attachmentPath, fileName,
                fileName, null, null, true);
        document.addFileAttachment(fileName, fileSpec);
    }
}

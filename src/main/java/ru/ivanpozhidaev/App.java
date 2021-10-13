package ru.ivanpozhidaev;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.collection.PdfCollection;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PDFPortfolio can make a portfolio in PDF file, that will contain a wide range of file types
 * created in different applications.
 * For example, it can include any MS Office documents and images.
 *
 */
public class App extends JFrame
{
    private static final long serialVersionUID = 1L;

    public static final String DEST = "target/Result.pdf";

    private static String SRC;

    private static List<Path> list = new ArrayList<>();

    private final JButton btnOpenDir;

    private final JFileChooser jFileChooser;

    public App() {
        super("PDFPortfolio");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Button for choosing a file directory
        btnOpenDir = new JButton("Открыть директорию файлов для портфолио");
        // Create JFileChooser instance
        jFileChooser = new JFileChooser();
        // Adding listeners to buttons
        addFileChooserListeners();
        // Layout of buttons in the interface
        JPanel contents = new JPanel();
        contents.add(btnOpenDir);
        setContentPane(contents);
        // Window
        setSize(360, 110);
        setDefaultLookAndFeelDecorated(true);
        setLocation(600,300);
        setVisible(true);
    }

    private void addFileChooserListeners()
    {
        btnOpenDir.addActionListener(e -> {
            jFileChooser.setDialogTitle("Выбор директории");
            // Directories only
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = jFileChooser.showOpenDialog(App.this);
            // If directory selected
            if (result == JFileChooser.APPROVE_OPTION ) {
                try {
                    SRC = jFileChooser.getSelectedFile().getAbsolutePath();
                    // Adding paths of the all files in selected directory
                    list = Files.walk(Paths.get(SRC))
                            .filter(Files::isRegularFile)
                            .collect(Collectors.toList());
                    // Creating new PDFPortfolio
                    new App().manipulatePdf();
                } catch (Exception ioException) {
                    ioException.printStackTrace();
                }
                // Message about selected directory
                JOptionPane.showMessageDialog(App.this,
                        jFileChooser.getSelectedFile());
            }
        });
    }

    public static void main(String[] args) {
        new App();
    }

    protected void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(App.DEST));
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("PDF Portfolio"));

        PdfCollection collection = new PdfCollection();
        collection.setView(PdfCollection.TILE);
        pdfDoc.getCatalog().setCollection(collection);

        for (Path s:list){
            String str = s.toString();
            addFileAttachment(pdfDoc, str, str);
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

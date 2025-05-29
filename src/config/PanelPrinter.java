package config;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PanelPrinter {

   public void generatePdf(Node node, String filename) throws IOException {
    // Take a snapshot of the node
    WritableImage image = node.snapshot(new SnapshotParameters(), null);

    // Convert the image to a BufferedImage
    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

    // Save the BufferedImage to a temporary file
    File tempfile = File.createTempFile("temp", ".png");
    ImageIO.write(bufferedImage, "png", tempfile);

    // Check if the file exists
    if (tempfile.exists()) {
        try {
            // Check if the file is a valid image
            BufferedImage tempBufferedImage = ImageIO.read(tempfile);
            if (tempBufferedImage != null) {
                // Create a PDF document
                PDDocument document = new PDDocument();

                // Create a new page
                PDPage page = new PDPage();
                document.addPage(page);

                // Add the image to the content stream
                PDImageXObject pdImage = PDImageXObject.createFromFile(tempfile.getAbsolutePath(), document);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.drawImage(pdImage, 0, 0);
                contentStream.close();

                // Save the PDF document
                document.save(new File(filename + ".pdf"));
                document.close();
            } else {
                System.out.println("Invalid image");
            }
        } catch (IOException e) {
            System.out.println("Error reading image");
        } finally {
            // Delete the temporary file
            tempfile.delete();
        }
    } else {
        System.out.println("File not found");
    }
}
}
package fr.schaller.uploadingfiles.print;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrintQuality;
import org.icepdf.ri.common.PrintHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

@Service
public class SimpleIPrintService implements IPrintService {

    PrintService[] services;

    public SimpleIPrintService()
    {
        init();
    }

    @Override
    public void init() {
        /**
         * Find Available printers
         */
        services = PrintServiceLookup.lookupPrintServices(
                        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
    }

    @Override
    public void print( String filePath) {
        if (services == null || services.length == 0)
        {
            init();
        }
        if (services.length >0)
        {
            Document pdf = new Document();

            try {
                pdf.setFile(filePath);
            } catch (PDFException e) {
                throw new PrintException("error to set file "+filePath,e);
            } catch (PDFSecurityException e) {
                throw new PrintException("error security on file "+filePath,e);
            } catch (IOException e) {
                throw new PrintException("error IO on file "+filePath,e);
            }

// create a new print helper with a specified paper size and print
// quality
        PrintHelper printHelper = new PrintHelper(null, pdf.getPageTree(),
                0f, MediaSizeName.ISO_A4, PrintQuality.DRAFT);
// try and print pages 1 - 10, 1 copy, scale to fit paper.

        printHelper.setupPrintService(services[0], 0, 0, 1, true);
// print the document
            try {
                printHelper.print();
            } catch (javax.print.PrintException e) {
                throw new PrintException("Error on print file "+filePath,e);
            }
        }
    }

    @Override
    public Stream<PrintService> listAvailablePrintService() {
        if (services == null)
        {
            init();
        }
        return Arrays.stream(services);
    }
}

package fr.schaller.uploadingfiles.print;



import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.cups4j.CupsClient;
import org.cups4j.CupsPrinter;
import org.cups4j.PrintJob;
import org.cups4j.PrintRequestResult;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
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
    public void print( String filePath,URL url, OptionImpression... options) {
        CupsClient cupsClient = null;
        try {
            cupsClient = new CupsClient();
        } catch (Exception e) {
            throw new PrintException("Erreur lors de la récupération du client cups.",e);
        }
        CupsPrinter cupsPrinter = null;
        try {
            cupsPrinter = cupsClient.getPrinter(url);
        } catch (Exception e) {
            throw new PrintException("Erreur lors de la récupération de l'imprimante par defaut.",e);
        }
        if (cupsPrinter == null )
        {
            throw new PrintException("Pas d'imprimante CUPS trouvée pour "+url.toString());
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new PrintException("Le fichier a imprimer n est pas trouve.",e);
        }
        PrintJob.Builder jobBuilder = new PrintJob.Builder(inputStream);
        if (options != null)
        {
            for (OptionImpression option : options) {
                jobBuilder = option.configureImpression(jobBuilder);
            }
        }
        PrintJob printJob = jobBuilder.pageFormat("iso-a4").build();
        try {
            PrintRequestResult printRequestResult = cupsPrinter.print(printJob);
        } catch (Exception e) {
           throw new PrintException("Erreur lors de l'impression.",e);
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

    @Override
    public Stream<CupsPrinter> listAvailableCupsService() {
        CupsClient cupsClient = null;
        try {
            cupsClient = new CupsClient();
        } catch (Exception e) {
            throw new PrintException("Erreur sur creation de CupsClient.",e);
        }
        try {
            return cupsClient.getPrinters().stream();
        } catch (Exception e) {
            throw new PrintException("Erreur sur la liste des imprimantes.",e);
        }
    }
}

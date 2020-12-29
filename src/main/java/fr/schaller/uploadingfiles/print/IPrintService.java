package fr.schaller.uploadingfiles.print;

import org.cups4j.CupsPrinter;

import javax.print.PrintService;
import java.net.URL;
import java.util.stream.Stream;

public interface IPrintService {
    void init();
    void print(String filePath, URL url,OptionImpression... options ) ;
    Stream<PrintService> listAvailablePrintService();
    Stream<CupsPrinter> listAvailableCupsService();
}

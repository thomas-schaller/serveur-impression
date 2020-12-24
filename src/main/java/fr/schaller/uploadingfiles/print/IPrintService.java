package fr.schaller.uploadingfiles.print;

import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;

import javax.print.PrintException;
import javax.print.PrintService;
import java.io.IOException;
import java.util.stream.Stream;

public interface IPrintService {
    void init();
    void print(String filePath) ;
    Stream<PrintService> listAvailablePrintService();
}

package fr.schaller.uploadingfiles.print;


import org.cups4j.PrintJob;

public interface OptionImpression {

    PrintJob.Builder configureImpression(PrintJob.Builder b);
}

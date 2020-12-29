package fr.schaller.uploadingfiles.print;

import org.cups4j.PrintJob;
public class Orientation implements OptionImpression{
    boolean estPortrait=true;

    public Orientation(boolean estPortrait) {
        this.estPortrait = estPortrait;
    }

    public boolean isPortrait() {
        return estPortrait;
    }

    public void setPortrait(boolean estPortrait) {
        this.estPortrait = estPortrait;
    }

    public PrintJob.Builder configureImpression(PrintJob.Builder b)
    {
        return b.portrait(estPortrait);
    }
}

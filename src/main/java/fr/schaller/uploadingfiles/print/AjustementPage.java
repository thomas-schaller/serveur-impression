package fr.schaller.uploadingfiles.print;

import org.cups4j.PrintJob;

import java.util.HashMap;
import java.util.Map;

public class AjustementPage implements OptionImpression {
    boolean doitAjuster;

    public AjustementPage(boolean doitAjuster) {
        this.doitAjuster = doitAjuster;
    }

    public boolean isAjuster() {
        return doitAjuster;
    }

    public void setAjuster(boolean doitAjuster) {
        this.doitAjuster = doitAjuster;
    }

    @Override
    public PrintJob.Builder configureImpression(PrintJob.Builder b) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("job-attributes", "print-quality:enum:3#fit-to-page:boolean:"+doitAjuster+"#sheet-collate:keyword:collated");
        return b.attributes(attributes);
    }
}

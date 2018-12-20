package it.drivek.quote.load

import grails.converters.JSON
import it.drivek.quote.Quote
import org.codehaus.groovy.grails.web.json.JSONElement


public class QuoteImporter {
    private final File sourceFile;

    public QuoteImporter(String sourcePath) {
        this.sourceFile = new File(sourcePath)
    }

    public List<Quote> getAll()  {
        String content = sourceFile.getText('UTF-8');
        List<Quote> allQuotes = new ArrayList<Quote>();

        JSONElement jsonContent = JSON.parse(content);
        jsonContent.each {message ->
            allQuotes.add(new Quote(content: message));
        }

        return allQuotes;
    }
}
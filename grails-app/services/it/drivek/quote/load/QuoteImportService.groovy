package it.drivek.quote.load

import it.drivek.quote.Quote

class QuoteImportService {
    def grailsApplication

    void initialize() throws Exception {
        log.info("QuoteImportService: Starting import...");
        def sourcePath = grailsApplication.config.messages.path
        QuoteImporter importer = new QuoteImporter(sourcePath)

        List<Quote> quoteList = importer.getAll()
        quoteList.each() { quote ->
            quote.save(failOnError:true)
        }
        log.info("QuoteImportService: Completed! ${quoteList.size()} quotes imported.");
    }
}

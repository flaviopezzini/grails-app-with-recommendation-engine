package it.drivek.quote

import grails.converters.JSON

class QuoteController {
    def quoteService

    def random() {
        def quote = quoteService.getRandomQuote()
        render quote as JSON
    }
}

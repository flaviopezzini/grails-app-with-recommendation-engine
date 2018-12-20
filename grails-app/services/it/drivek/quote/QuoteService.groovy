package it.drivek.quote

class QuoteService {
    Random random = new Random()

    Quote getRandomQuote() {
        def quote = Quote.findById(random.nextInt(Quote.count()) + 1)
        return quote;
    }
}

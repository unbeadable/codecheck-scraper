fun main(args: Array<String>) {
    val scraper = CodeCheckScraper()
    scraper.writeProductWithIngredientsByCategory("https://www.codecheck.info/kosmetik_koerperpflege/koerperpflege/koerperpeelings.kat", "koerperpeelings.json")
}


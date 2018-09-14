class DataCleaner {
    private fun Product.hasInvalidEan(): Boolean {
        return this.ean.orEmpty().matches(Regex("^(\\d{13})\$"))
    }

    fun clean(unprocessedData: List<Product>): List<Product> {
        return unprocessedData
                .filter { it -> it.ingredients.orEmpty().toList().isNotEmpty() }
                .filter { it -> it.hasInvalidEan() }
    }
}

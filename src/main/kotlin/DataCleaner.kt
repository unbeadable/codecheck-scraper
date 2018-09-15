class DataCleaner {
    companion object {
        const val THIRTEEN_DIGITS = "^(\\d{13})\$"
    }

    private fun Product.hasValidEan(): Boolean {
        return this.ean.orEmpty().matches(Regex(THIRTEEN_DIGITS))
    }

    fun clean(unprocessedData: List<Product>): List<Product> {
        return unprocessedData
                .asSequence()
                .filter { it.ingredients.orEmpty().toList().isNotEmpty() }
                .filter { it.hasValidEan() }
                .toList()
    }
}
class DataCleaner {
    private fun Product.hasInvalidEan(): Boolean {
        return this.ean.orEmpty().matches(Regex("^(\\d{13})\$"))
    }

    fun filterEmptyIngredients(unprocessedData: List<Product>): List<Product> {
        return unprocessedData.filter { it -> it.ingredients.orEmpty().toList().isNotEmpty() }
    }

    fun filterInvalidEans(unprocessedData: List<Product>): List<Product> {
        return unprocessedData.filter { it -> it.hasInvalidEan() }
    }
}

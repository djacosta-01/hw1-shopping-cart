import java.util.UUID

private val catalog = mapOf(
    // maps items to their prices (in USD)
    "apple box" to 5.0,
    "banana box" to 3.75,
    "orange box" to 10.0,
    "pear box" to 10.0,
    "grape box" to 3.45,
    "watermelon box" to 13.50,
    "pineapple box" to 15.00,
    "mango box" to 12.0,
    "kiwi box" to 7.0,
    "strawberry box" to 10.0,
    "shirts" to 15.0,
    "pants" to 20.0,
    "shoes" to 30.0,
    "socks" to 2.50,
    "nvidia rtx 3090" to 1500.0,
    "amd ryzen 9 5950X" to 800.0,
    "soccer ball" to 25.0,
    "basketball" to 30.0,
    "volleyball" to 20.0,
    "football" to 25.0,
    "baseball" to 10.0,
    "tennis ball" to 5.0,
    "lotion" to 7.0,
    "shampoo" to 5.0,
    "conditioner" to 5.0,
    "toothpaste" to 3.0,
    "toothbrush" to 2.0,
    "floss" to 1.0,
    "soap" to 2.0,
    "towel" to 10.0,
)

/**
 * Represents an item in the catalog
 *
 * @property name the name of the item
 * @throws IllegalArgumentException if the quantity is less than or equal to 0
 * @throws IllegalArgumentException if the name is not in the catalog
 * @throws IllegalArgumentException if the name is longer than 100 characters
 * @throws IllegalArgumentException if the name is empty
 */
private data class Item(val name: String) {
    init {
        require(name.isNotEmpty()) { "Name must be at least 1 character" }
        require(name.length <= 100) { "Name must be less than 100 characters" }
        require(catalog.containsKey(name)) { "Item not found in catalog" }
    }
}

/**
 * Shopping cart class that holds the shopping cart ID, customer ID, and
 * items in the cart. Each field is immutable and can only be accessed
 * through getters. The shopping cart ID is generated when the cart is
 * created and the items are stored in a map with the item as the key
 * and the quantity as the value. The customer ID is validated when the
 * cart is created. The class also has methods to update the cart with
 * new items, remove items, and calculate the total cost of the items.
 *
 * @property _customerID the customer ID associated with the cart
 * @property _shoppingCartID the ID of the shopping cart in UUID4 format
 * @property _items a map of items in the cart with the item as the key and the quantity as the value
 */
class ShoppingCart(private val _customerID: String) {
    private val _shoppingCartID = UUID.randomUUID()
    private val _items = mutableMapOf<Item, Int>()

    init {
        require(_customerID.matches(Regex("^\\p{L}{3}\\d{5}\\p{L}{2}-[AQ]$"))) { "Customer id is invalid" }
    }

    val items
        get() = convertItemMapToList()
    val id: String
        get() = _shoppingCartID.toString()
    val customerID: String
        get() = _customerID

    /**
     * Adds an item to the shopping cart or updates its quantity if it already exists.
     *
     * @param itemName the name of the item to add
     * @param quantity the quantity of the item to add
     */
    fun update(itemName: String, quantity: Int) {
        validateQuantity(quantity)

        val cleanedItemName = itemName.trim().lowercase()
        val item = Item(cleanedItemName)
        _items[item] = quantity
    }

    /**
     * Removes an item from the shopping cart
     *
     * @param itemName the name of the item to remove
     * @param quantity the quantity of the item to remove
     * @throws IllegalArgumentException if the item is not in the cart
     */
    fun remove(itemName: String, quantity: Int) {
        validateQuantity(quantity)

        val cleanedItemName = itemName.trim().lowercase()
        val item = Item(cleanedItemName)

        if (!_items.containsKey(item)) {
            throw IllegalArgumentException("Item not found in cart")
        }

        val currentQuantity = _items[item]!!
        if (currentQuantity - quantity <= 0) {
            _items.remove(item)
        } else {
            _items[item] = currentQuantity - quantity
        }
    }

    /**
     * Calculates the total cost of the items in the shopping cart
     *
     * @return the total cost of the items in the shopping cart as a string
     */
    fun totalCost(): String {
        if (_items.isEmpty()) {
            return "0.0"
        }

        var totalCost = 0.0
        for ((item, quantity) in _items) {
            // item was validated in update method, so it must be in the catalog
            totalCost += catalog[item.name]!! * quantity
        }

        return totalCost.toString()
    }

    // ----------------- private helper methods -----------------
    /**
     * Converts the map of items to a read-only list of strings
     * @return a list of strings representing the items and quantities in the shopping cart
     */
    private fun convertItemMapToList(): List<String> {
        return _items.map { (item, quantity) -> "${item.name}: $quantity" }
    }

    /**
     * Validates the quantity of an item
     *
     * @param quantity the quantity to validate
     * @throws IllegalArgumentException if the quantity is less than or equal to 0
     * @throws IllegalArgumentException if the quantity is greater than 100
     */
    private fun validateQuantity(quantity: Int) {
        require(quantity > 0) { "Quantity must be a positive number" }
        require(quantity <= 100) { "Quantity must be less than or equal to 100" }
    }
}
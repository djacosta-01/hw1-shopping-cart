import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.string.shouldMatch


class ShoppingCartTest : DescribeSpec({
    describe("An shopping cart with invalid customer ids") {
        it("should throw an exception when the customer id does not follow the specified pattern") {
            shouldThrow<IllegalArgumentException> { ShoppingCart("ABC12345DE") }
        }
        it("should throw an exception when the customer id does not follow the specified pattern 2") {
            shouldThrow<IllegalArgumentException> { ShoppingCart("ABC12345DE-B") }
        }

    }

    describe("A shopping cart with valid customer ids from different languages") {
        it("should not throw an exception when the customer id contains uppercase letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("ABC12345DE-A") }
        }
        it("should not throw an exception when the customer id contains lowercase letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("abc12345de-Q") }
        }
        it("should not throw an exception if customer id contains mixed case letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("AbC12345dE-A") }
        }
        it("should not throw an exception if customer id contains letters from romantic languages") {
            // mix of french, spanish and portuguese letters
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("éñó12345êã-A") }
        }
        it("should not throw an exception if customer id contains letters from slavic languages") {
            // mix of russian, polish and czech letters
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("вгд12345жз-A") }
        }
        it("should not throw an exception if customer id contains letters from germanic languages") {
            // mix of german, dutch and swedish letters
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("äöü12345ßß-A") }
        }
        it("should not throw an exception if customer id contains turkish letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("çşü12345ğı-A") }
        }
        it("should not throw an exception if customer id contains greek letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("αβγ54321δε-A") }
        }
        it("should not throw an exception if customer id contains arabic letters") {
            // switched direction to read from left-to-right just for testing purposes
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("أبج12345دس-A") }
        }
        it("should not throw an exception if customer id contains hebrew letters") {
            // switched direction to read from left-to-right just for testing purposes
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("אבג45678דה-Q") }
        }
        it("should not throw an exception if customer id contains japanese letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("あいう12345えお-A") }
        }
        it("should not throw an exception if customer id contains korean letters") {
            shouldNotThrow<IllegalArgumentException> { ShoppingCart("가나다12345라마-Q") }
        }
        // was gonna add more tests for other languages, but I think this is enough
    }

    describe("A shopping cart's fields") {
        val cart = ShoppingCart("ABC12345DE-A")
        it("should return the id of the shopping cart in UUID4 format") {
            val uiid4Regex = Regex("[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}")
            cart.id shouldMatch uiid4Regex
        }
        it("should return the customer id of the shopping cart following the specified pattern") {
            cart.customerID shouldBe "ABC12345DE-A"
        }
        it("should return an empty list of items") {
            cart.items.size shouldBe 0
        }
        it("should not allow the user to change the id") {
            // user should not be able to change cart id since strings
            // are immutable and all string methods return a new string
            val idBeforeAttemptedChange = cart.id
            cart.id.uppercase()
            cart.id shouldBe idBeforeAttemptedChange
        }
        it("should not allow the user to change the customer id") {
            // same as test immediately above
            val customerIDBeforeAttemptedChange = cart.customerID
            cart.customerID.uppercase()
            cart.customerID shouldBe customerIDBeforeAttemptedChange
        }

        it("should not allow the user to change the items directly even if they try to cast it to a mutable list") {
            (cart.items as MutableList<String>).add("apple box")
            cart.items shouldBe emptyList()
        }
        /*
         this test doesn't even compile since items is returned
         as a read-only copy of the list. modification methods
         aren't exposed (kinda cool tbh)
        */
        // it("should not allow the user to use modification methods on the items list") {
        // shouldThrow<UnsupportedOperationException> {
        //     cart.items.clear()
        // }
        // shouldThrow<UnsupportedOperationException> {
        //     cart.items.add("Hacking your cart MUAHAHAHA")
        // }
        // }

    }

    describe("Basic operations on a shopping cart") {
        it("should throw an error when trying to add a negative quantity") {
            val cart = ShoppingCart("ABC12345DE-A")
            shouldThrow<IllegalArgumentException> { cart.update("mango box", -1) }
        }
        it("should throw an error if the quantity is greater than 100") {
            val cart = ShoppingCart("ABC12345DE-A")
            shouldThrow<IllegalArgumentException> { cart.update("mango box", 101) }
        }
        it("should throw an error if the item name is not in the catalog") {
            val cart = ShoppingCart("ABC12345DE-A")
            shouldThrow<IllegalArgumentException> { cart.update("hard drugs", 100) }
        }
        it("should allow the user to add items to the cart") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("kiwi box", 4)
            cart.update("strawberry box", 3)
            cart.update("mango box", 1)
            cart.items shouldBe listOf("kiwi box: 4", "strawberry box: 3", "mango box: 1")
        }
        it("should allow the user to update an item in their cart") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("mango box", 1)
            cart.update("mango box", 2)
            cart.items.size shouldBe 1
            cart.items shouldBe listOf("mango box: 2")
        }
        it("should allow users to add/update a bunch of items and see them in their cart") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("KIWI BOX", 4)
            cart.update("     strawberry box       ", 3)
            cart.update("mango BOX", 1)
            cart.update("kIwI bOx", 2)
            cart.update("strawberry box", 1)
            cart.update("mango box", 3)
            cart.update("SOCCER ball", 100)
            cart.update("     NVIDIA RTX 3090 ", 20)
            cart.update("NVIDIA RTX 3090        ", 5)
            cart.update("strawberry box                ", 20)

            cart.items.size shouldBe 5
            cart.items shouldBe listOf(
                "kiwi box: 2",
                "strawberry box: 20",
                "mango box: 3",
                "soccer ball: 100",
                "nvidia rtx 3090: 5",
            )
        }
        it("should still not allow users to change the items field directly") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("kiwi box", 4)
            cart.update("strawberry box", 3)
            cart.update("mango box", 1)
            (cart.items as MutableList<String>).add("apple box")
            cart.items shouldBe listOf("kiwi box: 4", "strawberry box: 3", "mango box: 1")
        }
        it("should not allow the user to remove an item that is not in their cart") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("kiwi box", 4)
            cart.update("strawberry box", 3)
            cart.update("mango box", 1)
            shouldThrow<IllegalArgumentException> { cart.remove("apple box", 1) }
        }
        it("should not allow the user to remove an item with a negative quantity") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("kiwi box", 4)
            cart.update("strawberry box", 3)
            cart.update("mango box", 1)
            shouldThrow<IllegalArgumentException> { cart.remove("kiwi box", -1) }
        }
        it("should allow the user to remove an item from their cart") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("kiwi box", 4)
            cart.update("strawberry box", 3)
            cart.update("mango box", 1)
            cart.items.size shouldBe 3

            cart.remove("kiwi box", 1)
            cart.items.size shouldBe 3
            cart.items shouldBe listOf("kiwi box: 3", "strawberry box: 3", "mango box: 1")

            cart.remove("kiwi box", 3)
            cart.items.size shouldBe 2
            cart.items shouldBe listOf("strawberry box: 3", "mango box: 1")
        }
        it("should allow the users to get the total cost of their cart") {
            val cart = ShoppingCart("ABC12345DE-A")
            cart.update("kiwi box", 4)
            cart.update("strawberry box", 3)
            cart.update("mango box", 1)
            cart.totalCost() shouldBe "70.0"

            cart.remove("kiwi box", 1)
            cart.totalCost() shouldBe "63.0"
        }
        it("should return 0.0 if the cart is empty") {
            val emptyCart = ShoppingCart("ABC12345DE-A")
            emptyCart.totalCost() shouldBe "0.0"
        }
    }
})
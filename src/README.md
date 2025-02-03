# CMSI 662 HW1: Shopping Cart

Write a shopping cart library (module) in a language of your choice—but one that I know, so check with me first to see if your language choice is okay—together with good documentation and solid unit tests. Don’t use the same language that we used for the class code-along. Although we are only beginning our formal study of secure programming constructs, your task in this assignment is to implement the following requirements as best you can. The readings for this assignment above should help get you in the right mindset. Later lectures and code-alongs will help you fine tune you work so secure programming becomes second nature to you. <br>
<br>

Your module must:

<ul class="compressed">
<li>Define a new type (or class) for shopping carts, in which a cart must (1) have its own id, (2) hold the id of the customer that the cart belongs to, and (3) the items in the cart along with their quantities</li>
<li>Provide a way for users to query the id, customer id, and items from the cart.</li>
<li>Ensure that no one can change the cart indirectly by modifying the values (ids, items, etc.) you provided them (you may need to research “immutability” and “defensive copying”).</li>
<li>Provide a way for users to add, update, and remove items.</li>
<li>Ensure the id and customer id of the cart can <em>never</em> be changed.</li>
<li>Provide a method to get the total cost of the items in the cart.</li>
<li>Store shopping cart ids as uuid4s. (Find out how to generate them in the language of your choice.)</li>
<li>Require customer ids to have the following form: 3 letters, 5 numbers, 2 letters, a dash, and finally an A or a Q. (Always check for valid customer ids. Hint: Most languages have a way to check such things with regular expressions, you should use them if easy to do so.)</li>
<li>Ensure negative quantities for any item in the cart can never appear.</li>
<li>Check for upper bounds on every quantity as well (you should know why we need bounds like this, pretty much everywhere).</li>
<li>Make sure items added to the cart are in some kind of catalog, and are not just made up (hack in) by an adversary.</li>
<li>Ensure item names are length-bounded and the characters in the names restricted. (In fact, you should always make sure any string content in your project is bounded. That is an important security principle.)</li>
<li>Employ immutability everywhere that it makes to. (Immutability should always be the default.)</li>
<li>Maintain integrity at all times. (Do not allow bad data to creep in, ever. If something is wrong, throw an exception or return result objects, according to your language’s culture.)</li>
</ul>

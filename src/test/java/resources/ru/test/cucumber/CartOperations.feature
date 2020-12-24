
Feature: Cart operations

  Scenario: AddingAndRemoveItemFromCart
    When user added the 3 product with size 'Small' to the cart
    Then quantity increased by 3 in the cart
    When the user went to the cart and deleted all products from it
    Then the cart is empty
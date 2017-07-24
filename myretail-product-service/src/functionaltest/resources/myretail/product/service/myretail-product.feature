Feature: Products API GET and PUT

Scenario Outline: client makes GET call to products API
When client makes a call to GET /products with <productId>
Then client has <productId> "<name>" "<value>" "<currencyCode>" in the response

Examples: 
 | productId | name                                               | value | currencyCode |
 | 13860428  | The Big Lebowski (Blu-ray)                         | 12.45  | USD          |
 | 13860429  | SpongeBob SquarePants: SpongeBob's Frozen Face-off | 9.45   | USD          |
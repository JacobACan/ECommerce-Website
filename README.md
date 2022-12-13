# E-Store:  STLucky 

An online E-store system built in Java 17 and Angular 
  
## Team

- Christopher Nokes
- Samson Zhang
- Jacob Canedy
- Zachary Brafman
- Diogo Almeida

## Prerequisites

- Java 17 (Make sure to have correct JAVA_HOME setup in your environment)
- Maven (Run API)
- Angular (Run UI)
- Packages
  - npm install sweetalert2
  - npm install  --save-dev @types/three --force or npm -i three


## How to run it

1. Clone the repository and go to the estore-api directory.
2. Install mvn and Angular on your device
2. Execute `mvn clean compile exec:java`
3. Navigate to estore-ui/stlucky directory.
4. Execute `ng serve -o` This will open the website UI 

## Known bugs and disclaimers
* UI Bugs
  * Full detailed checkout info is not displayed.
    * Different attributes of products such as section colors not displayed
    * Shipping time not displayed
    * Cannot edit products in checkout
  * User not notified if section added or removed from a product (though this would be an unlikely occurance)
  * User can leave multiple reviews on the same product.
  * Users will be signed out when refreshing the page.
  * Users still able to search for products that are out of stock.
  * Search does not update all view of products 
  * Search contains products that are out of stock with no message indication that they are out of stock
  * Not all fields of part customization needed to checkout product
  * Able to rate products that you haven't bought
  * Product Page not working on opera

* API Bugs
  * Limit of 32 products able to be stored in the .json file 

## How to test it

The Maven build script provides hooks for run unit tests and generate code coverage
reports in HTML.

To run tests on all tiers together do this:

1. Execute `mvn clean test jacoco:report`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/index.html`

To run tests on a single tier do this:

1. Execute `mvn clean test-compile surefire:test@tier jacoco:report@tier` where `tier` is one of `controller`, `model`, `persistence`
2. Open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/{controller, model, persistence}/index.html`

To run tests on all the tiers in isolation do this:

1. Execute `mvn exec:exec@tests-and-coverage`
2. To view the Controller tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
3. To view the Model tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`
4. To view the Persistence tier tests open in your browser the file at `PROJECT_API_HOME/target/site/jacoco/model/index.html`

*(Consider using `mvn clean verify` to attest you have reached the target threshold for coverage)
  
  
## How to generate the Design documentation PDF

1. Access the `PROJECT_DOCS_HOME/` directory
2. Execute `mvn exec:exec@docs`
3. The generated PDF will be in `PROJECT_DOCS_HOME/` directory


## How to setup/run/test program 
1. Tester, first obtain the Acceptance Test plan
2. Tester, obtain a machine able to run STL-Estore
3. Execute mvn clean exec:compile in the >/estore-api directory.
4. Execute ng serve -o in the >/estore-ui/stlucky 
5. Examine Acceptance Test Plan spreadsheet and fill out whether tests pass or fail.

## License

MIT License

See LICENSE for details.

## Restaurant Search Service
API for searching local restaurants loaded in from a local .csv file

### Requirements
Java 11

### Feature
REST endpoint top 5 restaurants matching criteria:
```
    - Distance
    - Name
    - Rating
    - Price
    - Cuisine
```

### Test
 1. Navigate to project directory
 2. Run ./gradlew clean build

### Start up app

 1. Navigate to project directory
 2. Run ./gradlew bootrun
 3. Navigate to http://localhost:8080/swagger-ui.html - to view endpoint
 Note: Running the app on port 8080, To change add server.port to application.properties

### How to Search:
   1. Run endpoint http://localhost:8080/restaurants - will return empty list with no query params
   2. Add criteria as query params ex. http://localhost:8080/restaurants?distance=26 or http://localhost:8080/restaurants?distance=26&name=Mcd' for multiple params
   ```
   Possible Query Params:
    - name
    - customer_rating
    - distance
    - price
    - cuisine
   ```
    
If multiple matches are possible then the top five sorted by distance, dating, and then price will be returned.
 

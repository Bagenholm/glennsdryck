## Glenns Dryck

#Usage
Glenns Dryck can scrape five different stores for their alcohol-based products and gives you the cheapest alternatives for getting roughly 1 promille (or 0.5 promille, over roughly 3 hours).

Can't be run on JDK 11 or later without also bundling it with JavaFX due to a scraper using JavaFX webkit - either run it with JDK 1.8 or bundle it with JavaFX.

MySQL and RabbitMQ running on default ports. Use root/root for MySQL and guest/guest for RabbitMQ, or change username and password in application.properties.

#Endpoints

| Name                                                                | Method | Description                                                                             | Authorization |
|---------------------------------------------------------------------|--------|-----------------------------------------------------------------------------------------|---------------|
| /user/                                                              | POST   | Registers a user. Requires user's weight, fuelConsumption, gender as 'f' or 'm'         | All           |
| /login                                                              | POST   | Logs in with user. Requires username and password.                                      | All           |
| /user/{username}                                                    | GET    | Finds user.                                                                             | User, Admin   |
| /user/registeradmin/{username}                                      | POST   | Registers user as admin. Trust your users to decide if they're worthy.                  | User, Admin   |
| /scrape/all                                                         | GET   | Scrapes all stores. Overrides schedule and when store was last scraped.                 | Admin         |
| /scrape/systembolaget                                               | GET    | Scrapes Systembolaget, or gets from DB if scraped recently.                             | User, Admin   |
| /scrape/calle                                                       | GET    | Scrapes Calle, or gets from DB if scraped recently.                                     | User, Admin   |
| /scrape/fleggaard                                                   | GET    | Scrapes Fleggaard, or gets from DB if scraped recently.                                 | User, Admin   |
| /scrape/driveinbottleshop                                           | GET    | Scrapes Driveinbottleshop, or gets from DB if scraped recently.                         | User, Admin   |
| /scrape/stenaline                                                   | GET    | Scrapes Stenaline, or gets from DB if scraped recently.                                 | User, Admin   |
| /drinks                                                             | GET    | Fetches all previously scraped drinks.                                                  | All           |
| /drinks/volume/{volume}                                             | GET    | Fetches all drinks of {volume} volume.                                                  | All           |
| /drinks/apk                                                         | GET    | Fetches all drinks, sorts by best alcohol price.                                        | All           |
| /drinks/apk/store/all/{limit}                                       | GET    | Fetches {limit} amount of drinks from each store.                                       | All           |
| /drinks/apk/store/all/{type}/{limit}                                | GET    | Fetches {limit} amount of drinks from each store by {type}.                             | All           |
| /drinks/apk/{type}/{limit}                                          | GET    | Fetches {limit} amount of drinks by {type}.                                             | All           |
| /drinks/name/{name}                                                 | GET    | Fetches drinks containing {name}.                                                       | All           |
| /trips                                                              | GET    | Fetches all trips, or populates with pre-made trips if empty.                           | User, Admin   |
| /trips/destination/{destination}                                    | GET    | Fetches all trips to {destination}  .                                                    | User, Admin   |
| /trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}             | GET    | Get trip by tripId.                                                                      | User, Admin   |
| /trips                                                              | POST   | Adds trip.                                                                              | Admin         |
| /trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}             | DELETE | Deletes trip matching the parameters.                                                   | Admin         |
| /trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}             | PUT    | Replaces trip matching the parameters with Json body.                                   | Admin         |
| /trips/{startpoint}/{endpoint}/{tripinfo}/{wayoftravel}             | PATCH  | Updates trip matching the parameters with Json body.                                    | Admin         |
| /calculate/drunkPrice/{username}/{drunks}/{fetchAmount}             | GET    | {fetchAmount} from stores, returns user's cheapest way to get {drunks}.                 | User, Admin   |
| /calculate/drunksForBudget/{username}/{budget}/{fetchAmount}        | GET    | {fetchAmount} from stores, returns user's amount of drunks based on {budget}.           | User, Admin   |
| /calculate/drunksForBudget/{username}/{budget}/{fetchAmount}/{type} | GET    | {fetchAmount} of {type} from stores, returns user's amount of drunks based on {budget}. | User, Admin   |
| /store                                                              | GET    | Fetches all stores.                                                                         | All           |          
| /store/city/{city}                                                  | GET    | Fetches all stores placed in {city}.                                                       | All           |
| /store/{storeName}                                                  | GET    | Get store by {storeName}.                                                                   | All           |
| /store                                                              | PATCH  | Create joins between all stores and trips.                                                 | All           |                                    

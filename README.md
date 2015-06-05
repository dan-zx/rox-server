RoX Server
==========

RoX is an Recommender Service for tourists that recommends Points of Interests (POIs) based on user's likes, friend's likes and POI ratings. The service also gives routes of POIs based on all users check-ins history.

## API Endpoints

#### POIs

* Returns a list of POIs near the current location

  ```
  https://rox.herokuapp.com/api/pois/search?params
  ```

  HTTP Method: **GET**

  | Parameters             | Type  | Example    | Decription                                                                              |
  |------------------------|-------|------------|-----------------------------------------------------------------------------------------|
  | location               | Query | 44.3,37.2  | (**required**) Latitude and longitude of the user's location                            |
  | radius                 | Query | 500        | (**required**) Limit results to POIs within this many meters of the specified location  |
  | category_foursquare_id | Query | asad13242l | (**required**) Category Foursquare id of the POIs wanted                                |

  Response example:

  ```json
  {
      "response":[
          {
              "name":"Starbucks",
              "location":{
                  "latitude":19.051916205742696,
                  "longitude":-98.21938276290894
              },
              "foursquareId":"4c2ec541ac0ab713fa9b1b1e",
              "foursquareRating":7.4,
              "categories":[
                  {
                      "name":"Coffee Shop",
                      "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
                      "foursquareId":"4bf58dd8d48988d1e0931735"
                  }
              ]
          },
          {
              "name":"Profética - Casa de la Lectura",
              "location":{
                  "latitude":19.04288527071933,
                  "longitude":-98.20153534412384
              },
              "foursquareId":"4baace32f964a520a7873ae3",
              "foursquareRating":8.9,
              "categories":[
                  {
                      "name":"Coffee Shop",
                      "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
                      "foursquareId":"4bf58dd8d48988d1e0931735"
                  }
              ]
          }
      ]
  }
  ```

* Returns a list of recommended POIs near the current location

  ```
  https://rox.herokuapp.com/api/pois/recommend?params
  ```

  HTTP Method: **GET**

  | Parameters   | Type  | Example    | Decription                                                                                                      |
  |--------------|-------|------------|-----------------------------------------------------------------------------------------------------------------|
  | access_token | Query | asad13242l | (**optional**) User athentication token. If no value is specified, recommendations will be based in POI ratings |
  | location     | Query | 44.3,37.2  | (**required**) Latitude and longitude of the user's location                                                    |
  | radius       | Query | 500        | (**required**) Limit results to POIs within this many meters of the specified location                          |

  Response example:

  ```json
  {
      "response":[
          {
              "name":"Starbucks",
              "location":{
                  "latitude":19.051916205742696,
                  "longitude":-98.21938276290894
              },
              "foursquareId":"4c2ec541ac0ab713fa9b1b1e",
              "foursquareRating":7.4,
              "categories":[
                  {
                      "name":"Coffee Shop",
                      "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
                      "foursquareId":"4bf58dd8d48988d1e0931735"
                  }
              ]
          },
          {
              "name":"Profética - Casa de la Lectura",
              "location":{
                  "latitude":19.04288527071933,
                  "longitude":-98.20153534412384
              },
              "foursquareId":"4baace32f964a520a7873ae3",
              "foursquareRating":8.9,
              "categories":[
                  {
                      "name":"Coffee Shop",
                      "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
                      "foursquareId":"4bf58dd8d48988d1e0931735"
                  }
              ]
          }
      ]
  }
  ```

* Returns a route of POIs constructed with the given seed.

  ```
  https://rox.herokuapp.com/api/pois/route?params
  ```

  HTTP Method: **GET**

  | Parameters        | Type  | Example    | Decription                       |
  |-------------------|-------|------------|----------------------------------|
  | poi_foursquare_id | Query | asad13242l | (**required**) POI Foursquare id |

  Response example:

  ```json
  {
      "response":[
          {
              "name":"Starbucks",
              "location":{
                  "latitude":19.051916205742696,
                  "longitude":-98.21938276290894
              },
              "foursquareId":"4c2ec541ac0ab713fa9b1b1e",
              "foursquareRating":7.4,
              "categories":[
                  {
                      "name":"Coffee Shop",
                      "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
                      "foursquareId":"4bf58dd8d48988d1e0931735"
                  }
              ]
          },
          {
              "name":"Profética - Casa de la Lectura",
              "location":{
                  "latitude":19.04288527071933,
                  "longitude":-98.20153534412384
              },
              "foursquareId":"4baace32f964a520a7873ae3",
              "foursquareRating":8.9,
              "categories":[
                  {
                      "name":"Coffee Shop",
                      "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
                      "foursquareId":"4bf58dd8d48988d1e0931735"
                  }
              ]
          }
      ]
  }
  ```

* Returns a list of POI categories that match the given name.

  ```
  https://rox.herokuapp.com/api/pois/categories/like/{partial_name}
  ```

  HTTP Method: **GET**

  | Parameters   | Type | Example | Decription                                  |
  |--------------|------|---------|---------------------------------------------|
  | partial_name | Path | staur   | (**required**)  Part of the a category name |

  Response example:

  ```json
  {
      "response":[
          {
              "name":"Argentinian Restaurant",
              "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
              "foursquareId":"4bf58dd8d48988d107941735"
          },
          {
              "name":"Mexican Restaurant",
              "iconUrl":"https://ss3.4sqi.net/img/categories_v2/food/default_88.png",
              "foursquareId":"4bf58dd8d48988d1c1941735"
          }
      ]
  }
  ```

#### Users

* Registers a user in the system using social a social network. The access token return is used as an authorization in other endpoints.

  ```
  https://rox.herokuapp.com/api/users/register/{social_network}?params
  ```

  HTTP Method: **GET** **

  | Parameters         | Type  | Example    | Decription                                                                                   |
  |--------------------|-------|------------|----------------------------------------------------------------------------------------------|
  | social_network     | Path  | foursquare | (**required**)  Social network to be used in the registration. Only _foursquare_ is suported |
  | authorization_code | Query | asad13242l | (**required**)  The authorization code given by the social network                           |

  Response example:

  ```json
  {
      "response":{
          "accessToken":"asad13242l"
      }
  }
  ```

* Returns the user details of the acting user.

  ```
  https://rox.herokuapp.com/api/users/self?params
  ```

  HTTP Method: **GET** **

  | Parameters   | Type  | Example    | Decription                              |
  |--------------|-------|------------|-----------------------------------------|
  | access_token | Query | asad13242l | (**required**) User athentication token |

  Response example:

  ```json
  {
      "response":{
          "name":"John",
          "lastName":"Doe",
          "photoUrl":"https://irs0.4sqi.net/img/user/923847.jpg",
          "foursquareId":"923847"
      }
  }
  ```

* Returns the list of friends of the acting user.

  ```
  https://rox.herokuapp.com/api/users/self/friends?params
  ```

  HTTP Method: **GET** **

  | Parameters   | Type  | Example    | Decription                              |
  |--------------|-------|------------|-----------------------------------------|
  | access_token | Query | asad13242l | (**required**) User athentication token |

  Response example:

  ```json
  {
      "response":[
          {
              "name":"Jane",
              "lastName":"Doe",
              "photoUrl":"https://irs0.4sqi.net/img/user/234237.jpg",
              "foursquareId":"234237"
          },
          {
              "name":"John",
              "lastName":"Doe",
              "photoUrl":"https://irs0.4sqi.net/img/user/543863.jpg",
              "foursquareId":"543863"
          }
      ]
  }
  ```

* Returns a list of likes of the given user.

  ```
  https://rox.herokuapp.com/api/users/{user_foursquare_id}/likes?params
  ```

  HTTP Method: **GET** **

  | Parameters         | Type  | Example    | Decription                                                                         |
  |--------------------|-------|------------|------------------------------------------------------------------------------------|
  | user_foursquare_id | Path  | asad13242l | (**required**) User Foursquare id. Pass _self_ to get the likes of the acting user |
  | access_token       | Query | asad13242l | (**required**) User athentication token                                            |

  Response example:

  ```json
  {
      "response":[
          {
              "name":"Jane",
              "lastName":"Doe",
              "photoUrl":"https://irs0.4sqi.net/img/user/234237.jpg",
              "foursquareId":"234237"
          },
          {
              "name":"John",
              "lastName":"Doe",
              "photoUrl":"https://irs0.4sqi.net/img/user/543863.jpg",
              "foursquareId":"543863"
          }
      ]
  }
  ```

* Adds a new like of the acting user.

  ```
  https://rox.herokuapp.com/api/users/update/addlike?params
  ```

  HTTP Method: **PUT**

  | Parameters             | Type  | Example    | Decription                                       |
  |------------------------|-------|------------|--------------------------------------------------|
  | access_token           | Query | asad13242l | (**required**) User athentication token          |
  | category_foursquare_id | Query | asad13242l | (**required**) The Category Foursquare id to add |

  Response example:

  ```json
  {
      "response":{
          "success":true
      }
  }
  ```

* Deletes a like of the acting user.

  ```
  https://rox.herokuapp.com/api/users/update/removelike?params
  ```

  HTTP Method: **DELETE**

  | Parameters             | Type  | Example    | Decription                                          |
  |------------------------|-------|------------|-----------------------------------------------------|
  | access_token           | Query | asad13242l | (**required**) User athentication token             |
  | category_foursquare_id | Query | asad13242l | (**required**) The Category Foursquare id to delete |

  Response example:

  ```json
  {
      "response":{
          "success":true
      }
  }
  ```

#### Errors

The server will respond in this format in case of any error occur:

```json
{
    "error":{
        "errorCode":"server.error.code",
        "errorMessage":"Server error message"
    }
}
```
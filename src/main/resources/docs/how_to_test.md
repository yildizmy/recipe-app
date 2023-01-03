

## How to test?

### API Endpoints

Swagger page for the endpoints: [Recipe Data Rest APIs](http://localhost:8080/swagger-ui/index.html)

> **Note** All URIs are relative to *http://localhost:8080/api/v1*



| Class             | Method                                                    | HTTP request             | Description                                                                                       |
|-------------------|-----------------------------------------------------------|--------------------------|---------------------------------------------------------------------------------------------------|
| *RecipeController* | [**findById**](http://localhost:8080/api/v1/recipes/{id}) | **GET** /recipes/{id}    | Retrieves recipe by the given id                                                                  |
| *RecipeController* | [**findAll**](http://localhost:8080/api/v1/recipes)       | **GET** /recipes         | Retrieves all recipes                                                                             |
| *RecipeController* | [**search**](http://localhost:8080/api/v1/recipes/search) | **GET** /recipes/search         | Search recipes with detailed filter parameters e.g. vegetarian type, ingredient, instruction text |
| *RecipeController* | [**create**](http://localhost:8080/api/v1/recipes)        | **POST** /recipes        | Creates an recipe                                                                                 |
| *RecipeController* | [**update**](http://localhost:8080/api/v1/recipes)        | **PUT** /recipes         | Updates given recipe                                                                              |
| *RecipeController* | [**deleteById**](http://localhost:8080/api/v1/recipes/{id}) | **DELETE** /recipes/{id} | Deletes given recipe                                                                              |



<br/>



| Class                | Method                                                                | HTTP request                       | Description                                                                                                                                                                                                            |
|----------------------|-----------------------------------------------------------------------|------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| *CategoryController* | [**findById**](http://localhost:8080/api/v1/categories/{id}) | **GET** /categories/{id}    | Retrieves category by the given id                                                                                                                                                                                     |
| *CategoryController* | [**findAll**](http://localhost:8080/api/v1//categories)    | **GET** /categories    | Retrieves all categories                                                                                                                                                                                               |
| *CategoryController* | [**create**](http://localhost:8080/api/v1/categories) | **POST** /categories   | Creates a category                                                                                                                                                                                                     |
| *CategoryController* | [**update**](http://localhost:8080/api/v1/categories) | **PUT** /categories    | Updates given category                                                                                                                                                                                                 |
| *CategoryController* | [**deleteById**](http://localhost:8080/api/v1/categories/{id}) | **DELETE** /categories/{id} | Deletes given category |


<br/>


| Class                | Method                                                          | HTTP request                 | Description                          |
|----------------------|-----------------------------------------------------------------|------------------------------|--------------------------------------|
| *IngredientController* | [**findById**](http://localhost:8080/api/v1/ingredients/{id})   | **GET** /ingredients/{id}    | Retrieves ingredient by the given id |
| *IngredientController* | [**findAll**](http://localhost:8080/api/v1//ingredients)        | **GET** /ingredients         | Retrieves all ingredients            |
| *IngredientController* | [**create**](http://localhost:8080/api/v1/ingredients)          | **POST** /ingredients        | Creates an ingredient                |
| *IngredientController* | [**update**](http://localhost:8080/api/v1/ingredients)          | **PUT** /ingredients         | Updates given ingredient             |
| *IngredientController* | [**deleteById**](http://localhost:8080/api/v1/ingredients/{id}) | **DELETE** /ingredients/{id} | Deletes given ingredient             |



<br/>


| Class            | Method                                                    | HTTP request           | Description                    |
|------------------|-----------------------------------------------------------|------------------------|--------------------------------|
| *UnitController* | [**findById**](http://localhost:8080/api/v1/units/{id})   | **GET** /units/{id}    | Retrieves unit by the given id |
| *UnitController* | [**findAll**](http://localhost:8080/api/v1//units)        | **GET** /units         | Retrieves all units            |
| *UnitController* | [**create**](http://localhost:8080/api/v1/units)          | **POST** /units        | Creates an unit                |
| *UnitController* | [**update**](http://localhost:8080/api/v1/units)          | **PUT** /units         | Updates given unit             |
| *UnitController* | [**deleteById**](http://localhost:8080/api/v1/units/{id}) | **DELETE** /units/{id} | Deletes given unit             |



<br/>


| Class            | Method                                                    | HTTP request                                                                    | Description                                         |
|------------------|-----------------------------------------------------------|---------------------------------------------------------------------------------|-----------------------------------------------------|
| *RecipeIngredientController* | [**addIngredientToRecipe**](http://localhost:8080/api/v1/recipeIngredients)          | **POST** /recipeIngredients                                                     | Adds ingredient to the given recipe                 |
| *RecipeIngredientController* | [**removeIngredientFromRecipe**](http://localhost:8080/api/v1/recipeIngredients/recipes/{recipeId}/ingredients/{ingredientId}) | **DELETE** /recipeIngredients/recipes<br>/{recipeId}/ingredients/{ingredientId} | Removes ingredient from the given recipe |



<br/>



### Postman Requests

The following request examples can be modified and used for testing the endpoints. [Postman Collection](postman/recipe_api.postman_collection.json) is also shared in the resources and can be used.


### recipe-controller

* findById

```
http://localhost:8080/api/v1/recipes/1
```

<br/>

* findAll

```
{
    "filters": [
        {
            "key": "title",
            "operator": "LIKE",
            "field_type": "STRING",
            "value": "Beef"
        }
    ],
    "sorts": [
        {
            "key": "id",
            "direction": "ASC"
        }
    ],
    "page": null,
    "size": null
}
```

> **Note** The filters used in `findAll` methods can be created based on the following parameters:<br/>
`key` : the name of the field<br/>
`operator` : logical for predicate of `Criteria API` likes `EQUAL`, `NOT_EQUAL`, `LIKE`, `IN`, and `BETWEEN`<br/>
`field_type` : the type of the field<br/>
`value` : search value


<br/>

* search

```
{
    "isVegetarian": null,
    "servings": null,
    "ingredientIn": "",
    "ingredientEx": "Lentils",
    "text": ""
}
```


> **Note** The filter parameters used in `search` method: <br/>
`isVegetarian` : Whether or not the dish is vegetarian<br/>
`servings` : The number of servings<br/>
`ingredientIn` : Specific ingredients (include)<br/>
`ingredientEx` : Specific ingredients (exclude)<br/>
`text` : Text search value within the instructions


<br/>


* create

```
{
  "id": 0,
  "title": "Beef Burger",
  "description": "Description for the recipe",
  "categoryId": 4,
  "servings": 6,
  "prepTime": 30,
  "cookTime": 40,
  "difficulty": "MODERATE",
  "healthLabel": "DEFAULT",
  "instructions": "Directions for the recipe",
  "recipeIngredients": [
    {
      "recipeId": 0,
      "ingredientId": 14, 
      "ingredientName": "",
      "amount": 2,
      "unitId": 7
    },
    {
      "recipeId": 0,
      "ingredientId": 8,
      "ingredientName": "",
      "amount": 500,
      "unitId": 1
    },
    {
      "recipeId": 0,
      "ingredientId": 0, 
      "ingredientName": "Chili Sauce",
      "amount": 2,
      "unitId": 3
    }
  ]
}
```

> **Note** For adding a new ingredient to a recipe, set `ingredientId: 0` and `ingredientName: "New Ingredient name"`. In this request, we add two pre-defined ingredients and one new ingredient.



<br/>


* update

```
{
    "id": 2,
    "title": "Chicken Salad Sandwich",
    "description": "Description for the recipe",
    "categoryId": 7,
    "servings": 7,
    "prepTime": 37,
    "cookTime": 47,
    "difficulty": "MODERATE",
    "healthLabel": "DEFAULT",
    "instructions": "Instructions for the recipe",
    "recipeIngredients": []
}
```

> **Note** For adding/removing ingredient for a current recipe, use RecipeIngredient endpoints

<br/>



* delete

```
http://localhost:8080/api/v1/recipes/1
```


<br/>


### ingredient-controller

* findById

```
http://localhost:8080/api/v1/ingredients/1
```

<br/>



* findAll

```
http://localhost:8080/api/v1/ingredients
```

<br/>



* create

```
{
    "name": "Broccoli"
}
```

<br/>


* update

```
{
    "id": 1,
    "name": "potaTo"
}
```

<br/>


* deleteById

```
http://localhost:8080/api/v1/ingredients/1
```

<br/>




### Unit & Integration Tests

Unit and Integration Tests are provided for all the services, controllers, etc.

<br/>
<br/>
# API SPEC

## User

### POST /users/signup
#### Request Body
| name | type | required | Description |
| --- | --- | --- | --- |
| userId | String | Y |  |
| password | String | Y |  |
| name | String | Y |  |
| phone | String | Y |  |
| birth | Datetime | Y | YYYY-MM-DD |
| gender | String | Y | MALE / FEMALE |

```json
{
    "userId": "user",
    "password": "passwOrd",
    "name": "user",
    "phone": "01011112222",
    "birth": "2000-01-01",
    "gender": "MALE"
}
```

#### Response Body
| name | type | Description |
| --- | --- | --- |
| userId | String |  |
| userName | String |  |

```json
{
    "userId": "user",
    "userName": "user"
}
```

### POST /users/signin
#### Request Body
| name | type | required | Description |
| --- | --- | --- | --- |
| userId | String | Y |  |
| password | String | Y |  |

```json
{
    "userId": "user",
    "password": "passwOrd"
}
```

#### Response
| name | type | Description |
| --- | --- | --- |
| userId | String |  |

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJoYWlsZXkiLCJ1dWlkIjoiZWZhMjYxZWUtZjljNS00MmJmLThjZWUtZWNiNTAwOWNiMDA2In0.K0gSn0goBgWUP7vc6fixDFal3RexyNKrOvxM4DhI2m4"
}
```

### DELETE /users/withdrawal
#### Request Header
| name` | type | required |
| --- | --- | --- |
| Authorization | String | Y |
 
#### Response
| name | type | Description |
| --- | --- | --- |
| id | String |  |
| userId | String |  |
| withdrawal | boolean |  |

```json
{
    "userId": "user",
    "userName": "user",
    "withdrawal": true
}
```

### PUT /users/cancelWithdrawal
#### Request Body
| name | type | required | Description |
| --- | --- | --- | --- |
| userId | String | Y |  |
| password | String | Y |  |

```json
{
    "userId": "user",
    "password": "passwOrd"
}
```

#### Response
| name | type | Description |
| --- | --- | --- |
| id | String |  |
| userId | String |  |
| withdrawal | boolean |  |

```json
{
    "userId": "user",
    "userName": "user",
    "withdrawal": false
}
```


## Product

### GET /products
#### Response Body
| name | type | Description |
| --- | --- | --- |
| products  | array |  |
|       id | number |  |
|       name | string |  |
|       price | number |  |
|       stock | number |  |

```json
{
    "products": [
        {
            "id": 1,
            "name": "americano",
            "price": 5000,
            "stock": 200
        },
        {
            "id": 2,
            "name": "cafe latte",
            "price": 6000,
            "stock": 100
        },
        {
            "id": 3,
            "name": "ice tea",
            "price": 3000,
            "stock": 50
        },
        {
            "id": 4,
            "name": "black tea",
            "price": 4000,
            "stock": 20
        },
        {
            "id": 5,
            "name": "toast",
            "price": 5000,
            "stock": 5
        }
    ]
}
```

### GET /products/{productId}
#### Request Parameter
| name | type | required | Location | Description |
| --- | --- | --- | --- | --- |
| productId | number | Y | Path |  |

#### Response Body
| name | type | Description |
| --- | --- | --- |
| id | number |  |
| name | string |  |
| price | number |  |
| stock | number |  |

```json
{
    "id": 1,
    "name": "americano",
    "price": 5000,
    "stock": 200
}
```

## Order

### GET /cart
#### Request Header
| name | type | required |
| --- | --- | --- |
| Authorization | String | Y |

#### Response Body
| name | type | Description |
| --- | --- | --- |
| cartResponses  | array |  |
|       id | number |  |
|       productId | number |  |
|       price | number |  |
|       quantity | number |  |
|       available | boolean | 재고 있는 경우 true / 재고 부족한 경우 false |

```json
{
    "cartResponses": [
        {
            "id": 1,
            "productId": 2,
            "price": 6000,
            "quantity": 10,
            "available": true
        }
    ]
}
```

### POST /cart
#### Request Header
| name | type | required |
| --- | --- | --- |
| Authorization | String | Y |

#### Request Body
| name | type | required | Description |
| --- | --- | --- | --- |
| productId | number | Y |  |
| quantity | number | Y |  |

```json
{
    "productId": 2,
    "quantity": 10
}
```

#### Response
| name | type | Description |
| --- | --- | --- |
| cartResponses  | array |  |
|       id | number |  |
|       productId | number |  |
|       price | number |  |
|       quantity | number |  |
|       available | boolean | 재고 있는 경우 true / 재고 부족한 경우 false |
    
```json
{
    "cartResponses": [
        {
            "id": 1,
            "productId": 2,
            "price": 6000,
            "quantity": 10,
            "available": true
        }
    ]
}
```

### DELETE /cart
#### Request Header

| name | type | required |
| --- | --- | --- |
| Authorization | String | Y |

#### Request Body
| name | type | required | Description |
| --- | --- | --- | --- |
| cartItemIds | [number] | Y |  |

```json
{
    "cartItemIds": [1]
} 
```

#### Response
| name | type | Description |
| --- | --- | --- |
| id | number |  |
| productId | number |  |
| price | number |  |
| quantity | number |  |
| available | boolean | 재고 있는 경우 true / 재고 부족한 경우 false |
    
```json
{
    "cartResponses": [
        {
            "id": 2,
            "productId": 1,
            "price": 5000,
            "quantity": 30,
            "available": true
        }
    ]
}
```

### POST /order
#### Request Header
| name | type | required |
| --- | --- | --- |
| Authorization | String | Y |

#### Request Body
| name | type | required | Description |
| --- | --- | --- | --- |
| cartItemIds | [number] | Y |  |

```json
{
    "cartItemIds": [1]
}
```

#### Response
| name | type | Description |
| --- | --- | --- |
| id | number |  |
| userId | string |  |
| orderStatus | String | ORDERED, PAID, CANCELED |
| totalAmount | number |  |
| orderDatetime | DateTime |  |

```json
{
    "id": "83a54d0c-59d3-4beb-8017-53773a116908",
    "userId": "09fb5939-825c-45d4-87cd-b8bac133f76d",
    "orderStatus": "PAID",
    "totalAmount": 150000,
    "orderDatetime": "2024-06-04T13:31:02.275799+09:00"
}
```

### GET /order/history
#### Request Header
| name | type | required |
| --- | --- | --- |
| Authorization | String | Y |

#### Response
| name | type | Description |
| --- | --- | --- |
| orderHistory | array |  |
|       id | number |  |
|       userId | string |  |
|       orderStatus | String | ORDERED, PAID, CANCELED |
|       totalAmount | number |  |
|       orderDatetime | DateTime |  |

```json
{
    "orderHistory": [
        {
            "id": "83a54d0c-59d3-4beb-8017-53773a116908",
            "userId": "09fb5939-825c-45d4-87cd-b8bac133f76d",
            "orderStatus": "PAID",
            "totalAmount": 150000,
            "orderDatetime": "2024-06-04T13:31:02.275799+09:00"
        }
    ]
}
```

### DELETE /order/{orderId}
#### Request Header
| name | type | required |
| --- | --- | --- |
| Authorization | String | Y |

#### Request Parameter
| name    | type | required | Location | Description |
|---------| --- | --- | --- | --- |
| orderId | number | Y | Path |  |

#### Response
| name | type | Description |
| --- | --- | --- |
| id | number |  |
| userId | string |  |
| orderStatus | String | ORDERED, PAID, CANCELED |
| totalAmount | number |  |
| orderDatetime | DateTime |  |

```json
{
    "id": "83a54d0c-59d3-4beb-8017-53773a116908",
    "userId": "09fb5939-825c-45d4-87cd-b8bac133f76d",
    "orderStatus": "PAID",
    "totalAmount": 150000,
    "orderDatetime": "2024-06-04T13:31:02.275799+09:00"
}
```
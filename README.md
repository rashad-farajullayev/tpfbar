# notes:
## URLs

* database adminer URL: http://localhost:8080/
* traefik GUI url: http://localhost.com:8081/
* openldap GUI url: http://localhost:8389
* ms-auth swagger url: http://localhost/api/auth/swagger-ui.html


## Microservices

* ms-auth: authentication microservice
* ms-transaction: transactions microservice
* ms-stock: stocks microservice

## How to run this solution

> ***Note**: To run this solution you need to have docker and docker-compose installed on your machine*
1. Pull the repository
2. `cd` into the directory of this solution
3. run the following command in your terminal:
    ```shell
    docker-compose up --build
    ```
4. After all the systems are up and running (watch logs) point your browser to Open LDAP Web GUI to create users to log in: http://localhost.com:8389
5. Click the `login` link in the menu and enter the following credentials to log in:

    | Login           |    Password   |
    | ----------------|:-------------:|
    |`cn=admin,dc=paydaybank,dc=com` | `admin_pass`|

6. In the left menu choose `Create New Entry`.
    * Create new `Generic: Posix Group`. For example `IT`
    * Create new `Generic: User Account` by using the same `Create New Entry` link and enter details for new user of the system: In the details `Common Name` is the username used to login. In the GID Number you can choose the previously created group `IT`. 
    * Click the `Create` button and view the details. DN written above will show your username once more. 
    i.e. ```cn=rashadf,dc=paydaybank,dc=com```
    * Here: `rashadf` is username. Click the `Submit` button to create this user
7. Now you can use this user to log into the system. Point your browser to http://127.0.0.1/api/auth/swagger-ui.html
8. Expand the `auth-controller` and use the `POST /login` method. Fill in newly created username (i.e. `rashadf`) and your password, click the execute button. The result shall give you response.
9. From the response grab access token (Note, Refresh token not implemented yet).
10. Open swagger of transactions microservice: http://127.0.0.1/api/transaction/swagger-ui.html

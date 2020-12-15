## PayDay Bank Solution
## URLs

* database adminer URL: http://localhost:8080/
* traefik GUI url: http://localhost:8081/
    - System: `PostgreSQL`
    - Server: `postgres`
    - Username: `postgres`
    - Password: `postgres`
    - Database: `transactions`
* openldap GUI url: http://localhost:8389
    - username: `cn=admin,dc=paydaybank,dc=com`
    - password: `admin_pass`
* ms-auth swagger url: http://localhost/api/auth/swagger-ui.html
* ms-transaction swagger url: http://localhost/api/transaction/swagger-ui.html
* ms-notification swagger url: http://localhost/api/notification/swagger-ui.html
* ms-notification web GUI: http://localhost/api/notification
    - username: `rfarajullayev`
    - password: `rashad123456`

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

6. OpenLdap now comes with predefined users and their credentials for testing purposes. Here is the list of predefined users:
    | Login           |    Password   |
    | ----------------|:-------------:|
    |`admin` | `admin_pass`|
    |`rfarajullayev` | `rashad123456`|
    |`mamed` | `mamed`|
    You can use these users to login in from the ms-auth swagger or in the ms-notification test page.

    If you want to create your own new LDAP users then follow the instrcutsions below. 
    In the left menu choose `Create New Entry`.
    * Create new `Generic: Posix Group`. For example `IT`
    * Create new `Generic: User Account` by using the same `Create New Entry` link and enter details for new user of the system: In the details `Common Name` is the username used to login. In the GID Number you can choose the previously created group `IT`. 
    * Click the `Create` button and view the details. DN written above will show your username once more. 
    i.e. ```cn=rashadf,dc=paydaybank,dc=com```
    * Here: `rashadf` is username. Click the `Submit` button to create this user
7. Now you can use this user to log into the system. Point your browser to http://127.0.0.1/api/auth/swagger-ui.html
8. Expand the `auth-controller` and use the `POST /login` method. Fill in newly created username (i.e. `rashadf`) and your password, click the execute button. The result shall give you response.
9. From the response grab access token (Note, Refresh token not implemented yet).
10. Open swagger of transactions microservice: http://127.0.0.1/api/transaction/swagger-ui.html
11. On the right of this page click `Authorize` button and in the popup type:
    ```
    Bearer <your-access-token-here>
    ```
    Paste your access token there in the Value box after typing the "Bearer " value. Click `Authorize` to close popup
12. Expand transaction controller: Fill in values and execute:
    * hookUrl: http://www.google.com
    * maximumPrice: `123.45`
    * stockId: `67`
    * userId: `rashadex`
    
    Click execute.
13. Watch logs. After 2 mins a mock-up message will pop-up about rest request to hookUrl. 
14. After the transaction job finishes in two minutes it will automatically POST notification to corresponding user about it. You can see logs about it in the ms-transaction and ms-notification logs. Also, to see visually, you should preliminary be logged into test GUI at http://localhost/api/notification with the corresponding user
15. To manually post notifications to users use swagger of ms notification http://localhost/api/notification/swagger-ui.html. Note, you must be preliminary logged in to the system from ms-auth. Copy the access token. In the ms-notification swagger click Authorize button. Write down "Bearer " and then paste that access token

---
## TODO
1. Beautify code style
2. Write tests to increase test-coverage
3. Write tests of services
4. Implement other user stories
    
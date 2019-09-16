Automation tests of primitive RESTfull API service.

_Do you have any comments or questions? Welcome!_

---
**Technologies:**

Building - **[Gradle](https://github.com/gradle/gradle)**.

Test running and assertions - **[TestNG](https://github.com/cbeust/testng)**.

Running and validation of HTTP requests - **[REST Assured](https://github.com/rest-assured/rest-assured)**.

SQL queries - **[JOOQ](https://github.com/jOOQ/jOOQ)**.

JSON objects - **[org.json](https://github.com/stleary/JSON-java)**.

SQLite database [venv/main.db](venv/main.db) connected by native JDBC.

Tests separated by classes accordingly webservice endpoints. 

In case no response from webservice, suite failed without running any tests.

All parameters for tests managed by [testng.xml](testng.xml) file.

---
**RESTfull API service** [venv/superservice.py](venv/superservice.py) was writen on Python 
with using *bottle* and *sqlalchemy* libraries.
Endpoints:
- **/ping/** - just code 200 on GET.
- **/authorize/** - returns authorization token (valid 1 min) for user/password = supertest/superpassword.
- **/api/save_data/** - saves data to *venv/main.db* for authorized user (50% succeeded).

For starting *superservice* please install Python and import libraries. 

First start with parameter **migrate** for creating DB. Next starts with parameter **run**!
Automation tests of primitive RESTfull API service.

Building - [Gradle](https://github.com/gradle/gradle).

Test running framework - [TestNG](https://github.com/cbeust/testng).

Testing and validation of REST service - [REST Assured](https://github.com/rest-assured/rest-assured).

config.yml parsed by [Jackson](https://github.com/FasterXML/jackson-dataformat-yaml)

main.db connected by native JDBC.

Structure:
- one config file [config.yml](config.yml).
- one class for config loading [Config.java](src/main/java/com/rest/qa/Config.java).
- one file for tests [Tests.java](src/test/java/com/rest/qa/Tests.java)

---

RESTfull API service [venv/superservice.py](venv/superservice.py) was writen on Python 
with using *bottle* and *sqlalchemy* libraries.
Endpoints:
- **/ping/** - just code 200 on GET.
- **/authorize/** - returns authorization token (valid 1 min) for user/password = supertest/superpassword.
- **/api/save_data/** - saves data to *venv/main.db* for authorized user (50% succeeded).

For starting *superservice* please install Python and import libraries.
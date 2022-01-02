[![Codacy Badge](https://app.codacy.com/project/badge/Grade/a5b727a2118e4799a8280574a513a946)](https://www.codacy.com/gh/mahou147/graduation-restaurant-voting/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mahou147/graduation-restaurant-voting&amp;utm_campaign=Badge_Grade)
# Restaurant voting:  Graduation project from the [TopJava](https://javaops.ru/view/topjava) internship

----
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

-----------------------------
### API documentation
[Swagger UI](http://localhost:8080/swagger-ui.html)

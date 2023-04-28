
# Simple TODO List KTOR Backend 

This is a simple implementation of a TODO list backend in KTOR. As the project name
suggests , a user can add a todo list item , update and delete. So basically 
a simple demonstration of a CRUD application with an extra Authentication feature. Fiddle with it as you wish but keep in mind this implementation itself doesn't mean is the best approach
or recommended approach for developing KTOR sever side applications. However , this should not mean I am not
open to constructive criticism. Open issues , fix bugs , add features and let's have fun. Anyway , have fun exploring! 


### Libraries used:
1. [KTORM](https://www.ktorm.org/) : A lightweight and efficient ORM Framework for Kotlin directly based on pure JDBC. It provides strong-typed and flexible SQL DSL and convenient sequence APIs to reduce our duplicated effort on database operations
2. [KTOR JWT Authentication](https://ktor.io/docs/jwt.html) : an open standard that defines a way for securely transmitting information between parties as a JSON object.
3. [XAMPP PhP myAdmin MySql](https://www.phpmyadmin.net/) :phpMyAdmin is a free software tool written in PHP, intended to handle the administration of MySQL over the Web. 
4. [Commons Codec Library](https://commons.apache.org/proper/commons-codec/) :  provides implementations of common encoders and decoders such as Base64, Hex, Phonetic and URLs.
5. [KTOR Content Negotiation](https://ktor.io/docs/serialization.html): Negotiating media types between the client and server. For this, it uses the Accept and Content-Type headers. Also supports serializing/deserializing the content in a specific format. Ktor supports the following formats out-of-the-box: JSON, XML, CBOR, and ProtoBuf.
6. [Logback Classic](https://logback.qos.ch/):Implementation of the SLF4J API for Logback, a reliable, generic, fast and flexible logging framework.






## Deployment 
For this project to run locally ensure:

1.  You have the latest JDK kit installed in your PC.
2.  Ensure that you have Intellij IDE installed to run the project 
3. You have the latest Kotlin version installed in the IDE.
4. The project uses MySql Database and JDBC to connect to the database. In this case MySql db    provided by XAMPP and phpMyAdmin was used to create and manage the tables. You can use any MySql db manager but just ensure the tables follow this schema : 

#### Todos table

#### table name: todos

| Column name | Type        | Extra        |
| ----------- | ----------- | ----------- |
| id(primary Key)| Int(11)      |       AUTO INCREMENT    |
| title   | VARCHAR(255)        |          -   |
| done   | tinyInt(1)        |          -   |
| user_id   | VARCHAR(100)        |          -   |

#### NOTE: tinyInt represents the boolean type in MySql.


#### Users table

#### table name: users

| Column name | Type        | Extra        |
| ----------- | ----------- | ----------- |
| user_id(primary Key)| VARCHAR(10)      |       -   |
| username   | VARCHAR(100)        |          -   |
| password   | VARCHAR(100)        |          -   |
| salt   | VARCHAR(100)        |          -   |
| email   | VARCHAR(100)        |          -   |
| refresh_token   | VARCHAR(100)        |          -   |

5. Once you have the project setup in the IDE and the SQL database setup , within the project go to the 
database directory open the `DatabaseManager.kt ` file and update the `hostname` `port` `databaseName` `username` and `password` variables according to how your database values and server has been setup.

6. Open the terminal and run:

```
  ./gradlew build

```  

After finishing building , run :

```
 ./gradlew run

```

7. Test the endpoints on Postman or any other API testing platform of your choice.


## License

```
Copyright [2023] [James Gitonga]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```



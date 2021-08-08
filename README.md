# Kanban Board (Zaprojektowani)
'Zaprojektowani' is an application to managing projects using kanban board.

## Features
- The ability to create several projects and add application users to them
- Kanban board is divided into 4 lists (to do, in progress, testing and done)
- Assigning project users to individual tasks
- Defining the priority of the task
- Project chat
- Changing user data
- Security system using Spring Security and JWT
<p>
<img src="https://i.imgur.com/nlnRVQ2.png" alt="demo1" width="300"/>
<img src="https://i.imgur.com/AdWs71Z.png" alt="demo2" width="300"/>
<img src="https://i.imgur.com/qii0JRT.png" alt="demo3" width="300"/>
<img src="https://i.imgur.com/c0UIuOl.png" alt="demo4" width="300"/>
<img src="https://i.imgur.com/mYWN3Vb.png" alt="demo4" width="300"/>
</p>

## Technologies
- Java 11
- Spring Boot 2.4.3
- Spring Security and JSON Web Token (JWT)
- PostgreSQL 42.2.5
- Gradle
- Angular 11.2

## Usage
To run the application you will need <a href="https://git-scm.com/">Git</a>, <a href="https://nodejs.org/en/download/">Node.js</a>, <a href="https://www.oracle.com/java/technologies/javase-downloads.html">Java</a> and <a href="https://gradle.org/">Gradle</a> installed on your computer.
Firstly clone this repo and go to the project directory.
```shell
$ git clone https://github.com/mbalcer/kanban-board.git
$ cd kanban-board
```

### Backend
Run the application using maven plugin in your IDE using the command:
```shell
$ gradle bootRun
```

### Frontend
You must install dependencies and start the application. You will then be able to access it at localhost:4200.

```shell
# Install dependencies
$ npm install

# Start application
$ npm start
```

## Authors
- <a href="https://github.com/mbalcer"> Mateusz Balcer </a>
- <a href="https://github.com/betlewski"> Szymon Betlewski </a>

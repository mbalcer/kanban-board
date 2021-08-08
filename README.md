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
<p>
<img alt='Java 11' src='https://img.shields.io/badge/-Java%2011-e48620?logo=java&logoColor=white&style=plastic' />
<img alt='Spring Boot 2.4.3' src='https://img.shields.io/badge/-Spring%20Boot%202.4.3-6db33f?logo=spring&logoColor=white&style=plastic' />
<img alt='Spring Security' src='https://img.shields.io/badge/-Spring%20Security-6db33f?logo=&logoColor=white&style=plastic' />
<img alt='JSON Web Token' src='https://img.shields.io/badge/-JSON Web Token-02b3e9?logo=&logoColor=white&style=plastic' />
<img alt='PostgreSQL 42.2.5' src='https://img.shields.io/badge/-PostgreSQL%2042.2.5-31648d?logo=PostgreSQL&logoColor=white&style=plastic' />
<img alt='Gradle' src='https://img.shields.io/badge/-Gradle-032e38?logo=gradle&logoColor=white&style=plastic' />
<img alt='Angular 11.2' src='https://img.shields.io/badge/-Angular%2011.2-d60e2f?logo=angular&logoColor=white&style=plastic' />
</p>

## Usage
To run the application you will need <a href="https://git-scm.com/">Git</a>, <a href="https://nodejs.org/en/download/">Node.js</a>, <a href="https://www.oracle.com/java/technologies/javase-downloads.html">Java</a> installed on your computer.
Firstly clone this repo and go to the project directory.
```shell
$ git clone https://github.com/mbalcer/kanban-board.git
$ cd kanban-board
```

### Backend
Open the project in your IDE (e.g. IntelliJ IDEA) and run the application.
You can also run the application with Gradle using the command:
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

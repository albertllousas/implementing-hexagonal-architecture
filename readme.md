# Implementing hexagonal architecture

The idea of this project is to understand and see how we can apply hexagonal architecture in terms of packaging
, naming, structure and all concerns that you can face in a real project.

## Hexagonal architecture overview

Hexagonal architecture is an architectural pattern, a way to build an structure your applications in order to obtain
  testable, loosely-coupled, framework agnostic, domain-centric, easy to evolve and less prone
  to errors applications.
 
But hexagonal is not a new trend, it has been here for a while, this kind of architecture was invented by [Alistair
 Cockburn](https://en.wikipedia.org/wiki/Alistair_Cockburn) in an made public in an [article](https://alistair.cockburn.us/hexagonal-architecture/) published in 2005.
 
It is important to mention that hexagonal, like onion or clean architectures are a family of architectural patterns
 that rely on a pattern described by Rover C. Martin (Uncle Bob) in 1996, the [dependency inversion principle](https://blog.cleancoder.com/uncle-bob/2016/01/04/ALittleArchitecture.html), the D of solid, as a base pattern.

 
## Application overview: banking application

To drive all the project we need a real project, a real domain where we can see in a simple and pragmatic way how we
 can apply this super-useful architectural pattern.
 
In this example we will implement a simple use-case, a transfer between two accounts of the same bank, and here, our
 user story:
```text
As a customer of the Bank
I want to transfer money to other customer of this bank
So that, I can make an instant transfer
```

diagram acount -> bank -> account

## The hexagon in our app 

Trying to stick on the principles of the original idea of Alistair Cockburn and also influenced by Uncle Bob, we have
 tried to organize the code around these ideas; this is just a personal approach from the original article, so don't
  take it as a bible ;).
 
These are the packages of our app:
```shell
`-- com
    `-- bank
        `-- transfers
            |-- app 
            |   |-- domain
            |   |-- port
            |   |   |-- driven
            |   |   `-- driver
            |   `-- usecase
            `-- infrastructure
                `-- config
                `-- adapter
                    |-- driven
                    `-- driver

```
### App: 
The core of the hexagon, all the app code lives here, it is **isolated from the rest of the world**, only dependencies
 within itself.
- Domain: The heart of our application, all the business logic, state and behavior.
- Ports: **The boundary of the hexagon**, every communication from/to the outside world to our app will be through
 these boundaries, they are just interfaces.
    - Driver/primary/input:An input port is a simple interface that can be called by the entrypoints and it is
     implemented by a use case, basically it is the API of the application.
     
    - Driven/secondary/output: a simple interface that can be called by our use cases or domain if they need
     something
     from the outside (database access, for instance), this is an application of the Dependency Inversion
      Principle (the “D” in SOLID)
      
- Use cases: They implement the driver ports, they expose the functionality of the app orchestrating actions or steps
 defining the interactions within the domain and ports.
    
    
### Infrastructure

This layer is where all components outside of our app live, the I/O components UI, database, frameworks, 
devices, clients... 
- Adapters: In hexagonal architecture all the primary and secondary actors interact with the application ports
 through adapters, they are the translators between the domain and the infrastructure. 
    - Driver/primary/input: Primary adapters or “driving” adapters, they call the driver ports to initiate
         interactions with the app, the entrypoints to our app. (controllers, schedulers, queue consumers, console ...)
         
    - Driven/secondary/output: They implement the driven ports of our domain, they adapt any external interaction
         with the outside world to our domain, they are called from our app domain/usecases. (database, http-clients
          ...)
-Config and other packages: Here we can add any configuration that we need at framework level to run our app.

## tech stack

  

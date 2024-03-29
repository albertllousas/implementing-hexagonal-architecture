# Implementing hexagonal architecture

The idea of this project is to understand and see how we can apply hexagonal architecture in terms of packaging
, naming, structure and all concerns that you can face in a real project.

Everything will be covered with test during the process because it has been done doing outside-in tdd.

- [Implementing hexagonal architecture](#implementing-hexagonal-architecture)
  * [Hexagonal architecture overview](#hexagonal-architecture-overview)
    + [AKA Ports and Adapters](#aka-ports-and-adapters)
      - [App:](#app-)
      - [Outside world: the infrastructure](#outside-world--the-infrastructure)
      - [Flow of calls](#flow-of-calls)
  * [Banking application](#banking-application)
    + [Code structure](#code-structure)
    + [Important note: modern approaches](#important-note--modern-approaches)
  * [tech stack](#tech-stack)
  * [Running tests](#running-tests)
  * [Run the app](#run-the-app)
  * [Links](#links)

## Hexagonal architecture overview

Hexagonal architecture is an architectural pattern, a way to build and structure your applications in order to obtain
  testable, loosely-coupled, framework agnostic, domain-centric, easy to evolve and less prone to errors applications.
 
But hexagonal is not a new trend, it has been here for a while, this kind of architecture was invented by [Alistair
 Cockburn](https://en.wikipedia.org/wiki/Alistair_Cockburn) in an made public in an [article](https://alistair.cockburn.us/hexagonal-architecture/) published in 2005.
 
It is important to mention that hexagonal, like onion or clean architectures are a family of architectural patterns
 that rely on a pattern described by Robert C. Martin (Uncle Bob) in 1996, the [dependency inversion principle](http://blog.cleancoder.com/uncle-bob/2016/01/04/ALittleArchitecture.html), the D of solid, as a base pattern.

### AKA Ports and Adapters

When people talk about code architectures, they usually to talk about layers, but in hexagonal architecture there
 are no layers, it is all about ports, adapters and of course, dependency inversion.
 
As we just said, the whole idea is around *Ports* and *Adapters*, let's see what they mean ...

This is how a typical hexagonal app looks like:

<p align="center">
  <img src="misc/hexagonal.png">
</p>

#### App: 
The core of the hexagon, all the app code lives here, it is **isolated from the rest of the world**, only dependencies
 within itself.
- *Domain*: The heart of our application, all the business logic, state and behavior.
- **Ports**: **The boundaries of the hexagon**, every communication from/to the outside world to our app will be through
 these boundaries, they are just interfaces.
    - *Driver/left/input*: An input port is a simple interface that can be called by the entrypoints (primary actors) 
    and it is implemented by a use case, basically it is the API of the application. 
     
    - *Driven/right/output*: a simple interface that can be called by our use cases or domain (inside the hexagon) if 
    they need something from the outside (secondary actors, a database access, for instance). Here, we have 
     Dependency Inversion Principle (the “D” in SOLID) in practice. 
      
- *Use-cases/application-services*: They implement the driver ports, they expose the functionalities of the app
 , they orchestrate the infrastructure (ports) and the domain to achieve a single business use case. They are stateless. 
    
#### Outside world: the infrastructure

This layer is where all components outside of our app live, the I/O components UI, database, frameworks, libs
, devices, clients... everything but the app core.
- **Adapters**: In hexagonal architecture all the primary and secondary actors interact with the application ports
 through adapters, they are the translators between the domain and the infrastructure. 
    - *Driver/left/input*: Primary adapters or “driving” adapters, they call/use the driver ports to initiate
         interactions with the app, the entrypoints to the hexagon. Typically http controllers, schedulers, queue
          consumers, console and many others belong here.
         
    - *Driven/secondary/output*: They implement the driven ports of our domain, they adapt any external interaction
         with the outside world to our domain, they are called from the inside of the hexagon, either the app domain
          or the usecases. Repository implementations, http-client or queue producers are the most common ones.
          
- Everything else: Any other class outside the hexagon would belong here, framework related components, configuration
 ...
          
**Note:** Adapters are considered part of the hexagon, but the "outer" section in the infrastructure side.

#### Flow of calls

This is the flow of the calls since an entrypoint gets triggered by an actor.
```
actor -> driver adapter -> (driver port -> app -> driven port) -> driven adapter -> infrastructure`
       |________________|____________________________________|___________________|
         Outer Hexagon             Inner Hexagon                 Outer Hexagon
```

Let's imagine we have a REST service:

1. An actor calls our app by http
2. Our driver adapter gets triggered
3. Adapter calls our app through a driver port (inbound boundary)
4. App, that implements the driver port through a use-case wants starts the business and interacts with the domain
5. Interactions to the outside world are done through driven ports
6. Driven adapter gets called, notice that here the relationship of Port and Adapter is inverted, now the adapter
 implements the port.
7. Finally, the infrastructure, maybe a DB, AWS or Queue resource is hit.


## Banking application

To drive all the project we need a real domain where we can see in a simple and pragmatic way how we can apply this
 super-useful architectural pattern.
 
In this example we will implement a simple use-case, a transfer between two accounts of the same bank, and here, our
 user story:
```text
As a customer of the Bank
I want to transfer money to other customer of this bank
So that, I can make an instant transfer
```

Having said that, our app will be a simple banking app with one use case, we will try to apply the most simple 
 hexagonal approach that could be extrapolated for a bigger and complex application.

Next diagram shows how our components fit in the hexagon:

<p align="center">
  <img src="misc/hexagonal-bank-app.png">
</p>

Trying to stick on the principles of the original idea of Alistair Cockburn and also influenced by Uncle Bob, we have
 tried to organize the code around these ideas; this is just a personal approach from the original article, so don't
  take it as a bible ;).

### Code structure

These are the [packages](https://github.com/albertllousas/implementing-hexagonal-architecture/tree/master/src/main/kotlin/com/bank/transfers) the app, hexagonal does not force to have any package
 structure , it's all about ports and adapters, but we have tried to **go-by-the-book** and follow Alistair Cockburn first idea:
  
 ```kotlin
`-- com
    `-- bank
        `-- transfers
            |-- app // the inner hexagon
            |   |-- domain // our domain model
            |   |-- port // boundaries
            |   |   |-- driver // inbound/left boundary to the hexagon (interfaces), hexagon entrypoint
            |   |   `-- driven // outbound/right boundary to the hexagon (interfaces), hexagon calls to the outside
            |   `-- usecase // implement driver ports, orchestrators of the app exposed use-cases
            `-- infrastructure // outside world
                `-- config // framework config & wiring
                `-- adapter
                    |-- driver // use/call driver port interfaces
                    `-- driven // implement driven ports

```
You can notice that, we have separated the `app` from `infrastructure`, this separation comes from the original
 [diagrams](https://alistair.cockburn.us/hexagonal-architecture/) drawn in 2005, where you have the app, the core of
  your hexagon, and then the rest, the IT infrastructure, the composite hardware, software, network resources and
   services.  

### Important note: modern approaches

Maybe this an approach is a bit verbose in terms of naming and packaging, but it is made for the sake of understanding
 the pattern.

Modern approaches are based/inspired on this idea, omitting ports and adapters packages/names, using
 packaging by [package-by-feature](http://www.codingthearchitecture.com/2015/03/08/package_by_component_and_architecturally_aligned_testing.html) or [package-by-component](https://herbertograca.com/2017/11/16/explicit-architecture-01-ddd-hexagonal-onion-clean-cqrs-how-i-put-it-all-together/).
  
Hexagonal is also usually mixed with other patterns/approaches such as CQRS, EDA or DDD (like Vaughn Vernon [does in his samples of DDD](https://github.com/VaughnVernon/IDDD_Samples)).

**Modern layering**

It is important to mention that a practical approach and almost a convention in all hexagonal projects is to follow the packaging
 and layering inspired and used in DDD:
 ```
 - Application: Application Services (the use cases) 
 - Domain model: domain and ports
 - Infrastructure: adapters
 ```

We didn't follow these trends because we wanted to follow the original idea for the sake of explain the architectural pattern.

## tech stack

* Language: Kotlin
* JVM: 1.8.0*
* Web server: [Ktor](https://ktor.io/)
* Testing libraries/frameworks:
    * [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
    * [Assertj](https://joel-costigliola.github.io/assertj/)
    * [Mockk](https://mockk.io/)
    * [REST Assured](http://rest-assured.io/)

## Running tests
```shell
./gradlew test
```
## Run the app
```shell
./gradlew build
java -jar build/libs/implementing-hexagonal-architecture-all.jar 
```

## Links
- [Original article](https://alistair.cockburn.us/hexagonal-architecture/)
- [Ports and adapters pattern](https://jmgarridopaz.github.io/content/hexagonalarchitecture.html)
- Benefits[1](https://culttt.com/2014/12/31/hexagonal-architecture/)[2](https://crosp.net/blog/software-architecture/clean-architecture-part-1-databse-vs-domain/)[3](https://madewithlove.com/hexagonal-architecture-demystified/)
- [Hexagonal blog post](https://kurron.bitbucket.io/004/index.html)

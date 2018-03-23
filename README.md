
[![Build Status](https://travis-ci.org/pheymann/typedapi.svg?branch=master)](https://travis-ci.org/pheymann/typedapi)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.pheymann/typedapi-shared_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.pheymann/typedapi-shared_2.12)

# Typedapi
Define type safe APIs and let the Scala compiler do the rest:

### Client side
```Scala
import typedapi.client._

val MyApi = 
  // GET /fetch/user?sortBy=<>
  (:= :> "fetch" :> "user" :> Query[String]('sortBy) :> Get[List[User]]) :|:
  // POST /create/user
  (:= :> "create" :> "user" :> ReqBody[User] :> Post[Unit])

val (fetch :|: create :|: =:) = compile(MyApi)

import typedapi.client.http4s._
import cats.effect.IO
import org.http4s.client.blaze.Http1Client

implicit val cm = ClientManager(Http1Client[IO]().unsafeRunSync, "http://my-host", 8080)

fetch("age").run[IO]: IO[List[User]]
```

### Server side
```Scala
import typedapi.server._

def fetch(sortBy: String): IO[List[User]] = ???
def create(user: User): IO[User] = ???

val endpoints = ink(MyApi).to(fetch _ :|: create _ :|: =:)

import typedapi.server.http4s._
import cats.effect.IO
import org.http4s.server.blaze.BlazeBuilder

val sm     = ServerManager(BlazeBuilder[IO], "http://my-host", 8080)
val server = mount(sm, endpoints)

server.unsafeRunSync()
```

This is all you have to do to define an API with multiple endpoints and to create call functions for them. No extra code is needed.

## Motivation
This library is the result of the following questions:

> How much can we encode on the type level? Are we able to describe a whole API and generate the call functions from that without using Macros?

It is inspired by [Servant](https://github.com/haskell-servant/servant) and it provides an API layer which is independent of the underlying server/client implementation. Right now Typedapi supports:

  - [http4s](https://github.com/http4s/http4s)

But it is planned to add [akka-http](https://github.com/akka/akka-http) before the first stable release `0.1.0`.

## Get this library
It is available for Scala 2.11 and 2.12 and can downloaded as Maven artifact:

```
// dsl
"com.github.pheymann" %% "typedapi-client" % <version>
"com.github.pheymann" %% "typedapi-server" % <version>

// http4s support
"com.github.pheymann" %% "typedapi-http4s-client" % <version>
"com.github.pheymann" %% "typedapi-http4s-server" % <version>
```

You can also build it on your machine:

```
git clone https://github.com/pheymann/typedapi.git
cd typedapi
sbt "+ publishLocal"
```

## Documentation
The documentation is located in [docs](https://github.com/pheymann/typedapi/blob/master/docs) and covers the following topics so far:
 - [How to define an API](https://github.com/pheymann/typedapi/blob/master/docs/ApiDefinition.md)
 - [How to create a client](https://github.com/pheymann/typedapi/blob/master/docs/ClientCreation.md)
 - [How to create a server](https://github.com/pheymann/typedapi/blob/master/docs/ServerCreation.md)
 - [Extend the library](https://github.com/pheymann/typedapi/blob/master/docs/ExtendIt.md)

## Dependencies
 - [shapeless 2.3.3](https://github.com/milessabin/shapeless/)

## Contribution
Contributions are highly appreciated. If you find a bug or you are missing the support for a specific client/server library consider opening a PR with your solution.

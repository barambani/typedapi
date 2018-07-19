### 0.1.0
 - internal cleanups and refactorings
 - centralized http-support specs
 - added akka-http support on server and client side
 - added ScalaJS compilation support for shared and client code
 - implemented basic ScalaJS client
 - shared changes:
   - added body encoding types and made them mandatory
   ```Scala
   := :> ReqBody[Json, User] :> Get[Json, User]
   _______________^__________________^
   ```
   
   - `RawHeaders` was removed
   - fixed headers were added; a fixed header is a statically known key-value pair, therefore, no input is required
   ```Scala
   // dsl
   := :> Fixed("Access-Control-Allow-Origin", "*") :> Get[Json, User]
   
   // function
   api(Get[Json, User], headers = Headers add("Access-Control-Allow-Origin", "*"))
   ```
   
 - changes to the server API:
   - `NoReqBodyExecutor` and `ReqBodyExecutor` now expect a `MethodType`:
   ```Scala
   new NoReqBodyExecutor[El, KIn, VIn, M, F, FOut] {
   ____________________________________^
  
   new ReqBodyExecutor[El, KIn, VIn, Bd, M, ROut, POut, F, FOut] {
   ______________________________________^
   ```
 - changes to the client API:
   - new encoding types add `Content-Type` and `Accept` headers

### 0.1.0-RC5 / Almost there
 - changes to the client API:
 ```Scala
 val ApiList =
   (:= :> Get[Foo]) :|:
   (:= :> RequestBody[Foo] :> Put[Foo])
   
 // `:|:` removed for API compositions
 val (get, put) = deriveAll(ApiList)
 ```
 
 - changes to the server API:
 ```Scala
 // same for endpoint compositions
 val e = deriveAll[IO](ApiList).from(get, put)
 ```

### 0.1.0-RC4 / Towards a stable API
 - changes to the client API:
 ```Scala
 val Api     = := :> Get[Foo]
 val ApiList =
   (:= :> Get[Foo]) :|:
   (:= :> RequestBody[Foo] :> Put[Foo])
 
 // not `compile`, but
 val foo = derive(Api)
 
 val (foo2 :|: bar :|: =:) = deriveAll(ApiList)
 
 ...
 // explicitly pass ClientManager
 foo().run[IO](cm)
 _______________^
 ```
 
 - changes to the server API
 ```Scala
 // not `link.to`, but
 val endpoint = derive[IO](Api).from(...)
 
 val endpoints = deriveAll[IO](ApiList).from(...)
 ```
 
 - major changes were applied to the internal code to reach a stable state (see [this PR](https://github.com/pheymann/typedapi/pull/13))

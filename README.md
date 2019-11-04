# Exploding

## Run

```bash
sbt run
```

## Unit Test

```bash
sbt test
```

## Philosophy

We went for a solution that is pure. We user cats IO as an effect Monad.
And for testing we are using a StateT monad transformer on top of the IO monad and
MonadState from cats MTL that saves us some boilerplate

If we wanted to be more thorough we could use Case Class instead of string for our 
input and output type (in console) which would help for testing.


## Assumption

* We assume the player the card in the order it picks the
the newest at the beging of the list

## Improvements

* We could use cats `Show`, for display instead.
* More unit tests are needed for the models, 
and the main program try out different shuffling strategies, a cool thing would be to use ScalaCheck to generate
shuffling functions to have a closer case to reality.


# Exploding

## Run

```bash
sbt run
```

## Philosophy

We went for a solution that is pure. We user cats IO as an effect Monad.
And for testing we are using a StateT monad transformer on top of the IO monad and
MonadState from cats MTL that saves us some boilerplate

If we wanted to be more thorough we could use Case Class instead of string for our 
input and output type (in console) which would help for testing.
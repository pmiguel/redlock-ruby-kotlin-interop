# Ruby<->Redisson Interop Investigation

This document describes the differences between the implementations of Redisson's Lock mechanisms, and a redlock implementation in Ruby.
The objective is to find a common ground so that two heterogenous applications can share the same redis-lock mechanism.

Standard Redlock documentation algorythm
https://redis.io/docs/reference/patterns/distributed-locks/

## (Ruby) Redlock-rb format (Redlock)
Command Impl: https://github.com/leandromoreira/redlock-rb/blob/main/lib/redlock/scripts.rb

- Simple, if-exists lock.
- Returns true if thread already has a lock.
- Unlocks by deleting key.
- Driver long-polls for key to be deleted in order to acquire new lock.


```
GET lock_key1 => "8aa49485-fa83-4a0e-ade6-e5c6f77d291d"
TYPE lock_key1 => string
```


## (Java) Redisson-format (RedissonLock)
Command Impl: https://github.com/redisson/redisson/blob/master/redisson/src/main/java/org/redisson/RedissonLock.java

- Complex, reentrant lock.
    - If thread acquires new lock, a lock counter is incremented.
- Watchdog to extend lock TTL to avoid hang locks.
    - If thread crashes, TTL is not extended, lock is released.
- Relies on Redis PubSub for lock release notification.

```
GET lock_key2 => (error) WRONGTYPE Operation against a key holding the wrong kind of value
TYPE lock_key2 => hash
HGETALL lock_key2 =>
    "a13acce4-72d7-4ab6-9d8c-4b7f8fd39b35:1"
    "1"
```


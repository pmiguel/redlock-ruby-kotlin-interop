require 'redlock'
require 'redis'

redlock = Redlock::Client.new(
  [ 'redis://localhost:6379' ], {
  retry_count:   10,
  retry_delay:   3000, # milliseconds
  redis_timeout: 1  # seconds
})

puts("Acquiring Lock...")

res = redlock.lock("lock_key1", 30*1000)

puts(res.inspect)
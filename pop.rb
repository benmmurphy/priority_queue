#!/usr/bin/env ruby

require 'redis'


redis = Redis.new(:host => "localhost", :port => 6380)
start = Time.now

count = 0
while true
  next_value = redis.zremrangebyrank("q", 1 , 2)
  break if next_value.nil?
  count = count + 1
end

final = Time.now

elapsed = final - start

puts "Popped #{count}"
puts count / elapsed


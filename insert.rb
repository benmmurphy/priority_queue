#!/usr/bin/env ruby

require 'redis'

if ARGV.length < 2
  puts "Usage: ./insert.rb PORT N"
  exit(1)
end

N = ARGV[1].to_i
PORT = ARGV[0].to_i

redis = Redis.new(:host => "localhost", :port => PORT)
start = Time.now
(1..N).each do |key|
  redis.zadd("q", Random.rand(100) , key.to_s)
end

final = Time.now

elapsed = final - start

puts elapsed
puts N / elapsed


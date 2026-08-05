local key = KEYS[1]
local interval = tonumber(ARGV[1])
local now = tonumber(ARGV[2])

local nextAllowedTime = now
local stored = redis.call('GET', key)
if stored then
    local storedNum = tonumber(stored)
    if storedNum > now then
        nextAllowedTime = storedNum
    end
end

local newNextAllowedTime = nextAllowedTime + interval
redis.call('SET', key, newNextAllowedTime, 'PX', interval * 10)

return nextAllowedTime
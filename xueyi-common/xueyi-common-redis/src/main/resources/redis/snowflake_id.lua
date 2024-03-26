local isExist = redis.call('exists', KEYS[1])
if isExist == 1
then
    local workerId = redis.call('get', KEYS[1])
    workerId = (workerId + 1) % 1024
    redis.call('set', KEYS[1], workerId)
    return workerId
else
    redis.call('set', KEYS[1], 0)
    return 0
end
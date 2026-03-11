#!/bin/sh
SENTINEL_QUORUM=${SENTINEL_QUORUM:-2}
SENTINEL_DOWN_AFTER=${SENTINEL_DOWN_AFTER:-5000}
SENTINEL_FAILOVER=${SENTINEL_FAILOVER:-180000}
SENTINEL_ANNOUNCE_PORT=${SENTINEL_ANNOUNCE_PORT:-26379}

SENTINEL_ANNOUNCE_IP=$(getent ahostsv4 host.docker.internal | awk '{print $1}' | head -1)
echo "Announcing as $SENTINEL_ANNOUNCE_IP:$SENTINEL_ANNOUNCE_PORT"

# Resolve master IP once at startup so sentinel doesn't re-resolve after master dies
MASTER_IP=$(getent ahostsv4 redis-master | awk '{print $1}' | head -1)
echo "Resolved redis-master to $MASTER_IP"

sed -i "s/SENTINEL_QUORUM/$SENTINEL_QUORUM/g" /etc/sentinel.conf
sed -i "s/SENTINEL_DOWN_AFTER/$SENTINEL_DOWN_AFTER/g" /etc/sentinel.conf
sed -i "s/SENTINEL_FAILOVER/$SENTINEL_FAILOVER/g" /etc/sentinel.conf
sed -i "s/SENTINEL_ANNOUNCE_IP/$SENTINEL_ANNOUNCE_IP/g" /etc/sentinel.conf
sed -i "s/SENTINEL_ANNOUNCE_PORT/$SENTINEL_ANNOUNCE_PORT/g" /etc/sentinel.conf
sed -i "s/MASTER_IP/$MASTER_IP/g" /etc/sentinel.conf

echo "Waiting for redis-master..."
until redis-cli -h redis-master -p 6379 ping | grep -q PONG; do
  sleep 1
done
echo "redis-master is up, starting sentinel."

exec redis-sentinel /etc/sentinel.conf
```

**`sentinel/sentinel.conf`** — use `MASTER_IP` instead of `redis-master`, and remove `resolve-hostnames`:
```
port 26379
sentinel announce-ip SENTINEL_ANNOUNCE_IP
sentinel announce-port SENTINEL_ANNOUNCE_PORT
sentinel monitor redis-master MASTER_IP 6379 SENTINEL_QUORUM
sentinel down-after-milliseconds redis-master SENTINEL_DOWN_AFTER
sentinel parallel-syncs redis-master 1
sentinel failover-timeout redis-master SENTINEL_FAILOVER
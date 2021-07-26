docker rmi --force `docker images | grep redis-test | awk '{print $3}'`
docker rmi --force `docker images | grep none | awk '{print $3}'`

./gradlew clean --refresh-dependencies
./gradlew buildDocker -x test --stacktrace
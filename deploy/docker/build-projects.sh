
#run command to run this shell file-    ./build-projects.sh

#Go two directories back
cd ../../

#Build all jars for all projects
cd eurekaServer && ./mvnw clean package -DskipTests && cd ..
cd gateway && ./mvnw clean package -DskipTests && cd ..
cd configserver && ./mvnw clean package -DskipTests && cd ..
cd order-microservice && ./mvnw clean package -DskipTests && cd ..
cd user-microservice && ./mvnw clean package -DskipTests && cd ..
cd product-microservice && ./mvnw clean package -DskipTests && cd ..
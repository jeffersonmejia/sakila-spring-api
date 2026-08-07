FROM eclipse-temurin:21-jre

USER root

RUN apt-get update \
    && apt-get install -y --no-install-recommends postgresql-common \
    && /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh -y \
    && apt-get install -y --no-install-recommends postgresql-client-17 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY target/sakila-api.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

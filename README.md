# Projektstruktur (oversigt)

Følgende er et forslag til en basis-opsætning af et JPA projekt, der bruger Hibernate. 
I eksemplet er medtaget en enkelt entitet `Study`, der repræsenterer en Delphi-evaluering (a Delphi Study).

Når du skal opsætte et tilsvarende projekt, kan du evt. følge denne vejledning:

- [JPA Setup ](https://3sem.kursusmaterialer.dk/toolbox/java/orm/jpa-setup/)

Nedenfor er en kort oversigt over de vigtigste mapper og filer i projektet. Udeladt: `.idea`, `.mvn`, `docs`.

## Roden
- `pom.xml` — Maven-konfiguration og afhængigheder.
- `src/` — Kildekode og testkode.
- `target/` — Byg-output (genereret af Maven).

## `src/main/java/app`
- `Main.java` — Eksempel/entry point til at køre demoer.

### `app/config`
- `HibernateConfig.java` — Bygger og genbruger `EntityManagerFactory` til runtime.
- `HibernateEmfBuilder.java` — Opretter EMF ud fra Hibernate-properties.
- `HibernateBaseProperties.java` — Fælles Hibernate-indstillinger.
- `EntityRegistry.java` — Registrerer `@Entity`-klasser i Hibernate.

### `app/daos`
- `IDAO.java` — Generisk DAO-kontrakt.
- `StudyDAO.java` — CRUD og transaktionshåndtering for `Study`.

### `app/entities`
- `Study.java` — JPA-entity med felter, livscyklus-hooks og validering.

### `app/enums`
- `StudyPhase.java` — Enum til fasefeltet på `Study`.

### `app/exceptions`
- `ApiException.java` — Simpel runtime-exception med statuskode.

### `app/utils`
- `Utils.java` — Hjælpefunktioner, bl.a. til properties.

## `src/main/resources`
- `config.properties` — DB-konfiguration til dev.
- `logback.xml` — Logging-konfiguration.

## `src/test/java/app`

### `app/config`
- `HibernateTestConfig.java` — Test-EMF med Testcontainers og `create-drop`.

### `app/daos`
- `StudyDAOTest.java` — Integrationstests for `StudyDAO`.

### `app/testutils`
- `StudyTestPopulator.java` — Seeder testdata og returnerer entities.

```
delphi/
├─ pom.xml
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ app/
│  │  │     ├─ Main.java
│  │  │     ├─ config/
│  │  │     │  ├─ EntityRegistry.java
│  │  │     │  ├─ HibernateBaseProperties.java
│  │  │     │  ├─ HibernateConfig.java
│  │  │     │  └─ HibernateEmfBuilder.java
│  │  │     ├─ daos/
│  │  │     │  ├─ IDAO.java
│  │  │     │  └─ StudyDAO.java
│  │  │     ├─ entities/
│  │  │     │  └─ Study.java
│  │  │     ├─ enums/
│  │  │     │  └─ StudyPhase.java
│  │  │     ├─ exceptions/
│  │  │     │  └─ ApiException.java
│  │  │     └─ utils/
│  │  │        └─ Utils.java
│  │  └─ resources/
│  │     ├─ config.properties
│  │     └─ logback.xml
│  └─ test/
│     └─ java/
│        └─ app/
│           ├─ config/
│           │  └─ HibernateTestConfig.java
│           ├─ daos/
│           │  └─ StudyDAOTest.java
│           └─ testutils/
│              └─ StudyTestPopulator.java
```

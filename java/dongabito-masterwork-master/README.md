### [dongabito-masterwork]
# PortfolioHub

### Az alkalmazás célja

A PortfolioHub gyűjti és elérhetővé teszi programozók technológiai tudását bemutató portfóliókat.

Egy felhasználó regisztráció (`POST /register`) és az azt követő login (`POST /login`) után a frontenden a következő nézetekkel rendelkezhet:
- ...adminisztrációs felülettel(`GET /portfolio +TOKEN`), melyen
    - szerkesztheti adatait (`POST|PUT /portfolio/cv +TOKEN`)
    - hozzáadhat/törölhet porfóliójához képességeket(`POST|DELETE /portfolio/skill +TOKEN`) 
    - hozzáadhat/törölhet/szerkeszthet portfóliójához roadmapeket (`POST|DELETE /portfolio/roadmap +TOKEN`)
      - ehhez adhat/törölhet skilleket listájából (`POST|DELETE /roadmap/:id/skill/:skillId +TOKEN`)
      - beállíthatja tehcnológiai haladásának mértékét(`POST|PUT|DELETE /portfolio/technology/progress +TOKEN`)

- ...bárki számára elérhető szolgáltatásokat nyújtó nézettel:
    - keresés ID szerint felhasználóra(`GET /portfolio/{id}`)
    - keresés kompetencia alapján felhasználóra(`GET /portfolio/skill/{id}`)
    - keresés technológia szerint felhasználóra(`GET /portfolio/technology/{id}`)

- frontend fejlesztők számára nyújtott szolgáltatások:
  - összes kompetencia lekérése (`GET /skill`)
  - összes kategória lekérése (`GET /skill/skillcategory`)
  - összes technológia lekérése (`GET /technology`)
  - összes kulcsszó lekérése (`GET /techology/keywords`)
  - összes roadmap lekérése (`GET /roadmap`)
  - roadmap lekérése ID alapján (`GET /roadmap/{id}`)
  - összes roadmap lekérése szűrővel (`GET /roadmap?id=<programmer_id>`)

- Az adminisztrátor
  - hozzáadhat/törölhet/szerkeszthet kompetenciákat és azok kategóriáit (`POST|PUT|DELETE /skill +TOKEN`)
  - hozzáadhat/törölhet/szerkeszthet technológiákat és azok kulcsszavait (`POST|PUT|DELETE /skill +TOKEN`)
  - hozzáadhat/törölhet technológiákat skillekhez (`POST|DELETE /technology/:id/skill/:skillId +TOKEN`)
  - törölhet felhasználót (`DELETE /portfolio/{id} +TOKEN`)
    
### Környezeti változók
- MySQL_URL=<db_url> MySQL_UserName=<db_user>  MySQL_Password=<db_password> JWT_SECRET=<JWT_password> ADMIN_FNAME=<admin_firstname> ADMIN_LNAME=<admin_lastname> ADMIN_EMAIL=<admin_email> ADMIN_SECRET=<admin_SECRET>

### OpenAPI
OpenApi leírás: [SwaggerHub](https://app.swaggerhub.com/apis-docs/dongabito/Portfolio/1.0.0).

### Háttérszolgáltatás függőségek
- SQL adatbázis-szerver

### Telepítés
- `docker pull donga/masterwork` vagy `docker build -t <image-name> .`
- `docker run -p 8080:8080 -e MySQL_URL=<db_url> -e MySQL_UserName=<db_user> -e MySQL_Password=<db_password> -e JWT_SECRET=<password> -e ADMIN_FNAME=<admin_firstname> -e ADMIN_LNAME=<admin_lastname> -e ADMIN_EMAIL=<admin_email> -e ADMIN_SECRET=<admin_SECRET> <image_name>`

### Alkalmazott technológiák
- Springboot Starter Web
- Spring Data JPA
- Flyway
- Spring Security
- JWT
- Lombok
- H2
- Junit5
- Mockito

### Alkalmazott háttérszolgáltatások
#### CI/CD
- GitHub Actions
 #### Application hosting
- [Heroku](https://portfoliohubmv.herokuapp.com/)
#### SQL adatbázis
- AWS RDS
#### Endpoint testing
- Postman

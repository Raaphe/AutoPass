## AUTOPASS

Developed in part by Lam Nguyen, Raphael Paquin and Moncef Moutaki.

##### LINKS

- [Swagger-ui](http://localhost:9090/swagger-ui/index.html)
- [Swagger yaml](http://localhost:9090/v3/api-docs.yaml)
- [Swagger JSON](http://localhost:9090/v3/api-docs)
- [h2 Console](http://localhost:9090/h2-console)

---

#### H2 URL

> `jdbc:h2:mem:autopass`

---

> To generate client code use maven lifecycle command `mvn verify` or `verify -f pom.xml`.

---

## Accessing the app from another machine

1. Change the `application.properties` property `application.ip` to whatever the IP of the machine you will host on will be.
2. Run a `mvn verify`.
3. Access the page from `http://<YOUR_IP>:3000`.

---



# Scanner App

The app responsible for scanning user cards is the directory named `autopass-scanner`. This is an extension to our backend and will only serve as a view component of our app, but built natively through react and `electronjs`.

>  `> cd /autopass-scanner`

> `> npm run start`

> `> npm run electron start`


---




![img.png](img.png)

![img_1.png](img_1.png)

Architecture summary

The application has 3 layers. The first layer is the presentation layer which includes the Thymeleaf templates
and the REST API consumers. The 2nd layer is the application layer which is the Spring Boot backend which includes the controllers,
the services, and business logic. The 3rd layer is the data layer which includes MySQL for structured data and MongoDB for document-based
data.

The flow of data and control
1. The REST API clients make API calls to the Thymeleaf or REST controllers. 
2. The Thymeleaf controllers returns HTML while the REST controller returns API responses after processing some backend logic. 
3. The heart of the backend system. It ensures clean separation between the controller and data layers. 
4. The service layer interacts with the repository layer to do database operations. There is a MySQL db and a Mongo db. 
5. Each repository interacts directly with the database. 
6. Data that is retrieved from the database is mapped to Java model classes. 
7. The models are used in the response layer. For MVC, the models are passed from the controller to the thymeleaf templates and then rendered as html. For REST, the models/dtos are serialized into JSON and sent back to the client as part of an HTTP response. 
# Using GraphQL spqr with CDI

This project demonstrates how to use [spqr](https://github.com/leangen/graphql-spqr) with CDI (Context and Dependency Injection) and [JNoSQL](http://www.jnosql.org/).
It targets [Jakarta EE](https://jakarta.ee/) and [MicroProfile](https://microprofile.io/) platforms.

It is based on the [graphql-java tutorial](https://www.howtographql.com/graphql-java/0-introduction/) from Bojan Tomic.

The use case is the same: a basic HackerNews implementation with three domain objects: Link, User and Vote. 

The use of GraphQL is deliberately basic and the emphasis is on CDI integration.

This is a preliminary work for the [MicroProfile GraphQL initiative](https://jefrajames.wordpress.com/2019/01/04/when-graphql-meets-microprofile/) that has just started 
where spqr will be used as a basis.


## spqr overview
spqr enables to generate a GraphQL schema following a "code-first approach" where the schema is not predefined but is dynamically generated from annotated classes.
A code-first implementation makes sense for a strongly-typed language such as Java by avoiding duplication between the schema and the classes.

In this example, 3 kinds of classes are annotated:
1. **Query**: this is the facade for all GraphQL queries. Its methods are annotated with @GraphQLQuery,
2. **Mutation**: this is the facade for all GraphQL mutations. Its methods are annotated with @GraphQLMutation and @GraphQLArgument,
3. **Resolvers**: which are in charge of retrieving data for non-scalar types.Their methods are annotated with @GraphQLQuery and @GraphQLContext.



## CDI integration
In this demo project, I've tried to maximize spqr integration with CDI. 
First of all, Query, Mutation, AuthData, repositories and resolvers are 
standard CDI beans, so that they can easily be injected in other components.

The mechanism to detect GraphQL classes and generate the schema is generic and is implemented as a CDI extension.
CDI extension is a standard mechanism to interact with the CDI container through some specific life cycle events: 
ProcessAnntotatedType, ProcessBean, AfterBeanDiscovery etc ...
This enables to enrich the CDI meta-model by dynamically adding beans, parameters, annotations etc ...

In that specific case:

* GraphQL components (using spqr annotations) are identified by a specific CDI stereotype: **@GraphQLcomponent**, 
* GraphQL components are detected by **GraphQLExtension** using the **ProcessBean** CDI event,
* The objective of GraphQLExtension is to prepare the configuration for **SchemaProducer**,
* the GraphQL configuration discovered by GraphQLExtension is then stored in a specific **GraphQLConfig** bean,
* this bean is added to the CDI meta-model using the **AfterBeanDiscovery** CDI event,
* it is then injected in the SchemaProducer which uses it to generate the GraphQL schema in a **@PostConstruct** method,
* the schema generation is eagerly triggered after the CDI application context is ready by observing the **@Initialized** CDI event.

Please note that the CDI *bean-discovery-mode* is set to annotated.

All that CDI magic is isolated in the cdi sub-package.


## JNoSQL integration
I've decided to use [JNoSQL](http://www.jnosql.org/) to implement the repositories. 
As mentioned in [my previous post](https://wordpress.com/view/jefrajames.wordpress.com), JNoSQL will be the basis for the first Jakarta EE specification.
Mixing spqr and JNoSQL in a same project is an illustration of the future of Jakarta EE and MicroProfile.

Using JNoSQL impacts:
* the repository classes where the code is significantly simplified,
* the entity classes which are annotated *à la JPA*.


## Limitations
Compared to the [graphql-java tutorial](https://www.howtographql.com/graphql-java/0-introduction/), 
there are two limitations:

* pagination is not implemented (but should be easy to do),
* error management is not implemented. 
I've not found in spqr the equivalent to what has been described in [Error Handling](https://www.howtographql.com/graphql-java/7-error-handling/)




## How to run the demo
First off, there must be a running MongoDB instance accessible on standard TCP-IP port 27017.

Build the project and deploy the war file on your favorite runtime.

[GraphiQL](https://github.com/graphql/graphiql) is used to access the application and is set as main page (index.html).

### Runtime
The project has been developed and tested with OpenJDK-OpenJ9 1.8.0_192 and Payara-5.184. 
It has also been successfully tested with Glassfish 5.1, OpenLiberty 19.0.0.1, TomEE Plus 8.0.0.M1 and Wildfly 15.0.1.

The URLs to run the application are:

* to run GraphiQL (index.html): http://localhost:[portnum]/spqrdemo/
* to browse the GraphQL schema: http://localhost:[portnum]/spqrdemo/graphql/schema.json
    
Where [portnum] depends on the application server configuration. By default:
* 8080 for **Payara**, **GlassFish**, **WildFly** and **TomEE**,
* 9080 for **OpenLiberty**.


### Creating a user

Starting from an empty database, a user must be created first. 
For instance:

    mutation createUser {
        createUser(name: "Jef", authData: {email: "jef@gmail.com", password: "secret"}) {
        id
        }
    }
    
In this case, a user named jef is created and its id is returned.
The result is as follows:

    {
    "data": {
        "createUser": {
        "id": "5c643d99e1e5fc27542281c2"
        }
    }
    }

As we will see, this id is used as token to authenticate the user and index.html must be changed to take it into account.

To check that the user is properly created, user sign in can be run:

    mutation signinUser {
        signinUser(authData: {email: "jef@gmail.com", password: "secret"}) {
            token
            user {
                name
            }
        }
    }

The user is identified by its email email and password (not very secure in this demo). 
In case of success the user name is returned. 
The result is as follows:

    {
        "data": {
            "signinUser": {
                "token": "5c643d99e1e5fc27542281c2",
                "user": {
                    "name": "Jef"
                }
            }
        }
    }

All created users can also be listed:

    {
        allUsers {
            id
            email
            name
            password
        }
    }


### Creating a link

Creating a link requires to be authenticated. Unfortunately there is no way to do it properly using GraphiQL. 
To simulate an authentication, index.html must be updated by changing the Authorization header value on line 110.
The value must be set according to the id returned from createUser. For instance:

    'Authorization': '5c643d99e1e5fc27542281c2',
    

Then, the main page must be reloaded in the browser and a link can be created:

    mutation createLink {
        createLink(url: "https://www.howtographql.com/graphql-java/0-introduction/", description: "An excellent GraphQLtutorial") {
            id
            postedBy {
                name
            }
        }
    }

In that case, a link is created and the id and the user name who posted it (the one authenticated) are returned. 
The result is as follows:

    {
        "data": {
            "createLink": {
                "id": "5c6442d3e1e5fc27542281c5",
                "postedBy": {
                    "name": "Jef"
                }
            }
        }
    }


Running allLinks query enables to list all created links:

    {
        allLinks {id, description, postedBy {name,email}}
    }

In that case, the id and the description of each link are returned alongside the name and email of the user who created it.


### Voting for a link

It is also possible to vote for a link. 
For instance:

    mutation createVote {
        createVote(linkId: "5c6442d3e1e5fc27542281c5") {
            link {
                description
            }
            user {
                name
            }
        }
    }

In that case, a vote is made by the authenticated user and his user name is returned.

The result is as follows:

    {
        "data": {
            "createVote": {
                "link": {
                    "description": "An excellent GraphQLtutorial"
                },
                "user": {
                    "name": "Jef"
                }
            }
        }
    }



Running allVotes query enable to retrieve all votes. For instance:

    {
        allVotes {
            createdAt
            user {
                name
            }
            link {
                description
            }
        }
    }

In that case, we want to retrieve the time stamp, the user name and the link description for each vote.


## Conclusion and next steps
This project demonstrates how to integrate spqr with CDI and JNoSQL on a very simple use case. It is far from using all GraphQL subtleties (fragment, data loader, subscription ...), but it is a first and encouraging step : it works on all major Java EE 8 and MicroProfile platforms!

The result seems promising but we do think that we can go a step forward in terms of integration and developer experience for real life projects.
That's why we have proposed the MicroProfile GraphQL specification.
We want to achieve something similar to JAX-RS with a high level of integration with Jakarta EE and MicroProfile.
 
And, we are very happy [to have been approved to move to the MicroProfile sandbox for further development](https://microprofile.io/2019/02/12/eclipse-microprofile-2-2-is-now-available/).

## References

The following references have been particularly useful for me:

- https://www.baeldung.com/cdi-portable-extension
- https://www.programcreek.com/java-api-examples/?code=katharsis-project/katharsis-framework/katharsis-framework-master/katharsis-cdi/src/main/java/io/katharsis/cdi/internal/CdiServiceDiscovery.java
- https://gist.github.com/sermojohn/c1044df560dbd86e4b9fae0283c64265
- https://stackoverflow.com/questions/46034801/custom-scalar-in-graphql-java
- https://github.com/leangen/graphql-spqr/blob/master/src/test/java/io/leangen/graphql/ArgumentInjectionTest.java
- https://github.com/leangen/graphql-spqr/blob/master/src/main/java/io/leangen/graphql/util/Scalars.java#L144

 


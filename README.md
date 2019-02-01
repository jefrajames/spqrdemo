# Using GraphQL spqr with CDI

This project demonstrates how to use [spqr](https://github.com/leangen/graphql-spqr) with CDI (Context and Depedency Injection) and targets [MicroProfile](https://microprofile.io/) and [Jakarta EE](https://jakarta.ee/) platforms.

It is based on the [graphql-java Tutorial](https://www.howtographql.com/graphql-java/0-introduction/) from Bohan Tomic.

The use case is the same: a basic HackerNews implementation with three domain objects: Link, User and Vote. 
The use of GraphQL is deliberately basic and the emphasis is on CDI integration.

This is a preliminary work for the [Microprofile GrpahQL workgroup](https://jefrajames.wordpress.com/2019/01/04/when-graphql-meets-microprofile/) that has just started.

## spqr overview

spqr enables to generate a GraphQL schema following a "code first approach".
The schema is not pre-defined but is dynamically generated from annotated classes.
In this example, 3 kinds of classes are annotated:

1. Query: this is the facade for all GraphQL queries. Its methods are annotated with @GraphQLQuery,
1. Mutation: this is the facade for all GraphQL mutations. Its methods are annotated with @GraphQLMutation and @GraphQLArgument,
1. Resolvers: which are in charge of retrieving data for non scalar types.Their methods are annoted with @GraphQLQuery and @GraphQLContext.


## CDI integration

In this demo project, I've tried to maximise spqr integration with CDI. 
First of all, Query, Mutation, AuthData, repositories and resolvers are 
standard CDI beans, so that they can easilly be injected in other components.

The mechanism to detect GraphQL classes and generate the schema is generic and is implemented as a CDI extension.
CDI extension is a standard mechanism to interract with the CDI container through some specific lifecycle events: 
ProcessAnntotatedType, ProcessBean, AfterBeanDiscovery etc ...
This enables to enrich the CDI meta-model by dynamically adding beans, parameters, annotations etc ...

In that specific case:

* GraphQL components (using spqr annotations) are identified using a specific CDI stereotype: @GraphQLcomponent , 
* GraphQLcomponents are detected by GraphQLExtension using the ProcessBean event,
* the objective of GraphQLExtension is to prepare the configuration for SchemaProducer,
* the GraphQL configuration discovered is then stored in a specific GraphQLConfig bean,
* this bean is added to the CDI meta-model using the AfterBeanDiscovery event,
* it is then injected in the SchemaProducer which uses it to generate the GraphQL schema in a @PostConstruct method,
* the schema generation is eagerly triggered after the CDI application context is ready by observing the @Initialized CDI event.

All that CDI magic is isolated in the cdi subpackage.

## Limitations
Compared to the [graphql-java Tutorial](https://www.howtographql.com/graphql-java/0-introduction/), 
there are two limitations:

* pagination is not implemented (but should be easy to do),
* error management is not implemented. 
I've not found in spqr the equivallent to what has been described in the [Error Handling](https://www.howtographql.com/graphql-java/7-error-handling/)

## Runtime
This project has been developped and tested with Payara 5.184, my next step is to run it against OpenLiberty 18.0.4.

## Conclusion
This demo project demonstrates how to integrate spqr with CDI. 
I've tried to develop a fully generic mechanism to generate the GraphQL schema.
It aims to provide some input to the MicroProfile Workgroup to define the programming model.
At this stage, I've the feeling that spqr offers a good foundation but that the programming model can be enhanced to better fit with MicroProfile.

Hope this helps!

## References

The following references have been particularly useful for me:

- https://www.baeldung.com/cdi-portable-extension
- https://www.programcreek.com/java-api-examples/?code=katharsis-project/katharsis-framework/katharsis-framework-master/katharsis-cdi/src/main/java/io/katharsis/cdi/internal/CdiServiceDiscovery.java
- https://gist.github.com/sermojohn/c1044df560dbd86e4b9fae0283c64265
- https://stackoverflow.com/questions/46034801/custom-scalar-in-graphql-java
- https://github.com/leangen/graphql-spqr/blob/master/src/test/java/io/leangen/graphql/ArgumentInjectionTest.java
- https://github.com/leangen/graphql-spqr/blob/master/src/main/java/io/leangen/graphql/util/Scalars.java#L144

 


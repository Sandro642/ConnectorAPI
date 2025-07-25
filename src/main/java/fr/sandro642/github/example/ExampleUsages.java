package fr.sandro642.github.example;

import fr.sandro642.github.ConnectLib;
import fr.sandro642.github.api.ApiFactory;
import fr.sandro642.github.enums.MethodType;
import fr.sandro642.github.enums.ResourceType;
import fr.sandro642.github.enums.VersionType;
import fr.sandro642.github.misc.EnumLoader;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * ExampleUsages is a placeholder class that can be used to demonstrate how to use the ConnectLib library.
 * It can contain example methods or code snippets that show how to interact with the API, handle responses,
 * and utilize the features provided by the ConnectLib library.
 *
 * @author Sandro642
 * @version 1.0
 */

public class ExampleUsages {

    public enum ExampleRoutes implements EnumLoader.RouteImport {
        EXAMPLE_ROUTE("/api/example/route");

        final String route;

        ExampleRoutes(String route) {
            this.route = route;
        }

        @Override
        public String route() {
            return route;
        }
    }

    public void initializeLib() {

        // Optionally, you can specify routes if needed
        ConnectLib.initialize(ResourceType.MAIN_RESOURCES, ExampleRoutes.class);
        // You can also initialize without specifying routes
        ConnectLib.initialize(ResourceType.MAIN_RESOURCES);
    }

    // Add methods here to demonstrate how to use the ConnectLib library
    // For example, you can create methods to make API calls, handle responses, etc.

    // Example method to demonstrate usage
    public void exampleMethodSync() {
        try {
            // This method can be used to demonstrate how to interact with the API
            // For example, making a GET request to the EXAMPLE_ROUTE
            CompletableFuture<ApiFactory> apiFactoryCompletableFuture = ConnectLib.JobGetInfos()
                    .getRoutes(VersionType.V1_BRANCH, MethodType.GET, ExampleRoutes.EXAMPLE_ROUTE)
                    .getResponse();

            ApiFactory response = apiFactoryCompletableFuture.get(5, TimeUnit.SECONDS);

            System.out.println(response.display());
            System.out.println("Response Code: " + response.getData("code"));
            System.out.println("Response Message: " + response.getData("message"));
            System.out.println("Response Data: " + response.getSpecData("data", "exampleKey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Example method to demonstrate asynchronous usage
    public void exampleMethodAsync() {
        try {
            // This method can be used to demonstrate how to interact with the API asynchronously

            CompletableFuture<ApiFactory> apiFactoryCompletableFuture = ConnectLib.JobGetInfos()
                    .getRoutes(VersionType.V1_BRANCH, MethodType.GET, ExampleRoutes.EXAMPLE_ROUTE)
                    .getResponse();

            ApiFactory response = apiFactoryCompletableFuture.get(5, TimeUnit.SECONDS);

            System.out.println(response.display());
            System.out.println("Response Code: " + response.getData("code"));
            System.out.println("Response Message: " + response.getData("message"));
            System.out.println("Response Data: " + response.getSpecData("data", "exampleKey"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Example to use all methods in JobGetInfos
    public void exampleJobGetInfos() {
        Map<String, ?> body = Map.of();
        Map<String,?> params = Map.of();

        ConnectLib.JobGetInfos().getRoutes(VersionType.V1_BRANCH, MethodType.GET, ExampleRoutes.EXAMPLE_ROUTE, body, params);
        ConnectLib.JobGetInfos().getRoutes(VersionType.V1_BRANCH, MethodType.POST, ExampleRoutes.EXAMPLE_ROUTE, body);
        ConnectLib.JobGetInfos().getRoutes(VersionType.V1_BRANCH, MethodType.PUT, ExampleRoutes.EXAMPLE_ROUTE, null, params);
        ConnectLib.JobGetInfos().getRoutes(VersionType.V1_BRANCH, MethodType.PATCH, ExampleRoutes.EXAMPLE_ROUTE);
        ConnectLib.JobGetInfos().getRoutes(MethodType.GET, ExampleRoutes.EXAMPLE_ROUTE, body, params);
        ConnectLib.JobGetInfos().getRoutes(MethodType.POST, ExampleRoutes.EXAMPLE_ROUTE, body);
        ConnectLib.JobGetInfos().getRoutes(MethodType.DELETE, ExampleRoutes.EXAMPLE_ROUTE);
    }

}

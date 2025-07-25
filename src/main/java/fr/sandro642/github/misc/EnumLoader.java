package fr.sandro642.github.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * ConvertEnum is a utility class for converting enums that implement the RouteImport interface into a map of routes.
 * This class provides a method to convert an enum class into a map where the keys are the enum constants and the values are their corresponding routes.
 * @author Sandro642
 * @version 1.0
 * @since 1.0
 */

public class EnumLoader {

    /**
     * RouteImport is an interface that should be implemented by enums that provide a route.
     * Enums implementing this interface must define the route() method to return their associated route as a String.
     */
    public interface RouteImport {
        String route();
    }

    /**
     * Converts an enum class that implements RouteImport into a map.
     * The keys of the map are the enum constants, and the values are their corresponding routes.
     *
     * @param enumClass the class of the enum to convert
     * @return a map containing the enum constants as keys and their routes as values
     */
    public static Map<Enum<?>, String> convertRouteImport(Class<? extends Enum<?>> enumClass) {
        Enum<?>[] enums = enumClass.getEnumConstants();
        HashMap<Enum<?>, String> map = new HashMap<>();
        for (Enum<?> e : enums) {
            if (e instanceof RouteImport routeImport) {
                map.put((Enum<?>) routeImport, routeImport.route());
            }
        }
        return map;
    }
}

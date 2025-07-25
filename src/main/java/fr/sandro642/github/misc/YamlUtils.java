package fr.sandro642.github.misc;

import fr.sandro642.github.ConnectLib;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * YamlUtils is a utility class for handling YAML files in the ConnectLib library.
 * It provides methods to read configuration values such as the base URL, logging settings, and routes from a YAML file.
 *
 * @author Sandro642
 * @version 1.0
 */

public class YamlUtils {

	/**
	 * Gets the base URL from the YAML configuration file.
	 * * This method reads the `infos.yml` file located in the directory specified by the
	 * @return the base URL as a String, or null if an error occurs.
	 */
	public String getURL() {

		String yamlFilePath = ConnectLib.StoreAndRetrieve().store.get(ConnectLib.StoreAndRetrieve().FILE_LOCATION_KEY)
				+ "/infos.yml";

		try (InputStream inputStream = Files.newInputStream(Paths.get(yamlFilePath))) {
			Yaml yaml = new Yaml();
			Map<String, Object> yamlData = yaml.load(inputStream);
			return (String) yamlData.get("urlPath");
		} catch (Exception ex) {
			ConnectLib.Logger().ERROR("Error while reading urlPath from infos.yml: " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Checks if logging is enabled in the YAML configuration file.
	 * This method reads the `infos.yml` file and retrieves the `enableLogs` setting.
	 *
	 * @return true if logging is enabled, false if disabled, or null if an error occurs.
	 */
	public Boolean isLogEnabled() {
		String yamlFilePath = ConnectLib.StoreAndRetrieve().store.get(ConnectLib.StoreAndRetrieve().FILE_LOCATION_KEY)
				+ "/infos.yml";

		try (InputStream inputStream = Files.newInputStream(Paths.get(yamlFilePath))) {
			Yaml yaml = new Yaml();
			Map<String, Object> yamlData = yaml.load(inputStream);
			return (Boolean) yamlData.get("enableLogs");
		} catch (Exception ex) {
			ConnectLib.Logger().ERROR("Error while reading enableLogs from infos.yml: " + ex.getMessage());
			return null;
		}
	}

	/**
	 * Retrieves the routes defined in the YAML configuration file.
	 * This method reads the `infos.yml` file and returns a map of routes.
	 *
	 * @return a map of route names to their corresponding paths, or null if an error occurs.
	 */
	public Map<String, String> getRoutes() {

		String yamlFilePath = ConnectLib.StoreAndRetrieve().store.get(ConnectLib.StoreAndRetrieve().FILE_LOCATION_KEY)
				+ "/infos.yml";

		try (InputStream inputStream = Files.newInputStream(Paths.get(yamlFilePath))) {
			Yaml yaml = new Yaml();
			Map<String, Object> yamlData = yaml.load(inputStream);

			return (Map<String, String>) yamlData.get("routes");
		} catch (Exception ex) {
			ConnectLib.Logger().ERROR("Error while reading routes from infos.yml: " + ex.getMessage());
			return null;
		}
	}

/**
	 * Generates a template `infos.yml` file if it does not already exist.
	 * If the file exists, it updates the routes section with the provided routes.
	 *
	 * @param routes a map of route names to their corresponding paths
	 */
	public void generateTemplateIfNotExists(Map<Enum<?>, String> routes) {
		String basePath = ConnectLib.HookManager().BASE_PATH();

		File directory = new File(basePath);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		File file = new File(basePath, "infos.yml");

		if (file.exists()) {
			try {
				List<String> lines = new ArrayList<>();
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					String line;
					while ((line = reader.readLine()) != null) {
						lines.add(line);
					}
				}

				int routesStartIndex = -1;
				int routesEndIndex = -1;

				for (int i = 0; i < lines.size(); i++) {
					String line = lines.get(i).trim();
					if (line.equals("routes:")) {
						routesStartIndex = i;
					} else if (routesStartIndex != -1 && line.matches("^[a-zA-Z_][a-zA-Z0-9_]*:.*")) {

						if (!line.startsWith("#") && !line.matches("^\\s*[a-zA-Z_][a-zA-Z0-9_]*:\\s*\"/.*")) {
							routesEndIndex = i;
							break;
						}
					}
				}

				// If routesEndIndex is not found, it means we reached the end of the file
				if (routesStartIndex != -1 && routesEndIndex == -1) {
					routesEndIndex = lines.size();
				}

				if (routesStartIndex != -1) {

					List<String> newLines = new ArrayList<>();
					newLines.addAll(lines.subList(0, routesStartIndex + 1));

					newLines.add("  #info: \"/info/version\"");
					newLines.add("  #ping: \"/ping\"");
					newLines.add("  #status: \"/status\"");
					newLines.add("");


					for (Map.Entry<Enum<?>, String> entry : routes.entrySet()) {
						newLines.add("  " + entry.getKey().name().toLowerCase() + ": \"" + entry.getValue() + "\"");
					}

					if (routesEndIndex < lines.size()) {
						newLines.add("");
						newLines.addAll(lines.subList(routesEndIndex, lines.size()));
					}

					try (FileWriter writer = new FileWriter(file)) {
						for (String line : newLines) {
							writer.write(line + "\n");
						}
					}
				}

			} catch (IOException e) {
				ConnectLib.Logger().ERROR("Error while updating infos.yml" + e.getMessage());
			}
		} else {

			StringBuilder template = new StringBuilder(
					"# properties ConnectLib By Sandro642\n\n" +
							"urlPath: \"http://localhost:8080/api\"\n\n" +
							"routes:\n" +
							"  #info: \"/info/version\"\n" +
							"  #ping: \"/ping\"\n" +
							"  #status: \"/status\"\n\n");

			for (Map.Entry<Enum<?>, String> entry : routes.entrySet()) {
				template.append("  ")
						.append(entry.getKey().name().toLowerCase())
						.append(": \"")
						.append(entry.getValue())
						.append("\"\n");
			}

			template.append("\n\n# Logs\n")
					.append("enableLogs: true\n");

			try (FileWriter writer = new FileWriter(file)) {
				writer.write(template.toString());
			} catch (IOException e) {
				ConnectLib.Logger().ERROR("Error while creating infos.yml: " + e.getMessage());
			}
		}
	}
}

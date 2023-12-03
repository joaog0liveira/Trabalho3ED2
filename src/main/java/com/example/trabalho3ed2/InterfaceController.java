package com.example.trabalho3ed2;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class InterfaceController {

    @FXML
    private ComboBox<String> startCityComboBox;

    @FXML
    private ComboBox<String> endCityComboBox;

    @FXML
    private Label resultLabel;

    private Map<String, Map<String, Integer>> graph;

    public void initialize() {
        // Inicializar o grafo ao iniciar a aplicação
        graph = readGraphFromFile("C:\\Users\\Breno Reis\\Documents\\Angular\\ProjetoAngular\\Trabalho3ED2\\src\\main\\java\\com\\example\\trabalho3ed2\\cidades.txt");

        // Preencher ComboBox com as cidades
        startCityComboBox.getItems().addAll(graph.keySet());
        endCityComboBox.getItems().addAll(graph.keySet());
    }

    @FXML
    private void calculateShortestDistance() {
        String startCity = startCityComboBox.getValue();
        String endCity = endCityComboBox.getValue();

        if (startCity != null && endCity != null) {
            int shortestDistance = dijkstra(graph, startCity, endCity);
            resultLabel.setText(String.valueOf(shortestDistance));
        } else {
            resultLabel.setText("Selecione as cidades.");
        }
    }

    public static Map<String, Map<String, Integer>> readGraphFromFile(String filename) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String source = parts[0].trim();
                String destination = parts[1].trim();
                int weight = Integer.parseInt(parts[2].trim());

                graph.computeIfAbsent(source, k -> new HashMap<>());
                graph.computeIfAbsent(destination, k -> new HashMap<>());

                graph.get(source).put(destination, weight);
                graph.get(destination).put(source, weight);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            // Você pode optar por lançar uma exceção personalizada ou tratar de outra forma.
        }

        return graph;
    }

    public static int dijkstra(Map<String, Map<String, Integer>> graph, String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        for (String node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);

        Set<String> unvisitedNodes = new HashSet<>(graph.keySet());

        while (!unvisitedNodes.isEmpty()) {
            String currentNode = getMinDistanceNode(distances, unvisitedNodes);
            unvisitedNodes.remove(currentNode);

            for (Map.Entry<String, Integer> neighborEntry : graph.get(currentNode).entrySet()) {
                String neighbor = neighborEntry.getKey();
                int weight = neighborEntry.getValue();

                int potentialRoute = distances.get(currentNode) + weight;
                if (potentialRoute < distances.get(neighbor)) {
                    distances.put(neighbor, potentialRoute);
                }
            }
        }

        return distances.get(end);
    }

    private static String getMinDistanceNode(Map<String, Integer> distances, Set<String> unvisitedNodes) {
        String minDistanceNode = null;
        int minDistance = Integer.MAX_VALUE;

        for (String node : unvisitedNodes) {
            int distance = distances.get(node);
            if (distance < minDistance) {
                minDistance = distance;
                minDistanceNode = node;
            }
        }

        return minDistanceNode;
    }
}

package algorithms;

import LabelPropagationUtilities.RandomColorChooser;
import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.awt.*;
import java.util.*;

public class LabelPropagationRaghavan implements DynamicAlgorithm {

    private Graph graph;
    private int iteration;
    private UpdateMode updateMode = UpdateMode.SYNCHRONOUS_MODE;
    private int milliseconds = 300;
    private final int NOT_UPDATED = -1;

    @Override
    public void init(Graph graph) {
        this.graph = graph;
        initializeLabels();
        iteration = 1;
    }

    /*Initializes each node with a unique label in the form of an integer increasing from 0 to n.
    Furthermore, each node is given a unique color.*/
    private void initializeLabels() {
        int x = 0;
        for(Node n : graph) {
            n.setAttribute("startLabel", x++);
            n.setAttribute("updatedLabel", NOT_UPDATED);
            Color color = new Color(RandomColorChooser.randomRedValue(), RandomColorChooser.randomGreenValue(), RandomColorChooser.randomBlueValue());
            n.setAttribute("ui.color", color);
            n.setAttribute("startColor", color);
            n.setAttribute("updatedColor", null);
        }
    }

    @Override
    public void compute() {

        int count;
        int numberOfNodesInGraph = graph.getNodeCount();

        do {
            count = 0;

            Stack<Node> nodeStack = createNodeStackInRandomOrder();

            while(!nodeStack.isEmpty()) {
                Node actualNode = nodeStack.pop();

                try {
                    Thread.sleep(milliseconds);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                int maxLabel = getLabelThatMaximumNumberOfNeighbourHave(actualNode);
                Node maxLabelNode = graph.getNode(Integer.toString(maxLabel));
                actualNode.setAttribute("updatedLabel", maxLabel);
                actualNode.setAttribute("ui.color", maxLabelNode.getAttribute("startColor"));
                actualNode.setAttribute("updatedColor", maxLabelNode.getAttribute("startColor"));
            }

            for (Node n : graph) {
                int updatedLabel = (int) n.getAttribute("updatedLabel");
                n.setAttribute("updatedLabel", NOT_UPDATED);
                n.setAttribute("startLabel", updatedLabel);
                Color color = (Color) n.getAttribute("updatedColor");
                n.setAttribute("startColor", color);
                n.setAttribute("updatedColor", null);
            }

            for (Node n : graph) {
                ArrayList<Node> neighbourList = getNeighbourList(n);
                ArrayList<Integer> neighbourLabelList = getNeighbourLabelList(neighbourList, UpdateMode.SYNCHRONOUS_MODE);
                ArrayList<Integer> listWithMaximumLabels = createListWithMaximumLabels(neighbourLabelList);
                if (listWithMaximumLabels.contains((int) n.getAttribute("startLabel"))) {
                    count++;
                }
            }
            System.out.println("Iterationen: " + iteration);
            iteration++;
        } while (count < numberOfNodesInGraph);
    }


    @Override
    public void terminate() {
    }

    private Stack<Node> createNodeStackInRandomOrder() {
        Stack<Node> nodeStack = new Stack<>();

        for (int i = 0; i < graph.getNodeCount() ; i++) {
            nodeStack.push(graph.getNode(i));
        }

        Collections.shuffle(nodeStack);
        return nodeStack;
    }

    private ArrayList<Node> getNeighbourList(Node actualNode) {
        Iterator iterator = getNeighbourIterator(actualNode);
        ArrayList<Node> neighbour = new ArrayList<>();

        while (iterator.hasNext()) {
            neighbour.add((Node) iterator.next());
        }

        return neighbour;
    }

    private Iterator getNeighbourIterator(Node actualNode) {
        Iterator iterator = actualNode.neighborNodes().iterator();
        return iterator;
    }

    private ArrayList<Integer> getNeighbourLabelList(ArrayList<Node> neighbourList, UpdateMode updateMode) {
        ArrayList<Integer> neighbourLabelList = new ArrayList<>();

        for (Node neighbour : neighbourList) {
            int updatedLabel = (int) neighbour.getAttribute("updatedLabel");

            if (updateMode == UpdateMode.ASYNCHRONOUS_MODE) {
                if (updatedLabel == NOT_UPDATED) {
                    neighbourLabelList.add((Integer) neighbour.getAttribute("startLabel"));
                } else {
                    neighbourLabelList.add(updatedLabel);
                }
            } else if (updateMode == UpdateMode.SYNCHRONOUS_MODE){
                neighbourLabelList.add((Integer) neighbour.getAttribute("startLabel"));
            }
        }
        return neighbourLabelList;
    }

    private ArrayList<Integer> createListWithMaximumLabels(ArrayList<Integer> neighbourLabelList) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = 0; i < neighbourLabelList.size(); i++) {
            if(map.containsKey(neighbourLabelList.get(i))) {
                map.put(neighbourLabelList.get(i), map.get(neighbourLabelList.get(i)) + 1);
            } else {
                map.put(neighbourLabelList.get(i), 1);
            }
            if (map.get(neighbourLabelList.get(i)) > max) {
                max = map.get(neighbourLabelList.get(i));
            }
        }

        ArrayList<Integer> maxLabels = new ArrayList<>();
        for (int key : map.keySet()) {
            if (map.get(key) == max) {
                maxLabels.add(key);
            }
        }
        return maxLabels;
    }

    private int getLabelThatMaximumNumberOfNeighbourHave(Node actualNode) {

        ArrayList<Node> neighbourList = getNeighbourList(actualNode);
        ArrayList<Integer> neighbourLabelList = getNeighbourLabelList(neighbourList, updateMode);
        ArrayList<Integer> listWithMaximumLabels = createListWithMaximumLabels(neighbourLabelList);

        Collections.shuffle(listWithMaximumLabels);

        return listWithMaximumLabels.get(0);
    }
}

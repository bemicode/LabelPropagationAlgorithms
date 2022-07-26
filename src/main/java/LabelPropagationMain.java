import algorithms.LabelPropagationRaghavan;
import gui.LabelPropagationView;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
//import org.graphstream.ui.graphicGraph.stylesheet.Color;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class LabelPropagationMain {



    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        LabelPropagationView labelGui = new LabelPropagationView();
        Graph graph = new SingleGraph("Test Graph");
        graph.setAttribute("ui.stylesheet", "node {size-mode: dyn-size; size: 20px; fill-mode: dyn-plain;}");

        // Graph with two distinct communities
        graph.addNode("0");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");
        graph.addNode("5");
        graph.addNode("6");
        graph.addNode("7");
        graph.addEdge("01", "0", "1");
        graph.addEdge("02", "0", "2");
        graph.addEdge("03", "0", "3");
        graph.addEdge("12", "1", "2");
        graph.addEdge("13", "1", "3");
        graph.addEdge("23", "2", "3");
        graph.addEdge("34", "3", "4");
        graph.addEdge("45", "4", "5");
        graph.addEdge("46", "4", "6");
        graph.addEdge("47", "4", "7");
        graph.addEdge("56", "5", "6");
        graph.addEdge("57", "5", "7");
        graph.addEdge("67", "6", "7");

        // bipartite graph
        // Certainly not terminated in synchronous mode
//        graph.addNode("0");
//        graph.addNode("1");
//        graph.addNode("2");
//        graph.addNode("3");
//        graph.addNode("4");
//        graph.addNode("5");
//        graph.addNode("6");
//        graph.addNode("7");
//        graph.addEdge("01", "0", "1");
//        graph.addEdge("05", "0", "5");
//        graph.addEdge("12", "1", "2");
//        graph.addEdge("27", "2", "7");
//        graph.addEdge("34", "3", "4");
//        graph.addEdge("47", "4", "7");
//        graph.addEdge("56", "5", "6");

        // star graph
        // Certainly not terminated in synchronous mode
//        graph.addNode("0");
//        graph.addNode("1");
//        graph.addNode("2");
//        graph.addNode("3");
//        graph.addNode("4");
//        graph.addEdge("04", "0", "4");
//        graph.addEdge("14", "1", "4");
//        graph.addEdge("24", "2", "4");
//        graph.addEdge("34", "3", "4");

        // Two connected nodes
        // Certainly not terminated in synchronous mode
//        graph.addNode("0");
//        graph.addNode("1");
//        graph.addEdge("01", "0", "1");

        //Graph with two overlapping communities
//        graph.addNode("0");
//        graph.addNode("1");
//        graph.addNode("2");
//        graph.addNode("3");
//        graph.addNode("4");
//        graph.addNode("5");
//        graph.addNode("6");
//        graph.addEdge("01", "0", "1");
//        graph.addEdge("02", "0", "2");
//        graph.addEdge("12", "1", "2");
//        graph.addEdge("13", "1", "3");
//        graph.addEdge("23", "2", "3");
//        graph.addEdge("34", "3", "4");
//        graph.addEdge("35", "3", "5");
//        graph.addEdge("45", "4", "5");
//        graph.addEdge("46", "4", "6");
//        graph.addEdge("56", "5", "6");

        graph.display();

        LabelPropagationRaghavan algorithm = new LabelPropagationRaghavan();
        algorithm.init(graph);
        algorithm.compute();
        System.out.println("Finished");
    }
}

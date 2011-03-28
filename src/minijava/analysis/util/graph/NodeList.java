package minijava.analysis.util.graph;
public class NodeList<N> {
  public Node<N> head;
  public NodeList<N> tail;
  public NodeList(Node<N> h, NodeList<N> t) {head=h; tail=t;}
}




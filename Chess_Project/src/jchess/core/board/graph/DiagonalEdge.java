package jchess.core.board.graph;

/**
 * 
 * @author Maurice
 *
 */
public class DiagonalEdge extends DirectedGraphEdge {

	public DiagonalEdge(GraphNode node1, GraphNode node2, EdgeDirection dir) {
		super(node1, node2, dir);
	}

	@Override
	public EdgeType getEdgeType() {
		return EdgeType.DIAGONAL;
	}
}
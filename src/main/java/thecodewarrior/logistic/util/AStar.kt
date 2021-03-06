package thecodewarrior.logistic.util

import org.jgrapht.graph.SimpleWeightedGraph
import java.util.*

/**
 * Created by TheCodeWarrior
 */

class AStarNode<T>(val value: T, val heuristic: Double, var travelTime: Double) : Comparable<AStarNode<T>> {
    var cameFrom: AStarNode<T>? = null

    override fun compareTo(other: AStarNode<T>): Int {
        val v = (this.heuristic + this.travelTime) - (other.heuristic + other.travelTime)
        if(v < 0)
            return -1
        else if(v > 0)
            return 1
        return 0
    }

}

object AStar {

    fun <T, E> pathfind(start: T, goal: T, graph: SimpleWeightedGraph<T, E>, heuristic: (T) -> Double, weightAdjust: (Double) -> Double): List<T> {
        val visited = mutableSetOf<T>()
        val queueMap = mutableMapOf<T, AStarNode<T>>()
        val queue = TreeSet<AStarNode<T>>()

        val n = AStarNode(start, heuristic(start), 0.0)
        queueMap.put(start, n)
        queue.add(n)

        if(start == goal)
            throw IllegalArgumentException("`start` and `goal` have to be different")

        while(queue.isNotEmpty()) {
            val current = queue.pollFirst()
            if(current.value == goal)
                return getPath(current)
            queueMap.remove(current.value)
            visited.add(current.value)


            graph.edgesOf(current.value).forEach { edge ->
                val src = graph.getEdgeSource(edge)
                val tgt = graph.getEdgeTarget(edge)

                val neighbor = if(src == current.value) tgt else src

                if(neighbor in visited)
                    return@forEach

                val weight = weightAdjust(graph.getEdgeWeight(edge))

                if(neighbor in queueMap) {
                    val n = queueMap.get(neighbor)!!
                    val timeThroughUs = current.travelTime + weight
                    if(timeThroughUs < n.travelTime) {
                        n.travelTime = timeThroughUs
                        n.cameFrom = current
                    }
                } else {
                    val n = AStarNode(neighbor, heuristic(neighbor), current.travelTime + weight)
                    n.cameFrom = current
                    queueMap.put(neighbor, n)
                    queue.add(n)
                }
            }
        }

        return listOf()
    }

    private fun <T> getPath(node: AStarNode<T>): List<T> {
        val list = mutableListOf<T>()

        var current: AStarNode<T>? = node
        while(current != null) {
            list.add(current.value)
            current = current.cameFrom
        }

        return list
    }

}


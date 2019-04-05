package kz.ildar.sandbox.acm.bridges

class BridgesCalculator(
    private val bridgeHeight: Int = 60,
    /**
     * terrain profile description in points X,Y
     */
    private val inputGroundProfile: List<Pair<Int, Int>> = ArrayList<Pair<Int, Int>>().apply {
        add(Pair(0, 0))
        add(Pair(20, 20))
        add(Pair(30, 10))
        add(Pair(50, 30))
        add(Pair(70, 20))
    },
    private val pillarCostCoef: Int = 18,
    private val bridgeCostCoef: Int = 2
) {
    init {
        if (bridgeHeight <= 0) throw IllegalArgumentException("bridgeHeight <= 0")
        if (pillarCostCoef <= 0) throw IllegalArgumentException("pillarCostCoef <= 0")
        if (bridgeCostCoef <= 0) throw IllegalArgumentException("bridgeHeight <= 0")
        if (inputGroundProfile.size < 2) throw IllegalArgumentException("inputGroundProfile size should be 2 or more")
    }

    /**
     * get array of d for chosen points
     * [points] - list of chosen points to build pillars: 0..n-2
     * @return array of d(i)
     */
    fun choosePoints(vararg points: Int): Array<Int> {
        System.out.print(points.contentToString() + " -> ")
        if (points.size > inputGroundProfile.size - 2) throw IllegalArgumentException("invalid point count, should be 0 <= count < inputGroundProfile.size - 2")
        points.forEachIndexed { i, point ->
            if (point <= 1 || point > inputGroundProfile.size - 1) throw IllegalArgumentException("invalid point $point at $i, should be 1 < point < inputGroundProfile.size - 1")
        }
        val result = getDistances(0L, *points)
        System.out.println(result.contentToString())
        return result
    }

    private fun getDistances(
        currentIndex: Long,
        vararg points: Int
    ): Array<Int> {
        return when {
            points.isEmpty() -> {
                arrayOf(inputGroundProfile[inputGroundProfile.size - 1].first - inputGroundProfile[currentIndex.toInt()].first)
            }
            else -> {
                arrayOf(
                    inputGroundProfile[points[0] - 1].first - inputGroundProfile[currentIndex.toInt()].first,
                    *getDistances((points[0] - 1).toLong(), *points.drop(1).toIntArray())
                )
            }
        }
    }

    fun getPillars(vararg points: Int): List<Int> {
        val pillars = arrayListOf(buildPillar(1))
        points.forEach { point ->
            pillars += buildPillar(point)
        }
        pillars += buildPillar(inputGroundProfile.size)
        return pillars
    }

    /**
     * [index] 1 .. n
     * @return pillar height at given terrain point
     */
    fun buildPillar(index: Int): Int {
        if (index < 1 || index > inputGroundProfile.size) throw IllegalArgumentException("invalid index, should be 0 < index < inputGroundProfile.size")
        return bridgeHeight - inputGroundProfile[index - 1].second
    }

    /**
     * calc cost of bridge for chosen points
     */
    fun calcBridge(vararg points: Int): Int {
        val heights = getPillars(*points)
        val distances = getDistances(0, *points)
        var cost = 0
        distances.forEach {
            cost += it * it
        }
        cost = bridgeCostCoef * cost + pillarCostCoef * heights.sum()
        return cost
    }

    /**
     * calc all possible placements of pillars
     */
    fun possiblePlacements(): Set<Array<Int>> {
        val listOfIndexes = mutableListOf<Int>()
        inputGroundProfile.forEachIndexed { i, _ ->
            if (i == 0 || i == inputGroundProfile.size - 1) {
                listOfIndexes.add(i)
            }
        }

        return permute(*listOfIndexes.toIntArray())
    }

    fun permute(vararg points: Int): Set<Array<Int>> {
        if (points.isEmpty()) return emptySet()
        val places = listOf(0 until points.size)

        return emptySet()
    }
}

fun main() {

}
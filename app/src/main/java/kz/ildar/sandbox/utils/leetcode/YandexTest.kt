package kz.ildar.sandbox.utils.leetcode

/**
 * Дано квадратное поле, где каждая клетка либо проходима, либо непроходима (стена).
 * Даны координаты двух клеток внутри поля - входа и выхода. Необходимо определить,
 * существует ли путь от входа до выхода, не выходящий за пределы поля и не проходящий через стены.
 * Ходить можно по горизонтали либо по вертикали.
 *
 * Входные данные (0 - проход, 1 - стена):
 *    {
 *        {0, 0, 1, 0},
 *        {1, 0, 0, 0},
 *        {0, 0, 1, 1},
 *        {1, 0, 0, 0}
 *    }
 * Координаты входа: 0, 0
 * Координаты выхода: 3, 3
 *
 * 30.04.2020
 * https://code.yandex-team.ru/
 **/

fun main() {
    val mazeMap: Array<out IntArray> = arrayOf(
        intArrayOf(0, 1, 1, 0),
        intArrayOf(1, 0, 0, 0),
        intArrayOf(0, 0, 1, 1),
        intArrayOf(1, 0, 0, 0)
    )
    hasSolution(mazeMap, 0, 0, 3, 3)//start
    hasSolution(mazeMap, 2, 1, 3, 3)//middle
    hasSolution(mazeMap, 0, 1, 3, 3)//from wall
    hasSolution(mazeMap, 3, 0, 3, 3)//from edge
}

fun hasSolution(
    mazeMap: Array<out IntArray>,
    entranceX: Int,
    entranceY: Int,
    exitX: Int,
    exitY: Int
): Boolean {
    callCount = 0
    visited.clear()
    val result = rec(mazeMap, entranceX, entranceY, exitX, exitY)
    println("result: $result callCount: $callCount")
    return result
}

private val visited = mutableSetOf<Pair<Int, Int>>()
private var callCount = 0

private fun rec(
    mazeMap: Array<out IntArray>,
    x: Int,
    y: Int,
    exitX: Int,
    exitY: Int
): Boolean {
    callCount++
    if (y >= mazeMap.size || y < 0) return false//y out
    if (x >= mazeMap[y].size || x < 0) return false//x out
    if (mazeMap[exitY][exitX] == 1) return false//exit is the wall
    if (mazeMap[y][x] == 1) return false//we hit the wall
    if (x == exitX && y == exitY) return true//success!
    if (visited.contains(Pair(x, y))) return false//we've been here
    visited.add(Pair(x, y))
    val right = rec(mazeMap, x + 1, y, exitX, exitY)
    if (right) return true
    val down = rec(mazeMap, x, y + 1, exitX, exitY)
    if (down) return true
    val left = rec(mazeMap, x - 1, y, exitX, exitY)
    if (left) return true
    val up = rec(mazeMap, x, y - 1, exitX, exitY)
    if (up) return true
    return false// right || down || left || up
}

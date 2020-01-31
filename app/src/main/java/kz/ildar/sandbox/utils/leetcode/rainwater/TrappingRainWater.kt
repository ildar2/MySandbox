package kz.ildar.sandbox.utils.leetcode.rainwater

fun calcTrappedWaterSlow(height: IntArray): Int {
    var waterAmount = 0
    var currentHeight = 0
    val openReservoirs = mutableMapOf<Int, Int>()
    for (i in height.indices) {
        //	если уровень повысился, мы смотрим, есть ли открытые потенциальные
        //	сосуды ниже текущей высоты и закрываем их
        if (height[i] > currentHeight) {
            for (j in currentHeight until height[i]) {
                waterAmount += openReservoirs[j] ?: 0
                openReservoirs.remove(j)
            }
        }

        //	если уровень понизился, появляется список потенциальных
        //	сосудов, пропорционально высоте опускания
        if (height[i] < currentHeight) {
            for (j in height[i] until currentHeight) {
                openReservoirs[j] = 0
            }
        }
        //	все открытые сосуды увеличиваются на 1
        for (res in openReservoirs) {
            openReservoirs[res.key] = res.value + 1
        }
        currentHeight = height[i]
    }
    return waterAmount
}

fun calcTrappedWater(height: IntArray): Int {
    if (height.isEmpty()) return 0
    val size = height.size

    // Получаем максимальную высоту, на которую может подняться
    // вода в каждой точке при проходе слева (у правого края будут
    // неправильные данные, поэтому еще проходим справа)
    val leftMaxHeights = IntArray(size)
    leftMaxHeights[0] = height[0]
    for (i in 1 until size) {
        leftMaxHeights[i] = kotlin.math.max(height[i], leftMaxHeights[i - 1])
    }
    // Получаем максимальную высоту, на которую может подняться
    // вода в каждой точке при проходе справа
    val rightMaxHeights = IntArray(size)
    rightMaxHeights[size - 1] = height[size - 1]
    for (i in size - 2 downTo 0) {
        rightMaxHeights[i] = kotlin.math.max(height[i], rightMaxHeights[i + 1])
    }

    // Вычисляем объём воды, на который может подняться вода в каждой точке
    var waterAmount = 0
    for (i in 0 until size) {
        waterAmount += kotlin.math.min(rightMaxHeights[i], leftMaxHeights[i]) - height[i]
    }
    return waterAmount
}
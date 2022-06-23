package kz.ildar.sandbox.utils.leetcode.interview

import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * Дан массив [array] длиной N
 * Нужно вернуть массив, где каждое значение с индексом i -
 * это сумма всех остальных значений массива [array], кроме значения на i
 * array:  [2, 3, 1]
 * output: [4, 3, 5]
 */
fun sumExceptI(
    array: IntArray
): IntArray {
    return InterviewJava.sumOptimal(array)
//    return sumOptimal(array)
}

fun sumExceptIKotlin(
    array: IntArray
): IntArray = array.let {
    val sum = array.sum()
    array.map { sum - it }.toIntArray()
}

//private fun compareInternal(first: String, second: String) : Int {
//    if (first.size != second.size) return NOT_ANAGRAM
//    val chars1 = HashMap<Char, Int>()
//    val chars2 = HashMap<Char, Int>()
//
//    for (i in 0 until first.length) {
//        chars1[first.toCharArray(i)]++
//        chars2[second[i]]++
//        second[i]
//    }
//
//
//}

/**
 * explanation: 4 = 3 + 1, 3 = 2 + 1, 5 = 2 + 3
 */
private fun sumOptimal(array: IntArray): IntArray {
    val result = IntArray(array.size)

    val sum = array.sum()

    for (index in array.indices) {
        result[index] = sum - array[index]
    }
    return result
}

private fun sumNotOptimal(array: IntArray): IntArray {
    val result = IntArray(array.size)

    for (i in array.indices) {
        var sum = 0
        for (j in array.indices) {
            if (i != j) {
                sum += array[j]
            }
        }
        result[i] = sum
    }

    return result
}

fun main() {
    checkMultithreading()
}

@Volatile
var counter = 0

fun checkMultithreading() {
    for (i in 1..150) {
        thread {
            counter++
        }
    }
    println(counter)
}

val lock = Any()

fun checkMultithreading1() {
    for (i in 1..150) {

        thread {
            synchronized(lock) {
                counter++
            }
        }
    }
    Thread.sleep(20)
    println(counter)
}

class Point(val x: Int, val y: Int)

fun checking1() {
    val point1 = Point(1, 2)
    val point2 = Point(1, 2)
    println(point1 == point2)
    println(point1 === point2)
}

fun checking() {
    listOf("three", "two", "one").forEach {
        if (it == "one") {
            return
        } else {
            println(it)
        }
    }
    println("go!")
}

/*
как устроен проект, есть ли какая-то структура
как обрабатываются ошибки

методы класса Object
inline fun

git
корутины
MVP vs MVVM
Сериализовать LocFromServer
recyclerView лагает
SOLID
services, workManager
clean arch
задача

опыт с нотификациями?
особенности пуш-нотификаций
жизненный цикл View, Fragment, Activity
Koin vs Dagger vs Hilt
вас критикуют, ваши действия?




    public static int[] sumExceptI(int[] array) {

    }

    public static void main(String []args){
        int a = 1000_000_000;
        int b = 2000_000_000;
        System.out.println(a+b);
    }

	public static void main(String[] args) {
        int a1 = 1000, a2 = 1000;
        System.out.println(a1 == a2);//=>true
        Integer b1 = 1000, b2 = 1000;
        System.out.println(b1 == b2);//=>false
        Integer c1 = 100, c2 = 100;
        System.out.println(c1 == c2);//=>true
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.user_name_text).text = myDataset[position].name

        if (myDataset[position].image != null) {
            holder.itemView.findViewById<ImageView>(R.id.user_avatar_image)
                    .setImageResource(myDataset[position].image!!)
        }

        holder.itemView.setOnClickListener {
            val bandle = bundleOf("user" to myDataset[position])

            holder.itemView.findNavController().navigate(
                    R.id.action_leaderboard_to_userProfile,
                bandle)
        }
    }

 */

// 1st array = ["abc", "def", "ghik"],
//       2nd = ["abb", "efd", "igk"]

// abc = cab

// abc(b) abb(c) = 1 modification

// [1, 0, -1]

//any char but lower reg
// size of array = 1k, lenght = 1k

// O(N*M)
// M >= K

const val NOT_ANAGRAM = -1

fun compare(arr1: Array<String>, arr2: Array<String>): IntArray {
    //skip validation
    val result = IntArray(arr1.size)

    for (i in 0 until arr1.size) {
        result[i] = compareInternal1(arr1[i], arr2[i])
    }

    return result
}

private fun compareInternal1(first: String, second: String) : Int {
    if (first.length != second.length) return NOT_ANAGRAM

    val chars1 = HashMap<Char, Int>()
    val chars2 = HashMap<Char, Int>()

    for (i in first.indices) {
        chars1[first[i]] = (chars1[first[i]] ?: 0) + 1
        chars2[second[i]] = (chars2[second[i]] ?: 0) + 1
    }

    var differences = 0

    chars1.forEach { (ch: Char, count: Int) ->
        val count2 = chars2[ch] ?: 0
        differences += kotlin.math.abs(count - count2)
    }

    return differences
}



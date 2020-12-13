package kz.ildar.sandbox.utils.leetcode.interview

import kotlin.concurrent.thread

/**
 * Дан массив [array] длиной N
 * Нужно вернуть массив, где каждое i-e значение -
 * это сумма всех значений массива [array], кроме значения на i
 * array:  [2, 3, 1]
 * output: [4, 3, 5]
 */
fun sumExceptI(
    array: IntArray
): IntArray {
    return InterviewJava.sumOptimal(array)
//    return sumOptimal(array)
}

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
    checkMultithreading1()
}

//@Volatile
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
            val bundle = bundleOf("user" to myDataset[position])

            holder.itemView.findNavController().navigate(
                    R.id.action_leaderboard_to_userProfile,
                bundle)
        }
    }

 */

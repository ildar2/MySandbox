package kz.ildar.sandbox.utils.leetcode.interview

import kotlin.collections.*

/**
 * We have an organization and need to print an org chart
 * in the terminal
 * The input is a list of strings.
 * Each string is a management/direct report relationship
 * For example: "A1,B2,C3,D4"
 * Explanation: A1 is the manager of B2, C3, D4 where A1, B2, C3, D4
 * are unique employee IDs separated by a comma.
 */


/*
 * Complete the 'Print' function below.
 *
 * The function accepts STRING_ARRAY data as parameter.
 * ["B2,E5,F6", "A1,B2,C3,D4", "D4,G7,I9", "G7,H8"]
 * O(m*n)
 */
fun print(data: Array<String>) {
    val managers: MutableList<Employee> = mutableListOf()
    val sub = HashSet<Employee>()
    data.forEach {
        val manager = parse(it)
        managers.add(manager)
        sub.addAll(manager.employees)
    }

    managers.forEach {
        if (!sub.contains(it)) {
            it.print()
        }
    }
}

private val allEmployees = HashMap<String, Employee>()

private fun parse(
    input: String,
    names: List<String> = input.split(",")
): Employee = getOrCreate(
    names.first(),
    names.subList(1, names.size).map { getOrCreate(it, emptyList()) }
)

private fun getOrCreate(
    name: String,
    subordinates: List<Employee>
): Employee = allEmployees[name]?.also {
    it.employees = (subordinates + it.employees).distinct()
} ?: Employee(name, subordinates).also {
    allEmployees[name] = it
}

class Employee(
    val name: String,
    var employees: List<Employee> = emptyList()
) {

    override fun toString() = name

    override fun equals(other: Any?) = other is Employee && other.name == name

    fun print(tab: Int = 0) {
        println("${"    ".repeat(tab)}$name")
        employees.forEach {
            it.print(tab + 1)
        }
    }

    override fun hashCode() = 31 * name.hashCode()
}

fun main(args: Array<String>) {
    print(arrayOf("B2,E5,F6", "A1,B2,C3,D4", "D4,G7,I9", "G7,H8"))
}

private fun readInput() {
    val dataCount = readLine()!!.trim().toInt()

    val data = Array(dataCount) { "" }
    for (i in 0 until dataCount) {
        val dataItem = readLine()!!
        data[i] = dataItem
    }

    print(data)
}

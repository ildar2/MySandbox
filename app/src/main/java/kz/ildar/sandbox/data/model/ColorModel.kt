package kz.ildar.sandbox.data.model

data class ColorModel(
    val alpha: Int,
    val red: Int,
    val green: Int,
    val blue: Int,
    val id: Int = -1,
    val name: String = ""
)
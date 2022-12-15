package encryptdecrypt
import java.io.File
fun main(args: Array<String>) {
    val argsMap = args.toList().zipWithNext().toMap()
    val mode = argsMap.getOrDefault("-mode", "enc")
    val key = argsMap.getOrDefault("-key", "0").toInt()
    val alg = argsMap.getOrDefault("-alg","shift")
    val data = if ("-data" in args) {
        argsMap.getOrDefault("-data", "")
    } else {
        val fileName = argsMap.getOrDefault("-in", "")
        val text = File(fileName).readText()
        text
    }
    println(data)
    val fileOut = argsMap.getOrDefault("-out", "") ?: ""
    val result = when(mode) {
        "enc" -> encrypt(data, key, alg)
        "dec" -> decrypt(data, key, alg)
        else -> "Error"
    }

    when {
        (fileOut.isEmpty()) -> println(result)
        else -> {
            val myFile = File(fileOut)
            myFile.writeText(result)
            println(myFile.readText())
        }
    }
}

fun encrypt(data: String, key: Int, alg: String): String {
    if(alg == "shift") {
        return data.map {
            if(it.code in 65..90) ((it.code+key+65)%26+65).toChar()
            else if(it.code in 97..122)  ((it.code+key-97)%26+97).toChar()
            else it
        }.joinToString("")

    }
    return data.map { it.plus(key) }.joinToString("")
}

fun decrypt(data: String, key: Int, alg: String): String {
    if (alg == "shift"){
        return data.map {
            if (it.code in 'a'.code..'z'.code) ((it.code - 'a'.code-key).mod('z'.code - 'a'.code + 1) + 'a'.code).toChar()
            else if ( it.code  in 'A'.code..'Z'.code ) ( (it.code - 'A'.code -key).mod('Z'.code - 'A'.code + 1) + 'A'.code).toChar()
            else  it
        }.joinToString("")
    }
    return data.map { it.minus(key)}.joinToString("")
}
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.util.*
import kotlin.experimental.xor

private val encoder = Base64.getEncoder()
private val decoder = Base64.getDecoder()

fun main(args: Array<String>) {
    try {
        if (args.size == 1 && "check" == args[0]) {
            val process = ProcessBuilder("java", "-jar", "Interact-task8-hard.jar").start()
            BufferedReader(InputStreamReader(process.inputStream)).use { bufferedReader ->
                val printWriter = PrintWriter(process.outputStream)
                val AES = bufferedReader.readLine()
                val IV = bufferedReader.readLine()

                var decodedIV = decoder.decode(IV)
                ++decodedIV[2]

                printWriter.println("NO\n$AES\n" + encoder.encodeToString(decodedIV))
                printWriter.flush()

                if (bufferedReader.readLine() == "Wrong padding") {
                    printWriter.println("YES\nNo")
                    printWriter.flush()
                } else {
                    decodedIV = decoder.decode(IV)
                    decodedIV[2] = decodedIV[2] xor 's'.toByte() xor 14.toByte()

                    (3..15).forEach { i ->
                        decodedIV[i] = decodedIV[i] xor 3.toByte()
                    }

                    printWriter.println("NO\n$AES\n" + encoder.encodeToString(decodedIV))
                    println("NO\n$AES\n" + encoder.encodeToString(decodedIV))
                    printWriter.flush()

                    val help = "YES\n" + if ("Ok" == bufferedReader.readLine()) "Yes" else "N/A"
                    printWriter.println(help)
                    println(help)
                    printWriter.flush()
                }
            }
        } else {
            BufferedReader(InputStreamReader(System.`in`)).use { bufferedReader ->
                val AES = bufferedReader.readLine()
                val IV = bufferedReader.readLine()

                var decodedIV = decoder.decode(IV)
                ++decodedIV[2]

                println("NO\n$AES\n" + encoder.encodeToString(decodedIV))

                if (bufferedReader.readLine() == "Wrong padding") {
                    println("YES\nNo")
                } else {
                    decodedIV = decoder.decode(IV)
                    decodedIV[2] = decodedIV[2] xor 's'.toByte() xor 14.toByte()
                    (3..15).forEach { i ->
                        decodedIV[i] = decodedIV[i] xor 3.toByte()
                    }
                    println("NO\n$AES\n" + encoder.encodeToString(decodedIV))
                    println("YES\n" + if ("Ok" == bufferedReader.readLine()) "Yes" else "N/A")
                }
            }
        }
    } catch (e: IOException) {
        System.err.println("ERROR: Input - " + e.localizedMessage)
    }
}